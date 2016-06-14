package com.cumulocity.me.agent;

public interface AgentTemplates {
    public static final String XID = "c8y_j2me_base_templates_v9";
    public static final String TEMPLATES 
            = "10,100,POST,/inventory/managedObjects,application/vnd.com.nsn.cumulocity.managedObject+json,application/vnd.com.nsn.cumulocity.managedObject+json,&&,,\"{\"\"agentName\"\":\"\"&&\"\",\"\"c8y_IsDevice\"\":{},\"\"com_cumulocity_model_Agent\"\":{},\"\"c8y_RequiredAvailability\"\":{\"\"responseInterval\"\":&&},\"\"type\"\":\"\"J2ME-Agent\"\"}\"\r\n"
            + "10,101,GET,/identity/externalIds/&&/&&,,,&&,,\r\n"
            + "10,110,POST,/identity/globalIds/&&/externalIds,application/vnd.com.nsn.cumulocity.externalId+json,application/vnd.com.nsn.cumulocity.externalId+json,&&,,\"{\"\"externalId\"\":\"\"&&\"\",\"\"type\"\":\"\"&&\"\"}\"\r\n"
            + "10,120,PUT,/inventory/managedObjects/&&,application/vnd.com.nsn.cumulocity.managedObject+json,,&&,UNSIGNED STRING STRING STRING,\"{\"\"c8y_Hardware\"\":{\"\"model\"\":\"\"&&\"\",\"\"revision\"\":\"\"&&\"\",\"\"serialNumber\"\":\"\"&&\"\"}}\"\r\n"
            + "10,121,PUT,/inventory/managedObjects/&&,application/vnd.com.nsn.cumulocity.managedObject+json,,&&,UNSIGNED STRING,\"{\"\"c8y_SupportedOperations\"\":[&&]}\"\r\n"
            + "10,130,PUT,/devicecontrol/operations/&&,application/vnd.com.nsn.cumulocity.operation+json,,&&,UNSIGNED,\"{\"\"status\"\":\"\"EXECUTING\"\"}\"\r\n"
            + "10,131,PUT,/devicecontrol/operations/&&,application/vnd.com.nsn.cumulocity.operation+json,,&&,UNSIGNED,\"{\"\"status\"\":\"\"SUCCESSFUL\"\"}\"\r\n"
            + "10,132,PUT,/devicecontrol/operations/&&,application/vnd.com.nsn.cumulocity.operation+json,,&&,UNSIGNED,\"{\"\"status\"\":\"\"FAILED\"\"}\"\r\n"
            + "10,133,PUT,/devicecontrol/operations/&&,application/vnd.com.nsn.cumulocity.operation+json,,&&,UNSIGNED STRING,\"{\"\"status\"\":\"\"FAILED\"\",\"\"failureReason\"\":\"\"&&\"\"}\"\r\n"
            + "11,1000,,$.c8y_IsDevice,$.id\r\n"
            + "11,1010,,$.externalId,$.managedObject.id";
    
    public static final int CREATE_MANAGED_OBJECT_REQUEST_MESSAGE_ID = 100;
    public static final int GET_EXTERNAL_ID_REQUEST_MESSAGE_ID = 101;
    public static final int CREATE_EXTERNAL_ID_REQUEST_MESSAGE_ID = 110;
    public static final int UPDATE_HARDWARE_REQUEST_MESSAGE_ID = 120;
    public static final int UPDATE_SUPPOERTED_OPERATIONS_REQUEST_MESSAGE_ID = 121;
    public static final int UPDATE_OPERATION_EXECUTING_REQUEST_MESSAGE_ID = 130;
    public static final int UPDATE_OPERATION_SUCCESSFUL_REQUEST_MESSAGE_ID = 131;
    public static final int UPDATE_OPERATION_FAILED_REQUEST_MESSAGE_ID = 132;
    public static final int UPDATE_OPERATION_FAILED_WITH_REASON_REQUEST_MESSAGE_ID = 133;

    public static final int MANAGED_OBJECT_ID_RESPONSE_MESSAGE_ID = 1000;
    public static final int EXTERNAL_ID_RESPONSE_MESSAGE_ID = 1010;
}
