package com.cumulocity.me.util;

public abstract class BeanUtils {

    protected BeanUtils() {
    }
    
    public static final Object newInstance(Class clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
