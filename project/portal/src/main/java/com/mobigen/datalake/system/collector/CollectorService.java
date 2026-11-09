package com.mobigen.datalake.system.collector;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobigen.datalake.post.file.FileMapper;
import com.mobigen.framework.utility.AESGCMUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CollectorService {
  private final CollectorMapper collectorMapper;
  private final ObjectMapper objectMapper;
  private final FileMapper fileMapper;

  @Value("${properties.catalog-api.base_url}")
  private String catalogApiBaseUrl;

  @Value("${properties.catalog-api.url.execute}")
  private String catalogApiUrlExecute;

  @Value("${properties.catalog-api.url.dag}")
  private String catalogApiUrlDag;

  @Value("${properties.cryptKey}")
  private String cryptKey;

  @Value("${properties.airflow.base_url}")
  private String airflowUrl;

  Map<String, Object> getCollectorListInfo(Map<String, Object> params) throws Exception {
    Map resultMap = new HashMap<>();
    List<Map<String, Object>> list = new ArrayList<>();
    int total = 0;

    try {
      total = collectorMapper.getCollectorListTotal(params);
      list = collectorMapper.getCollectorList(params);

      if (list != null && list.size() > 0) {
        for (int i = 0; i < list.size(); i++) {
          // filter_config값 Map 형태로 변환
          list.get(i).put("filter_config", convertJsonbToMap(list.get(i).get("filter_config")));

          // 상태값에 따라 수동수집 버튼 활성화 여부 값 설정
          String status = list.get(i).get("status") != null ? list.get(i).get("status").toString() : null;
          if (status != null) {
            // 비활성화(1. 등록 직후, 2. 즉시수집 클릭 직후 및 INGESTION 상태)
            // 활성화(REGISTER_COMPLETED, INGESTION_COMPLETED, INGESTION_FAILED)
            if (!status.equals("INGESTION") &&
                (status.equals("REGISTER_COMPLETED")
                    || status.equals("INGESTION_COMPLETED")
                    || status.equals("INGESTION_FAILED"))) {
              list.get(i).put("isActiveIngestion", true);
            } else {
              list.get(i).put("isActiveIngestion", false);
            }
          } else {
            list.get(i).put("isActiveIngestion", false);
          }

          String system_id = list.get(i).get("system_id").toString();
          List<Map<String, Object>> fileInfoList = collectorMapper.getFileInfoList(system_id);
          Map<String, Object> coreSiteFileInfo = null;
          Map<String, Object> hdfsSiteFileInfo = null;
          Map<String, Object> esFileInfo = null;
          if (fileInfoList != null) {
            for (int j = 0; j < fileInfoList.size(); j++) {
              // HIVE인 경우 core-site.xml, hdfs-site.xml 파일 조회
              if (list.get(i).get("system_type").equals("Hive")){
                if (fileInfoList.get(j).get("file_name").toString().equals("core-site.xml")) {
                coreSiteFileInfo = fileInfoList.get(j);
                } else if (fileInfoList.get(j).get("file_name").toString().equals("hdfs-site.xml")) {
                  hdfsSiteFileInfo = fileInfoList.get(j);
                }
              }
              // ElasticSearch인 경우 ssl 파일 조회
              if (list.get(i).get("system_type").equals("ElasticSearch")){
                esFileInfo = fileInfoList.get(j);
              }
            }
          }

          list.get(i).put("core_site_file", coreSiteFileInfo);
          list.get(i).put("hdfs_site_file", hdfsSiteFileInfo);
          list.get(i).put("es_ssl_file", esFileInfo);
        }
      }

      // jsonb 타입 => Map 변환
      resultMap.put("list", list);
      resultMap.put("total", total);

    } catch (Exception e) {
      resultMap.put("list", new ArrayList<>());
      resultMap.put("total", 0);
    }

    return resultMap;
  }

  // 수집대상 시스템 ID 중복확인
  public Object systemIdDupCheck(String system_id) throws Exception {
    int cnt = collectorMapper.systemIdDupCheck(system_id);
    if (cnt == 0) {
      return true;
    } else {
      return false;
    }
  }

  // 수집대상 등록 (put으로 같이 사용)
  public Object addCollector(Map<String, Object> params) throws Exception {
    Map<String, Object> result = new HashMap<>();
    int addCount = 0;

    try {
      // 비밀번호 암호화
      AESGCMUtil encoder = new AESGCMUtil(cryptKey);
      if (params.get("password") != null && !params.get("password").toString().equals("")) {
        params.put("password", encoder.encrypt(params.get("password").toString()));
      }

      if (params.get("es_ssl_password") != null && !params.get("es_ssl_password").toString().equals("")) {
        params.put("es_ssl_password", encoder.encrypt(params.get("es_ssl_password").toString()));
      }

      addCount = collectorMapper.addCollector(params);
      if (addCount > 0) {
        putIngestionDagApi(params.get("system_id").toString());
      }

      result.put("addCount", addCount);
    } catch (Exception e) {
      e.printStackTrace();
      result.put("addCount", 0);
    }

    return result;
  }

  // 수집대상 수정 (put으로 같이 사용)
  public Object updateCollector(Map<String, Object> params) throws Exception {
    Map<String, Object> result = new HashMap<>();
    int updateCount = 0;

    try {
      AESGCMUtil encoder = new AESGCMUtil(cryptKey);

      // 비밀번호를 입력한 경우는 암호화해서 같이 저장
      if (params.get("password") != null && !params.get("password").toString().equals("")) {
        params.put("password", encoder.encrypt(params.get("password").toString()));
      }

      // Elasticsearch SSL 비밀번호 암호화
      if (params.get("es_ssl_password") != null && !params.get("es_ssl_password").toString().equals("")) {
        params.put("es_ssl_password", encoder.encrypt(params.get("es_ssl_password").toString()));
      }

      updateCount = collectorMapper.updateCollector(params);

      if (updateCount > 0) {
        putIngestionDagApi(params.get("system_id").toString());
      }

      result.put("updateCount", updateCount);
    } catch (Exception e) {
      e.printStackTrace();
      result.put("updateCount", 0);
    }

    return result;
  }

  // 수집대상 삭제
  public Object deleteCollector(String system_id) throws Exception {
    boolean result = false;

    // Airflow Dag 삭제 성공 시 DB에서 수집 시스템 정보 제거
    boolean dagDeleteResult = deleteIngestionDagApi(system_id);
    
    if (dagDeleteResult) {
      int deleteCount = collectorMapper.deleteCollector(system_id);
      if (deleteCount > 0) {
        // 실제 파일 삭제 처리
        deleteRealFileFromSystemId(system_id);
        result = true;
      }
    }
    return result;
  }

  // 수동 수집 실행
  public Object directCollect(String system_id) throws Exception {
    Map<String, Object> result = new HashMap<>();
    result.put("result", ingestionExecute(system_id));
    return result;
  }

  // Jsonb => Map 타입 변환 함수
  public Map<String, Object> convertJsonbToMap(Object jsonb) throws Exception {
    if (jsonb != null) {
      return objectMapper.readValue((String) jsonb, new TypeReference<Map<String, Object>>() {
      });
    }
    return null;
  }

  // 수동 수집 실행 POST
  public boolean ingestionExecute(String system_id) throws Exception {
    RestTemplate restTemplate = new RestTemplate();

    String executeUrl = catalogApiBaseUrl + catalogApiUrlExecute;

    Map<String, Object> requestBody = new HashMap<>();
    List<String> include = new ArrayList<>();
    List<String> exclude = new ArrayList<>();
    requestBody.put("system_id", system_id);
    requestBody.put("include", include);
    requestBody.put("exclude", exclude);
    ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(executeUrl, requestBody, HashMap.class);
    if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
      return true;
    }

    return false;
  }

  // 수집수정 시 DAG 수정 API (PUT)
  private boolean putIngestionDagApi(String system_id) throws Exception {
    RestTemplate restTemplate = new RestTemplate();
    String executeUrl = catalogApiBaseUrl + catalogApiUrlDag;


    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      String requestBody = "{ \"system_id\": \"" + system_id + "\" }";
      HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
      ResponseEntity<HashMap> responseEntity = restTemplate.exchange(executeUrl, HttpMethod.PUT, requestEntity,
          HashMap.class);
      if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
        return true;
      }

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return false;
  }

  // 수집삭제 시 DAG 삭제 API
  private boolean deleteIngestionDagApi(String system_id) throws Exception {
    RestTemplate restTemplate = new RestTemplate();
    String executeUrl = catalogApiBaseUrl + catalogApiUrlDag;

    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      String requestBody = "{ \"system_id\": \"" + system_id + "\" }";
      HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
      ResponseEntity<HashMap> responseEntity = restTemplate.exchange(executeUrl, HttpMethod.DELETE, requestEntity,
          HashMap.class);
      if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
        return true;
      }

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return false;
  }

  // 시스템 타입 조회
  public Object getSystemTypes() throws Exception {
    Map<String, Object> result = new HashMap<>();

    try {
      List<String> list = collectorMapper.getSystemTypes();
      if (list != null) {
        result.put("list", list);
      } else {
        result.put("list", new ArrayList<>());
      }

    } catch (Exception e) {
      result.put("list", new ArrayList<>());
    }

    return result;
  }

  // 시스템 정보 1개 조회
  public Object getCollectorOneInfo(String system_id) throws Exception {
    Map<String, Object> result = new HashMap<>();

    try {
      Map<String, Object> collector = collectorMapper.getCollectorOne(system_id);
      if (collector != null) {
        String status = collector.get("status") != null ? collector.get("status").toString() : null;
        if (status != null) {
          // 비활성화(1. 등록 직후, 2. 즉시수집 클릭 직후 및 INGESTION 상태)
          // 활성화(REGISTER_COMPLETED, REGISTER_FAILED, INGESTION_COMPLETED, INGESTION_FAILED)
          if (!status.equals("INGESTION") &&
              (status.equals("REGISTER_COMPLETED")
                  || status.equals("REGISTER_FAILED")
                  || status.equals("INGESTION_COMPLETED")
                  || status.equals("INGESTION_FAILED"))) {
            collector.put("isActiveIngestion", true);
          } else {
            collector.put("isActiveIngestion", false);
          }
        } else {
          collector.put("isActiveIngestion", false);
        }

        // HIVE인 경우 core-site.xml, hdfs-site.xml 파일 조회
        List<Map<String, Object>> fileInfoList = collectorMapper.getFileInfoList(system_id);
        Map<String, Object> coreSiteFileInfo = null;
        Map<String, Object> hdfsSiteFileInfo = null;
        Map<String, Object> esFileInfo = null;
        if (fileInfoList != null) {
          for (int j = 0; j < fileInfoList.size(); j++) {
            if (collector.get("system_type").equals("Hive")){
              if (fileInfoList.get(j).get("file_name").toString().equals("core-site.xml")) {
              coreSiteFileInfo = fileInfoList.get(j);
              } else if (fileInfoList.get(j).get("file_name").toString().equals("hdfs-site.xml")) {
                hdfsSiteFileInfo = fileInfoList.get(j);
              }
            }
            if (collector.get("system_type").equals("ElasticSearch")) {
              esFileInfo = fileInfoList.get(j);
            }
          }
        }

        collector.put("core_site_file", coreSiteFileInfo);
        collector.put("hdfs_site_file", hdfsSiteFileInfo);
        collector.put("es_ssl_file", esFileInfo);

        result.put("result", true);
        result.put("collector", collector);
      } else {
        result.put("result", false);
        result.put("collector", null);
      }
    } catch (Exception e) {
      log.error("Fail to select one system info..");

      result.put("result", false);
      result.put("collector", null);
    }

    return result;
  }

  public Map<String, Object> getMinioSystemInfo() throws Exception {
    Map<String, Object> result = new HashMap<>();

    try {
      String system_type = ""; // 시스템 타입
      String system_name = ""; // 시스템 명
      String meta_type = ""; // 데이터 타입
      String bucket_name = ""; // 파일 위치 (버켓명)
      // 1. code테이블에서 비정형 전용 시스템 타입, 시스템 명 조회
      List<Map<String, Object>> minioCodeList = collectorMapper.getMinioCodeInfo();
      if (minioCodeList != null && minioCodeList.size() > 0) {
        for (int i=0; i<minioCodeList.size(); i++) {
          if (minioCodeList.get(i).get("name") != null && minioCodeList.get(i).get("name").equals("system_type"))
            system_type = minioCodeList.get(i).get("value").toString();
            
          if (minioCodeList.get(i).get("name") != null && minioCodeList.get(i).get("name").equals("system_name"))
            system_name = minioCodeList.get(i).get("value").toString();

          if (minioCodeList.get(i).get("name") != null && minioCodeList.get(i).get("name").equals("meta_type"))
            meta_type = minioCodeList.get(i).get("value").toString();

          if (minioCodeList.get(i).get("name") != null && minioCodeList.get(i).get("name").equals("bucket_name"))
            bucket_name = minioCodeList.get(i).get("value").toString();
        }

        if (system_name != null && !system_name.equals("") && system_type != null && !system_type.equals("")) {
          Map<String, Object> collectorMap = collectorMapper.getMinioSystemInfo(system_name, system_type);
          if (collectorMap != null) {
            collectorMap.put("meta_type", meta_type);
            collectorMap.put("bucket_name", bucket_name);
            result.put("result", true);
            result.put("minioInfo", collectorMap);
          } else {
            log.error("Fail to select one for system info.. - no info");

            result.put("result", false);
            result.put("minioInfo", null);
          }
        } else {
          log.error("Fail to select one for system info.. - no system_name or system_type");

          result.put("result", false);
          result.put("minioInfo", null);
        }
      } else {
        log.error("Fail to select one for system info.. - no code list");

        result.put("result", false);
        result.put("minioInfo", null);
      }

    } catch (Exception e) {
      log.error("Fail to select one system info.. - exception");

      result.put("result", false);
      result.put("minioInfo", null);
    }
    
    return result;
  }

  // Airflow URL 조회
  public Map<String, Object> getAirflowUrl() throws Exception {
    Map<String, Object> result = new HashMap<>();
    result.put("airflowUrl", airflowUrl);
    return result;
  }

  // 실제 파일 삭제
  private void deleteRealFileFromSystemId(String system_id) throws Exception {
    List<Map<String, Object>> fileInfoList = new ArrayList<>();
    // 해당 파일의 첨부파일이 있는지 확인하여 있는 경우 첨부파일도 삭제 처리
    try {
      fileInfoList = collectorMapper.getCollectorFileInfoList(system_id);
    } catch (Exception e) {
      log.error("Fail to load FileInfoList");
    }
    if (fileInfoList != null && fileInfoList.size() > 0) {
      try {
        for (int i = 0; i < fileInfoList.size(); i++) {
          String file_id = fileInfoList.get(i).get("file_id").toString();
          String file_location = fileInfoList.get(i).get("file_location").toString();
          log.info("deleting real file id : " + file_id);
          log.info("deleting real file path : " + file_location);

          File file = new File(file_location);
          File parentDir = file.getParentFile();

          if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
              log.info("Success to delete file in server..");

              // DB에서 내역 삭제
              try {
                fileMapper.deleteFile(file_id);
              } catch (Exception e) {
                log.error("Fail to delete file info..");
              }
            } else {
              log.error("Fail to delete file in server..");
            }
          } else {
            log.info("No Exist file in server..");
          }

          // 마지막 파일까지 삭제 후 디렉토리 삭제
          if (i == fileInfoList.size() - 1 && parentDir != null) {
            deleteDirectory(parentDir);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        log.error("Fail to delete file in server..");
      }
    } else {
      log.error("No fileInfo..");
    }
  }

  private void deleteDirectory(File dir) {
    if (dir.exists()) {
      File[] files = dir.listFiles();
      if (files != null) { // 내부 파일 및 폴더 삭제
        for (File file : files) {
          if (file.isDirectory()) {
            deleteDirectory(file); // 재귀 호출
          } else {
            file.delete();
          }
        }
      }
      dir.delete(); // 최종적으로 빈 폴더 삭제
      log.info("success to delete directory..");
    } else {
      log.info("No exists directory..");
    }
  }
}