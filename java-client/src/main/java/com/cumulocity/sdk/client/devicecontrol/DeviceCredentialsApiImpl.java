package com.cumulocity.sdk.client.devicecontrol;

import com.cumulocity.rest.representation.devicebootstrap.DeviceCredentialsRepresentation;
import com.cumulocity.rest.representation.operation.DeviceControlMediaType;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;

public class DeviceCredentialsApiImpl implements DeviceCredentialsApi {
	
	private static final String DEVICE_CREDENTIALS_URI = "devicecontrol/deviceCredentials";
	
	private final RestConnector restConnector;
	private final String url;
	
	public DeviceCredentialsApiImpl(PlatformParameters platformParameters, RestConnector restConnector) {
        this.restConnector = restConnector;
        this.url = platformParameters.getHost() + DEVICE_CREDENTIALS_URI;
	}

	@Override
	public void hello(String deviceId) {
		DeviceCredentialsRepresentation representation = new DeviceCredentialsRepresentation();
		representation.setDeviceId(deviceId);
		restConnector.post(url, DeviceControlMediaType.DEVICE_CREDENTIALS, representation);
	}

	@Override
    public DeviceCredentialsRepresentation getCredentials(String deviceId) {
		String deviceUrl = url + "/" + deviceId;
		return restConnector.get(deviceUrl, DeviceControlMediaType.DEVICE_CREDENTIALS, DeviceCredentialsRepresentation.class);
    }
}
