package com.mobigen.ovp.search_detail.dto.response;

import com.mobigen.ovp.common.openmete_client.dto.Owner;
import com.mobigen.ovp.common.openmete_client.dto.Tables;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class DataModelDetailResponse {
    private String id;
    private String fqn;
    private String name;
    private String description;
    private Owner owner;
    private int followers;
    private int upVotes;
    private int downVotes;
    private List<String> depth;

    public DataModelDetailResponse(Tables tablesName) {
        this.id = tablesName.getId();
        this.fqn = tablesName.getFullyQualifiedName();
        this.name = tablesName.getName();
        this.description = (tablesName.getDescription() != null) ? tablesName.getDescription() : "";
        this.owner = (tablesName.getOwner() != null) ? tablesName.getOwner() : null;
        this.followers = tablesName.getFollowers().size();
        this.upVotes = tablesName.getVotes().getUpVotes();
        this.downVotes = tablesName.getVotes().getDownVotes();

        String[] splitArray = this.fqn.split("\\.");
        List<String> resultList = new ArrayList<>(Arrays.asList(splitArray));
        resultList.remove(resultList.size() - 1);
        this.depth = resultList;
    }
}
