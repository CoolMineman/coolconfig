package io.github.coolmineman.coolconfig.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Java9DefaultMethodCaller extends DefaultMethodCaller {

	@Override
	public Object callDefaultMethod(Object proxy, Method method, Object[] args) {
        try {
            MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(method.getDeclaringClass(), MethodHandles.lookup());

            MethodHandle handle = null;
            MethodType methodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());
                
            if (Modifier.isStatic(method.getModifiers())) {
                handle = lookup.findStatic(method.getDeclaringClass(), method.getName(), methodType);
            } else {        
                handle = lookup.findSpecial(method.getDeclaringClass(), method.getName(), methodType, method.getDeclaringClass());
            }
            return handle.bindTo(proxy).invokeWithArguments(args);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
	}
    
}
