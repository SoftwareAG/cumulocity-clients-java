package com.cumulocity.me.util;

public abstract class SmartCometChannel {

	public static final String DEVICE_PUSH_ENDPOINT = "/devicecontrol/notifications";
	
	public static final String REALTIME_ENDPOINT = "/cep/realtime";
	
	public static final String CUSTOM_NOTIFICATIONS_ENDPOINT = "/cep/customnotifications";
	
	public static final String REALTIME_CHANNEL_MANAGEDOBJECTS = "/managedobjects/";
	
	public static final String REALTIME_CHANNEL_MEASUREMENTS = "/measurements/";
	
	public static final String REALTIME_CHANNEL_EVENTS = "/events/";
	
	public static final String REALTIME_CHANNEL_ALARMS = "/alarms/";
	
	public static final String REALTIME_CHANNEL_OPERATIONS = "/operations/";
	
	public static final String WILDCARD = "*";
}
