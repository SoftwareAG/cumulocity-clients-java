package com.cumulocity.lpwan.codec.sdk.model;

import lombok.Getter;

import java.util.List;

@Getter
public class ManagedObjectProperty {
    public ManagedObjectProperty(String name, Object value) {
    }

    public ManagedObjectProperty(String name, Object value, String unit) {
    }

    public ManagedObjectProperty(String name, List<ManagedObjectProperty> properties) {
    }

    private String name;
    private Object value;
    private String unit;
    private List<ManagedObjectProperty> properties;
}
