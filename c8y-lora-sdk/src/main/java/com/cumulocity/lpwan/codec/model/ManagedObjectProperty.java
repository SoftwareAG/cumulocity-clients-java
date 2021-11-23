/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.model;

import com.google.common.base.Strings;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The ManagedObjectProperty class represents the properties that can be added to a Managed Object
 * <p>
 * name - represents the name of the property.
 * value - represents the value that the property caries.
 * unit - represents the unit of the value.
 * childProperties - list of the sub properties of this one.
 */
@Getter
public class ManagedObjectProperty {

    private String name;
    private Object value;
    private String unit;
    private List<ManagedObjectProperty> childProperties;

    public ManagedObjectProperty(String name, Object value) {
        this(name, value, null);
    }

    public ManagedObjectProperty(String name, Object value, String unit) {
        this.name = name;
        this.value = value;
        this.unit = unit;
    }

    public ManagedObjectProperty(String name, List<ManagedObjectProperty> childProperties) {
        this.name = name;
        this.childProperties = childProperties;
    }


    public Map<String, Object> getPropertyAsMap() {
        Map<String, Object> oneProperty = new HashMap<>(1);

        if (Objects.nonNull(childProperties) && !childProperties.isEmpty()) {
            List<Map<String, Object>> childPropertyMaps = childProperties.stream().map(ManagedObjectProperty::getPropertyAsMap).collect(Collectors.toCollection(() -> new ArrayList<>(childProperties.size())));
            oneProperty.put(name, childPropertyMaps);
        } else {
            if (Objects.nonNull(value) && !Strings.isNullOrEmpty(unit)) {
                Map<String, Object> childProperties = new HashMap<>(2);
                childProperties.put("value", value);
                childProperties.put("unit", unit);
                oneProperty.put(name, childProperties);
            } else if (Objects.nonNull(value) && Strings.isNullOrEmpty(unit)) {
                oneProperty.put(name, value);
            }
        }

        return oneProperty;
    }
}
