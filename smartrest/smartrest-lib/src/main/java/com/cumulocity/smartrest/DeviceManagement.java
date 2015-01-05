package com.cumulocity.smartrest;

import java.util.Hashtable;

public interface DeviceManagement {
	static final String XID = "DM_1.0";
	
	void setHardware(String model, String revision, String serial);
	void setSoftware(Hashtable software);
	void setConfiguration(String text);
	void setMobile(String iccid, String imei, String imsi, String lac,
			String cellId, String mcc, String mnc);
	void setReqAvailability(int minutes);
	void sendBattery(int level);
	void sendSignalStrength(int rawRssi, int rawBer);
	void sendDeviceLog(String text);
	void sendDeviceAlarm(String text);
}
