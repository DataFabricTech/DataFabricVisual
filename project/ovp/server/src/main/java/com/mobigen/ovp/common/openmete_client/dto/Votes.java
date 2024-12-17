package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

import java.util.List;

@Data
public class Votes {
    int upVotes;
    int downVotes;
    List<Voters> upVoters;
    List<Voters> downVoters;
}
