package com.mobigen.datalake.system.role;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.core.type.TypeReference;

@Service
@RequiredArgsConstructor
public class RoleManagementService {
    private final RoleManagementMapper roleManagementMapper;

    @Value("${properties.essync.url.roleupdates}")
    private String esSyncUrlRoleUpdates;

    // 권한 관리 리스트 조회
    public List<Map> getRoleManagementSearchList(Map<String, Object> param) throws Exception {
        List<Map> searchList = (List<Map>) roleManagementMapper.getRoleManagementSearchList(param);
        return searchList;
    }

    // 권한 관리 리스트 조회 카운트
    public Integer getRoleManagementSearchListCount(Map<String, Object> param) throws Exception {
        Integer total = roleManagementMapper.getRoleManagementSearchListCount(param);
        return total;
    }

    // 권한 관리 삭제
    public Integer deleteRoleItem(@RequestBody Map<String, Object> param) throws Exception {
        Integer total = roleManagementMapper.deleteRoleItem(param);
        return total;
    }

    // 권한 관리 등록
    public Integer registerRoleItem(@RequestBody Map<String, Object> param) throws Exception {
        Integer total = roleManagementMapper.registerRoleItem(param);
        return total;
    }

    // 권한 관리 조회
    public List<Map> searchRoleItem(@RequestBody Map<String, Object> param) throws Exception {
        List<Map> searchList = roleManagementMapper.searchRoleItem(param);
        return searchList;
    }

    // 권한 관리 조회
    public Integer getRoleUserCount(@RequestBody Map<String, Object> param) throws Exception {
        Integer total = roleManagementMapper.getRoleUserCount(param);
        return total;
    }

    // 권한 관리 상세 조회
    public List<Map> getRoleDetailSearchList(Map<String, Object> param) throws Exception {
        List<Map> searchList = (List<Map>) roleManagementMapper.getRoleDetailSearchList(param);
        return searchList;
    }

    // 권한 관리 조회 - 사용자
    public List<Map> getUserRoleSearchList(Map<String, Object> param) throws Exception {
        List<Map> searchList = (List<Map>) roleManagementMapper.getUserRoleSearchList(param);
        return searchList;
    }

    // 권한 관리 조회 - 사용자
    public Integer getUserRoleSearchListCount(@RequestBody Map<String, Object> param) throws Exception {
        Integer total = roleManagementMapper.getUserRoleSearchListCount(param);
        return total;
    }

    // 권한 관리 조회 - 조직
    public List<Map> getDeptRoleSearchList(Map<String, Object> param) throws Exception {
        List<Map> searchList = (List<Map>) roleManagementMapper.getDeptRoleSearchList(param);
        return searchList;
    }

    // 권한 관리 조회 - 조직
    public Integer getDeptRoleSearchListCount(@RequestBody Map<String, Object> param) throws Exception {
        Integer total = roleManagementMapper.getDeptRoleSearchListCount(param);
        return total;
    }

    // 권한 관리 조회 - 데이타
    public List<Map> getMetaRoleSearchList(Map<String, Object> param) throws Exception {
        List<Map> searchList = (List<Map>) roleManagementMapper.getMetaRoleSearchList(param);
        return searchList;
    }

    // 권한 관리 조회 - 데이타
    public Integer getMetaRoleSearchListCount(@RequestBody Map<String, Object> param) throws Exception {
        Integer total = roleManagementMapper.getMetaRoleSearchListCount(param);
        return total;
    }

    // 권한 관리 조회 - 사용자 조회
    public List<Map> searchUserRole(Map<String, Object> param) throws Exception {
        List<Map> searchList = (List<Map>) roleManagementMapper.searchUserRole(param);
        return searchList;
    }
    
    // 권한 관리 조회 - 사용자 저장
    public Integer registerUserRoleItem(List<Map<String, Object>> list) throws Exception {
        Integer total = roleManagementMapper.registerUserRoleItem(list);
        return total;
    }

    // 권한 관리 조회 - 사용자 삭제
    public Integer deleteUserRoleItem(List<Map<String, Object>> list) throws Exception {
        Integer total = roleManagementMapper.deleteUserRoleItem(list);
        return total;
    }
    
    // 권한 관리 조회 - 조직 조회
    public List<Map> searchDeptRole(Map<String, Object> param) throws Exception {
        List<Map> searchList = (List<Map>) roleManagementMapper.searchDeptRole(param);
        return searchList;
    }

    // 권한 관리 조회 - 조직 저장
    public Integer registerDeptRoleItem(List<Map<String, Object>> list) throws Exception {
        Integer total = roleManagementMapper.registerDeptRoleItem(list);
        return total;
    }

    // 권한 관리 조회 - 조직 삭제
    public Integer deleteDeptRoleItem(List<Map<String, Object>> list) throws Exception {
        Integer total = roleManagementMapper.deleteDeptRoleItem(list);
        return total;
    }

