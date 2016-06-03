package com.cumulocity.me.agent.fieldbus.evaluator;

import com.cumulocity.me.agent.fieldbus.FieldbusTemplates;
import com.cumulocity.me.agent.fieldbus.impl.DeviceTypeBuilder;
import com.cumulocity.me.agent.fieldbus.impl.FieldbusDeviceList;
import com.cumulocity.me.agent.fieldbus.model.*;
import com.cumulocity.me.agent.util.BooleanParser;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.impl.SmartRow;

public class FieldbusTypeEvaluator implements SmartResponseEvaluator{

    private final FieldbusChild child;
    private final FieldbusDeviceList toAppend;
    private final DeviceTypeBuilder typeBuilder = new DeviceTypeBuilder();

    public FieldbusTypeEvaluator(FieldbusChild child, FieldbusDeviceList toAppend) {
        this.child = child;
        this.toAppend = toAppend;
    }

    public void evaluate(SmartResponse response) {
        SmartRow[] rows = response.getDataRows();
        for (int i = 0; i < rows.length; i++) {
            SmartRow row = rows[i];
            if (FieldbusTemplates.DEVICE_TYPE_BASE_RESPONSE_MESSAGE_ID == row.getMessageId()) {
                handleBaseRow(row);
            } else {
                handleDefinitionRow(row);
            }
        }
        typeBuilder.withType(child.getType());
        FieldbusDevice device = new FieldbusDevice(child.getId(), child.getName(), child.getAddress(), typeBuilder.build());
        toAppend.addElement(device);
    }

    private void handleDefinitionRow(SmartRow row) {
        String uniqueId = row.getData(0);
        switch (row.getMessageId()) {
            case FieldbusTemplates.DEVICE_TYPE_COIL_RESPONSE_MESSAGE_ID:
                int coilNumber = Integer.parseInt(row.getData(1));
                String coilName = row.getData(2);
                boolean coilInput = BooleanParser.parse(row.getData(3)).booleanValue();
                typeBuilder.withCoil(uniqueId, coilNumber, coilName, coilInput);
                break;
            case FieldbusTemplates.DEVICE_TYPE_COIL_STATUS_RESPONSE_MESSAGE_ID:
                StatusMapping coilStatusMapping = parseStatusMapping(row);
                typeBuilder.withCoil(uniqueId, coilStatusMapping);
                break;
            case FieldbusTemplates.DEVICE_TYPE_COIL_ALARM_RESPONSE_MESSAGE_ID:
                AlarmMapping coilAlarmMapping = parseAlarmMapping(row);
                typeBuilder.withCoil(uniqueId, coilAlarmMapping);
                break;
            case FieldbusTemplates.DEVICE_TYPE_COIL_EVENT_RESPONSE_MESSAGE_ID:
                EventMapping coilEventMapping = parseEventMapping(row);
                typeBuilder.withCoil(uniqueId, coilEventMapping);
                break;
            case FieldbusTemplates.DEVICE_TYPE_REGISTER_RESPONSE_MESSAGE_ID:
                int registerNumber = Integer.parseInt(row.getData(1));
                String registerName = row.getData(2);
                boolean registerInput = BooleanParser.parse(row.getData(3)).booleanValue();
                boolean registerSigned = BooleanParser.parse(row.getData(4)).booleanValue();
                int registerStartBit = Integer.parseInt(row.getData(5));
                int registerNoBits = Integer.parseInt(row.getData(6));
                typeBuilder.withRegister(uniqueId, registerNumber, registerName, registerInput, registerSigned, registerStartBit, registerNoBits);
                int registerMultiplier = Integer.parseInt(row.getData(7));
                int registerDivisor = Integer.parseInt(row.getData(8));
                int registerOffset = Integer.parseInt(row.getData(9));
                int registerDecimalPlaces = Integer.parseInt(row.getData(10));
                typeBuilder.withRegister(uniqueId, registerMultiplier, registerDivisor, registerOffset, registerDecimalPlaces);
                break;
            case FieldbusTemplates.DEVICE_TYPE_REGISTER_STATUS_RESPONSE_MESSAGE_ID:
                StatusMapping registerStatusMapping = parseStatusMapping(row);
                typeBuilder.withRegister(uniqueId, registerStatusMapping);
                break;
            case FieldbusTemplates.DEVICE_TYPE_REGISTER_ALARM_RESPONSE_MESSAGE_ID:
                AlarmMapping registerAlarmMapping = parseAlarmMapping(row);
                typeBuilder.withRegister(uniqueId, registerAlarmMapping);
                break;
            case FieldbusTemplates.DEVICE_TYPE_REGISTER_EVENT_RESPONSE_MESSAGE_ID:
                EventMapping registerEventMapping = parseEventMapping(row);
                typeBuilder.withRegister(uniqueId, registerEventMapping);
                break;
            case FieldbusTemplates.DEVICE_TYPE_REGISTER_MEASUREMENT_RESPONSE_MESSAGE_ID:
                MeasurementMapping registerMeasurementMapping = parseMeasurementMapping(row);
                typeBuilder.withRegister(uniqueId, registerMeasurementMapping);
        }
    }

    private void handleBaseRow(SmartRow row) {
        String name = row.getData(0);
        typeBuilder.withName(name);
        boolean useServerTime = BooleanParser.parse(row.getData(1)).booleanValue();
        typeBuilder.withUseServerTime(useServerTime);
    }

    private MeasurementMapping parseMeasurementMapping(SmartRow row) {
        int measurementTemplate = Integer.parseInt(row.getData(1));
        String measurementType = row.getData(2);
        String measurementSeries = row.getData(3);
        return new MeasurementMapping(measurementTemplate, measurementType, measurementSeries);
    }

    private StatusMapping parseStatusMapping(SmartRow row) {
        StatusMappingType coilStatusMappingType = StatusMappingType.get(row.getData(1));
        return new StatusMapping(coilStatusMappingType);
    }

    private AlarmMapping parseAlarmMapping(SmartRow row) {
        int alarmTemplate = Integer.parseInt(row.getData(1));
        String alarmType = row.getData(2);
        String alarmText = row.getData(3);
        AlarmSeverity alarmSeverity = AlarmSeverity.get(row.getData(4));
        return new AlarmMapping(alarmTemplate, alarmType, alarmText, alarmSeverity);
    }

    private EventMapping parseEventMapping(SmartRow row) {
        int eventTemplate = Integer.parseInt(row.getData(1));
        String eventType = row.getData(2);
        String eventText = row.getData(3);
        return new EventMapping(eventTemplate, eventType, eventText);
    }
}
