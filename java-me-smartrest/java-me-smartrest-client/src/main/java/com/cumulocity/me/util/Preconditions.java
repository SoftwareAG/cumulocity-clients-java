package com.cumulocity.me.util;

public class Preconditions {

    public static void checkState(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
