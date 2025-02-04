package com.mobigen.datalake.system.collector;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CollectorService {
  private final CollectorMapper collectorMapper;    
  private final ObjectMapper objectMapper;
  
  @Value("${properties.ingestion.url}")
  private String ingestionExecuteUrl;
  
  Map<String, Object> getCollectorListInfo(Map<String, Object> params) throws Exception {
      Map resultMap = new HashMap<>();
      List<Map<String, Object>> list = new ArrayList<>();
      int total = 0;

      try {
        total = collectorMapper.getCollectorListTotal(params);
        list = collectorMapper.getCollectorList(params);

        if (list != null && list.size() > 0) {
          for (int i=0; i<list.size(); i++) {
            list.get(i).put("filter_config", convertJsonbToMap(list.get(i).get("filter_config")));
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
    boolean result = false;
    boolean apiResult = false;
    int addCount = collectorMapper.addCollector(params);
    if (addCount > 0) {
      result = true;
      apiResult = putIngestionDagApi(params.get("system_id").toString());
    }
    return result;
  }

  // 수집대상 수정 (put으로 같이 사용)
  public Object updateCollector(Map<String, Object> params) throws Exception {
    boolean result = false;
    boolean apiResult = false;
    int updateCount = collectorMapper.updateCollector(params);
    if (updateCount > 0) {
      result = true;
      apiResult = putIngestionDagApi(params.get("system_id").toString());
    }
    return result;
  }

  // 수집대상 삭제
  public Object deleteCollector(String system_id) throws Exception {
    boolean result = false;
    boolean apiResult = false;
    int deleteCount = collectorMapper.deleteCollector(system_id);
    if (deleteCount > 0) {
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
    String executeUrl = ingestionExecuteUrl + "/execute?system_id=" + system_id;

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
  //   String executeUrl = ingestionExecuteUrl + "/dag";

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
    String executeUrl = ingestionExecuteUrl + "/dag";

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
    String executeUrl = ingestionExecuteUrl + "/dag";

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
}