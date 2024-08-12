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

@Data
public class DataModelDetailLineageTableResponse {
    List<Map<String, String>> rawNodes;
    List<Map<String, String>> rawEdges;

    public DataModelDetailLineageTableResponse(Lineage lineage) {
        rawNodes = new ArrayList<>();
        rawEdges = new ArrayList<>();
        for(Node node: lineage.getNodes()) {
            Map<String, String> tempNode = new HashMap<String, String>();
            tempNode.put("id", node.getId());
            tempNode.put("fqn", node.getFullyQualifiedName());
            tempNode.put("name", node.getDisplayName());

            String depth = node.getFullyQualifiedName().replaceAll("\\.[^.]*$", "");
            tempNode.put("path", depth);

            rawNodes.add(tempNode);

            for(LineageData lineageData: node.getLineage()) {
                Map<String, String> tempRqwNode = new HashMap<>();
                Entity toEntity = lineageData.getToEntity();
                Entity fromEntity = lineageData.getFromEntity();
                tempRqwNode.put("fromId", fromEntity.getId());
                tempRqwNode.put("toId", toEntity.getId());
                rawEdges.add(tempRqwNode);
            }
        }
    }
}
