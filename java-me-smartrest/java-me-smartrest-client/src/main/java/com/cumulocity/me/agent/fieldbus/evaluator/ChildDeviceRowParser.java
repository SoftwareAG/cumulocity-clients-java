package com.cumulocity.me.agent.fieldbus.evaluator;

import com.cumulocity.me.agent.fieldbus.FieldbusTemplates;
import com.cumulocity.me.smartrest.client.impl.SmartRow;

public class ChildDeviceRowParser {
    private final SmartRow row;

    public ChildDeviceRowParser(SmartRow row) {
        this.row = row;
    }

    public FieldbusChild parse(){
        String childId = row.getData(0);
        if (FieldbusTemplates.MODBUS_CHILD_DEVICE_RESPONSE_MESSAGE_ID == row.getMessageId()){
            String childName = row.getData(1);
            String modbusAddress = row.getData(2);
            String fieldbusType = row.getData(3);
            return new FieldbusChild(FieldbusProtocol.MODBUS, childId, childName, Integer.parseInt(modbusAddress), fieldbusType);
        } else if (FieldbusTemplates.CAN_CHILD_DEVICE_RESPONSE_MESSAGE_ID == row.getMessageId()){
            String fieldbusType = row.getData(1);
            return new FieldbusChild(FieldbusProtocol.CAN, childId, childId, -1, fieldbusType);
        }
        return null;
    }
}
