package com.cumulocity.mqtt.connect.client.model;

/**
 * Indicator for the payload.
 */
public enum PayloadFormatIndicator {

    /**
     * The format of the bytes of the payload is not known.
     */
    UNSPECIFIED,
    /**
     * The format of the bytes of the payload is UTF-8.
     */
    UTF_8
}
