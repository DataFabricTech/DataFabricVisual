package com.mobigen.framework.security;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomHttpSessionListener implements HttpSessionListener {
    @Value("${server.servlet.session.timeout}")
    private int sessionTimeout;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.debug("Session Created - {}", se.getSession().getId());
        se.getSession().setMaxInactiveInterval(sessionTimeout);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        log.debug("Session Destroyed - {}", se.getSession().getId());
        HttpSessionListener.super.sessionDestroyed(se);
    }
}