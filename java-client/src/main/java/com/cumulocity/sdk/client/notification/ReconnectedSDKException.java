package com.cumulocity.sdk.client.notification;

import com.cumulocity.sdk.client.SDKException;

public class ReconnectedSDKException extends SDKException {

    public static final int UNKNOWN_CLIENT = 402;

    public ReconnectedSDKException(String message) {
        super(UNKNOWN_CLIENT, message);
    }
}
