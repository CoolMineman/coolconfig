package io.github.coolmineman.coolconfig.schema;

import io.github.coolmineman.coolconfig.CoolConfigException;

public enum ValueType implements Type {
    //Primitives
    BYTE,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    BOOLEAN,
    CHAR,
    //Objects
    STRING,;

	@Override
	public Object getDefaultValue(Class<?> parent, String key) {
		switch(this) {
            case BYTE:
                return (Byte)(byte) 0;
            case SHORT:
                return (Short)(short) 0;
            case INT:
                return (Integer)0;
            case LONG:
                return (Long)0l;
            case FLOAT:
                return (Float)0f;
            case DOUBLE:
                return (Double)0d;
            case BOOLEAN:
                return (Boolean)false;
            case CHAR:
                return (Character)(char) 0;
            case STRING:
                return "";
        }
		throw new CoolConfigException("Unreachable");
	}
}
