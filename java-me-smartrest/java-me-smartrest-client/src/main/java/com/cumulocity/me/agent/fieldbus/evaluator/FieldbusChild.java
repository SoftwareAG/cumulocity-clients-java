package com.cumulocity.me.agent.fieldbus.evaluator;

public class FieldbusChild {
    private final String protocol;
    private final String id;
    private final String name;
    private final int address;
    private final String type;

    public FieldbusChild(String protocol, String id, String name, int address, String type) {
        this.protocol = protocol;
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getAddress() {
        return address;
    }
}
