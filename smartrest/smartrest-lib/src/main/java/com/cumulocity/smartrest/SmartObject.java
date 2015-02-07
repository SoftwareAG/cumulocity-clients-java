package com.cumulocity.smartrest;

import com.cumulocity.smartrest.client.SmartConnection;

/**
 * A handle to an object in the Cumulocity inventory.
 * 
 * @see SmartObjectFactory
 */
public interface SmartObject {
	static final String XID = "C8Y_1.0";

	/**
	 * @return The managed object ID of the object in the inventory.
	 */
	String getId();
	
	/**
	 * @return An authenticated connection to Cumulocity.
	 */
	SmartConnection getConnection();
	
	void setSupportedOps(String[] ops);
	void addChild(String id);
	
	void setSuccessfulOperation(String operationId);
	void setFailedOperation(String operationId, String error);
	
	/**
	 * @return A handle to device management related functionality for the object.
	 */
	DeviceManagement deviceManagement();
	
	/**
	 * @return A handle to sensor and control related functionality for the object.
	 */
	SensorLibrary sensorLibrary();
}
