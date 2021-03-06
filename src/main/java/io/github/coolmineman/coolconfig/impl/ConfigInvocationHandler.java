package io.github.coolmineman.coolconfig.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import io.github.coolmineman.coolconfig.annotation.NotConfigValue;
import io.github.coolmineman.coolconfig.schema.ObjectType;
import io.github.coolmineman.coolconfig.tree.ConfigDataObject;

public class ConfigInvocationHandler implements InvocationHandler {

	private final Class<?> clazz;
	private final ObjectType schema;
	private ConfigDataObject data;

	public ConfigInvocationHandler(Class<?> clazz) {
		this.clazz = clazz;
		this.schema = new ObjectType(clazz);
	}

	public void initData(Object proxy) {
		this.data = new ConfigDataObject(clazz, this.schema, proxy);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
		if ("getSchema".equals(methodName)) return schema;
		if ("getData".equals(methodName)) return data;
		if (method.getAnnotation(NotConfigValue.class) != null) return DefaultMethodCaller.INSTANCE.callDefaultMethod(proxy, method, args);
		if ("equals".equals(methodName)) return this == args[0];
		if ("hashCode".equals(methodName)) return clazz.hashCode();
		if ("toString".equals(methodName)) return "ConfigProxy";
		return data.values.get(methodName);
	}
    
}
