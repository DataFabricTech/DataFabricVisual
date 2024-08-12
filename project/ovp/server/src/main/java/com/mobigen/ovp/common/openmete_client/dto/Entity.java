package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

@Data
public class Entity {
    String fqn;
    String id;
    String type;
    Votes votes;
}
