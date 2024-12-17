package com.mobigen.ovp.mypage.dto.response;

import lombok.Data;

import java.util.Map;

@Data
public class UserInfoResponse {

    private String id;
    private String description;
    private String displayName;
    private String name;
    private String email;
    private boolean isAdmin;
    private String role;

    public UserInfoResponse(Map<String, Object> newUserInfo) {
        this.id = newUserInfo.get("id").toString();
        this.description = (String) newUserInfo.get("description");
        this.displayName = (String) newUserInfo.get("displayName");
        this.name = (String) newUserInfo.get("name");
        this.email = (String) newUserInfo.get("email");
        this.isAdmin = (boolean) newUserInfo.get("isAdmin");
        this.role = (String) newUserInfo.get("role");
    }

}
