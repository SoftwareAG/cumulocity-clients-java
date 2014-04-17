package com.cumulocity.sdk.client.devicecontrol;

import static com.cumulocity.rest.representation.operation.DeviceControlMediaType.DEVICE_CREDENTIALS;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.cumulocity.rest.representation.devicebootstrap.DeviceCredentialsRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.polling.AlteringRateResultPoller;
import com.cumulocity.sdk.client.polling.AlteringRateResultPoller.GetResultTask;
import com.cumulocity.sdk.client.polling.PollingStrategy;

public class DeviceCredentialsApiImpl implements DeviceCredentialsApi {

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
        representation.setId(deviceId);
        restConnector.post(url, DEVICE_CREDENTIALS, representation);
    }

    @Override
    public DeviceCredentialsRepresentation pollCredentials(String deviceId, int interval, int timeout) {
        PollingStrategy pollingStrategy = new PollingStrategy(true, (long) timeout, SECONDS, (long) interval);
        return pollCredentials(deviceId, pollingStrategy);
    }
    
    @Override
    public DeviceCredentialsRepresentation pollCredentials(String deviceId, PollingStrategy pollingStrategy) {
        return aPoller(deviceId, pollingStrategy).startAndPoll();
    }

    private AlteringRateResultPoller<DeviceCredentialsRepresentation> aPoller(final String deviceId, PollingStrategy pollingStrategy) {
        GetResultTask<DeviceCredentialsRepresentation> pollingTask = new GetResultTask<DeviceCredentialsRepresentation>() {

            @Override
            public DeviceCredentialsRepresentation tryGetResult() {
                return restConnector.get(url + "/" + deviceId, DEVICE_CREDENTIALS, DeviceCredentialsRepresentation.class);
            }

        };
        return new AlteringRateResultPoller<DeviceCredentialsRepresentation>(pollingTask, pollingStrategy);
    }
}
