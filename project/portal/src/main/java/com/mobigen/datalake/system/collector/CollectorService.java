package com.mobigen.datalake.system.collector;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobigen.datalake.post.file.FileMapper;
import com.mobigen.framework.utility.AESGCMUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  
  Map<String, Object> getCollectorListInfo(Map<String, Object> params) throws Exception {
      Map resultMap = new HashMap<>();
      List<Map<String, Object>> list = new ArrayList<>();
      int total = 0;

      try {
        total = collectorMapper.getCollectorListTotal(params);
        list = collectorMapper.getCollectorList(params);

        if (list != null && list.size() > 0) {
          for (int i=0; i<list.size(); i++) {
            // filter_config값 Map 형태로 변환
            list.get(i).put("filter_config", convertJsonbToMap(list.get(i).get("filter_config")));

            // 상태값에 따라 수동수집 버튼 활성화 여부 값 설정
            String status = list.get(i).get("status") != null ? list.get(i).get("status").toString() : null;
            if (status != null) {
              // 비활성화(1. 등록 직후, 2. 즉시수집 클릭 직후 및 INGESTION 상태)
              // 활성화(REGISTER_COMPLETED, INGESTION_COMPLETED, INGESTION_FAILED)
              if (!status.equals("INGESTION") &&
                  (status.equals("REGISTER_COMPLETED")
                 ||status.equals("INGESTION_COMPLETED")
                 ||status.equals("INGESTION_FAILED")
                )) {
                list.get(i).put("isActiveIngestion", true);
              } else {
                list.get(i).put("isActiveIngestion", false);
              }
            } else {
              list.get(i).put("isActiveIngestion", false);
            }

            // HIVE인 경우 core-site.xml, hdfs-site.xml 파일 조회
            String system_id = list.get(i).get("system_id").toString();
            List<Map<String, Object>> fileInfoList = collectorMapper.getFileInfoList(system_id);
            Map<String, Object> coreSiteFileInfo = null;
            Map<String, Object> hdfsSiteFileInfo = null;
            if (fileInfoList != null) {
              for (int j=0; j<fileInfoList.size(); j++) {
                if (fileInfoList.get(j).get("file_name").toString().equals("core-site.xml")) {
                  coreSiteFileInfo = fileInfoList.get(j);
                } else if (fileInfoList.get(j).get("file_name").toString().equals("hdfs-site.xml")) {
                  hdfsSiteFileInfo = fileInfoList.get(j);
                }
              }
            }

            list.get(i).put("core_site_file", coreSiteFileInfo);
            list.get(i).put("hdfs_site_file", hdfsSiteFileInfo);
          }
        }

        // jsonb 타입 => Map 변환
        resultMap.put("list", list);
        resultMap.put("total",total);

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
      params.put("password", encoder.encrypt(params.get("password").toString()));
      
      addCount = collectorMapper.addCollector(params);
      if (addCount > 0) {
        putIngestionDagApi(params.get("system_id").toString());
      }

      result.put("addCount", addCount);
    } catch (Exception e) {
      result.put("addCount", 0);
    }
    
    return result;
  }

  // 수집대상 수정 (put으로 같이 사용)
  public Object updateCollector(Map<String, Object> params) throws Exception {
    Map<String, Object> result = new HashMap<>();
    int updateCount = 0;

    try {
      // 비밀번호를 입력한 경우는 암호화해서 같이 저장
      if (params.get("password") != null && !params.get("password").equals("")) {
        AESGCMUtil encoder = new AESGCMUtil(cryptKey);
        params.put("password", encoder.encrypt(params.get("password").toString()));
        updateCount = collectorMapper.updateCollector(params);
      } else {
        params.remove("password");
        updateCount = collectorMapper.updateCollectorNoPassword(params);
      }

      if (updateCount > 0) {
        putIngestionDagApi(params.get("system_id").toString());
      }

      result.put("updateCount", updateCount);
    } catch (Exception e) {
      result.put("updateCount", 0);
    }
    
    return result;
  }

  // 수집대상 삭제
  public Object deleteCollector(String system_id) throws Exception {
    boolean result = false;
    boolean apiResult = false;
    int deleteCount = collectorMapper.deleteCollector(system_id);
    if (deleteCount > 0) {
      // 실제 파일 삭제 처리
      deleteRealFileFromSystemId(system_id);
      result = true;
      apiResult = deleteIngestionDagApi(system_id);
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
        return objectMapper.readValue((String) jsonb, new TypeReference<Map<String, Object>>() {});    
    }
    return null;
  }
 
  // 수동 수집 실행 POST, system_id는 queryString
  public boolean ingestionExecute(String system_id) throws Exception {
    RestTemplate restTemplate = new RestTemplate();

    String executeUrl = catalogApiBaseUrl + catalogApiUrlExecute + "?system_id=" + system_id;

    Map<String, Object> requestBody = new HashMap<>();
    ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(executeUrl, requestBody, HashMap.class);
    if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
      return true;
    }
    
    return false;
  }

  // 수집등록 시 DAG 등록 API (POST)
  // private boolean addIngestionDagApi(String system_id) throws Exception {
  //   RestTemplate restTemplate = new RestTemplate();
  //   String executeUrl = catalogApiBaseUrl + catalogApiUrlDag;

  //   Map<String, Object> requestBody = new HashMap<>();
  //   requestBody.put("system_id", system_id);
  //   try {
  //     ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity(executeUrl, requestBody, HashMap.class);
  //     if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
  //       return true;
  //     }
  //   } catch (Exception e) {
  //     e.printStackTrace();
  //     return false;
  //   }

  //   return false;
  // }

  // 수집수정 시 DAG 수정 API (PUT)
  private boolean putIngestionDagApi(String system_id) throws Exception {
    RestTemplate restTemplate = new RestTemplate();
    String executeUrl = catalogApiBaseUrl + catalogApiUrlDag;

    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      String requestBody = "{ \"system_id\": \"" + system_id + "\" }";
      HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
      ResponseEntity<HashMap> responseEntity = restTemplate.exchange(executeUrl, HttpMethod.PUT, requestEntity, HashMap.class);
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
      ResponseEntity<HashMap> responseEntity = restTemplate.exchange(executeUrl, HttpMethod.DELETE, requestEntity, HashMap.class);
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
      
      
    } catch(Exception e) {
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
          // 활성화(REGISTER_COMPLETED, INGESTION_COMPLETED, INGESTION_FAILED)
          if (!status.equals("INGESTION") &&
              (status.equals("REGISTER_COMPLETED")
              ||status.equals("INGESTION_COMPLETED")
              ||status.equals("INGESTION_FAILED")
            )) {
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
        if (fileInfoList != null) {
          for (int j=0; j<fileInfoList.size(); j++) {
            if (fileInfoList.get(j).get("file_name").toString().equals("core-site.xml")) {
              coreSiteFileInfo = fileInfoList.get(j);
            } else if (fileInfoList.get(j).get("file_name").toString().equals("hdfs-site.xml")) {
              hdfsSiteFileInfo = fileInfoList.get(j);
            }
          }
        }

        collector.put("core_site_file", coreSiteFileInfo);
        collector.put("hdfs_site_file", hdfsSiteFileInfo);
        
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
                for (int i=0; i<fileInfoList.size(); i++) {
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