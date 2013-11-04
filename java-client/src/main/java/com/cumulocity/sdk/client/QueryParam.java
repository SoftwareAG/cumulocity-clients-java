package com.cumulocity.sdk.client;

public class QueryParam {
    private Param key;
    private String value;
    
    public QueryParam(Param key, String value) {
        this.key = key;
        this.value = value;
    }

    public Param getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
