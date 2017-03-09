package com.cumulocity.sms.client.model;

public enum Protocol {
    MSISDN("tel"), ICCID, ACR;

    private String value;

    private Protocol() {
        this.value = name().toLowerCase();
    }

    private Protocol(String name) {
        this.value = name;
    }

    public String value() {
        return value;
    }

    public static Protocol fromValue(String value) {
        for (Protocol protocol : values()) {
            if (protocol.value().equalsIgnoreCase(value)) {
                return protocol;
            }
        }
        throw new IllegalArgumentException("Can't find protocol " + value);
    }

}
