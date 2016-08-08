package com.cumulocity.me.agent.fieldbus;

import com.cumulocity.me.agent.fieldbus.evaluator.CreateAlarmEvaluator;
import com.cumulocity.me.agent.fieldbus.evaluator.FieldbusChild;
import com.cumulocity.me.agent.fieldbus.impl.AlarmMapKey;
import com.cumulocity.me.agent.fieldbus.impl.ChildDeviceScanner;
import com.cumulocity.me.agent.fieldbus.impl.FieldbusBuffer;
import com.cumulocity.me.agent.fieldbus.impl.FieldbusDeviceList;
import com.cumulocity.me.agent.fieldbus.model.AlarmMapping;
import com.cumulocity.me.agent.smartrest.SmartrestService;
import com.cumulocity.me.agent.util.Callback;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;

import java.util.Hashtable;

public class FieldbusService {

    private volatile FieldbusDeviceList devices;
    private final ChildDeviceScanner childDeviceScanner;
    private final FieldbusBuffer buffer;
    private final SmartrestService smartrestService;
    private final Hashtable alarmMap;

    public FieldbusService(String deviceId, FieldbusBuffer buffer, SmartrestService smartrestService) {
        this.buffer = buffer;
        this.smartrestService = smartrestService;
        this.alarmMap = new Hashtable();
        this.devices = new FieldbusDeviceList();
        this.childDeviceScanner = new ChildDeviceScanner(deviceId, smartrestService, alarmMap);
    }

    public void scanChildDevices(Callback onFinished) {
        childDeviceScanner.scan(devices, onFinished);

    }


    public void addDevice(FieldbusChild child, Callback onFinished) {
        childDeviceScanner.scanSingleDevice(child, devices, onFinished);
    }

    public FieldbusDeviceList getDevices() {
        return devices;
    }

    public byte[] getValue(FieldbusBufferKey key) {
        return buffer.get(key);
    }

    public byte[] setValue(FieldbusBufferKey key, byte[] value) {
        return buffer.put(key, value);
    }

    public void createAlarm(String deviceId, AlarmMapping alarmMapping) {
        StringBuffer requestData = new StringBuffer(deviceId);
        requestData.append(',').append(alarmMapping.getType());
        requestData.append(',').append(alarmMapping.getText());
        requestData.append(',').append(alarmMapping.getSeverity().getValue());
        SmartRequest request = new SmartRequestImpl(FieldbusTemplates.CREATE_ALARM_REQUEST_MESSAGE_ID, requestData.toString());
        smartrestService.sendRequest(request, FieldbusTemplates.XID, new CreateAlarmEvaluator(deviceId, alarmMapping.getType(), alarmMap));
    }

    public void clearAlarm(String deviceId, String type) {
        String alarmId = (String) alarmMap.remove(new AlarmMapKey(deviceId, type));
        if (alarmId != null) {
            SmartRequest request = new SmartRequestImpl(FieldbusTemplates.CLEAR_ALARM_REQUEST_MESSAGE_ID, alarmId);
            smartrestService.sendRequest(request, FieldbusTemplates.XID);
        }
    }

    public Hashtable getAlarmMap() {
        return alarmMap;
    }

    public ChildDeviceScanner getChildDeviceScanner() {
        return childDeviceScanner;
    }
}
