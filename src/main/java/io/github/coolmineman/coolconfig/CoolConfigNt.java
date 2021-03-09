package io.github.coolmineman.coolconfig;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
            if (type == null) continue;
            data.values.put(entry.getKey(), convert(type, entry.getValue()));
        }
    }

    public static NestedTextNode save(Config config) {
        ObjectType schema = config.getSchema();
        ConfigDataObject data = config.getData();
        LinkedHashMap<String, NestedTextNode> result = new LinkedHashMap<>();
        for (Entry<String, Type> entry : schema.value.entrySet()) {
            NestedTextNode node = save(entry.getValue(), data.values.get(entry.getKey()));
            node.setComment(data.comments.get(entry.getKey()));
            result.put(entry.getKey(), node);
        }
        NestedTextNode resultNode = NestedTextNode.of(result);
        resultNode.setComment(data.comment);
        return resultNode;
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
        Type keyType = type.keyType;
        Type valueType = type.valueType;
        LinkedHashMap<Object, Object> result = new LinkedHashMap<>();
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
            default:
                throw new CoolConfigException("Unreachable");
        }
    }

    private static Object convertValueType(ValueType type, NestedTextNode node) {
        String value = node.asLeafString();
        return convertValueType(type, value);
    }

    private static NestedTextNode save(Type type, Object value) {
        if (type instanceof MapType) {
            return saveMap((MapType)type, (Map<Object, Object>)value);
        } else if (type instanceof ListType) {
            return saveList((ListType)type, (List<Object>)value);
        } else {
            return NestedTextNode.of(value.toString());
        }
    }

    private static NestedTextNode saveMap(MapType type, Map<Object, Object> map) {
        LinkedHashMap<String, NestedTextNode> result = new LinkedHashMap<>();
        for (Entry<Object, Object> entry : map.entrySet()) {
            result.put(entry.getKey().toString(), save(type.valueType, entry.getValue()));
        }
        return NestedTextNode.of(result);
    }

    private static NestedTextNode saveList(ListType type, List<Object> list) {
        ArrayList<NestedTextNode> result = new ArrayList<>();
        for (Object o : list) {
            result.add(save(type.valueType, o));
        }
        return NestedTextNode.of(result);
    }
}
