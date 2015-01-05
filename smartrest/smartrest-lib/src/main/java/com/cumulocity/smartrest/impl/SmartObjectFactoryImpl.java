package com.cumulocity.smartrest.impl;

import com.cumulocity.smartrest.SmartObject;
import com.cumulocity.smartrest.SmartObjectFactory;
import com.cumulocity.smartrest.client.SmartConnectionFactory;

public class SmartObjectFactoryImpl implements SmartObjectFactory {
	private SmartConnectionFactory connections;
	
	public SmartObjectFactoryImpl(SmartConnectionFactory connections) {
		this.connections = connections;
	}

	public SmartObject createOrGet(String extIdType, String extId, String type,
			String defaultName) {
		return null; // TODO
	}
}
