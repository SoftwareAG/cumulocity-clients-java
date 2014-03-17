package com.cumulocity.sdk.client.devicecontrol;

import static com.cumulocity.rest.representation.operation.DeviceControlMediaType.DEVICE_CREDENTIALS;

import com.cumulocity.rest.representation.devicebootstrap.DeviceCredentialsRepresentation;
import com.cumulocity.sdk.client.FixedRateResultPoller;
import com.cumulocity.sdk.client.FixedRateResultPoller.GetResultTask;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;

public class DeviceCredentialsApiImpl implements DeviceCredentialsApi {
    
    public static final long POLL_INTERVAL = 5 * 1000;
    public static final long POLL_TIMEOUT = 60 * 60 * 1000;
    public static final String DEVICE_CREDENTIALS_URI = "devicecontrol/deviceCredentials";

    private final RestConnector restConnector;
    private final String url;

    public DeviceCredentialsApiImpl(PlatformParameters platformParameters, RestConnector restConnector) {
        this.restConnector = restConnector;
        this.url = platformParameters.getHost() + DEVICE_CREDENTIALS_URI;
    }

    @Override
    public void hello(final String deviceId) {
        DeviceCredentialsRepresentation representation = new DeviceCredentialsRepresentation();
        representation.setDeviceId(deviceId);
        restConnector.post(url, DEVICE_CREDENTIALS, representation);
    }

    @Override
    public DeviceCredentialsRepresentation pollCredentials(final String deviceId) {
        return aPoller(deviceId).startAndPoll();
    }

    private FixedRateResultPoller<DeviceCredentialsRepresentation> aPoller(final String deviceId) {
        GetResultTask<DeviceCredentialsRepresentation> pollingTask = new GetResultTask<DeviceCredentialsRepresentation>() {

            @Override
            public DeviceCredentialsRepresentation tryGetResult() {
                return restConnector.get(url + "/" + deviceId, DEVICE_CREDENTIALS, DeviceCredentialsRepresentation.class);
            }
            
        };
        return new FixedRateResultPoller<DeviceCredentialsRepresentation>(pollingTask, POLL_INTERVAL, POLL_TIMEOUT);
    }
}
