package com.cumulocity.rest.representation.operation;

import com.cumulocity.rest.representation.CumulocityMediaType;

public class DeviceControlMediaType extends CumulocityMediaType {
    
    public static final DeviceControlMediaType OPERATION = new DeviceControlMediaType("operation");

    public static final String OPERATION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "operation+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final DeviceControlMediaType OPERATION_COLLECTION = new DeviceControlMediaType("operationCollection");

    public static final String OPERATION_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "operationCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final DeviceControlMediaType DEVICE_CONTROL_API = new DeviceControlMediaType("devicecontrolApi");

    public static final String DEVICE_CONTROL_API_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "devicecontrolApi+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;
    
    public static final DeviceControlMediaType NEW_DEVICE_REQUEST = new DeviceControlMediaType("newDeviceRequest");
    
    public static final String NEW_DEVICE_REQUEST_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "newDeviceRequest+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;
    
    public static final DeviceControlMediaType NEW_DEVICE_REQUEST_COLLECTION = new DeviceControlMediaType("newDeviceRequestCollection");
    
    public static final String NEW_DEVICE_REQUEST_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "newDeviceRequestCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;
    
    public static final DeviceControlMediaType DEVICE_CREDENTIALS = new DeviceControlMediaType("deviceCredentials");
    
    public static final String DEVICE_CREDENTIALS_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "deviceCredentials+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;
    
    public static final DeviceControlMediaType DEVICE_CREDENTIALS_COLLECTION = new DeviceControlMediaType("deviceCredentialsCollection");
    
    public static final String DEVICE_CREDENTIALS_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "deviceCredentialsCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final DeviceControlMediaType BULK_NEW_DEVICE_REQUEST = new DeviceControlMediaType("bulkNewDeviceRequest");

    public static final String BULK_NEW_DEVICE_REQUEST_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "bulkNewDeviceRequest+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public DeviceControlMediaType(String string) {
        super(string);
    }
}
