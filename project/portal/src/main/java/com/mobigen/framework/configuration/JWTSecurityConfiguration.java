package com.mobigen.framework.configuration;

import com.mobigen.framework.iris.IRISProperties;
import com.mobigen.framework.iris.Token;
import com.mobigen.framework.security.JwtFilter;
import com.mobigen.framework.security.SessionManager;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTSecurityConfiguration extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private Token token;
    private SessionManager sessionManager;
    private IRISProperties properties;

    public JWTSecurityConfiguration(Token token, SessionManager sessionManager, IRISProperties properties) {
        this.token = token;
        this.sessionManager = sessionManager;
        this.properties = properties;
    }

    @Override
    public void configure(HttpSecurity http) {
        JwtFilter filter = new JwtFilter(token, sessionManager, properties);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
