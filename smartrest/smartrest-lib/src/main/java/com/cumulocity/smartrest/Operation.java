package com.cumulocity.smartrest;

public interface Operation {
	OperationType getOperationType();
	String getOperationId();
	String getDeviceId();
}
