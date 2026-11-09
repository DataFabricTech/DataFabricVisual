package com.mobigen.framework.utility;

import org.apache.commons.collections4.map.ListOrderedMap;

public class CamelListMap extends ListOrderedMap {
    private static final long serialVersionUID = 1L;

    private String toProperCase(String s, boolean isCapital) {

        String rtnValue = "";

        if (isCapital) {
            rtnValue = s.substring(0, 1).toUpperCase() +
                    s.substring(1).toLowerCase();
        } else {
            rtnValue = s.toLowerCase();
        }
        return rtnValue;
    }

    private String toCamelCase(String s) {
        String[] parts = s.split("_");
        StringBuilder camelCaseString = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            camelCaseString.append(toProperCase(part, (i != 0 ? true : false)));
        }

        return camelCaseString.toString();
    }

    @Override
    public Object put(Object key, Object value) {
        return super.put(toCamelCase((String) key), value);
    }

    public Object put2(Object key, Object value) {
        return super.put(key, value);
    }
}
