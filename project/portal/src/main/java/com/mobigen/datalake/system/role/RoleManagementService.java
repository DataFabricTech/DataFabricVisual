package com.mobigen.datalake.system.role;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class RoleManagementService {
    private final RoleManagementMapper roleManagementMapper;

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

    // 권한 관리 조회 - 데이타타 삭제
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

}
