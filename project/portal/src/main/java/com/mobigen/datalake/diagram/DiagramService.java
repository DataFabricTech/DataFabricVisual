package com.mobigen.datalake.diagram;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobigen.framework.component.Messages;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DiagramService {
    private final Messages message;
    private final DiagramMapper diagramMapper;
    private final ObjectMapper objectMapper;

    public Object getMessage() {
        return message.get("sample.data");
    }

    public HashMap getNodeRelation(String direction, String nodeId) throws Exception {
        HashMap param = new HashMap();
        HashMap returmMap = new HashMap<>();

        param.put("nodeId", nodeId);
        param.put("direction", direction);

        ArrayList nodeList = diagramMapper.getNodeInfo(param);

        param.put("nodeList", nodeList);

        if (nodeList.size() > 0) returmMap.put("linkInfo", diagramMapper.getLinkInfo(param));
        returmMap.put("nodeInfo", nodeList);
        
        return returmMap;
    }

    public Object getImages(int count) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Map<String, Object> image = new HashMap<>();
            image.put("id", i);
            image.put("src", "https://i.pravatar.cc/300?img=" + (i + 1));
            list.add(image);
        }
        return list;
    }

    public Object getNodeLinksInfo(String guid)throws Exception {
        Map result = new HashMap();
        ArrayList links = new ArrayList();

        // to-node 목록 검색
        List children = new ArrayList();
        children = this.getChildNodeLink(children, guid);

        // from-node 목록 검색
        List parents = new ArrayList();
        parents = this.getParentNodeLink(parents, guid);

        // 최상위 node가 list 상단에 가도록 from-node 목록 역순으로 추가
        int index = parents.size() - 1;
        for (int i = index; i >= 0; i--) {
            links.add(parents.get(i));
        }
        links.addAll(children);

        result.put("links", links);
        return result;
    }

    /** to node 목록 검색 */
    private List getChildNodeLink(List children, String nodeID)throws Exception {
        List<Map> tempChildren = diagramMapper.getChildNodeLink(nodeID);

        if (tempChildren.size() > 0) {
            children.addAll(tempChildren);

            // tempChildren.forEach(child -> {     String toNodeID = (String)
            // child.get("to_node_id");     try {         this.getChildNodeLink(children,
            // toNodeID);     } catch (Exception e) {         throw new RuntimeException(e);
            // } });
        }

        return children;
    }

    /** from node 목록 검색 */
    private List getParentNodeLink(List parents, String nodeID)throws Exception {
        List<Map> tempParent = diagramMapper.getParentNodeLink(nodeID);

        if (tempParent.size() > 0) {
            parents.addAll(tempParent);

            // tempParent.forEach(parent -> {     String fromNodeID = (String)
            // parent.get("from_node_id");     try {         this.getParentNodeLink(parents,
            // fromNodeID);     } catch (Exception e) {         throw new
            // RuntimeException(e);     } });
        }

        return parents;
    }

    public Object getNodeInfo(String guid)throws Exception {
        List<Map<String, Object>> result = null;
        result = diagramMapper.findTooltip(guid);
        return result;
    }

    public Integer saveNodeInfo(List<Map<String, Object>> params)throws Exception {
        Integer result = 0;
        result = diagramMapper.insertNodeInfo(params);
        return result;
    }

    public Integer saveNodeLinksInfo(List<Map<String, Object>> params)throws Exception {
        Integer result = 0;

        for (Map node : params) {
            String linkID = (String)node.get("link_id");
            if (linkID == null || ObjectUtils.isEmpty(linkID)) {
                // link_id 없으면 신규 등록
                result += diagramMapper.insertNodeLink(node);
            } else {
                // link_id 있으면 수정
                result += diagramMapper.updateNodeLink(node);
            }

            result += this.saveNodeInfo(node);
        }

        return result;
    }

    private Integer saveNodeInfo(Map<String, Object> node)throws Exception {
        Integer result = 0;

        HashMap fromNode = new HashMap(){{
            put("node_id", node.get("from_node_id"));
            put("node_name", node.get("from_node_name"));
        }};
        result += diagramMapper.saveNodeInfo(fromNode);

        HashMap toNode = new HashMap(){{
            put("node_id", node.get("to_node_id"));
            put("node_name", node.get("to_node_name"));
        }};
        result += diagramMapper.saveNodeInfo(toNode);

        return result;
    }

    public Object deleteNodeLink(Integer linkID)throws Exception {
        return diagramMapper.deleteNodeLink(linkID);
    }    

    public Boolean checkDuplicate(String data) {
        int count = diagramMapper.checkDuplicate(data);
        return count > 0;
     }

    public Integer saveSelfNode(Map<String, Object> params) {
        Integer result = 0;
         result = diagramMapper.saveSelfNode(params);
        return result;
    }

    public Integer updateSelfNode(Map<String, Object> params) {
        Integer result = 0;
        result = diagramMapper.updateSelfNode(params);
        return result;
    }

    public Object saveNodeLink(Map<String, Object> params)throws Exception {
        return diagramMapper.insertNodeLinks(params);
    }

    public List<Map<String, Object>> getMetaDataForTooltip(List<String> guidList) throws Exception {
        List<Map<String, Object>> metaInfoList = new ArrayList<>();
        if (guidList != null && guidList.size() > 0) {
            metaInfoList = diagramMapper.getMetaDataForTooltip(guidList);

            for (int i=0; i<metaInfoList.size(); i++) {
                // Postgresql jsonb 타입(JSON 문자열) Map 형태로 변환
                metaInfoList.get(i).put("attributes", convertJsonbToMap(metaInfoList.get(i).get("attributes")));
                metaInfoList.get(i).put("business_attributes", convertJsonbToMap(metaInfoList.get(i).get("business_attributes")));
                
                // _varchar 객체 배열 Map 형태로 변환 (카테고리)
                String[] ids = (String[]) metaInfoList.get(i).get("classifications");

                if (ids != null && ids.length > 0) {
                    List<Map<String, Object>> classificationList = diagramMapper.getClassificationList(ids);
                    metaInfoList.get(i).put("classifications", classificationList);
                }
            }
        } 

        return metaInfoList;
    }

    public Map<String, Object> getMetaDataForTooltipOne(Map<String, Object> params) throws Exception {
        return diagramMapper.getMetaDataForTooltipOne(params);
    }

    // Jsonb => Map 타입 변환 함수
    public Map<String, Object> convertJsonbToMap(Object jsonb) throws Exception {
        if (jsonb != null) {
            return objectMapper.readValue((String) jsonb, new TypeReference<Map<String, Object>>() {});    
        }
        return null;
    }

    // _varchar => List<Map> 타입 변환 함수
    public List<Map<String, String>> convert_varcharToListMap(Object _varchar) throws Exception {
        if (_varchar != null) {
            return objectMapper.readValue((String) _varchar, new TypeReference<List<Map<String, String>>>() {});    
        }
        return null;
    }

    // 노드 검색
    public Map<String, Object> getMetaDataList(Map<String, Object> params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> metaInfoList = new ArrayList<>();

        metaInfoList = diagramMapper.getMetaDataList(params);
        if (metaInfoList != null) {
            for (int i=0; i<metaInfoList.size(); i++) {
                // Postgresql jsonb 타입(JSON 문자열) Map 형태로 변환
                metaInfoList.get(i).put("attributes", convertJsonbToMap(metaInfoList.get(i).get("attributes")));
                metaInfoList.get(i).put("business_attributes", convertJsonbToMap(metaInfoList.get(i).get("business_attributes")));
                
                // _varchar 객체 배열 Map 형태로 변환 (카테고리)
                String[] ids = (String[]) metaInfoList.get(i).get("classifications");
    
                if (ids != null && ids.length > 0) {
                    List<Map<String, Object>> classificationList = diagramMapper.getClassificationList(ids);
                    metaInfoList.get(i).put("classifications", classificationList);
                }
            }
            result.put("list", metaInfoList);
            result.put("total", diagramMapper.getMetaDataListTotal(params));
        }
        return result;
    }

    // 노드 검색 총 개수
    public int getMetaDataListTotal(Map<String, Object> params) throws Exception {
        return diagramMapper.getMetaDataListTotal(params);
    }

    // DB 목록 조회
    public List<String> getDbList() throws Exception {
        return diagramMapper.getDbList();
    }
}
