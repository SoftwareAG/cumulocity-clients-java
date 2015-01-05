package com.cumulocity.smartrest;

import com.cumulocity.smartrest.client.SmartRow;

public class RestartOperation extends AbstractOperation {
	public static final OperationType TYPE = new OperationType("c8y_Restart", DeviceManagement.XID, 201);

	public RestartOperation(SmartRow operation) {
		super(operation);
	}

	public OperationType getOperationType() {
		return TYPE;
	}
}
