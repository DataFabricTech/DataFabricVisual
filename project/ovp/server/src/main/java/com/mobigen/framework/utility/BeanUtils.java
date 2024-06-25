package com.mobigen.framework.utility;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class BeanUtils {
    public static Object getValue(Object o, String field) throws Exception {
        for (Method method : o.getClass().getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0
                    && method.getReturnType() != void.class && (method.getName().equals(field))) {
				return method.invoke(o);
            }
        }

        return null;
    }

    public static void setValue(Object o, String field, Object value) throws Exception {
        o.getClass().getField(field).set(o, value);
    }
}
