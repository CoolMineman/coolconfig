package io.github.coolmineman.coolconfig.tree;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jetbrains.annotations.Nullable;

import io.github.coolmineman.coolconfig.CoolConfigException;
import io.github.coolmineman.coolconfig.annotation.Comment;
import io.github.coolmineman.coolconfig.annotation.NotConfigValue;
import io.github.coolmineman.coolconfig.impl.DefaultMethodCaller;
import io.github.coolmineman.coolconfig.schema.ObjectType;
import io.github.coolmineman.coolconfig.schema.Type;

public class ConfigDataObject {
    public final Map<String, Object> values = new HashMap<>();
    public final Map<String, String> comments = new HashMap<>();
    public final @Nullable String comment;

    public ConfigDataObject(Class<?> clazz, ObjectType schema) {
        for (Entry<String, Type> entry : schema.value.entrySet()) {
            values.put(entry.getKey(), entry.getValue().getDefaultValue(clazz, entry.getKey()));
        }
        comment = comments(clazz);
    }

    public ConfigDataObject(Class<?> clazz, ObjectType schema, Object proxy) {
        try {
            for (Entry<String, Type> entry : schema.value.entrySet()) {
                Method method = clazz.getMethod(entry.getKey());
                if (method.isDefault()) {
                    values.put(entry.getKey(), DefaultMethodCaller.INSTANCE.callDefaultMethod(proxy, method, null));
                } else {
                    values.put(entry.getKey(), entry.getValue().getDefaultValue(clazz, entry.getKey()));
                }
            }
        } catch (Exception e) {
            throw new CoolConfigException(e);
        }
        comment = comments(clazz);
    }

    private String comments(Class<?> clazz) {
        try {
            for (Method method : clazz.getMethods()) {
                String methodName = method.getName();
                if (method.getAnnotation(NotConfigValue.class) != null) continue;
                Comment commentAnnotations = method.getAnnotation(Comment.class);
                if (commentAnnotations != null) {
                    comments.put(methodName, commentAnnotations.value());
                }
            }
            Comment clazzComment = clazz.getAnnotation(Comment.class);
            return clazzComment == null ? null : clazzComment.value();
        } catch (Exception e) {
            throw new CoolConfigException(e);
        }
    }
}
