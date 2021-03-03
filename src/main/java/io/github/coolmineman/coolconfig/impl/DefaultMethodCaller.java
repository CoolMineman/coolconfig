package io.github.coolmineman.coolconfig.impl;

import java.lang.reflect.Method;

import org.jetbrains.annotations.Nullable;

//http://netomi.github.io/2020/04/17/default-methods.html
//Pain
@SuppressWarnings("all")
public abstract class DefaultMethodCaller {
    public static final DefaultMethodCaller INSTANCE;

    public abstract Object callDefaultMethod(Object proxy, Method method, @Nullable Object[] args);
    
    static {
        if (Double.parseDouble(System.getProperty("java.specification.version")) <= 1.8) {
            INSTANCE = new Java8DefaultMethodCaller();
        } else {
            INSTANCE = new Java9DefaultMethodCaller();
        }
    }
}
