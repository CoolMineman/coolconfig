package io.github.coolmineman.coolconfig.impl;

import java.util.regex.Pattern;

public class GenericUtil {
    private GenericUtil() {}

    public static final Pattern LIST_GENERIC_PATTERN = Pattern.compile("java\\.util\\.List<(.*)>");
    public static final Pattern MAP_GENERIC_PATTERN = Pattern.compile("java\\.util\\.Map<(.*)>");

    public static String getRawClass(String genericString) {
        int i = genericString.indexOf('<');
        if (i < 0) return genericString;
        return genericString.substring(0, i);
    }
}
