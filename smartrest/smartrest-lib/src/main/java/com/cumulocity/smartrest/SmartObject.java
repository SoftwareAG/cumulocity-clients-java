package com.cumulocity.smartrest;

public interface SmartObject {
	String getId();
	void addChild(String id);
	void setSuccessfulOperation(String operationId);
	void setFailedOperation(String operationId, String error);
	void setSupportedOps(String[] ops);
	DeviceManagement deviceManagement();
	SensorLibrary sensorLibrary();
}
