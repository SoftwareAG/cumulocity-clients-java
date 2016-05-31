package com.cumulocity.me.agent.fieldbus.evaluator;

import com.cumulocity.me.agent.fieldbus.FieldbusTemplates;
import com.cumulocity.me.agent.fieldbus.impl.FieldbusDeviceList;
import com.cumulocity.me.agent.smartrest.SmartrestService;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;
import com.cumulocity.me.smartrest.client.impl.SmartRow;

import java.util.Vector;

public class ChildDevicesEvaluator implements SmartResponseEvaluator{

    private final SmartrestService smartrestService;
    private final FieldbusDeviceList toFill;

    public ChildDevicesEvaluator(SmartrestService smartrestService, FieldbusDeviceList toFill) {
        this.smartrestService = smartrestService;
        this.toFill = toFill;
    }

    public void evaluate(SmartResponse response) {
        SmartRow[] rows = response.getDataRows();
        for (int i = 0; i < rows.length; i++) {
            SmartRow row = rows[i];
            FieldbusChild fieldbusChild = new ChildDeviceRowParser(row).parse();
            SmartRequest request = new SmartRequestImpl(FieldbusTemplates.GET_FIELDBUS_DEVICE_TYPE_REQUEST_MESSAGE_ID, fieldbusChild.getType());
            FieldbusTypeEvaluator fieldbusTypeEvaluator = new FieldbusTypeEvaluator(fieldbusChild, toFill);
            smartrestService.sendRequest(request, FieldbusTemplates.XID, fieldbusTypeEvaluator);
        }
    }
}
