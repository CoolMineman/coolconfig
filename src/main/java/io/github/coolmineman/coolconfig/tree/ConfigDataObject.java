package io.github.coolmineman.coolconfig.tree;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.github.coolmineman.coolconfig.CoolConfigException;
import io.github.coolmineman.coolconfig.impl.DefaultMethodCaller;
import io.github.coolmineman.coolconfig.schema.ObjectType;
import io.github.coolmineman.coolconfig.schema.Type;

public class ConfigDataObject {
    public final Map<String, Object> values = new HashMap<>();

    public ConfigDataObject(Class<?> clazz, ObjectType schema) {
        for (Entry<String, Type> entry : schema.value.entrySet()) {
            values.put(entry.getKey(), entry.getValue().getDefaultValue(clazz, entry.getKey()));
        }
    }

    public ConfigDataObject(Class<?> clazz, ObjectType schema, Object proxy) {
        try {
            for (Entry<String, Type> entry : schema.value.entrySet()) {
                Method method = clazz.getMethod(entry.getKey());
                if (method.isDefault()) {
                    values.put(entry.getKey(), DefaultMethodCaller.INSTANCE.callDefaultMethod(proxy, method, null));
                } else {
                    values.put(entry.getKey(), entry.getValue().getDefaultValue(clazz, entry.getKey()));
                }
            }
        } catch (Exception e) {
            throw new CoolConfigException(e);
        }
    }
}
