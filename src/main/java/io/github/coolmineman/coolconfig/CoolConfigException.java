package io.github.coolmineman.coolconfig;

public class CoolConfigException extends RuntimeException {
	private static final long serialVersionUID = -3086745422758793610L;
    
    public CoolConfigException(String message) {
        super(message);
    }

    public CoolConfigException(Exception e) {
        super(e);
    }
}
