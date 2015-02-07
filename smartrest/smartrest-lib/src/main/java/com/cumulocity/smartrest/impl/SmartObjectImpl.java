package com.cumulocity.smartrest.impl;

import com.cumulocity.smartrest.DeviceManagement;
import com.cumulocity.smartrest.SensorLibrary;
import com.cumulocity.smartrest.SmartObject;
import com.cumulocity.smartrest.client.SmartConnection;
import com.cumulocity.smartrest.client.SmartRow;
import com.cumulocity.smartrest.util.SmartBuffer;
import com.cumulocity.smartrest.util.StringUtils;

public class SmartObjectImpl implements SmartObject {
	public static final int CREATE = 100;
	public static final int GET_BINDING = 101;
	public static final int CREATE_BINDING = 102;
	public static final int SET_SUPPORTED_OPS = 105;

	private SmartConnection connection;
	private String moId;

	public SmartObjectImpl(SmartConnection connection, String moId) {
		this.connection = connection;
		this.moId = moId;
	}

	static String getBinding(SmartConnection connection, String extIdType,
			String extId) {
		SmartRow result = new SmartBuffer(GET_BINDING).append(extIdType)
				.append(extId).send(connection, XID);
		// How is the result sent?
	}

	static SmartObject create(SmartConnection connection, String type,
			String defaultName, boolean isDevice, boolean isAgent) {
		String createParam = isDevice ? "\"c8y_IsDevice\": {}, " : "";
		createParam += isAgent ? "\"com_cumulocity_model_Agent\": {}, " : "";
		SmartRow result = new SmartBuffer(CREATE).append(createParam)
				.append(type).append(defaultName).send(connection, XID);
		return new SmartObjectImpl(connection, result.getData(1));
	}

	public SmartConnection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addChild(String id) {
		// TODO Auto-generated method stub

	}

	public void setSuccessfulOperation(String operationId) {
		// TODO Auto-generated method stub

	}

	public void setFailedOperation(String operationId, String error) {
		// TODO Auto-generated method stub

	}

	public void setSupportedOps(String[] ops) {
		String escapedOps = StringUtils.escape(ops);
		new SmartBuffer(SET_SUPPORTED_OPS, id).append(escapedOps).send(
				connection, XID);
	}

	public DeviceManagement deviceManagement() {
		// TODO Auto-generated method stub
		return null;
	}

	public SensorLibrary sensorLibrary() {
		// TODO Auto-generated method stub
		return null;
	}

}
