package io.github.coolmineman.coolconfig;

import java.lang.reflect.Proxy;

import io.github.coolmineman.coolconfig.impl.ConfigInvocationHandler;

public final class CoolConfig {
    private CoolConfig() { }

    @SuppressWarnings("unchecked")
    public static <T extends Config> T create(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, new ConfigInvocationHandler(clazz));
    }
}
