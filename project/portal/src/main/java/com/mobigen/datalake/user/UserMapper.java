package com.mobigen.datalake.user;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("userMapper")
public interface UserMapper {
    Object getUser(String username) throws Exception;
    Object getUserMaster(String username) throws Exception;
    Integer updateLoginUser(String user_id) throws Exception;
    Integer updateLoginFailCount(String user_id) throws Exception;    
    Integer insertLoginHistory(Map<String, String> param) throws Exception;    
    String getLastLoginTime(String user_id) throws Exception;
    List<Map> getAllDepartments(String word) throws Exception;
}
