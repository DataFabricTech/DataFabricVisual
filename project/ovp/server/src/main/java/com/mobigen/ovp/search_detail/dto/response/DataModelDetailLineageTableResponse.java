package com.mobigen.ovp.search_detail.dto.response;

import com.mobigen.ovp.common.openmete_client.dto.Entity;
import com.mobigen.ovp.common.openmete_client.dto.Lineage;
import com.mobigen.ovp.common.openmete_client.dto.LineageData;
import com.mobigen.ovp.common.openmete_client.dto.Node;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class DataModelDetailLineageTableResponse {
    List<Map<String, String>> rawNodes;
    List<Map<String, String>> rawEdges;

    public DataModelDetailLineageTableResponse(Lineage lineage) {
        rawNodes = new ArrayList<>();
        rawEdges = new ArrayList<>();
        List<String> nodeIds = new ArrayList<>();

        for(Node node: lineage.getNodes()) {
            Map<String, String> tempNode = new HashMap<String, String>();
            tempNode.put("id", node.getId());
            tempNode.put("fqn", node.getFullyQualifiedName());
            tempNode.put("name", node.getDisplayName());

            String depth = "";
            if (!"container".equals(node.getType())) {
                depth = node.getFullyQualifiedName().replaceAll("\\.[^.]*$", "");
            } else  {
                String [] pathList = node.getFullyQualifiedName().split(".");
                depth = new StringBuffer(pathList[0]).append(".").append(pathList[1]).toString();
            }
            tempNode.put("path", depth);

            rawNodes.add(tempNode);
            nodeIds.add(node.getId());

            for(LineageData lineageData: node.getLineage()) {
                Map<String, String> tempRqwNode = new HashMap<>();
                Entity toEntity = lineageData.getToEntity();
                Entity fromEntity = lineageData.getFromEntity();
                tempRqwNode.put("fromId", fromEntity.getId());
                tempRqwNode.put("toId", toEntity.getId());
                rawEdges.add(tempRqwNode);
            }
        }

        if (rawNodes.size() == 0 && rawEdges.size() == 0) {
            Node entity = lineage.getEntity();
            Map<String, String> tempNode = new HashMap<String, String>();
            tempNode.put("id", entity.getId());
            tempNode.put("fqn", entity.getFullyQualifiedName());
            tempNode.put("name", entity.getDisplayName());

            String depth = "";
            if (!"container".equals(entity.getEntityType())) {
                depth = entity.getFullyQualifiedName().replaceAll("\\.[^.]*$", "");
            } else  {
                String [] pathList = entity.getFullyQualifiedName().split("\\.");
                depth = new StringBuffer(pathList[0]).append(".").append(pathList[1]).toString();
            }
            tempNode.put("path", depth);

            rawNodes.add(tempNode);
        } else {
            // NOTE: node 개수에 비해 edge가 너무 많이 와서 검증하고 필터 처리
            rawEdges = rawEdges.stream().filter(edge -> nodeIds.contains(edge.get("toId")) && nodeIds.contains(edge.get("fromId"))).collect(Collectors.toList());
        }
    }
}
