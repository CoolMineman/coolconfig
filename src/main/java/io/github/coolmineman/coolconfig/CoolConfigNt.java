package io.github.coolmineman.coolconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.github.coolmineman.coolconfig.schema.ListType;
import io.github.coolmineman.coolconfig.schema.MapType;
import io.github.coolmineman.coolconfig.schema.ObjectType;
import io.github.coolmineman.coolconfig.schema.Type;
import io.github.coolmineman.coolconfig.schema.ValueType;
import io.github.coolmineman.coolconfig.tree.ConfigDataObject;
import io.github.coolmineman.nestedtext.api.tree.NestedTextNode;

public class CoolConfigNt {
    private CoolConfigNt() { }

    public static void load(Config config, NestedTextNode node) {
        ObjectType schema = config.getSchema();
        ConfigDataObject data = config.getData();
        Map<String, NestedTextNode> nodes = node.asMap();
        for (Entry<String, NestedTextNode> entry : nodes.entrySet()) {
            Type type = schema.value.get(entry.getKey());
            data.values.put(entry.getKey(), convert(type, entry.getValue()));
        }
    }

    private static Object convert(Type type, NestedTextNode node) {
        if (type instanceof ValueType) {
            return convertValueType((ValueType)type, node);
        }
        if (type instanceof ListType) {
            return convertListType((ListType)type, node);
        }
        if (type instanceof MapType) {
            return convertMapType((MapType)type, node);
        }
        return null;
    }

    private static Object convertMapType(MapType type, NestedTextNode node) {
        Type keyType = type.valueType;
        Type valueType = type.valueType;
        HashMap<Object, Object> result = new HashMap<>();
        for (Entry<String, NestedTextNode> entry : node.asMap().entrySet()) {
            result.put(convertValueType((ValueType)keyType, entry.getKey()), convert(valueType, entry.getValue()));
        }
        return result;
    }

    private static Object convertListType(ListType type, NestedTextNode node) {
        Type valueType = type.valueType;
        ArrayList<Object> result = new ArrayList<>();
        for (NestedTextNode node2 : node.asList()) {
            result.add(convert(valueType, node2));
        }
        return result;
    }

    private static Object convertValueType(ValueType type, String value) {
        switch(type) {
            case BYTE:
                return Byte.valueOf(value);
            case SHORT:
                return Short.valueOf(value);
            case INT:
                return Integer.valueOf(value);
            case LONG:
                return Long.valueOf(value);
            case FLOAT:
                return Float.valueOf(value);
            case DOUBLE:
                return Double.valueOf(value);
            case BOOLEAN:
                return Boolean.valueOf(value);
            case CHAR:
                return value.charAt(0);
            case STRING:
                return value;
        }
		throw new CoolConfigException("Unreachable");
    }

    private static Object convertValueType(ValueType type, NestedTextNode node) {
        String value = node.asLeafString();
        return convertValueType(type, value);
    }
}
