package com.cumulocity.me.smartrest.client.impl;


import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponse;

public class YawidMajaSmartHttpConnection extends SmartHttpConnection {

	/*
	 * Observation: when there are too many Connector.open(..) calls within a short period of time,
	 * the Yawid Maja device crashes. Therefore, this SmartHttpConnection subclass blocks between
	 * Connector.open(..) calls for 5 seconds
	 */
	private final static long WAITING_TIME_MILLIS = 5000L;
	
	private final static Object monitor = new Object();
	private static long lastConnectorOpenCallTime = System.currentTimeMillis() - WAITING_TIME_MILLIS;
	
	public YawidMajaSmartHttpConnection(String host, String xid) {
		super(host, xid);
	}
	
	public YawidMajaSmartHttpConnection(String host, String xid, String authorization) {
		super(host, xid, authorization);
	}
	
	public YawidMajaSmartHttpConnection(String host, String tenant, String username, String password, String xid) {
		super(host, tenant, username, password, xid);
	}
	
	public SmartResponse executeRequest(SmartRequest request) {
		SmartResponse smartResponse = null;
		synchronized( monitor ) {
			
			while (System.currentTimeMillis() - lastConnectorOpenCallTime < WAITING_TIME_MILLIS) {
				try {
					//System.out.println("Waiting 1000ms for HTTP gate");
					monitor.wait(1000);
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			//System.out.println("Blocking sleeping time is over");
			smartResponse = super.executeRequest(request);
			lastConnectorOpenCallTime = System.currentTimeMillis();
			
			monitor.notify();
		}
		return smartResponse;
	}


}
