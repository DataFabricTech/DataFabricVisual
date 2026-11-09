package com.mobigen.datalake.system.role;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Repository("roleManagement")
public interface RoleManagementMapper {
    List getRoleManagementSearchList(Map<String, Object> param) throws Exception;

    Integer getRoleManagementSearchListCount(Map<String, Object> param) throws Exception;

    Integer deleteRoleItem(@RequestBody Map<String, Object> param) throws Exception;

    Integer registerRoleItem(@RequestBody Map<String, Object> param) throws Exception;

    List searchRoleItem(Map<String, Object> param) throws Exception;

    Integer getRoleUserCount(Map<String, Object> param) throws Exception;

    List getRoleDetailSearchList(Map<String, Object> param) throws Exception;

    List getUserRoleSearchList(Map<String, Object> param) throws Exception;

    Integer getUserRoleSearchListCount(Map<String, Object> param) throws Exception;

    List getDeptRoleSearchList(Map<String, Object> param) throws Exception;

    Integer getDeptRoleSearchListCount(Map<String, Object> param) throws Exception;

    List getMetaRoleSearchList(Map<String, Object> param) throws Exception;

    Integer getMetaRoleSearchListCount(Map<String, Object> param) throws Exception;

    List searchUserRole(Map<String, Object> param) throws Exception;

    Integer registerUserRoleItem(List<Map<String, Object>> list) throws Exception;

    Integer deleteUserRoleItem(List<Map<String, Object>> list) throws Exception;

    List searchDeptRole(Map<String, Object> param) throws Exception;

    Integer registerDeptRoleItem(List<Map<String, Object>> list) throws Exception;

    Integer deleteDeptRoleItem(List<Map<String, Object>> list) throws Exception;

    List searchMetaRole(Map<String, Object> param) throws Exception;

    Integer registerMetaRoleItem(List<Map<String, Object>> list) throws Exception;

    Integer deleteMetaRoleItem(List<Map<String, Object>> list) throws Exception;

    List<Map<String, Object>> getMetaDataManageList(Map<String, Object> params) throws Exception;

    int getMetaDataManageListTotCnt(Map<String, Object> params) throws Exception;

    List getMetaDataInfo(Map<String, Object> param) throws Exception;

    List getMetaDataList(Map<String, Object> param) throws Exception;

    Integer getMetaDataListCount(Map<String, Object> param) throws Exception;

    List getUserDataInfo(Map<String, Object> param) throws Exception;

    List getUserDataList(Map<String, Object> param) throws Exception;

    Integer getUserDataListCount(Map<String, Object> param) throws Exception;

    List getUserMetaDataList(Map<String, Object> param) throws Exception;

    Integer getUserMetaDataListCount(Map<String, Object> param) throws Exception;

    List searchMetaRoleInfo(Map<String, Object> param) throws Exception;

    Integer getMenuListCount(Map<String, Object> param) throws Exception;

    List getMenuList(Map<String, Object> param) throws Exception;

    Integer menuDelete(Map<String, Object> param) throws Exception;

    Integer menuRegister(Map<String, Object> param) throws Exception;

    List menuDeleteCheck(Map<String, Object> param) throws Exception;
}