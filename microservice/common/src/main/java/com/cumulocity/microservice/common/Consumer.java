package com.cumulocity.microservice.common;

public interface Consumer<T> {
    void accept(T t);
}
