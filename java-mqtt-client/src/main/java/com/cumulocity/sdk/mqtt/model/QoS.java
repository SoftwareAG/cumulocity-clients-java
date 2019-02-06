package com.cumulocity.sdk.mqtt.model;

public enum QoS {

    AT_MOST_ONCE(0),
    AT_LEAST_ONCE(1),
    EXACTLY_ONCE(2);

    private int qos;

    QoS(int qos) {
        this.qos = qos;
    }

    public int getValue() {
        return qos;
    }
}
