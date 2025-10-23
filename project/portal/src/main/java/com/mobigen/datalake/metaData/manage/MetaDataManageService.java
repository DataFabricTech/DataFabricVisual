package com.mobigen.datalake.metaData.manage;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mobigen.datalake.metaData.manage.DTO.UploadMinioFileForm;
import com.mobigen.datalake.system.collector.CollectorService;
import com.mobigen.framework.utility.MinioFactory;
import com.mobigen.framework.utility.RabbitmqProducer;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetaDataManageService {
    private final ObjectMapper objectMapper;
    private final CollectorService collectorService;
    private final MetaDataManageMapper metaDataManageMapper;

    @Value("${properties.ESSync.base_url}")
    private String esSyncBaseUrl;

    @Value("${properties.ESSync.url.update}")
    private String esSyncUrlUpdate;

    @Value("${properties.ESSync.url.updates}")
    private String esSyncUrlUpdates;

    @Value("${properties.catalog-api.base_url}")
    private String catalogApiBaseUrl;
    
    @Value("${properties.catalog-api.url.executeSync}")
    private String catalogApiUrlExecuteSync;

    @Value("${properties.catalog-api.url.profile.default}")
    private String catalogApiUrlProfile;

    @Value("${properties.minio.endpoint}")
    private String endpoint;

    @Value("${properties.minio.accessKey}")
    private String accessKey;

    @Value("${properties.minio.secretKey}")
    private String secretKey;

    @Value("${properties.minio.unstructured_bucket}")
    private String unstructuredBucket;

    @Autowired
    private RabbitmqProducer producer;

    // 메타 데이터 통합 관리 대상 목록 조회
    public Map<String, Object> getMetaDataManageList(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        int totCnt = 0;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        // 전체 검색일 경우 메타 데이터 유형 목록 조회
        List<Map<String, Object>> metaTypeList = new ArrayList<>();
        if (params.get("metaType") != null && !"".equals(params.get("metaType").toString())) {

        } else {
            metaTypeList = metaDataManageMapper.getMetaTypeList();

            if (metaTypeList != null && metaTypeList.size() > 0) {
                params.put("metaTypeList", metaTypeList);
            }
        }

        totCnt = metaDataManageMapper.getMetaDataManageListTotCnt(params);
        resultList = metaDataManageMapper.getMetaDataManageList(params);
        
        result.put("totalCount", totCnt);
        result.put("list", resultList);

        return result;
    }

    public List<Map<String, Object>> getMappedMetaDataManageListByClassificationId(Map<String, Object> params)
            throws Exception {
        return metaDataManageMapper.getMappedMetaDataManageListByClassificationId(params);
    };

    public List<Map<String, Object>> getMetaDataListByParentId(String parentId) throws Exception {
        return metaDataManageMapper.getMetaDataListByParentId(parentId);
    }

    public int updateMetaDataClassification(Map<String, Object> params) throws Exception {
        return metaDataManageMapper.updateMetaDataClassification(params);
    }

    public int deleteMetaDataClassification(Map<String, Object> params) throws Exception {
        return metaDataManageMapper.deleteMetaDataClassification(params);
    }

    public Map<String, Object> getFilterOptions() throws Exception {
        List<Map<String, Object>> systemTypeList = metaDataManageMapper.getFilterSystemType();
        List<Map<String, Object>> metaTypeList = metaDataManageMapper.getFilterMetaType();
        List<Map<String, Object>> fileFormatList = metaDataManageMapper.getFilterFileFormat();
        Map<String, Object> result = new HashMap<>();
        result.put("systemTypeList", systemTypeList);
        result.put("metaTypeList", metaTypeList);
        result.put("fileFormatList", fileFormatList);
        return result;
    }

    // 공개설정 권한 처리
    public Object setDataAuth(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        boolean addResult = false;
        boolean esResult = false;
        try {
            // meta_ids가 있는 경우만 처리
            List<String> meta_ids = (List<String>) params.get("meta_ids");
            if (meta_ids != null && meta_ids.size() > 0) {
                int addResultCount = metaDataManageMapper.updateMetaDataRelease(params);
                if (addResultCount > 0) {
                    addResult = true;
                    essyncUpdates(meta_ids, 2, "데이터 권한 공개설정");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.put("result", addResult);
        result.put("esResult", esResult);

        return result;
    }

    // ES 동기화 API 호출
    public boolean essyncUpdates(List<String> meta_ids, Integer eventStatus, String eventName) throws Exception {
        boolean result = false;

        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("metaids", meta_ids);
            requestBody.put("event_status", eventStatus); // 1=생성, 2=수정, 3=삭제
            requestBody.put("event_name", eventName);

            String url = esSyncBaseUrl + esSyncUrlUpdates;
            ResponseEntity<HashMap> restResponse = restTemplate.postForEntity(url, requestBody, HashMap.class);
            if (restResponse != null && restResponse.getBody() != null) {
                Map<String, Object> body = restResponse.getBody();
                if (body != null && body.get("result") != null) {
                    Map<String, Object> bodyResultMap = (Map<String, Object>) body.get("result");
                    if (bodyResultMap != null) {
                        String rs = bodyResultMap.get("result").toString();
                        if (rs.equals("success")) {
                            result = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("essyncUpdates API 호출 오류");
            result = false;
        }

        return result;
    }

    // 수집대상 시스템 ID 중복확인
    public Object metaIdDupCheck(String meta_id) throws Exception {
        int cnt = metaDataManageMapper.metaIdDupCheck(meta_id);
        if (cnt == 0) {
        return true;
        } else {
        return false;
        }
    }

    // 수집 파일 중복확인
    public Object metaQualifiedNameDupCheck(String qualified_name) throws Exception {
        Integer cnt = metaDataManageMapper.metaQualifiedNameDupCheck(qualified_name);
        if (cnt == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Map<String, Object> saveFileMetaData(Map<String, Object> metadata, Map<String, Object> user) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Integer saveEventResult = 0; // event log 저장 성공 여부
        Integer saveMetadataResult = 0; // 메타데이터 테이블 저장 성공 여부
        boolean isUpdate = false; // 비정형 데이터 파일 중복 등록 여부
        
        try {
            // 1. event_log 저장
            Map<String, Object> eventParam = new HashMap<>();
            eventParam.put("event_type", 0); // 메타데이터 이벤트: 0 | 시스템 이벤트: 1

            // qualified_name이 같은 meta_id 조회
            String meta_id = metaDataManageMapper.getMetaIdFromQualifiedName(metadata.get("qualified_name").toString());
            if (meta_id != null) isUpdate = true;
            
            // 이벤트 상태값 설정
            int event_status = 1;
            if (isUpdate) {
                event_status = 2;
            }
            eventParam.put("event_status", event_status); // 0 해지 | 1:발생(생성) | 2: 변경 | 3: 삭제
            eventParam.put("event_name", "비정형 메타 데이터 등록[발생]");
            eventParam.put("event_user_id", user.get("user_id"));
            eventParam.put("meta_id", metadata.get("meta_id"));
            eventParam.put("meta_name", metadata.get("meta_name"));
            eventParam.put("qualified_name", metadata.get("qualified_name"));
            saveEventResult = metaDataManageMapper.saveEvent(eventParam);
            if (saveEventResult != null && saveEventResult > 0) {
                log.info("event log saved !!");
            } else {
                log.error("event log not saved !!");
            }

            String dateString = metadata.get("create_time") != null ? metadata.get("create_time").toString() : "";
            // LocalDateTime 변환
            LocalDateTime ldt = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            // Timestamp 변환
            Timestamp ts = Timestamp.valueOf(ldt);

            // _varchar 타입은 ArrayList가 아닌 String[] 값을 넣어줘야함.
            List<String> classificationList = (List<String>) metadata.get("classifications");
            String[] classifications = null;
            if (classificationList != null) {

                classifications = classificationList.toArray(new String[0]);
            }
            metadata.put("classifications", classifications);

            // _varchar 타입은 ArrayList가 아닌 String[] 값을 넣어줘야함.
            List<String> tagList = (List<String>) metadata.get("tags");
            String[] tags = null;
            if (tagList != null) {
                tags = tagList.toArray(new String[0]);
            }
            metadata.put("tags", tags);
            
            // jsonb 타입은 Map -> JSON형태의 String값으로 변환 필요 넣어줘야함.
            metadata.put("attributes", objectMapper.writeValueAsString(metadata.get("attributes")));
            metadata.put("business_attributes", objectMapper.writeValueAsString(metadata.get("business_attributes")));
            
            // 2. 메타데이터 테이블에 저장
            if (isUpdate) {
                metadata.put("meta_id", meta_id);
                metadata.put("update_by", user.get("user_id").toString());
                metadata.put("update_time", ts);

                saveMetadataResult = metaDataManageMapper.updateMetadata(metadata);
            } else {
                metadata.put("create_by", user.get("user_id").toString());
                metadata.put("create_time", ts);

                saveMetadataResult = metaDataManageMapper.saveMetadata(metadata);
            }
                
            if (saveMetadataResult != null && saveMetadataResult > 0) {
                log.info("metadata saved !! !!");
                // ESSync release 갱신
                Map<String, Object> essyncParams = new HashMap<>();
                essyncParams.put("metaid", meta_id);
                essyncParams.put("event_status", 2);
                essyncParams.put("event_name", "데이터 권한설정 변경");
                essyncUpdate(essyncParams);
            } else {
                log.error("metadata not saved !!");
            }

            result.put("saveEventCount", saveEventResult);
            result.put("saveMetadataCount", saveMetadataResult);
            
        } catch (Exception e) {
            log.error("save file metadata Exception");
            e.printStackTrace();
            result.put("saveEventCount", saveEventResult);
            result.put("saveMetadataCount", saveMetadataResult);
        }
        
        return result;
    }

    public Object uploadFile(UploadMinioFileForm file) throws Exception {
        Map<String, Object> result = new HashMap<>();
        boolean uploadResult = false; // 파일 minio 저장 성공 여부
        
        MultipartFile uploadFile = file.getFile();
        try (InputStream is = uploadFile.getInputStream()){
            
            boolean res = this.uploadMultipart(
                    uploadFile.getOriginalFilename(),
                    is,
                    uploadFile.getContentType(),
                    uploadFile.getSize()
            );
            
            if (res) {
                uploadResult = true;        
            }
        
            result.put("uploadResult", uploadResult);
            
        } catch (Exception e) {
            log.error("upload file metadata Exception");
            e.printStackTrace();
            result.put("uploadResult", uploadResult);
        }
        
        return result;
    }

    // Minio Upload Multipart API 호출
    public boolean uploadMultipart(String fileName, InputStream inputStream, String contentType, long size) throws Exception {
        boolean result = false;

        try {
            MinioClient minioClient = MinioFactory.create(endpoint, accessKey, secretKey);


            // 버킷 확인 및 생성
            boolean isExist = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(unstructuredBucket).build()
            );
            if (!isExist) {
                log.info("create new bucket !!");
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(unstructuredBucket).build());
            } else {
                log.info("bucket is exists !!");
            }

            // 파일 업로드
            minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(unstructuredBucket)
                        .object(fileName)
                        .stream(inputStream, size, -1)  // (stream, size, partSize)
                        .contentType(contentType)
                        .build()
            );

            System.out.println(fileName + " success to upload file to minio !!");
            result = true;
        } catch (MinioException e) {
            e.printStackTrace();
            log.error("fail to upload file: " + e.getMessage());
            result = false;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("fail to upload file: " + e.getMessage());
            result = false;
        }

        return result;
    }

    // MinIO 수동수집 실행
    public Object executeCollect(Map<String, Object> executeBody) throws Exception {
        Map<String, Object> result = new HashMap<>();
        boolean executeResult = false;

        try {
            String system_id = "";
            String qualified_name = "";
            if (executeBody.get("system_id") != null) {
                system_id = executeBody.get("system_id").toString();
            }
            if (executeBody.get("qualified_name") != null) {
                qualified_name = executeBody.get("qualified_name").toString();
            }

            log.info("start to execute ingestion execute");
            
            // 시작 시각 초단위까지
            long startTime = System.currentTimeMillis(); // 시작 시간(ms)
            executeResult = ingestionExecuteSync(system_id, qualified_name);
            // 종료 시각 초단위까지
            long endTime = System.currentTimeMillis(); // 종료 시간(ms)
            long execute_duration_ms = endTime - startTime;
            double execute_duration_sec = execute_duration_ms / 1000.0;
            log.info("end to execute ingestion execute - " + execute_duration_sec);

        } catch (Exception e) {
            executeResult = false;
            log.error("fail to execute direct Collect");
            e.printStackTrace();
        }
        
        result.put("executeResult", executeResult);
        return result;
    }

    // MinIO 프로파일링 실행
    public Object executeProfile(Map<String, Object> executeBody) throws Exception {
        Map<String, Object> result = new HashMap<>();
        boolean executeResult = false;

        try {
            String system_id = "";
            String qualified_name = "";
            if (executeBody.get("system_id") != null) {
                system_id = executeBody.get("system_id").toString();
            }
            if (executeBody.get("qualified_name") != null) {
                qualified_name = executeBody.get("qualified_name").toString();
            }

            log.info("start to execute profile execute");
            executeResult = profileExecute(system_id, qualified_name);
            log.info("end to execute profile execute");

        } catch (Exception e) {
            executeResult = false;
            log.error("fail to execute direct Collect");
            e.printStackTrace();
        }
        
        result.put("executeResult", executeResult);
        return result;
    }
  
    // 수동 수집 실행 POST - SYNC
    public boolean ingestionExecuteSync(String system_id, String qualified_name) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        String executeUrl = catalogApiBaseUrl + catalogApiUrlExecuteSync;

        Map<String, Object> requestBody = new HashMap<>();
        List<String> include = new ArrayList<>();
        List<String> exclude = new ArrayList<>();
        
        if (!qualified_name.equals("")) {
            // object=6b68d2f1-3705-4f1b-8e9e-6653b9203828.fabricdata.virtual_system_data_1year.csv
            include.add("object=" + qualified_name);
        }

        requestBody.put("system_id", system_id);
        requestBody.put("include", include); 
        requestBody.put("exclude", exclude);
        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(executeUrl, requestBody, HashMap.class);
        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
            return true;
        }

        return false;
    }

    // 수동 프로파일링 실행 POST
    public boolean profileExecute(String system_id, String qualified_name) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        String executeUrl = catalogApiBaseUrl + catalogApiUrlProfile;

        Map<String, Object> requestBody = new HashMap<>();
        List<String> include = new ArrayList<>();
        List<String> exclude = new ArrayList<>();

        if (!qualified_name.equals("")) {
            // object=6b68d2f1-3705-4f1b-8e9e-6653b9203828.fabricdata.virtual_system_data_1year.csv
            include.add("object=" + qualified_name);
        }
        
        requestBody.put("system_id", system_id);
        requestBody.put("include", include); 
        requestBody.put("exclude", exclude);
        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(executeUrl, requestBody, HashMap.class);
        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
            return true;
        }

        return false;
    }

    public Map<String, Object> essyncUpdate(Map<String, Object> params) throws Exception {
        Map<String, Object> restResponse = new HashMap<>();

        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("metaid", params.get("metaid"));
            requestBody.put("event_status", params.get("event_status")); // 1=생성, 2=수정, 3=삭제
            requestBody.put("event_name", params.get("event_name"));

            String url = esSyncBaseUrl + esSyncUrlUpdate;
            restResponse = restTemplate.postForObject(url, requestBody, HashMap.class);
        } catch (Exception e) {
            log.error("essyncUpdate API 호출 오류");
        }

        return restResponse;
    }

    public void extractMetadata(Map<String, Object> params, HttpServletResponse response) throws Exception {
        List<LinkedHashMap<String, Object>> extractMetaList = new ArrayList<>();
        if (params.get("extract_type").toString().equalsIgnoreCase("XML")) {
            extractMetaList = metaDataManageMapper.getExtractMetaList(params);

            XMLOutputFactory factory = XMLOutputFactory.newInstance();

            try (OutputStream os = response.getOutputStream()) {
                XMLStreamWriter writer = factory.createXMLStreamWriter(os, "UTF-8");

                writer.writeStartDocument("UTF-8", "1.0");
                writer.writeCharacters("\n");
                writer.writeStartElement("RESULTS");
                writer.writeCharacters("\n");

                for (Map<String, Object> row : extractMetaList) {
                    writer.writeCharacters("\t");
                    writer.writeStartElement("META");
                    writer.writeAttribute("ID", row.get("meta_id").toString());
                    writer.writeCharacters("\n");

                    for (Map.Entry<String, Object> entry : row.entrySet()) {
                        writer.writeCharacters("\t\t");
                        writer.writeStartElement(entry.getKey());

                        Object value = entry.getValue();

                        if (value != null) {
                            try {
                                // JDBC Array 타입 (_varchar 등)
                                if (value instanceof java.sql.Array) {
                                    Object arrayObj = ((java.sql.Array) value).getArray();
                                    if (arrayObj instanceof Object[]) {
                                        Object[] arr = (Object[]) arrayObj;
                                        for (Object v : arr) {
                                            writer.writeCharacters("\n\t\t\t");
                                            writer.writeStartElement("item");
                                            writer.writeCharacters(v != null ? v.toString() : "");
                                            writer.writeEndElement();
                                        }
                                    }
                                } else if (value instanceof Object[]) {
                                    Object[] arr = (Object[]) value;
                                    for (Object v : arr) {
                                        writer.writeCharacters("\n\t\t\t");
                                        writer.writeStartElement("item");
                                        writer.writeCharacters(v != null ? v.toString() : "");
                                        writer.writeEndElement();
                                    }
                                }
                                // JSON / JSONB
                                else {
                                    String strValue = value.toString().trim();
                                    if ((strValue.startsWith("{") && strValue.endsWith("}")) ||
                                        (strValue.startsWith("[") && strValue.endsWith("]"))) {
                                        JsonNode jsonNode = objectMapper.readTree(strValue);
                                        int depth = 2;
                                        writeJsonAsXml(writer, jsonNode, depth + 1);
                                        // ❌ 중복 개행 제거
                                    } else {
                                        writer.writeCharacters(strValue);
                                    }
                                }
                            } catch (Exception e) {
                                writer.writeCharacters(value.toString());
                            }
                        }

                        writer.writeEndElement(); // </COLUMN>
                        writer.writeCharacters("\n");
                    }

                    writer.writeCharacters("\t");
                    writer.writeEndElement(); // </META>
                    writer.writeCharacters("\n");
                }

                writer.writeEndElement(); // </RESULTS>
                writer.writeCharacters("\n");
                writer.writeEndDocument();

                writer.flush();
                writer.close();
            }
        }

        if (params.get("extract_type").toString().equalsIgnoreCase("JSON")) {
            ObjectMapper mapper = new ObjectMapper();
            extractMetaList = metaDataManageMapper.getExtractMetaList(params);

            ObjectNode root = mapper.createObjectNode();
            root.putPOJO("data", extractMetaList);
            
            // 파일 다운로드
            try (OutputStream os = response.getOutputStream()) {
                String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
                os.write(jsonString.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                log.error("extractMetadata Exception !!");
                e.printStackTrace();
            }
        }
    }

    private void writeJsonAsXml(XMLStreamWriter writer, JsonNode node, int depth) throws Exception {
        String indent = "\t".repeat(depth);

        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();

                writer.writeCharacters("\n" + indent);       // 열기 전에 줄바꿈
                writer.writeStartElement(field.getKey());

                writeJsonAsXml(writer, field.getValue(), depth + 1);

                writer.writeEndElement();                    // 닫는 태그
            }
        } else if (node.isArray()) {
            for (JsonNode child : node) {
                writer.writeCharacters("\n" + indent);
                writer.writeStartElement("item");

                writeJsonAsXml(writer, child, depth + 1);

                writer.writeEndElement();
            }
        } else {
            // 단순 값이면 그냥 문자열만
            writer.writeCharacters(node.asText());
        }
    }

    public Map<String, Object> exportMetadataToQueue(Map<String, Object> params) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        boolean result = false;
        String message = "";
        String userId = params.get("user_id").toString();
        String extractType = params.get("extract_type").toString().toUpperCase(Locale.ROOT);
        String queueName = params.getOrDefault("queue_name", "metadata.queue").toString();

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss"));
        String id = userId + "_" + timestamp;

        // 전체 검색일 경우 메타 데이터 유형 목록 조회
        List<Map<String, Object>> metaTypeList = new ArrayList<>();
        if (params.get("metaType") != null && !"".equals(params.get("metaType").toString())) {

        } else {
            metaTypeList = metaDataManageMapper.getMetaTypeList();

            if (metaTypeList != null && metaTypeList.size() > 0) {
                params.put("metaTypeList", metaTypeList);
            }
        }

        List<LinkedHashMap<String, Object>> extractMetaList = metaDataManageMapper.getExtractMetaList(params);

        ObjectMapper mapper = new ObjectMapper(); // JSON 변환용
        if (!extractMetaList.isEmpty()) {
            try {
                int totalSize = extractMetaList.size();
                int index = 0;

                for (LinkedHashMap<String, Object> meta : extractMetaList) {

                    // tags 정상출력 되도록 변환 처리
                    Object tagsValue = meta.get("tags");
                    if (tagsValue instanceof String[]) {
                        meta.put("tags", mapper.writeValueAsString((String[]) tagsValue));
                    } else if (tagsValue instanceof Collection) {
                        meta.put("tags", mapper.writeValueAsString(tagsValue));
                    }

                    // labels 정상출력 되도록 변환 처리
                    Object labelsValue = meta.get("labels");
                    if (tagsValue instanceof String[]) {
                        meta.put("labels", mapper.writeValueAsString((String[]) labelsValue));
                    } else if (tagsValue instanceof Collection) {
                        meta.put("labels", mapper.writeValueAsString(labelsValue));
                    }

                    String payload;

                    if ("JSON".equals(extractType)) {
                        payload = createJsonSingle(meta);
                    } else if ("XML".equals(extractType)) {
                        payload = createXmlSingle(meta);
                    } else {
                        throw new IllegalArgumentException("Unsupported extract_type: " + extractType);
                    }

                    // RabbitMQ 전송용 메시지
                    Map<String, Object> metadataMessage = new LinkedHashMap<>();
                    metadataMessage.put("id", id);
                    metadataMessage.put("index", index++);
                    metadataMessage.put("user_id", userId);
                    metadataMessage.put("extract_type", extractType);
                    metadataMessage.put("total_size", totalSize);
                    metadataMessage.put("payload", payload);

                    // RabbitMQ 전송
                    producer.publishMetadataTask(queueName, metadataMessage);
                }

                result = true;
                message = "메타데이터 " + extractMetaList.size() + "건을 큐로 전송했습니다.";

            } catch (Exception e) {
                message = "메타데이터 전송 중 오류가 발생했습니다.";
                log.error("fail to export metadata to queue", e);
            }

        } else {
            message = "추출할 메타데이터가 존재하지 않습니다.";
        }

        resultMap.put("result", result);
        resultMap.put("message", message);
        return resultMap;
    }


    private String createJsonSingle(Map<String, Object> meta) throws Exception {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("meta_id", meta.get("meta_id").toString());
        root.putPOJO("data", meta);
        return objectMapper.writeValueAsString(root);
    }

    private String createXmlSingle(Map<String, Object> meta) throws Exception {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        StringWriter sw = new StringWriter();
        XMLStreamWriter writer = factory.createXMLStreamWriter(sw);

        writer.writeStartDocument("UTF-8", "1.0");
        writer.writeCharacters("\n");
        writer.writeStartElement("META");
        writer.writeAttribute("ID", meta.get("meta_id").toString());
        writer.writeCharacters("\n");

        for (Map.Entry<String, Object> entry : meta.entrySet()) {
            writer.writeCharacters("\t");
            writer.writeStartElement(entry.getKey());
            Object value = entry.getValue();
            if (value != null) {
                writer.writeCharacters(value.toString());
            }
            writer.writeEndElement();
            writer.writeCharacters("\n");
        }

        writer.writeEndElement(); // </META>
        writer.writeEndDocument();
        writer.flush();
        writer.close();

        return sw.toString();
    }

    public Map<String, Object> getMetadataTotal(Map<String, Object> params) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        // 전체 검색일 경우 메타 데이터 유형 목록 조회
        List<Map<String, Object>> metaTypeList = new ArrayList<>();
        if (params.get("metaType") != null && !"".equals(params.get("metaType").toString())) {

        } else {
            metaTypeList = metaDataManageMapper.getMetaTypeList();

            if (metaTypeList != null && metaTypeList.size() > 0) {
                params.put("metaTypeList", metaTypeList);
            }
        }
        
        Integer totalCount = metaDataManageMapper.getMetadataTotal(params);
        resultMap.put("totalCount", totalCount);
        return resultMap;
    }
}