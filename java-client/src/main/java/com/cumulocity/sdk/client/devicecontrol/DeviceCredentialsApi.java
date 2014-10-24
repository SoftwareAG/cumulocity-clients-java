package com.cumulocity.sdk.client.devicecontrol;

import com.cumulocity.rest.representation.devicebootstrap.DeviceCredentialsRepresentation;
import com.cumulocity.sdk.client.polling.PollingStrategy;

/**
 * Api for device bootstrap
 * 
 * @author dombiel
 *
 */
public interface DeviceCredentialsApi {
	
	/**
	 * Schedule polling credentials task, invoking it at the specified execution time and subsequently with the given interval 
	 * 
	 * Execution will end after timeout
	 * 
	 * @param deviceId
	 * @param interval - how often request is sent in seconds
	 * @param timeout - after how many seconds polling process will expire
	 * 
	 */
	DeviceCredentialsRepresentation pollCredentials(String deviceId, int interval, int timeout);
	
	/**
	 * Device poll credentials
	 * 
	 * @param deviceId
	 * @param strategy
	 */
	DeviceCredentialsRepresentation pollCredentials(String deviceId, PollingStrategy strategy);

    /**
     * Executes single request to credentials endpoint
     * 
     * @param deviceId
     * @return
     */
    DeviceCredentialsRepresentation pollCredentials(String deviceId);
}
