package io.github.coolmineman.coolconfig.schema;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import io.github.coolmineman.coolconfig.CoolConfigException;
import io.github.coolmineman.coolconfig.annotation.NotConfigValue;
import io.github.coolmineman.coolconfig.impl.ReflectUtil;
import io.github.coolmineman.coolconfig.tree.ConfigDataObject;

public final class ObjectType implements Type {
    public final Map<String, Type> value;

    public ObjectType(Map<String, Type> value) {
        this.value = value;
    }

    public ObjectType(Class<?> clazz) throws CoolConfigException {
        if (!clazz.isInterface()) throw new CoolConfigException("Config class must be an interface extending Config");
        this.value = new LinkedHashMap<>();
        for (Method method : ReflectUtil.getDeclaredMethodsInOrder(clazz)) {
            if (method.getAnnotation(NotConfigValue.class) != null) continue;
            if (method.getParameterCount() != 0) throw new CoolConfigException("Config methods must not take parameters");
            this.value.put(method.getName(), Type.of(method));
        }
    }

    @Override
    public Object getDefaultValue(Class<?> parent, String key) {
        try {
			return new ConfigDataObject(parent.getMethod(key).getReturnType(), this);
		} catch (Exception e) {
            throw new CoolConfigException(e);
		}
    }
}
