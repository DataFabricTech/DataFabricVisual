package com.mobigen.ovp.user.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    private String id;
    private String name;
    private String fullyQualifiedName;
    private String displayName;
    private String email;
    private boolean isAdmin; // admin으로 변경되서 출력
}
