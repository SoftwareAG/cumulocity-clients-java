package com.cumulocity.microservice.customdecoders.api.util;

public enum REGISTER_DATA_TYPE {
    Integer, Float;

    static REGISTER_DATA_TYPE safeValueOf(String v) {
        if(v == null || "".equals(v)) {
            return null;
        }
        return valueOf(v);
    }
}
