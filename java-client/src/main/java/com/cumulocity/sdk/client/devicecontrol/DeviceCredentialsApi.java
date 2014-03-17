package com.cumulocity.sdk.client.devicecontrol;

import com.cumulocity.rest.representation.devicebootstrap.DeviceCredentialsRepresentation;

public interface DeviceCredentialsApi {
	
	void hello(String deviceId);
	
	DeviceCredentialsRepresentation pollCredentials(String deviceId);

}
