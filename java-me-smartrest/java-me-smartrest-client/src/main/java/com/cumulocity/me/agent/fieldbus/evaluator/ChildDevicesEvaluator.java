package com.cumulocity.me.agent.fieldbus.evaluator;

import com.cumulocity.me.agent.fieldbus.FieldbusTemplates;
import com.cumulocity.me.agent.fieldbus.impl.FieldbusDeviceList;
import com.cumulocity.me.agent.smartrest.SmartrestService;
import com.cumulocity.me.agent.util.Callback;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;
import com.cumulocity.me.smartrest.client.impl.SmartRow;

import java.util.Hashtable;

public class ChildDevicesEvaluator implements SmartResponseEvaluator{

    private final SmartrestService smartrestService;
    private final FieldbusDeviceList toFill;
    private final Hashtable alarmMap;
    private final Callback onFinished;

    public ChildDevicesEvaluator(SmartrestService smartrestService, FieldbusDeviceList toFill, Hashtable alarmMap, Callback onFinished) {
        this.smartrestService = smartrestService;
        this.toFill = toFill;
        this.alarmMap = alarmMap;
        this.onFinished = onFinished;
    }

    public void evaluate(SmartResponse response) {
        SmartRow[] rows = response.getDataRows();
        if (rows.length > 0) {
            Callback combinedCallback = new MultipleCallCallback(rows.length, onFinished);
            for (int i = 0; i < rows.length; i++) {
                SmartRow row = rows[i];
                final FieldbusChild fieldbusChild = new ChildDeviceRowParser(row).parse();
                SmartRequest request = new SmartRequestImpl(FieldbusTemplates.GET_FIELDBUS_DEVICE_TYPE_REQUEST_MESSAGE_ID, fieldbusChild.getType());
                FieldbusTypeEvaluator fieldbusTypeEvaluator = new FieldbusTypeEvaluator(fieldbusChild, toFill, combinedCallback);
                smartrestService.sendRequest(request, FieldbusTemplates.XID, fieldbusTypeEvaluator);

                SmartRequest alarmRequest = new SmartRequestImpl(FieldbusTemplates.GET_ACTIVE_ALARMS_REQUEST_MESSAGE_ID, fieldbusChild.getId());
                smartrestService.sendRequest(alarmRequest, FieldbusTemplates.XID, new AlarmListEvaluator(fieldbusChild.getId(), alarmMap));
            }
        } else {
            Callback.execute(onFinished);
        }
    }

    private class MultipleCallCallback extends Callback {
        private int number;
        private final Callback delegate;

        private MultipleCallCallback(int number, Callback delegate) {
            this.number = number;
            this.delegate = delegate;
        }

        public void execute() {
            if (--number <= 0) {
                Callback.execute(delegate);
            }
        }
    }

}
