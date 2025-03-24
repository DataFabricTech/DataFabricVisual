package com.mobigen.ovp.common.openmete_client.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Data
public class Tables {
    String id;
    String fullyQualifiedName;
    String name;
    String displayName;
    String description;
    Owner owner;
    String serviceType;
    List<Followers> followers;
    Votes votes;
    List<Columns> columns;
    Service service;
    Entity entity;
    List<Tag> tags;
    // NOTE: Containers 전용(Storage)
    String prefix;
    StorageColumn dataModel;
    long updatedAt;

    // 포맷된 날짜 문자열 반환용 Getter
    public String getFormattedUpdatedAt() {
        if (updatedAt == 0) return null; // 0이면 날짜 없음
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Timestamp(updatedAt));
    }
}
