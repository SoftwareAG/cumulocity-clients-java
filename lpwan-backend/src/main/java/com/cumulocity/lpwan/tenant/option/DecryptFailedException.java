/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.tenant.option;

public class DecryptFailedException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -4997180499970932425L;

    public DecryptFailedException(String message) {
        super(message);
    }

    public DecryptFailedException(String message, IllegalStateException cause) {
        super(message, cause);
    }
}
