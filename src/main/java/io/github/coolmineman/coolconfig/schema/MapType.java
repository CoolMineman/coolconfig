package io.github.coolmineman.coolconfig.schema;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.regex.Matcher;

import io.github.coolmineman.coolconfig.CoolConfigException;
import io.github.coolmineman.coolconfig.impl.GenericUtil;

public final class MapType implements Type {
    public final Type keyType;
    public final Type valueType;

    public MapType(Type keyType, Type valueType) {
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public MapType(Method method) {
        this(method.getGenericReturnType().getTypeName());
    }

    public MapType(String returnString) {
        Matcher matcher = GenericUtil.MAP_GENERIC_PATTERN.matcher(returnString);
        if (!matcher.find()) throw new CoolConfigException("Unsupported Map Generic " + returnString);
        String generic = matcher.group(1);
        String[] genericParams = generic.split(", ");
        if (genericParams.length != 2) throw new CoolConfigException("Unsupported Map Generic " + returnString);
        this.keyType = Type.of(genericParams[0]);
        this.valueType = Type.of(genericParams[1]);
    }

    @Override
    public Object getDefaultValue(Class<?> parent, String key) {
        return new HashMap<>();
    }
}
