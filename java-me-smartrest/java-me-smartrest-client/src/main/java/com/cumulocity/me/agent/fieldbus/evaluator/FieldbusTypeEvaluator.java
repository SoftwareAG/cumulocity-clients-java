package com.cumulocity.me.agent.fieldbus.evaluator;

import com.cumulocity.me.agent.fieldbus.FieldbusTemplates;
import com.cumulocity.me.agent.fieldbus.impl.DeviceTypeBuilder;
import com.cumulocity.me.agent.fieldbus.impl.FieldbusDeviceList;
import com.cumulocity.me.agent.fieldbus.impl.RegisterDefinitionBuilder;
import com.cumulocity.me.agent.fieldbus.model.*;
import com.cumulocity.me.agent.util.BooleanParser;
import com.cumulocity.me.agent.util.Callback;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.impl.SmartRow;

public class FieldbusTypeEvaluator implements SmartResponseEvaluator{

    private final FieldbusChild child;
    private final FieldbusDeviceList toAppend;
    private final DeviceTypeBuilder typeBuilder = new DeviceTypeBuilder();
    private final Callback onFinished;

    public FieldbusTypeEvaluator(FieldbusChild child, FieldbusDeviceList toAppend, Callback onFinished) {
        this.child = child;
        this.toAppend = toAppend;
        this.onFinished = onFinished;
    }

    public void evaluate(SmartResponse response) {
        SmartRow[] rows = response.getDataRows();
        for (int i = 0; i < rows.length; i++) {
            SmartRow row = rows[i];
            if (isBaseRow(row)) {
                handleBaseRow(row);
            } else {
                handleDefinitionRow(row);
            }
        }
        typeBuilder.withType(child.getType());
        FieldbusDevice device = new FieldbusDevice(child.getId(), child.getName(), child.getAddress(), typeBuilder.build(), child.getProtocol());
        toAppend.addElement(device);
        Callback.execute(onFinished);

    }

    private boolean isBaseRow(SmartRow row) {
        return FieldbusTemplates.DEVICE_TYPE_NAME_RESPONSE_MESSAGE_ID == row.getMessageId() || FieldbusTemplates.DEVICE_TYPE_TIME_RESPONSE_MESSAGE_ID == row.getMessageId();
    }

    private void handleDefinitionRow(SmartRow row) {
        String uniqueId = row.getData(0);
        switch (row.getMessageId()) {
            case FieldbusTemplates.DEVICE_TYPE_COIL_RESPONSE_MESSAGE_ID:
                parseCoilBaseParameters(row, uniqueId);
                break;
            case FieldbusTemplates.DEVICE_TYPE_COIL_STATUS_RESPONSE_MESSAGE_ID:
                parseCoilStatusMapping(row, uniqueId);
                break;
            case FieldbusTemplates.DEVICE_TYPE_COIL_ALARM_RESPONSE_MESSAGE_ID:
                parseCoilAlarmMapping(row, uniqueId);
                break;
            case FieldbusTemplates.DEVICE_TYPE_COIL_EVENT_RESPONSE_MESSAGE_ID:
                parseCoilEventMapping(row, uniqueId);
                break;
            case FieldbusTemplates.DEVICE_TYPE_REGISTER_RESPONSE_MESSAGE_ID:
                parseRegisterBaseParameters(row, uniqueId);
                break;
            case FieldbusTemplates.DEVICE_TYPE_REGISTER_WITHOUT_OFFSET_RESPONSE_MESSAGE_ID:
                parseRegisterBaseParametersWithoutOffset(row, uniqueId);
                break;
            case FieldbusTemplates.DEVICE_TYPE_REGISTER_STATUS_RESPONSE_MESSAGE_ID:
                parseRegisterStatusMapping(row, uniqueId);
                break;
            case FieldbusTemplates.DEVICE_TYPE_REGISTER_ALARM_RESPONSE_MESSAGE_ID:
                parseRegisterAlarmMapping(row, uniqueId);
                break;
            case FieldbusTemplates.DEVICE_TYPE_REGISTER_EVENT_RESPONSE_MESSAGE_ID:
                parseRegisterEventMapping(row, uniqueId);
                break;
            case FieldbusTemplates.DEVICE_TYPE_REGISTER_MEASUREMENT_RESPONSE_MESSAGE_ID:
                parseRegisterMeasurementMapping(row, uniqueId);
        }
    }

    private void parseRegisterMeasurementMapping(SmartRow row, String uniqueId) {
        MeasurementMapping measurementMapping = parseMeasurementMapping(row);
        typeBuilder.withRegister(uniqueId, measurementMapping);
    }

    private void parseRegisterEventMapping(SmartRow row, String uniqueId) {
        EventMapping eventMapping = parseEventMapping(row);
        typeBuilder.withRegister(uniqueId, eventMapping);
    }

    private void parseRegisterAlarmMapping(SmartRow row, String uniqueId) {
        AlarmMapping alarmMapping = parseAlarmMapping(row);
        typeBuilder.withRegister(uniqueId, alarmMapping);
    }

