package com.cumulocity.smartrest.impl;

import com.cumulocity.smartrest.SmartObject;
import com.cumulocity.smartrest.SmartObjectFactory;
import com.cumulocity.smartrest.client.SmartConnectionFactory;
import com.cumulocity.smartrest.client.SmartRequest;
import com.cumulocity.smartrest.client.SmartResponse;
import com.cumulocity.smartrest.client.impl.SmartRequestImpl;

public class SmartObjectFactoryImpl implements SmartObjectFactory {
	private SmartConnectionFactory connections;
	
	public SmartObjectFactoryImpl(SmartConnectionFactory connections) {
		this.connections = connections;
	}

	public SmartObject createOrGet(String extIdType, String extId, String type,
			String defaultName, boolean isAgent, boolean isDevice) {
		// Get XtId -> moId
		// If != null get(moId)
		// If == null create(), register, return by moId 
		
		// TODO Auto-generated method stub
		return null;
	}

	public SmartObject get(String extIdType, String extId) {
			SmartRequest request = new SmartRequestImpl(CREATE_DEVICE, type + ","
					+ name);
			SmartResponse response = connection.executeRequest(XID, request);
			return response.getRow(0).getData(1);
	}

	public SmartObject get(String moId) {
		return new SmartObjectImpl(connections.get(), moId);
	}
}
