package com.mobigen.framework.result;

import org.apache.commons.collections4.map.ListOrderedMap;

@SuppressWarnings("serial")
public class UnderscoreToCamelMap extends ListOrderedMap {
    public Object put(Object key, Object value) {
        String s = ((String) key);
        String[] atoms = s.split("_");
        StringBuilder camel = new StringBuilder();

        if (atoms.length > 1) {
            camel.append(atoms[0].toLowerCase());
            for (int i = 1; i < atoms.length; i++) {
                camel.append(Character.toUpperCase(atoms[i].charAt(0)));
                camel.append(atoms[i].substring(1).toLowerCase());
            }
        } else {
            camel.append(s.toLowerCase());
        }

        return super.put(camel.toString(), value);
    }

}
