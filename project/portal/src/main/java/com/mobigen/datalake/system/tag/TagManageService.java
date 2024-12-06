package com.mobigen.datalake.system.tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.mobigen.framework.result.JsonResult;
import com.mobigen.framework.security.SessionManager;
import com.mobigen.framework.utility.AtlasErrorMap;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TagManageService {
  private final TagManageMapper tagManageMapper;
  private final SessionManager sessionManager;
  
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

    Object result = tagManageMapper.insertTagInfo(param);
    return result;
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

    if (updateResult == 0){
      isFailed = true;
    }

    result.put("isFailed", isFailed);
    return result;
  }
}