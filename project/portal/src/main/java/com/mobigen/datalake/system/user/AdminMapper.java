package com.mobigen.datalake.system.user;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("adminMapper")
public interface AdminMapper {
    List getUserList(Map<String, Object> param) throws Exception;

    Integer getUserListTotal(Map<String, Object> param) throws Exception;

    Object getUser(String user_id);

    Integer updateUserInfo(Map<String, String> param) throws Exception;

    List getUserListWithRoleName(Map<String, Object> param) throws Exception;

    Integer getUserListTotalWithRoleName(Map<String, Object> param) throws Exception;

    Integer initPassword(Map<String, String> param) throws Exception;

    List<Map<String, Object>> getEmailManageList() throws Exception;

    List<Map<String, Object>> getLogoImageList() throws Exception;

    List<Map<String, Object>> getCommonCodeList(Map<String, String> param) throws Exception;

    Integer isExistsLogoFileId() throws Exception;
    Integer insertLogoFileId(String file_id) throws Exception;
    Integer updateLogoFileId(String file_id) throws Exception;
}