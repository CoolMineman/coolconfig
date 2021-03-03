package io.github.coolmineman.coolconfig.schema;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import io.github.coolmineman.coolconfig.CoolConfigException;
import io.github.coolmineman.coolconfig.impl.GenericUtil;

public interface Type {
    Object getDefaultValue(Class<?> parent, String key);

    public static Type of(Method method) {
        Class<?> clazz = method.getReturnType();
        if (clazz == List.class) {
            return new ListType(method);
        }
        if (clazz == Map.class) {
            return new MapType(method);
        }
        if (clazz == byte.class || clazz == Byte.class) {
            return ValueType.BYTE;
        }
        if (clazz == short.class || clazz == Short.class) {
            return ValueType.SHORT;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return ValueType.INT;
        }
        if (clazz == long.class || clazz == Long.class) {
            return ValueType.LONG;
        }
        if (clazz == float.class || clazz == Float.class) {
            return ValueType.FLOAT;
        }
        if (clazz == double.class || clazz == Double.class) {
            return ValueType.DOUBLE;
        }
        if (clazz == boolean.class || clazz == Boolean.class) {
            return ValueType.BOOLEAN;
        }
        if (clazz == char.class || clazz == Character.class) {
            return ValueType.CHAR;
        }
        if (clazz == String.class) {
            return ValueType.STRING;
        }
        if (clazz.isInterface()) {
            return new ObjectType(clazz);
        }
        return null;
    }

    public static Type of(String string) {
        Class<?> clazz;
        try {
			clazz = Class.forName(GenericUtil.getRawClass(string));
		} catch (ClassNotFoundException e) {
			throw new CoolConfigException("Unsupported Generic " + string);
        }
        if (clazz == List.class) {
            return new ListType(string);
        }
        if (clazz == Map.class) {
            return new MapType(string);
        }
        if (clazz == byte.class || clazz == Byte.class) {
            return ValueType.BYTE;
        }
        if (clazz == short.class || clazz == Short.class) {
            return ValueType.SHORT;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return ValueType.INT;
        }
        if (clazz == long.class || clazz == Long.class) {
            return ValueType.LONG;
        }
        if (clazz == float.class || clazz == Float.class) {
            return ValueType.FLOAT;
        }
        if (clazz == double.class || clazz == Double.class) {
            return ValueType.DOUBLE;
        }
        if (clazz == boolean.class || clazz == Boolean.class) {
            return ValueType.BOOLEAN;
        }
        if (clazz == char.class || clazz == Character.class) {
            return ValueType.CHAR;
        }
        if (clazz == String.class) {
            return ValueType.STRING;
        }
        if (clazz.isInterface()) {
            return new ObjectType(clazz);
        }
        return null;
    }
}
