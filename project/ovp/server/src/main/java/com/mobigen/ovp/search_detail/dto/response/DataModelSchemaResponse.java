package com.mobigen.ovp.search_detail.dto.response;

import com.mobigen.ovp.common.openmete_client.dto.Owner;
import com.mobigen.ovp.common.openmete_client.dto.Tables;
import lombok.Data;

@Data
public class DataModelSchemaResponse {
    private String id;
    private String fqn;
    private String name;
    private String description;
    private Owner owner;
    private int followers;
    private int upVotes;
    private int downVotes;

    public DataModelSchemaResponse(Tables tablesName) {
        this.id = tablesName.getId();
        this.fqn = tablesName.getFullyQualifiedName();
        this.name = tablesName.getName();
        this.description = (tablesName.getDescription() != null) ? tablesName.getDescription() : "";
        this.owner = (tablesName.getOwner() != null) ? tablesName.getOwner() : null;
        this.followers = tablesName.getFollowers().size();
        this.upVotes = tablesName.getVotes().getUpVotes();
        this.downVotes = tablesName.getVotes().getDownVotes();
    }
}
