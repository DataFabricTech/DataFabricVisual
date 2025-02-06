package com.mobigen.datalake.user;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.Get;
import org.springframework.stereotype.Repository;

@Repository("userMapper")
public interface UserMapper {
    Object getUser(String username) throws Exception;
    Object getUserMaster(String username) throws Exception;
    Object getApprovalUserMaster(String username) throws Exception;
    Object getPendingApprovalUsers(String username) throws Exception;
    int getCheckDeptExists(Map<String, String> param) throws Exception;
    int getCheckUserExists(Map<String, String> param) throws Exception;
    Integer updateLoginUser(String user_id) throws Exception;
    Integer updateLoginFailCount(String user_id) throws Exception;    
    Integer insertLoginHistory(Map<String, String> param) throws Exception;    
    String getLastLoginTime(String user_id) throws Exception;
    List<Map> getDepartments(String word) throws Exception;
    int deptRegister(Map<String, String> params) throws Exception;
    int deptDelete(Map<String, String> params) throws Exception;
    int checkDupUserId(String user_id) throws Exception;
    int registerUser(Map<String, String> params) throws Exception;
    List<Map> getRoleList(String role_type) throws Exception;
    List<Map> getDeptInfo(String role_type) throws Exception;
    Integer delete(Map<String, String> param) throws Exception;    
    Integer updatePassword(Map<String, String> param) throws Exception;
}
