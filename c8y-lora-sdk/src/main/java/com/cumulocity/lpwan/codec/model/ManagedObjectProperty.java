/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.model;

import lombok.Getter;

import java.util.List;

/**
 * The ManagedObjectProperty class represents the properties that can be added to a Managed Object
 *
 * name - represents the name of the property.
 * value - represents the value that the property caries.
 * unit - represents the unit of the parameter.
 */
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
