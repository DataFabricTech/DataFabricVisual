package com.mobigen.datalake.system.tag;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.security.SessionManager;
import com.mobigen.framework.utility.AtlasErrorMap;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagManageService {
  private final TagManageMapper tagManageMapper;
  private final SessionManager sessionManager;
  
  @Value("${properties.ESSync.base_url}")
  private String esSyncBaseUrl;

  @Value("${properties.ESSync.url.update}")
  private String esSyncUrlUpdate;
  
  /**
   * Tag 추가
   */
  public Object insertTagInfo(Map<String, String> param, HttpServletRequest request) throws Exception {
    Map<String, Object> user = sessionManager.getUserByAccessToken(request);
    String loginId = "";
    if(user != null) {
      loginId = user.get("user_id").toString();
    }

    param.put("login_id", loginId);

    Object insertResult = tagManageMapper.insertTagInfo(param);
    return insertResult;
  }

  /**
   * Tag 목록 조회
   */
  public List<Map> getTagList(Map<String, String> param) throws Exception {
    List<Map> tag = (List<Map>) tagManageMapper.getTagList(param);
    return tag;
  }

  /**
   * Tag 목록 개수 조회
   */
  public Integer getTagListTotal(Map<String, String> param) throws Exception {
      Integer total = tagManageMapper.getTagListTotal(param);
      return total;
  }

  // tag 삭제
  public Object deleteTagInfo(Map<String, String> param) throws Exception {
    String tag_name = param.get("tag_name");

    int rs = tagManageMapper.deleteTagInfo(tag_name);

    Map<String, Object> result = new HashMap<>();
    Boolean isFailed = false;
    result.put("isFailed", isFailed);
    return result;
  }  

  //tag 매핑
  public Object mapLabels(Map<String, Object> param, HttpServletRequest request) throws Exception {
    Map<String, Object> result = new HashMap<>();
    Boolean isFailed = false;

    String guid = param.get("guid").toString();
    List<String> tagList = (List<String>)param.get("tagList");
    List<String> deleteTagList = (List<String>)param.get("deleteTagList");

    //post는 기존꺼 초기화 하고 새로 넣기 (tag 매핑 삭제시 사용)
    //put은 기존꺼에 append , 중복은 덮어씀 (tag 매핑 추가시 사용)
    int updateResult = 0;
    if(request.getMethod().equals("POST")){
      Map<String, Object> upsertParams = new HashMap<>();
      upsertParams.put("guid", guid);

      String[] stringArray = tagList.toArray(new String[0]);
      upsertParams.put("tags", stringArray);

      // 1. 전체 태그 덮어쓰기로 저장
      updateResult += tagManageMapper.upsertMetaDataTag(upsertParams);
      

      //delete tag 목록은 개수 -1 함
      for (String tag_name : deleteTagList) {        
        tagManageMapper.mapperCountDown(tag_name);        
      }
      
    }else if(request.getMethod().equals("PUT")){
      // 매핑된 태그는 개수 +1 하고, count 테이블에 태그가 입력 안되어 있으면 insert 함.
      int isUpdated = 0;
      for (String tag_name : tagList) {
        Map<String, Object> insertParams = new HashMap<>();
        insertParams.put("guid", guid);
        insertParams.put("tag_name", tag_name);
        isUpdated = tagManageMapper.updateMetaDataTag(insertParams);

        if (isUpdated == 0) { // 이미 추가된 데이터였으면 태그 카운트 증가하지 않음
          continue;
        } else {
          updateResult += isUpdated;
          isUpdated = 0;
        }

        Integer tag_cnt = tagManageMapper.checkTagCount(tag_name);
        if(tag_cnt == 0){
          tagManageMapper.insertTagCount(tag_name);
        }else{
          tagManageMapper.mapperCount(tag_name);
        }
      }      
    }

    //es sync api 호출
    Integer failCount = 0;

    Map<String, Object> esParam = new HashMap<>();
    esParam.put("meta_id", guid);
    esParam.put("event_status", 2);
    esParam.put("meta_id", "카테고리 매핑");
    Map<String, Object> restResponse = essyncUpdate(esParam);
    if (!restResponse.isEmpty()) {
        Map<String, String> res = (Map<String, String>) restResponse.get("result");
        if (res.get("result").equals("success")) {
        } else {
            failCount++;                    
        }
    }
    if(failCount > 0){
        result.put("essyncIsFailed", true);
    }

    if (updateResult == 0){
      isFailed = true;
    }

    result.put("isFailed", isFailed);
    return result;
  }

  public Map<String, Object> essyncUpdate(Map<String, Object> params) throws Exception {
    Map<String, Object> restResponse = new HashMap<>();
    try {
      RestTemplate restTemplate = new RestTemplate();
      
      Map<String, Object> requestBody = new HashMap<>();
      requestBody.put("metaid", params.get("meta_id"));
      requestBody.put("event_status", params.get("event_status")); // 1=생성, 2=수정, 3=삭제
      requestBody.put("event_name", params.get("event_name"));

      String url = esSyncBaseUrl + esSyncUrlUpdate;
      restResponse = restTemplate.postForObject(url, requestBody, HashMap.class);
    } catch (Exception e) {
      log.error("essyncUpdate API 호출 오류");
    }
      
    return restResponse;
  }
}