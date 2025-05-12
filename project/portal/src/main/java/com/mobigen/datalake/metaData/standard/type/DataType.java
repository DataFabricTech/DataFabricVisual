package com.mobigen.datalake.metaData.standard.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataType {
    CHAR("C"),
    VARCHAR("V"),
    TEXT("T"),
    NUMERIC("N"),
    INTEGER("I"),
    DATETIME("D");

    private final String sign;

    public static DataType getDataTypeBySign(String sign) {
        for (DataType dataType : DataType.values()) {
            if (dataType.getSign().equals(sign)) {
                return dataType;
            }
        }
        throw new IllegalArgumentException("Error getDataTypeBySign => " + DataType.class);
    }

    public static DataType from(String name) {
        for (DataType dataType : DataType.values()) {
            if (dataType.name().equals(name)) {
                return dataType;
            }
        }
        throw new IllegalArgumentException("Error getDataTypeBySign => " + DataType.class);
    }

    public static boolean isValidDataTypeString(String name) {
        for (DataType dataType : DataType.values()) {
            if (dataType.name().equals(name)) {
                return true;
            }
        }
        return false;
    }


}
