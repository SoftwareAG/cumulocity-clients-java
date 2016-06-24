package com.cumulocity.me.agent.fieldbus.evaluator;

import com.cumulocity.me.agent.fieldbus.FieldbusTemplates;
import com.cumulocity.me.agent.fieldbus.impl.AlarmMapKey;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.impl.SmartRow;

import java.util.Hashtable;

public class AlarmListEvaluator implements SmartResponseEvaluator {
    private final String deviceId;
    private final Hashtable alarmMap;

    public AlarmListEvaluator(String deviceId, Hashtable alarmMap) {
        this.deviceId = deviceId;
        this.alarmMap = alarmMap;
    }

    public void evaluate(SmartResponse response) {
        SmartRow[] rows = response.getDataRows();
        for (int i = 0; i < rows.length; i++) {
            SmartRow row = rows[i];
            if (!isAlarmArrayResponseRow(row)) {
                continue;
            }
            String alarmId = row.getData(0);
            String alarmType = row.getData(1);
            AlarmMapKey key = new AlarmMapKey(deviceId, alarmType);
            alarmMap.put(key, alarmId);
        }
    }

    private boolean isAlarmArrayResponseRow(SmartRow row){
        return row.getMessageId() == FieldbusTemplates.ALARMS_ARRAY_ID_TYPE_RESPONSE_MESSAGE_ID;
    }
}
