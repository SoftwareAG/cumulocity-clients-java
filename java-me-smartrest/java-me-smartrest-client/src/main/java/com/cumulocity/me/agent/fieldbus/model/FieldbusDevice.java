package com.cumulocity.me.agent.fieldbus.model;

public class FieldbusDevice {
    private final int address;
    private final FieldbusDeviceType type;

    public FieldbusDevice(int address, FieldbusDeviceType type) {
        this.address = address;
        this.type = type;
    }

    public FieldbusDeviceType getType() {
        return type;
    }

    public int getAddress() {
        return address;
    }
}
