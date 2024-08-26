package com.mobigen.ovp.common.constants;

public enum ModelType {
    TABLES("table"),
    STORAGE("storage"),
    MODELS("model");

    private final String value;

    ModelType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
