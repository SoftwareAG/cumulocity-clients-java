package com.cumulocity.smartrest;

public interface SensorLibrary {
	void sendPosition(double lat, double lng, double alt);
	void setRelay(boolean open);
	void setRelayArray(boolean[] open);
	void sendTemperature(double val);
	void sendAcceleration(double val);
	void sendLight(double val);
	void sendHumidity(double val);
	void sendMoisture(double val);
	void sendDistance(double val);
	void sendElectricity(double ap, double am, double pp, double pm);
	void sendCurrent(double val);
	void sendVoltage(double val);
	void sendPower(double val);
	void sendBarometerReading(double pressure, double altitude);
}
