package com.cumulocity.smartrest.client.impl;

import com.cumulocity.smartrest.client.SmartConnection;
import com.cumulocity.smartrest.client.SmartConnectionFactory;
import com.cumulocity.smartrest.util.HttpConnectionFactory;

public class SmartHttpConnectionFactory implements SmartConnectionFactory {
	private String host;
	private String authorization;
	private HttpConnectionFactory connections;

	public SmartHttpConnectionFactory(String host, String authorization, HttpConnectionFactory connections) {
		this.host = host;
		this.authorization = authorization;
		this.connections = connections;
	}
	
	public SmartConnection get() {
		return new SmartHttpConnection(host, authorization, connections);
	}
}
