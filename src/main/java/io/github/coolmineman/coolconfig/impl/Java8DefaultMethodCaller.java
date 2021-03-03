package io.github.coolmineman.coolconfig.impl;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Java8DefaultMethodCaller extends DefaultMethodCaller {
    private static final Constructor<MethodHandles.Lookup> CONSTRUCTOR;

    static {
        try {
			CONSTRUCTOR = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
		} catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (!CONSTRUCTOR.isAccessible()) {
            CONSTRUCTOR.setAccessible(true);
        }
    }

	@Override
	public Object callDefaultMethod(Object proxy, Method method, Object[] args) {
		try {
			return CONSTRUCTOR.newInstance(method.getDeclaringClass())
			.unreflectSpecial(method, method.getDeclaringClass())
			.bindTo(proxy)
			.invokeWithArguments(args);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}
    
}
