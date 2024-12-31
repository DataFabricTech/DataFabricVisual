package com.mobigen.datalake.diagram;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Repository("diagramMapper")
public interface DiagramMapper {
    ArrayList getNodeInfo(HashMap param) throws Exception;
    ArrayList getLinkInfo(HashMap param) throws Exception;
    ArrayList getChildNodeLink(String nodeID) throws Exception;
    ArrayList getParentNodeLink(String nodeID) throws Exception;
    ArrayList nodeList(String searchType, String searchWord, int offset, int limit) throws Exception;    
    Integer saveSelfNode(Map<String, Object> params);
    Integer updateSelfNode(Map<String, Object> params);
    ArrayList findTooltip(String guid);
    Integer insertNodeLink(Map param) throws Exception;
    Integer updateNodeLink(Map param) throws Exception;
    Integer saveNodeInfo(HashMap param) throws Exception;
    Integer insertNodeInfo(List<Map<String, Object>> param) throws Exception;
    Integer deleteNodeLink(Integer linkID) throws Exception;
    int checkDuplicate(String data);
    Integer insertNodeLinks(Map<String, Object> params) throws Exception;
    List getMetaDataForTooltip(List<String> guidList) throws Exception;
    Map<String, Object> getMetaDataForTooltipOne(Map<String, Object> params) throws Exception;
    List<Map<String, Object>> getClassificationList(String[] ids);
    List<String> getDbList() throws Exception;
    List<Map<String, Object>> getMetaDataList(Map<String, Object> params) throws Exception;
    int getMetaDataListTotal(Map<String, Object> params) throws Exception;
}
