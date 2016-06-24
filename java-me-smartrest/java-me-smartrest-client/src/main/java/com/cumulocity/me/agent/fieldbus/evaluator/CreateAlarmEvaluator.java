package com.cumulocity.me.agent.fieldbus.evaluator;

import com.cumulocity.me.agent.fieldbus.FieldbusTemplates;
import com.cumulocity.me.agent.fieldbus.impl.AlarmMapKey;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.impl.SmartRow;

import java.util.Hashtable;

public class CreateAlarmEvaluator implements SmartResponseEvaluator{
    private final String deviceId;
    private final String alarmType;
    private final Hashtable alarmMap;

    public CreateAlarmEvaluator(String deviceId, String alarmType, Hashtable alarmMap) {
        this.alarmMap = alarmMap;
        this.deviceId = deviceId;
        this.alarmType = alarmType;
    }

    public void evaluate(SmartResponse response) {
        SmartRow row = response.getRow(0);
        if (row.getMessageId() == FieldbusTemplates.CREATE_ALARM_RESPONSE_MESSAGE_ID) {
            String alarmId = row.getData(0);
            alarmMap.put(new AlarmMapKey(deviceId, alarmType), alarmId);
        }
    }
}
