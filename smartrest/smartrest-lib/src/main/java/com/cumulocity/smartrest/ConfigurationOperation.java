package com.cumulocity.smartrest;

import com.cumulocity.smartrest.client.SmartRow;

public class ConfigurationOperation extends AbstractOperation {
	public static final OperationType TYPE = new OperationType("c8y_Configuration", DeviceManagement.XID, 202);

	private String configuration;
	
	public ConfigurationOperation(SmartRow operation) {
		super(operation);
		this.configuration = operation.getData(3);
	}
	
	public OperationType getOperationType() {
		return TYPE;
	}
	
	public String getConfiguration() {
		return configuration;
	}
}
