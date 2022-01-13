/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

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
