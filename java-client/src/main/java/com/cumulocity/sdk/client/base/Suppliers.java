package com.cumulocity.sdk.client.base;

public class Suppliers {
    public static <T> Supplier<T> ofInstance(final T instance) {
        return new Supplier<T>() {
            public T get() {
                return instance;
            }
        };
    }
}
