package com.mobigen.ovp.service_manager.client.response;

import com.mobigen.ovp.common.openmete_client.dto.RepositoryDescription;
import lombok.Data;

@Data
public class RepositoryDescriptionResponse {
    private RepositoryDescription description;

    public RepositoryDescriptionResponse(RepositoryDescription description) {
        this.description = description;
    }
}
