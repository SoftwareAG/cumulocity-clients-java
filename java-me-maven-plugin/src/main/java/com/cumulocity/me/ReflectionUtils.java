package com.cumulocity.me;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static Object getField(Class<?> clazz, Object target, String name) {
        try {
            final Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(target);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setField(Class<?> clazz, Object target, String name, Object object) {
        try {
            final Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, object);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
