package com.mobigen.ovp.common.openmete_client.dto.classification.detail;

//import com.mobigen.ovp.common.openmete_client.dto.tags.Tags;
import lombok.Data;

//import java.util.List;

@Data
public class ClassificationDetail {
    private String id;
    private String name;
    private String displayName;
    private String description;
//    List<Tags> tags;
}
