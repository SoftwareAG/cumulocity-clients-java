/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.decoder.exception;

/**
 * The <b>DecoderException</b> is a custom exception class that extends the <b>Exception</b> class and can be used to throw custom exceptions.
 */
public class DecoderException extends Exception {

    /**
     * Instantiates a new DecoderException.
     *
     * @param message This is a custom exception message that is shown when an exception is thrown.
     */
    public DecoderException(String message) {
        super(message);
    }

    /**
     * Instantiates a new DecoderException.
     *
     * @param message This is a custom exception message that is shown when an exception is thrown.
     * @param cause   This contains the cause/stacktrace of the exception
     */
    public DecoderException(String message, Throwable cause) {
        super(message, cause);
    }
}
