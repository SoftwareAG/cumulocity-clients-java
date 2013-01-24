package com.cumulocity.me.rest.representation.operation;

import com.cumulocity.me.rest.representation.BaseCumulocityMediaType;

public class DeviceControlMediaType extends BaseCumulocityMediaType {
    
    public static final DeviceControlMediaType OPERATION = new DeviceControlMediaType("operation");

    public static final String OPERATION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "operation+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final DeviceControlMediaType OPERATION_COLLECTION = new DeviceControlMediaType("operationCollection");

    public static final String OPERATION_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "operationCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final DeviceControlMediaType DEVICE_CONTROL_API = new DeviceControlMediaType("devicecontrolApi");

    public static final String DEVICE_CONTROL_API_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "devicecontrolApi+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public DeviceControlMediaType(String string) {
        super(string);
    }
}
