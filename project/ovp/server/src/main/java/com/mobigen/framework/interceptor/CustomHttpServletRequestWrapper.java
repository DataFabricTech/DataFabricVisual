package com.mobigen.framework.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.HashMap;
import java.util.Map;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String[]> modifiedParams;

    public CustomHttpServletRequestWrapper(HttpServletRequest request, String paramName, String paramValue) {
        super(request);
        this.modifiedParams = new HashMap<>(request.getParameterMap());
        this.modifiedParams.put(paramName, new String[]{paramValue});
    }

    @Override
    public String getParameter(String name) {
        String[] params = modifiedParams.get(name);
        if (params != null && params.length > 0) {
            return params[0];
        }
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return modifiedParams;
    }

    @Override
    public String[] getParameterValues(String name) {
        return modifiedParams.get(name);
    }
}