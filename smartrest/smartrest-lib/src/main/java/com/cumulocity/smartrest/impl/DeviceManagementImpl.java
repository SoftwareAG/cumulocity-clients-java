package com.cumulocity.smartrest.impl;

import java.util.Hashtable;

import com.cumulocity.smartrest.DeviceManagement;
import com.cumulocity.smartrest.client.SmartConnection;
import com.cumulocity.smartrest.client.SmartRequest;
import com.cumulocity.smartrest.client.SmartResponse;
import com.cumulocity.smartrest.client.SmartRow;
import com.cumulocity.smartrest.client.impl.SmartRequestImpl;
import com.cumulocity.smartrest.util.SmartBuffer;
import com.cumulocity.smartrest.util.StringUtils;

public class DeviceManagementImpl implements DeviceManagement {
	public static final int CREATE_DEVICE = 100;
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

	public DeviceManagementImpl(SmartConnection connection, String extId,
			String type, String name) {
		this.connection = connection;
	}

	public DeviceManagementImpl(SmartConnection connection, String id) {

	}

	public String getId() {
		return id;
	}

	private String createDevice(String type, String name) {
		SmartRequest request = new SmartRequestImpl(CREATE_DEVICE, type + ","
				+ name);
		SmartResponse response = connection.executeRequest(XID, request);
		return response.getRow(0).getData(1);
	}

	public void setHardware(String model, String revision, String serial) {
		request(new SmartBuffer(SET_HARDWARE, id).append(model)
				.append(revision).append(serial));
	}

	public void setSoftware(Hashtable software) {
		String escapedSw = StringUtils.escape(software);
		request(new SmartBuffer(SET_SOFTWARE, id).append(escapedSw));
	}

	public void setConfiguration(String text) {
		String escapedText = StringUtils.escape(text);
		request(new SmartBuffer(SET_CONFIGURATION, id).append(escapedText));
	}

	public void setMobile(String iccid, String imei, String imsi, String lac,
			String cellId, String mcc, String mnc) {
		request(new SmartBuffer(SET_MOBILE, id).append(iccid).append(imei)
				.append(imsi).append(lac).append(cellId).append(mcc)
				.append(mnc));
	}

	public void setReqAvailability(int minutes) {
		request(new SmartBuffer(SET_REQ_AVAILABILITY, id).append(minutes));
	}

	public void sendBattery(int level) {
		request(new SmartBuffer(SEND_BATTERY, id).appendNow().append(level));
	}

	public void sendSignalStrength(int rawRssi, int rawBer) {
		String rssi = rawRssi == 99 ? "" : Integer.toString(-53
				- (30 - rawRssi) * 2);
		String ber = rawBer == 99 ? "" : BER_TABLE[rawBer];
		request(new SmartBuffer(SEND_MODEM, id).appendNow().append(rssi)
				.append(ber));
	}

	public void sendDeviceLog(String text) {
		request(new SmartBuffer(SEND_DEVICE_EVENT, id).appendNow().append(text));
	}

	public void sendDeviceAlarm(String text) {
		request(new SmartBuffer(SEND_DEVICE_ALARM, id).appendNow().append(text));
	}

	private SmartRow request(SmartBuffer data) {
		SmartRequest request = new SmartRequestImpl(data.toString());
		SmartResponse response = connection.executeRequest(XID, request);
		return response.getRow(0);
	}
}
