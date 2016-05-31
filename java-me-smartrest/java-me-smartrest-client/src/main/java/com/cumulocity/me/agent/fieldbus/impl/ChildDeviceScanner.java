package com.cumulocity.me.agent.fieldbus.impl;

import com.cumulocity.me.agent.fieldbus.FieldbusTemplates;
import com.cumulocity.me.agent.fieldbus.evaluator.ChildDevicesEvaluator;
import com.cumulocity.me.agent.fieldbus.evaluator.FieldbusChild;
import com.cumulocity.me.agent.fieldbus.evaluator.FieldbusTypeEvaluator;
import com.cumulocity.me.agent.fieldbus.model.FieldbusDevice;
import com.cumulocity.me.agent.smartrest.SmartrestService;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;

import java.util.Vector;

public class ChildDeviceScanner {
    private final String deviceId;
    private final SmartrestService smartrestService;

    public ChildDeviceScanner(String deviceId, SmartrestService smartrestService) {
        this.deviceId = deviceId;
        this.smartrestService = smartrestService;
    }

    public void scan(FieldbusDeviceList toFill){
        SmartRequest request = new SmartRequestImpl(FieldbusTemplates.GET_CHILD_DEVICES_REQUEST_MESSAGE_ID, deviceId);
        ChildDevicesEvaluator callback = new ChildDevicesEvaluator(smartrestService, toFill);
        send(request, callback);
    }

    public void scanSingleDevice(FieldbusChild child, FieldbusDeviceList toAppend) {
        SmartRequest request = new SmartRequestImpl(FieldbusTemplates.GET_FIELDBUS_DEVICE_TYPE_REQUEST_MESSAGE_ID, child.getType());
        FieldbusTypeEvaluator callback = new FieldbusTypeEvaluator(child, toAppend);
        send(request, callback);
    }

    private void send(SmartRequest request, SmartResponseEvaluator callback) {
        smartrestService.sendRequest(request, FieldbusTemplates.XID, callback);
    }

}
