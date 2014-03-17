package com.cumulocity.sdk.client.devicecontrol;

import com.cumulocity.rest.representation.devicebootstrap.DeviceCredentialsRepresentation;
import com.cumulocity.rest.representation.operation.DeviceControlMediaType;
import com.cumulocity.sdk.client.RestConnector;

public class DeviceCredentialsApiImpl implements DeviceCredentialsApi {
	
	public static final String DEVICE_CREDENTIALS_URL = "devicecontrol/deviceCredentials";
	
	protected final RestConnector restConnector;
	
	public DeviceCredentialsApiImpl(RestConnector restConnector) {
		this.restConnector = restConnector;
	}

	@Override
	public void hello(String deviceId) {
		DeviceCredentialsRepresentation representation = new DeviceCredentialsRepresentation();
		representation.setDeviceId(deviceId);
		restConnector.post(DEVICE_CREDENTIALS_URL, DeviceControlMediaType.DEVICE_CREDENTIALS, representation);
	}

	@Override
    public DeviceCredentialsRepresentation getCredentials(String deviceId) {
		String url = DEVICE_CREDENTIALS_URL + "/" + deviceId;
		return restConnector.get(url, DeviceControlMediaType.DEVICE_CREDENTIALS, DeviceCredentialsRepresentation.class);
    }
}
