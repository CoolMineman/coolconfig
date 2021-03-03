package io.github.coolmineman.coolconfig.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.github.coolmineman.coolconfig.schema.ObjectType;
import io.github.coolmineman.coolconfig.schema.Type;

public class ConfigDataObject {
    public final Map<String, Object> values = new HashMap<>();

    public ConfigDataObject(Class<?> clazz, ObjectType schema) {
        for (Entry<String, Type> entry : schema.value.entrySet()) {
            values.put(entry.getKey(), entry.getValue().getDefaultValue(clazz, entry.getKey()));
        }
    }
}
