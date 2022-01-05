/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.exception;

public class LpwanCodecServiceException extends Exception {
    public LpwanCodecServiceException(String message) {
        super(message);
    }

    public LpwanCodecServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
