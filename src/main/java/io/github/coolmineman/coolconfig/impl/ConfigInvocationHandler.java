package io.github.coolmineman.coolconfig.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import io.github.coolmineman.coolconfig.schema.ObjectType;
import io.github.coolmineman.coolconfig.tree.ConfigDataObject;

public class ConfigInvocationHandler implements InvocationHandler {
	private final ObjectType schema;
	private final ConfigDataObject data;

	public ConfigInvocationHandler(Class<?> clazz) {
		this.schema = new ObjectType(clazz);
		this.data = new ConfigDataObject(clazz, this.schema);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
		if ("getSchema".equals(methodName)) return schema;
		if ("getData".equals(methodName)) return data;
		return data.values.get(methodName);
	}
    
}