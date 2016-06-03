package com.cumulocity.me.agent.fieldbus;

import com.cumulocity.me.agent.fieldbus.evaluator.FieldbusChild;
import com.cumulocity.me.agent.fieldbus.impl.ChildDeviceScanner;
import com.cumulocity.me.agent.fieldbus.impl.FieldbusBuffer;
import com.cumulocity.me.agent.fieldbus.impl.FieldbusDeviceList;

public class FieldbusService {

    private final FieldbusDeviceList devices;
    private final ChildDeviceScanner scanner;
    private final FieldbusBuffer buffer;

    public FieldbusService(ChildDeviceScanner scanner, FieldbusBuffer buffer) {
        this.buffer = buffer;
        this.devices = new FieldbusDeviceList();
        this.scanner = scanner;
    }

    public void scanChildDevices(){
        scanner.scan(devices);
    }

    public void addDevice(FieldbusChild child){
        scanner.scanSingleDevice(child, devices);
    }

    public FieldbusDeviceList getDevices(){
        return devices;
    }

    public byte[] getValue(FieldbusBufferKey key){
        return buffer.get(key);
    }

    public byte[] setValue(FieldbusBufferKey key, byte[] value) {
        return buffer.put(key, value);
    }
}
