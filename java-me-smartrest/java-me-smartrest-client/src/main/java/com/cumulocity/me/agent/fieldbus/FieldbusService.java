package com.cumulocity.me.agent.fieldbus;

import com.cumulocity.me.agent.fieldbus.evaluator.FieldbusChild;
import com.cumulocity.me.agent.fieldbus.impl.ChildDeviceScanner;
import com.cumulocity.me.agent.fieldbus.impl.FieldbusDeviceList;
import com.cumulocity.me.agent.fieldbus.model.FieldbusDevice;

public class FieldbusService {

    private final FieldbusDeviceList devices;
    private final ChildDeviceScanner scanner;

    public FieldbusService(ChildDeviceScanner scanner) {
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
}
