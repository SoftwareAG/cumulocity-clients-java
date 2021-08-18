package com.cumulocity.sdk.client.devicecontrol;

import com.cumulocity.rest.representation.devicebootstrap.DeviceCredentialsRepresentation;
import com.cumulocity.rest.representation.devicebootstrap.NewDeviceRequestRepresentation;
import com.cumulocity.sdk.client.polling.PollingStrategy;

/**
 * Api for device bootstrap
 *
 */
public interface DeviceCredentialsApi {

	/**
	 * Register a new device.
	 * @param id external id of the device to register
	 * @return new device request representation
	 */
	NewDeviceRequestRepresentation register(String id);

	/**
	 * Remove a device registration.
	 *
	 * @param representation representation of new device request to delete
	 */
	void delete(NewDeviceRequestRepresentation representation);

	/**
	 * Schedule polling credentials task, invoking it at the specified execution time and subsequently with the given interval
	 *
	 * Execution will end after timeout
	 *
	 * @param deviceId device unique identifier
	 * @param interval - how often request is sent in seconds
	 * @param timeout - after how many seconds polling process will expire
	 * @return device credentials representation
	 */
	DeviceCredentialsRepresentation pollCredentials(String deviceId, int interval, int timeout);

	/**
	 * Device poll credentials
	 *
	 * @param deviceId device unique identifier
	 * @param strategy credentials polling strategy
	 * @return device credentials representation
	 */
	DeviceCredentialsRepresentation pollCredentials(String deviceId, PollingStrategy strategy);

    /**
     * Executes single request to credentials endpoint
     *
     * @param deviceId device unique identifier
     * @return device credentials representation
     */
    DeviceCredentialsRepresentation pollCredentials(String deviceId);
}
