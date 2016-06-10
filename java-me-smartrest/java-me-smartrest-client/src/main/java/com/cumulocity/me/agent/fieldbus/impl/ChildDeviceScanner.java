package com.cumulocity.me.agent.fieldbus.impl;

import com.cumulocity.me.agent.fieldbus.FieldbusTemplates;
import com.cumulocity.me.agent.fieldbus.evaluator.ChildDevicesEvaluator;
import com.cumulocity.me.agent.fieldbus.evaluator.FieldbusChild;
import com.cumulocity.me.agent.fieldbus.evaluator.FieldbusTypeEvaluator;
import com.cumulocity.me.agent.smartrest.SmartrestService;
import com.cumulocity.me.agent.util.Callback;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;

import java.util.Hashtable;

public class ChildDeviceScanner {
    private final String deviceId;
    private final SmartrestService smartrestService;
    private final Hashtable alarmMap;

    public ChildDeviceScanner(String deviceId, SmartrestService smartrestService, Hashtable alarmMap) {
        this.deviceId = deviceId;
        this.smartrestService = smartrestService;
        this.alarmMap = alarmMap;
    }

    public void scan(FieldbusDeviceList toFill, Callback onFinished){
        SmartRequest request = new SmartRequestImpl(FieldbusTemplates.GET_CHILD_DEVICES_REQUEST_MESSAGE_ID, deviceId);
        ChildDevicesEvaluator callback = new ChildDevicesEvaluator(smartrestService, toFill, alarmMap, onFinished);
        send(request, callback);
    }

    public void scanSingleDevice(FieldbusChild child, FieldbusDeviceList toAppend, Callback onFinished) {
        SmartRequest request = new SmartRequestImpl(FieldbusTemplates.GET_FIELDBUS_DEVICE_TYPE_REQUEST_MESSAGE_ID, child.getType());
        FieldbusTypeEvaluator callback = new FieldbusTypeEvaluator(child, toAppend, onFinished);
        send(request, callback);
    }

    private void send(SmartRequest request, SmartResponseEvaluator callback) {
        smartrestService.sendRequest(request, FieldbusTemplates.XID, callback);
    }

}
