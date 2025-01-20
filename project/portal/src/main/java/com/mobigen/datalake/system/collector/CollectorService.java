package com.mobigen.datalake.system.collector;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
  
  @Value("${properties.intergration.url}")
  private String intergrationExecuteUrl;
  
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

  // 수집대상 등록
  public Object addCollector(Map<String, Object> params) throws Exception {
    boolean result = false;
    int addCount = collectorMapper.addCollector(params);
    if (addCount > 0) {
      result = true;
    }
    return result;
  }

  // 수집대상 수정
  public Object updateCollector(Map<String, Object> params) throws Exception {
    boolean result = false;
    int updateCount = collectorMapper.updateCollector(params);
    if (updateCount > 0) {
      result = true;
    }
    return result;
  }

  // 수집대상 삭제
  public Object deleteCollector(String system_id) throws Exception {
    boolean result = false;
    int deleteCount = collectorMapper.deleteCollector(system_id);
    if (deleteCount > 0) {
      result = true;
    }
    return result;
  }

  // 수동 수집 실행
  public Object directCollect(String system_id) throws Exception {
    Map<String, Object> result = new HashMap<>();

    try {
      Map<String, Object> restResponse = intergrationExecute(system_id); 

      // 반환값 확인 필요
      if (restResponse != null && !restResponse.isEmpty()) {
          Map<String, Object> res = (Map<String, Object>) restResponse.get("result");
          if (res.get("result").equals("success")) {
            result.put("result", true);
          } else {
            result.put("result", false);
          }
        }
    } catch (Exception e) {
      result.put("result", false);
      e.printStackTrace();
    }
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
  public Map<String, Object> intergrationExecute(String system_id) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String executeUrl = intergrationExecuteUrl + "?system_id=" + system_id;

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> restResponse = restTemplate.postForObject(executeUrl, requestBody, HashMap.class);
        return restResponse;
    }
}