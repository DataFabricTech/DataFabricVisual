package com.mobigen.ovp.common.openmete_client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonPatchOperation {

    @JsonProperty("op")
    private String op;

    @JsonProperty("path")
    private String path;

    @JsonProperty("value")
    private String value;
}
