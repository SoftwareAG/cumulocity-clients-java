package com.cumulocity.me.agent.fieldbus.model;

public class FieldbusDevice {
    private final String id;
    private final String name;
    private final int address;
    private final FieldbusDeviceType type;

    public FieldbusDevice(String id, String name, int address, FieldbusDeviceType type) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
    }

    public FieldbusDeviceType getType() {
        return type;
    }

    public int getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
