package com.mobigen.ovp.common.openmete_client.dto.classification;

import com.mobigen.ovp.common.openmete_client.dto.Paging;
import lombok.Data;

import java.util.List;

@Data
public class ClassificationData {
    List<Classification> data;
    Paging paging;
    // TODO : error도 있으면 넣을 수 있음!!
}
