package com.cumulocity.smartrest;

import com.cumulocity.smartrest.client.SmartRow;

public abstract class AbstractOperation implements Operation {
	private String operationId;
	private String deviceId;

	public AbstractOperation(SmartRow operation) {
		this.operationId = operation.getData(1);
		this.deviceId = operation.getData(2);
	}

	public String getOperationId() {
		return operationId;
	}

	public String getDeviceId() {
		return deviceId;
	}

}
