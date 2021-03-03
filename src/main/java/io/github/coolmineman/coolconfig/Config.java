package io.github.coolmineman.coolconfig;

import java.lang.invoke.MethodHandles;

import io.github.coolmineman.coolconfig.schema.ObjectType;
import io.github.coolmineman.coolconfig.tree.ConfigDataObject;

public interface Config {
    ObjectType getSchema();
    ConfigDataObject getData();

    public static MethodHandles.Lookup METHOD_HANDLES = MethodHandles.lookup();
}
