package com.mobigen.ovp.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDTO {
    private String id;
    private String name;
    private String fullyQualifiedName;
    private String displayName;
    private String email;
    private boolean isAdmin; // admin으로 변경되서 출력
}
