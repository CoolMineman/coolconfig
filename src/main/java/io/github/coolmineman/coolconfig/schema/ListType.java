package io.github.coolmineman.coolconfig.schema;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Matcher;

import io.github.coolmineman.coolconfig.CoolConfigException;
import io.github.coolmineman.coolconfig.impl.GenericUtil;

public final class ListType implements Type {
    public final Type valueType;

    public ListType(Type valueType) {
        this.valueType = valueType;
    }

    public ListType(Method method) {
        this(method.getGenericReturnType().getTypeName());
    }

    public ListType(String returnString) {
        Matcher matcher = GenericUtil.LIST_GENERIC_PATTERN.matcher(returnString);
        if (!matcher.find()) throw new CoolConfigException("Unsupported List Generic " + returnString);
        this.valueType = Type.of(matcher.group(1));
    }

    @Override
    public Object getDefaultValue(Class<?> parent, String key) {
        return new ArrayList<>();
    }
}
