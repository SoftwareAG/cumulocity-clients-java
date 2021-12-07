package com.cumulocity.microservice.customdecoders.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class DataFragmentUpdate implements Serializable {
    private String key;
    private String value;
    private Object valueAsObject;

    public DataFragmentUpdate(String key, String value) {
        this(key, value, null);
    }

    public DataFragmentUpdate(String key, Object valueAsObject) {
        this(key, null, valueAsObject);
    }

    public DataFragmentUpdate(String key, String value, Object valueAsObject) {
        this.key = key;
        this.value = value;
        this.valueAsObject = valueAsObject;
    }
}
