package com.cumulocity.me.agent.fieldbus.model;

public class FieldbusDeviceType {
    private final String name;
    private final boolean useServerTime;

    private final CoilDefinition[] coils;
    private final RegisterDefinition[] registers;

    public FieldbusDeviceType(String name, boolean useServerTime, CoilDefinition[] coils, RegisterDefinition[] registers) {
        this.name = name;
        this.useServerTime = useServerTime;
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
}
