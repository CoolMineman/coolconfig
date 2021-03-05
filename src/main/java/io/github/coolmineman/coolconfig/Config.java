package io.github.coolmineman.coolconfig;

import io.github.coolmineman.coolconfig.annotation.NotConfigValue;
import io.github.coolmineman.coolconfig.schema.ObjectType;
import io.github.coolmineman.coolconfig.tree.ConfigDataObject;

public interface Config {
    @NotConfigValue
    ObjectType getSchema();
    @NotConfigValue
    ConfigDataObject getData();
}
