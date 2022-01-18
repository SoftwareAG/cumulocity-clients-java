/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

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
