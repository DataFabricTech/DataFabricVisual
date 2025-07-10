package com.mobigen.datalake.metaData.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobigen.datalake.metaData.manage.DTO.UploadMinioFileForm;
import com.mobigen.datalake.system.collector.CollectorService;

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

    @Value("${properties.ESSync.url.updates}")
    private String esSyncUrlUpdates;

    @Value("${properties.MinioService.base_url}")
    private String minioServiceBaseUrl;

    @Value("${properties.MinioService.url.upload_multipart}")
    private String minioServiceUrlUploadMultipart;

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

    public List<Map<String, Object>> getFilterOptions() throws Exception {
        return metaDataManageMapper.getFilterOptions();
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

    public Map<String, Object> saveFileMetaData(Map<String, Object> metadata, Map<String, Object> user) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Integer saveEventResult = 0; // event log 저장 성공 여부
        Integer saveMetadataResult = 0; // 메타데이터 테이블 저장 성공 여부
        
        try {
            // 1. event_log 저장
            Map<String, Object> eventParam = new HashMap<>();
            eventParam.put("event_type", 0); // 메타데이터 이벤트: 0 | 시스템 이벤트: 1
            eventParam.put("event_status", 1); // 0 해지 | 1:발생(생성) | 2: 변경 | 3: 삭제
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
            
            metadata.put("create_by", user.get("user_id").toString());
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
            metadata.put("business_attributes", objectMapper.writeValueAsString(metadata.get("business_attributes")));
            
            // 2. 메타데이터 테이블에 저장
            saveMetadataResult = metaDataManageMapper.saveMetadata(metadata);
            if (saveMetadataResult != null && saveMetadataResult > 0) {
                log.info("metadata saved !! !!");
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
        
        try {
            // 3. 파일 fabricdata(minio)에 저장
            boolean res = uploadMultipart(file.getFile());
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
    public boolean uploadMultipart(MultipartFile multipartFile) throws Exception {
        boolean result = false;

        try {
            RestTemplate restTemplate = new RestTemplate();

            // 1. 파일 데이터를 ByteArrayResource로 변환
            ByteArrayResource fileAsResource = new ByteArrayResource(multipartFile.getBytes()) {
                @Override
                public String getFilename() {
                    return multipartFile.getOriginalFilename(); // 파일 이름 지정
                }
            };

            // 2. MultiValueMap에 파일 추가
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileAsResource);

            // 3. HttpHeaders 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 4. HttpEntity 생성
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 5. 요청 전송
            String url = minioServiceBaseUrl + minioServiceUrlUploadMultipart;

            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                Map.class
            );
            
            if (response != null) {
                Map<String, Object> responseBody = response.getBody();
                log.info("응답 결과: " + response.getBody());
                if (responseBody != null && responseBody.get("result") != null && responseBody.get("result").toString().equals("success")) {
                    result = true;
                }
            }

        } catch (Exception e) {
            log.error("uploadMultipart API 호출 오류");
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    // MinIO 수동수집 실행
    public Object executeCollect() throws Exception {
        Map<String, Object> result = new HashMap<>();
        boolean executeResult = false;

        try {
            // // 1. 코드테이블에서 fabric_storage > system_name 조회
            // List<Map<String, Object>> codeList = metaDataManageMapper.getCodeForFabricStorage();
            // if (codeList != null) {
            //     for (int i=0; i<codeList.size(); i++) {
            //         if (codeList.get(i).get("system_name") != null && codeList.get(i).get("system_name").toString().toUpperCase().equals("FABRIC_STORAGE")) {
            //             String system_name = codeList.get(i).get("system_name").toString();
            //             // 2. MinIO FabricStorage system_id 조회
            //             Map<String, Object> fabricStorageInfo = metaDataManageMapper.getFabricStorageInfoFromSystemName(system_name);

            //             if (fabricStorageInfo != null) {
            //                 String system_id = fabricStorageInfo.get("system_id").toString();
            //                 executeResult = collectorService.ingestionExecute(system_id);
            //             }
            //         }
            //     }
            // }
            // result.put("executeResult", executeResult);

            result.put("executeResult", true);
            return result;

        } catch (Exception e) {
            result.put("executeResult", false);
            log.error("fail to execute direct Collect");
        }
        
        return result;
    }
  
}
