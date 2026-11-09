package com.mobigen.framework.iris;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@ToString
@Builder
public class User implements UserDetails {
    private static final long serialVersionUID = -7141725149692824852L;

    private String userId;
    private String userPass;
    private String reUserPass;
    private String encryptPass;
    private String groupId;
    private String msg;
    private String sessionId;
    private String clientIP;
    private String updateFlag;
    private String createDate;
    private String name;
    private String deptId;
    private String deptName;
    private String email;
    private String phone;
    private String roleCode;
    private String desc;
    private String roleName;
    private String groupName;
    private String xAccessToken;
    private String angoraToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }
}
