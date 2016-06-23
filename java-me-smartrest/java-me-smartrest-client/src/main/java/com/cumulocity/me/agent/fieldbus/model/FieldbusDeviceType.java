package com.cumulocity.me.agent.fieldbus.model;

public class FieldbusDeviceType {
    private final String name;
    private final boolean useServerTime;
    private final String type;

    private final CoilDefinition[] coils;
    private final RegisterDefinition[] registers;

    public FieldbusDeviceType(String name, boolean useServerTime, String type, CoilDefinition[] coils, RegisterDefinition[] registers) {
        this.name = name;
        this.useServerTime = useServerTime;
        this.type = type;
        this.coils = coils;
        this.registers = registers;
    }

    public String getName() {
        return name;
    }

    public boolean isUseServerTime() {
        return useServerTime;
    }

    public CoilDefinition[] getCoils() {
        return coils;
    }

    public RegisterDefinition[] getRegisters() {
        return registers;
    }

    public String getType() {
        return type;
    }
}
