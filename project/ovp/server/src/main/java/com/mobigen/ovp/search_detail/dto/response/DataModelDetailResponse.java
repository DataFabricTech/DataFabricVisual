package com.mobigen.ovp.search_detail.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mobigen.ovp.common.constants.ModelType;
import com.mobigen.ovp.common.openmete_client.dto.Followers;
import com.mobigen.ovp.common.openmete_client.dto.Owner;
import com.mobigen.ovp.common.openmete_client.dto.Tables;
import com.mobigen.ovp.common.openmete_client.dto.Tag;
import com.mobigen.ovp.common.openmete_client.dto.Voters;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class DataModelDetailResponse {
    String serviceIcon;
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
    List<Tag> terms;
    List<Tag> originTags;
    String prefix;

    public DataModelDetailResponse(Tables tables, String type, String userId) {
        this.serviceIcon = new StringBuffer("type-img type-img-").append(tables.getServiceType().toLowerCase()).toString();
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

        this.modelDesc = tables.getDescription();
        this.owner = tables.getOwner();

        if (this.owner != null) {
            this.ownerId = (this.owner.getId() != null) ? this.owner.getId() : "";
            this.ownerDisplayName = (this.owner.getDisplayName() != null) ?this.owner.getDisplayName() : "";
        }

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

        this.followers = tables.getFollowers().size();
        this.upVotes = tables.getVotes().getUpVotes();
        this.downVotes = tables.getVotes().getDownVotes();
        this.tags = new ArrayList<>();
        this.terms = new ArrayList<>();
        this.originTags = tables.getTags();

        String[] splitArray = this.fqn.split("\\.");
        List<String> resultList = new ArrayList<>(Arrays.asList(splitArray));
        if (!ModelType.STORAGE.getValue().equals(type)) {
            resultList.remove(resultList.size() - 1);
            this.depth = resultList;
        } else {
            this.depth = List.of(new String[]{resultList.get(0), resultList.get(1)});
        }

        if (tables.getPrefix() != null) {
            this.prefix = tables.getPrefix();
        }
    }
}