    // 권한 관리 조회 - 데이타 조회
    public List<Map> searchMetaRole(Map<String, Object> param) throws Exception {
        List<Map> searchList = (List<Map>) roleManagementMapper.searchMetaRole(param);
        return searchList;
    }

    // 권한 관리 조회 - 데이타 저장
    public Integer registerMetaRoleItem(List<Map<String, Object>> list) throws Exception {
        Integer total = roleManagementMapper.registerMetaRoleItem(list);
        return total;
    }

    // 권한 관리 조회 - 데이타 삭제
    public Integer deleteMetaRoleItem(List<Map<String, Object>> list) throws Exception {
        Integer total = roleManagementMapper.deleteMetaRoleItem(list);
        return total;
    }


    // 데이타 권한 조회 - 조회
    public Map<String, Object> getMetaDataManageList(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        int totCnt = 0;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        totCnt = roleManagementMapper.getMetaDataManageListTotCnt(params);
        resultList = roleManagementMapper.getMetaDataManageList(params);

        result.put("totalCount", totCnt);
        result.put("list", resultList);

        return result;
    }

    // 데이타 권한 조회(상세) - 조회
    public List<Map> getMetaDataInfo(Map<String, Object> param) throws Exception {
        List<Map> searchList = (List<Map>) roleManagementMapper.getMetaDataInfo(param);
        return searchList;
    }

    // 데이타 권한 조회 - 리스트 조회
    public List<Map> getMetaDataList(Map<String, Object> param) throws Exception {
        List<Map> searchList = (List<Map>) roleManagementMapper.getMetaDataList(param);
        return searchList;
    }

    // 데이타 권한 조회(상세) - 리스트 조회 건수
    public Integer getMetaDataListCount(Map<String, Object> param) throws Exception {
        Integer total = roleManagementMapper.getMetaDataListCount(param);
        return total;
    }
    
    // 사용자 권한 조회 - 조회
    public List<Map> getUserDataList(Map<String, Object> param) throws Exception {
        List<Map> searchList = (List<Map>) roleManagementMapper.getUserDataList(param);
        return searchList;
    }

    // 사용자 권한 조회 - 리스트 조회 건수
    public Integer getUserDataListCount(Map<String, Object> param) throws Exception {
        Integer total = roleManagementMapper.getUserDataListCount(param);
        return total;
    }

    // 사용자 권한 조회(상세) - 조회
    public List<Map> getUserDataInfo(Map<String, Object> param) throws Exception {
        List<Map> searchList = (List<Map>) roleManagementMapper.getUserDataInfo(param);
        return searchList;
    }
    
    // 사용자 권한 조회(상세) - 리스트 조회
    public List<Map> getUserMetaDataList(Map<String, Object> param) throws Exception {
        List<Map> searchList = (List<Map>) roleManagementMapper.getUserMetaDataList(param);
        return searchList;
    }

    // 사용자 권한 조회(상세) - 리스트 조회 건수
    public Integer getUserMetaDataListCount(Map<String, Object> param) throws Exception {
        Integer total = roleManagementMapper.getUserMetaDataListCount(param);
        return total;
    }

    // es - 권한 동기화
    public boolean essyncRolesUpdates(List<String> roleIds, String roleStauts) throws Exception {
        boolean result = false;

        try {
            // 권한 타입이 데이터 권한인 경우에만 동기화
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("role_id", roleIds.get(0));
            List<Map> searchList = roleManagementMapper.searchRoleItem(param);

            if (searchList.size() > 0 && searchList.get(0).get("role_type").toString().equals("0")) {
                return true;
            }

            // role id로 meta role 조회
            if(!"delete".equals(roleStauts)) {
                searchList = roleManagementMapper.searchMetaRoleInfo(param);

                if (searchList == null || searchList.size() == 0) {
                    return true;
                }
            }

            // rest api 호출
            RestTemplate restTemplate = new RestTemplate();
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("roleids", roleIds);
            ResponseEntity<String> restResponse = restTemplate.postForEntity(esSyncUrlRoleUpdates, requestBody, String.class);
            
            if (restResponse != null && restResponse.getBody() != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> body = objectMapper.readValue(restResponse.getBody(), new TypeReference<Map<String, Object>>(){});
                if (body != null && body.get("result") != null) {
                    Map<String, Object> bodyResultMap = (Map<String, Object>) body.get("result");
                    if (bodyResultMap != null) {
                        String rs = bodyResultMap.get("result").toString();
                        if (rs.equals("success")) {
                            result = true;
                        }

                        // 
                        if (bodyResultMap.get("message").toString().contains("roleids Meta Not Found")) {
                            result = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        
        return result;
    }

}
