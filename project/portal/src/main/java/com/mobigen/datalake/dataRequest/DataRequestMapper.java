package com.mobigen.datalake.dataRequest;

import java.util.List;
import java.util.Map;

public interface DataRequestMapper {
    List getDataRequestList(Map<String, String> parma) throws Exception;
    Integer getDataRequestListTotal(Map<String, String> parma) throws Exception;

    List getDataRequestAnswerList(String id);
    int getDataRequestAnswerListCount(int id);
    Map<String, Object> getDataRequestOne(String id) throws Exception;

    Integer insertDataRequest(Map<String, Object> param) throws Exception;
    List getMailUserList() throws Exception;

    Integer deleteDataRequest(Integer id) throws Exception;
    Integer updateDataRequest(Map<String, Object> param) throws Exception;
    int updateDataRequestStatus(int id, int process_status) throws Exception;

    Integer updateAdminReply(Map<String, Object> param) throws Exception;
    Integer insertDataRequestAnswer(Map<String, Object> param) throws Exception;
    Integer updateDataRequestAnswer(Map<String, Object> param) throws Exception;
    Integer deleteAdminReply(Integer id) throws Exception;
    Integer deleteAdminReplyFromDataRequest(Integer id) throws Exception;

    Integer getLatestAnswerProcessStatus(Integer data_request_id) throws Exception;

    List<Map<String, Object>> getDataRequestFileInfoList(Integer id) throws Exception;
    Map<String, Object> getFileInfo(Integer file_id) throws Exception;
}
