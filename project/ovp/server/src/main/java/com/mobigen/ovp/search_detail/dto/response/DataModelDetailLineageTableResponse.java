package com.mobigen.ovp.search_detail.dto.response;

import com.mobigen.ovp.common.openmete_client.dto.Entity;
import com.mobigen.ovp.common.openmete_client.dto.Lineage;
import com.mobigen.ovp.common.openmete_client.dto.LineageData;
import com.mobigen.ovp.common.openmete_client.dto.Node;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
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

        for (Node node : lineage.getNodes()) {
            Map<String, String> tempNode = new HashMap<String, String>();
            tempNode.put("id", node.getId());
            tempNode.put("fqn", node.getFullyQualifiedName());
            tempNode.put("name", node.getDisplayName());

            // 3depth 까지만 표시
            String[] pathList = node.getFullyQualifiedName().split("\\.");
            String depth = String.join(".", Arrays.copyOfRange(pathList, 0, Math.min(3, pathList.length)));
            tempNode.put("path", depth);

            rawNodes.add(tempNode);
            nodeIds.add(node.getId());

            for (LineageData lineageData : node.getLineage()) {
                Map<String, String> tempRqwNode = new HashMap<>();
                Entity toEntity = lineageData.getToEntity();
                Entity fromEntity = lineageData.getFromEntity();
                tempRqwNode.put("fromId", fromEntity.getId());
                tempRqwNode.put("toId", toEntity.getId());
                rawEdges.add(tempRqwNode);
            }
        }

        if (rawNodes.isEmpty() && rawEdges.isEmpty()) {
            Node entity = lineage.getEntity();
            Map<String, String> tempNode = new HashMap<String, String>();
            tempNode.put("id", entity.getId());
            tempNode.put("fqn", entity.getFullyQualifiedName());
            tempNode.put("name", entity.getDisplayName());

            // 3depth 까지만 표시
            String[] pathList = entity.getFullyQualifiedName().split("\\.");
            String depth = String.join(".", Arrays.copyOfRange(pathList, 0, Math.min(3, pathList.length)));
            tempNode.put("path", depth);

            rawNodes.add(tempNode);
        } else {
            // NOTE: node 개수에 비해 edge가 너무 많이 와서 검증하고 필터 처리
            rawEdges = rawEdges.stream().filter(edge -> nodeIds.contains(edge.get("toId")) && nodeIds.contains(edge.get("fromId"))).collect(Collectors.toList());
        }
    }
}
