package com.mobigen.ovp.search_detail.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mobigen.ovp.common.openmete_client.dto.Followers;
import com.mobigen.ovp.common.openmete_client.dto.Owner;
import com.mobigen.ovp.common.openmete_client.dto.Tables;
import com.mobigen.ovp.common.openmete_client.dto.Voters;
import com.mobigen.ovp.glossary.client.dto.common.Tag;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class DataModelDetailResponse {

    String serviceType;
    String type;
    String id;
    String fqn;
    DataModelDetailCategory category;
    String categoryId;
    String firModelNm;
    String modelNm;
    String displayName;
    String modelDesc;
    String ownerId;
    Owner owner;
    String ownerDisplayName;
    int followers;
    int upVotes;
    int downVotes;
    @JsonProperty("isFollow")
    boolean isFollow;
    @JsonProperty("isUpVote")
    boolean isUpVote;
    @JsonProperty("isDownVote")
    boolean isDownVote;
    List<String> depth;
    List<Tag> tags;

    public DataModelDetailResponse(Tables tables, String type, String userId) {
        this.serviceType = tables.getService().getType();
        this.type = type;
        this.id = tables.getId();
        this.fqn = tables.getFullyQualifiedName();
        this.category = new DataModelDetailCategory();
        this.modelNm = tables.getName();
        String displayName = tables.getDisplayName();

        if (displayName == null || "".equals(displayName)) {
            this.firModelNm = "";
        } else {
            this.firModelNm = this.modelNm;
            this.displayName = displayName;
            this.modelNm = displayName;
        }

        this.modelDesc = (tables.getDescription() != null) ? tables.getDescription() : "";
        this.owner = tables.getOwner();

        if (this.owner != null) {
            this.ownerId = (this.owner.getId() != null) ? this.owner.getId() : "";
//            this.owner = (tables.getOwner().getName() != null) ? tables.getOwner().getName() : "";
            this.ownerDisplayName = (this.owner.getDisplayName() != null) ?this.owner.getDisplayName() : "";

            if (!"".equals(this.ownerId)) {

                for(Voters votes: tables.getVotes().getUpVoters()) {
                    if (votes.getId().equals(userId)) {
                        this.isUpVote = true;
                        break;
                    }
                }
                for(Voters votes: tables.getVotes().getDownVoters()) {
                    if (votes.getId().equals(userId)) {
                        this.isDownVote = true;
                        break;
                    }
                }
                for(Followers followers: tables.getFollowers()) {
                    if (followers.getId().equals(userId)) {
                        this.isFollow = true;
                        break;
                    }
                }
            }
        }

        this.followers = tables.getFollowers().size();
        this.upVotes = tables.getVotes().getUpVotes();
        this.downVotes = tables.getVotes().getDownVotes();
        this.tags = new ArrayList<>();


        String[] splitArray = this.fqn.split("\\.");
        List<String> resultList = new ArrayList<>(Arrays.asList(splitArray));
        resultList.remove(resultList.size() - 1);
        this.depth = resultList;
    }
}
