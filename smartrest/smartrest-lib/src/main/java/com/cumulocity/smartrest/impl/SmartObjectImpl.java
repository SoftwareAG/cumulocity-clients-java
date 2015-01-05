package com.cumulocity.smartrest.impl;

import com.cumulocity.smartrest.DeviceManagement;
import com.cumulocity.smartrest.SensorLibrary;
import com.cumulocity.smartrest.SmartObject;
import com.cumulocity.smartrest.util.SmartBuffer;
import com.cumulocity.smartrest.util.StringUtils;

public class SmartObjectImpl implements SmartObject {
	public static final int SET_SUPPORTED_OPS = 105;

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
		request(new SmartBuffer(SET_SUPPORTED_OPS, id).append(escapedOps));
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
