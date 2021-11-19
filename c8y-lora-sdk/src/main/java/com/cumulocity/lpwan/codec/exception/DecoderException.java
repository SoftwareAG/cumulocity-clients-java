/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.exception;

import java.text.MessageFormat;

public class DecoderException extends Exception {

    public DecoderException(String message) {
        super(message);
    }

    public DecoderException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecoderException(String message, String... messageArgs) {
        super(MessageFormat.format(message, messageArgs));
    }

    public DecoderException(String message, Throwable cause, String... messageArgs) {
        super(MessageFormat.format(message, messageArgs), cause);
    }
}