    private void parseRegisterStatusMapping(SmartRow row, String uniqueId) {
        StatusMapping statusMapping = parseStatusMapping(row);
        typeBuilder.withRegister(uniqueId, statusMapping);
    }

    private void parseRegisterBaseParameters(SmartRow row, String uniqueId) {
        int number = parseNumber(row.getData(1));
        String name = row.getData(2);
        boolean isInput = BooleanParser.parse(row.getData(3)).booleanValue();
        boolean isSigned = BooleanParser.parse(row.getData(4)).booleanValue();
        int startBit = Integer.parseInt(row.getData(5));
        int noBits = Integer.parseInt(row.getData(6));
        typeBuilder.withRegister(uniqueId, number, name, isInput, isSigned, startBit, noBits);
        int multiplier = Integer.parseInt(row.getData(7));
        int divisor = Integer.parseInt(row.getData(8));
        int offset = Integer.parseInt(row.getData(9));
        int decimalPlaces = Integer.parseInt(row.getData(10));
        typeBuilder.withRegister(uniqueId, multiplier, divisor, offset, decimalPlaces);
    }

    private void parseRegisterBaseParametersWithoutOffset(SmartRow row, String uniqueId) {
        RegisterDefinitionBuilder registerDefinitionBuilder = typeBuilder.getRegisterDefinitionBuilder(uniqueId);
        if (registerDefinitionBuilder == null || registerDefinitionBuilder.getName() == null) {
            int number = parseNumber(row.getData(1));
            String name = row.getData(2);
            boolean isInput = BooleanParser.parse(row.getData(3)).booleanValue();
            boolean isSigned = BooleanParser.parse(row.getData(4)).booleanValue();
            int startBit = Integer.parseInt(row.getData(5));
            int noBits = Integer.parseInt(row.getData(6));

            int multiplier = Integer.parseInt(row.getData(7));
            int divisor = Integer.parseInt(row.getData(8));
            int decimalPlaces = Integer.parseInt(row.getData(9));

            typeBuilder.withRegister(uniqueId, number, name, isInput, isSigned, startBit, noBits);
            typeBuilder.withRegister(uniqueId, multiplier, divisor, 0, decimalPlaces);
        }
    }

    private void parseCoilEventMapping(SmartRow row, String uniqueId) {
        EventMapping eventMapping = parseEventMapping(row);
        typeBuilder.withCoil(uniqueId, eventMapping);
    }

    private void parseCoilAlarmMapping(SmartRow row, String uniqueId) {
        AlarmMapping alarmMapping = parseAlarmMapping(row);
        typeBuilder.withCoil(uniqueId, alarmMapping);
    }

    private void parseCoilStatusMapping(SmartRow row, String uniqueId) {
        StatusMapping statusMapping = parseStatusMapping(row);
        typeBuilder.withCoil(uniqueId, statusMapping);
    }

    private void parseCoilBaseParameters(SmartRow row, String uniqueId) {
        int number = parseNumber(row.getData(1));
        String name = row.getData(2);
        boolean isInput = BooleanParser.parse(row.getData(3)).booleanValue();
        typeBuilder.withCoil(uniqueId, number, name, isInput);
    }

    private void handleBaseRow(SmartRow row) {
        switch (row.getMessageId()) {
            case FieldbusTemplates.DEVICE_TYPE_NAME_RESPONSE_MESSAGE_ID:
                parseDeviceTypeName(row);
                break;
            case FieldbusTemplates.DEVICE_TYPE_TIME_RESPONSE_MESSAGE_ID:
                parseDeviceTypeTime(row);
        }
    }

    private void parseDeviceTypeTime(SmartRow row) {
        boolean useServerTime = BooleanParser.parse(row.getData(0)).booleanValue();
        typeBuilder.withUseServerTime(useServerTime);
    }

    private void parseDeviceTypeName(SmartRow row) {
        String name = row.getData(0);
        typeBuilder.withName(name);
    }

    private MeasurementMapping parseMeasurementMapping(SmartRow row) {
        int template = Integer.parseInt(row.getData(1));
        String type = row.getData(2);
        String series = row.getData(3);
        return new MeasurementMapping(template, type, series);
    }

    private StatusMapping parseStatusMapping(SmartRow row) {
        StatusMappingType statusMappingType = StatusMappingType.get(row.getData(1));
        return new StatusMapping(statusMappingType);
    }

    private AlarmMapping parseAlarmMapping(SmartRow row) {
        int template = Integer.parseInt(row.getData(1));
        String type = row.getData(2);
        String text = row.getData(3);
        AlarmSeverity severity = AlarmSeverity.get(row.getData(4));
        return new AlarmMapping(template, type, text, severity);
    }

    private EventMapping parseEventMapping(SmartRow row) {
        int template = Integer.parseInt(row.getData(1));
        String type = row.getData(2);
        String text = row.getData(3);
        return new EventMapping(template, type, text);
    }

    private int parseNumber(String source) {
        if (source.startsWith("0x")){
            return Integer.parseInt(source.substring(2), 16);
        }
        return Integer.parseInt(source);
    }
}
