package com.cumulocity.me.smartrest.client;

import java.util.Hashtable;

import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;
import com.cumulocity.me.smartrest.client.impl.SmartRow;
import com.cumulocity.me.util.SmartBuffer;
import com.cumulocity.me.util.StringUtils;

public class SmartDevice {
	public static final int CREATE_DEVICE = 100;
	public static final int SET_HARDWARE = 101;
	public static final int SET_FIRMWARE = 102;
	public static final int SET_SOFTWARE = 103;
	public static final int SET_CONFIGURATION = 104;
	public static final int SET_SUPPORTED_OPS = 105;
	public static final int SET_MOBILE = 106;
	public static final int SET_REQ_AVAILABILITY = 107;
	public static final int SEND_BATTERY = 108;
	public static final int SEND_MODEM = 109;

	public static final String[] BER_TABLE = { "0.14", "0.28", "0.57", "1.13",
			"2.26", "4.53", "9.05", "18.10" };

	private SmartConnection connection;
	private String id;

	public SmartDevice(SmartConnection connection, String extId, String type,
			String name) {
		this.connection = connection;
	}

	public SmartDevice(SmartConnection connection, String id) {

	}

	public String getId() {
		return id;
	}

	private String createDevice(String type, String name) {
		SmartRequest request = new SmartRequestImpl(CREATE_DEVICE, type + ","
				+ name);
		SmartResponse response = connection.executeRequest(request);
		return response.getRow(0).getData(1);
	}

	public void setSoftware(Hashtable software) {
		String escapedSw = StringUtils.escape(software);
		request(new SmartBuffer(SET_SOFTWARE, id).append(escapedSw));		
	}

	public void setConfiguration(String text) {
		String escapedText = StringUtils.escape(text);
		request(new SmartBuffer(SET_CONFIGURATION, id).append(escapedText));
	}

	public void setSupportedOps(String[] ops) {
		String escapedOps = StringUtils.escape(ops);
		request(new SmartBuffer(SET_SUPPORTED_OPS, id).append(escapedOps));
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

	public void sendModem(int rawRssi, int rawBer) {
		String rssi = rawRssi == 99 ? "" : Integer.toString(-53
				- (30 - rawRssi) * 2);
		String ber = rawBer == 99 ? "" : BER_TABLE[rawBer];
		request(new SmartBuffer(SEND_MODEM, id).appendNow().append(rssi)
				.append(ber));
	}

	private SmartRow request(SmartBuffer data) {
		SmartRequest request = new SmartRequestImpl(data.toString());
		SmartResponse response = connection.executeRequest(request);
		return response.getRow(0);
	}
}
