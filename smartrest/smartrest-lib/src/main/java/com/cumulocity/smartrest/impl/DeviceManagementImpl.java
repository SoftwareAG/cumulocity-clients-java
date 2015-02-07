package com.cumulocity.smartrest.impl;

import java.util.Hashtable;

import com.cumulocity.smartrest.DeviceManagement;
import com.cumulocity.smartrest.client.SmartConnection;
import com.cumulocity.smartrest.util.SmartBuffer;
import com.cumulocity.smartrest.util.StringUtils;

public class DeviceManagementImpl implements DeviceManagement {
	public static final int SET_HARDWARE = 101;
	public static final int SET_FIRMWARE = 102;
	public static final int SET_SOFTWARE = 103;
	public static final int SET_CONFIGURATION = 104;
	public static final int SET_MOBILE = 106;
	public static final int SET_REQ_AVAILABILITY = 107;
	public static final int SEND_BATTERY = 108;
	public static final int SEND_MODEM = 109;
	public static final int SEND_DEVICE_EVENT = 110;
	public static final int SEND_DEVICE_ALARM = 111;

	public static final String[] BER_TABLE = { "0.14", "0.28", "0.57", "1.13",
			"2.26", "4.53", "9.05", "18.10" };

	private SmartConnection connection;
	private String id;

	public DeviceManagementImpl(SmartConnection connection, String id) {
		this.connection = connection;
		this.id = id;
	}

	public void setHardware(String model, String revision, String serial) {
		new SmartBuffer(SET_HARDWARE, id).append(model).append(revision)
				.append(serial).send(connection, XID);
	}

	public void setSoftware(Hashtable software) {
		String escapedSw = StringUtils.escape(software);
		new SmartBuffer(SET_SOFTWARE, id).append(escapedSw).send(connection,
				XID);
	}

	public void setConfiguration(String text) {
		String escapedText = StringUtils.escape(text);
		new SmartBuffer(SET_CONFIGURATION, id).append(escapedText).send(
				connection, XID);
	}

	public void setMobile(String iccid, String imei, String imsi, String lac,
			String cellId, String mcc, String mnc) {
		new SmartBuffer(SET_MOBILE, id).append(iccid).append(imei).append(imsi)
				.append(lac).append(cellId).append(mcc).append(mnc)
				.send(connection, XID);
	}

	public void setReqAvailability(int minutes) {
		new SmartBuffer(SET_REQ_AVAILABILITY, id).append(minutes).send(
				connection, XID);
	}

	public void sendBattery(int level) {
		new SmartBuffer(SEND_BATTERY, id).appendNow().append(level)
				.send(connection, XID);
	}

	public void sendSignalStrength(int rawRssi, int rawBer) {
		String rssi = rawRssi == 99 ? "" : Integer.toString(-53
				- (30 - rawRssi) * 2);
		String ber = rawBer == 99 ? "" : BER_TABLE[rawBer];
		new SmartBuffer(SEND_MODEM, id).appendNow().append(rssi).append(ber)
				.send(connection, XID);
	}

	public void sendDeviceLog(String text) {
		new SmartBuffer(SEND_DEVICE_EVENT, id).appendNow().append(text)
				.send(connection, XID);
	}

	public void sendDeviceAlarm(String text) {
		new SmartBuffer(SEND_DEVICE_ALARM, id).appendNow().append(text)
				.send(connection, XID);
	}
}
