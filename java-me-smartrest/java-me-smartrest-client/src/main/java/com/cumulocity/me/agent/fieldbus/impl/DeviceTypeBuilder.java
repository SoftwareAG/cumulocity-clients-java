package com.cumulocity.me.agent.fieldbus.impl;

import com.cumulocity.me.agent.fieldbus.model.*;
import com.cumulocity.me.agent.util.Arrays;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class DeviceTypeBuilder {
    private String name;
    private boolean useServerTime;
    private String type;
    private Vector coilDefinitions = new Vector();
    private Vector registerDefinitions = new Vector();
    private Hashtable coilBuilderMap = new Hashtable();
    private Hashtable registerBuilderMap = new Hashtable();

    public DeviceTypeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DeviceTypeBuilder withUseServerTime(boolean useServerTime) {
        this.useServerTime = useServerTime;
        return this;
    }

    public DeviceTypeBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public DeviceTypeBuilder withCoilDefinition(CoilDefinition coilDefinition) {
        this.coilDefinitions.addElement(coilDefinition);
        return this;
    }

    public DeviceTypeBuilder withRegisterDefinition(RegisterDefinition registerDefinition) {
        this.registerDefinitions.addElement(registerDefinition);
        return this;
    }

    public DeviceTypeBuilder withCoil(String id, int number, String name, boolean input){
        ensureCoilBuilderExists(id);
        CoilDefinitionBuilder builder = (CoilDefinitionBuilder) coilBuilderMap.get(id);
        builder.withNumber(number);
        builder.withName(name);
        builder.withInput(input);
        return this;
    }

    public DeviceTypeBuilder withCoil(String id, StatusMapping statusMapping){
        ensureCoilBuilderExists(id);
        CoilDefinitionBuilder builder = (CoilDefinitionBuilder) coilBuilderMap.get(id);
        builder.withStatusMapping(statusMapping);
        return this;
    }

    public DeviceTypeBuilder withCoil(String id, AlarmMapping alarmMapping){
        ensureCoilBuilderExists(id);
        CoilDefinitionBuilder builder = (CoilDefinitionBuilder) coilBuilderMap.get(id);
        builder.withAlarmMapping(alarmMapping);
        return this;
    }

    public DeviceTypeBuilder withCoil(String id, EventMapping eventMapping){
        ensureCoilBuilderExists(id);
        CoilDefinitionBuilder builder = (CoilDefinitionBuilder) coilBuilderMap.get(id);
        builder.withEventMapping(eventMapping);
        return this;
    }

    private void ensureCoilBuilderExists(String id) {
        if (!coilBuilderMap.containsKey(id)){
            coilBuilderMap.put(id, new CoilDefinitionBuilder());
        }
    }

    public DeviceTypeBuilder withRegister(String id, int number, String name, boolean input, boolean signed, int startBit, int noBits){
        ensureRegisterBuilderExists(id);
        RegisterDefinitionBuilder builder = (RegisterDefinitionBuilder) registerBuilderMap.get(id);
        builder.withNumber(number);
        builder.withName(name);
        builder.withInput(input);
        builder.withSigned(signed);
        builder.withStartBit(startBit);
        builder.withLength(noBits);
        return this;
    }

    public DeviceTypeBuilder withRegister(String id, int multiplier, int divisor, int offset, int decimalPlaces){
        ensureRegisterBuilderExists(id);
        RegisterDefinitionBuilder builder = (RegisterDefinitionBuilder) registerBuilderMap.get(id);
        builder.withConversion(new Conversion(multiplier, divisor, decimalPlaces, offset));
        return this;
    }

    public DeviceTypeBuilder withRegister(String id, StatusMapping statusMapping){
        ensureRegisterBuilderExists(id);
        RegisterDefinitionBuilder builder = (RegisterDefinitionBuilder) registerBuilderMap.get(id);
        builder.withStatusMapping(statusMapping);
        return this;
    }

    public DeviceTypeBuilder withRegister(String id, AlarmMapping alarmMapping){
        ensureRegisterBuilderExists(id);
        RegisterDefinitionBuilder builder = (RegisterDefinitionBuilder) registerBuilderMap.get(id);
        builder.withAlarmMapping(alarmMapping);
        return this;
    }

    public DeviceTypeBuilder withRegister(String id, EventMapping eventMapping){
        ensureRegisterBuilderExists(id);
        RegisterDefinitionBuilder builder = (RegisterDefinitionBuilder) registerBuilderMap.get(id);
        builder.withEventMapping(eventMapping);
        return this;
    }

    public DeviceTypeBuilder withRegister(String id, MeasurementMapping measurementMapping) {
        ensureRegisterBuilderExists(id);
        RegisterDefinitionBuilder builder = (RegisterDefinitionBuilder) registerBuilderMap.get(id);
        builder.withMeasurementMapping(measurementMapping);
        return this;
    }


    private void ensureRegisterBuilderExists(String id) {
        if (!registerBuilderMap.containsKey(id)){
            registerBuilderMap.put(id, new RegisterDefinitionBuilder());
        }
    }

    public FieldbusDeviceType build(){
        includeSubBuilders();
        CoilDefinition[] coilDefinitionsArray = new CoilDefinition[coilDefinitions.size()];
        coilDefinitions.copyInto(coilDefinitionsArray);
        Arrays.bubbleSort(coilDefinitionsArray, new CoilDefinitionComparator());
        RegisterDefinition[] registerDefinitionArray = new RegisterDefinition[registerDefinitions.size()];
        registerDefinitions.copyInto(registerDefinitionArray);
        Arrays.bubbleSort(registerDefinitionArray, new RegisterDefinitionComparator());

        return new FieldbusDeviceType(name, useServerTime, type, coilDefinitionsArray, registerDefinitionArray);
    }

    private void includeSubBuilders() {
        Enumeration enumeration = coilBuilderMap.elements();
        while (enumeration.hasMoreElements()) {
            CoilDefinitionBuilder builder = (CoilDefinitionBuilder) enumeration.nextElement();
            coilDefinitions.addElement(builder.build());
        }
        enumeration = registerBuilderMap.elements();
        while (enumeration.hasMoreElements()) {
            RegisterDefinitionBuilder builder = (RegisterDefinitionBuilder) enumeration.nextElement();
            registerDefinitions.addElement(builder.build());
        }
    }
}
