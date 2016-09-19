package com.cumulocity.me.agent.fieldbus.model;

public class FieldbusDevice {
    private final String id;
    private final String name;
    private final int address;
    private final FieldbusDeviceType type;
    private final String protocol;

    public FieldbusDevice(String id, String name, int address, FieldbusDeviceType type, String protocol) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
        this.protocol = protocol;
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

    public String getProtocol() {
        return protocol;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldbusDevice device = (FieldbusDevice) o;

        return id != null ? id.equals(device.id) : device.id == null;

    }

    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
