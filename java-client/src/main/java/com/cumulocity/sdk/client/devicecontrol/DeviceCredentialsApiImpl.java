package com.cumulocity.sdk.client.devicecontrol;

import static com.cumulocity.rest.representation.operation.DeviceControlMediaType.DEVICE_CREDENTIALS;
import static com.cumulocity.rest.representation.operation.DeviceControlMediaType.NEW_DEVICE_REQUEST;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.cumulocity.rest.representation.devicebootstrap.DeviceCredentialsRepresentation;
import com.cumulocity.rest.representation.devicebootstrap.NewDeviceRequestRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.polling.AlteringRateResultPoller;
import com.cumulocity.sdk.client.polling.AlteringRateResultPoller.GetResultTask;
import com.cumulocity.sdk.client.polling.PollingStrategy;

public class DeviceCredentialsApiImpl implements DeviceCredentialsApi {

    public static final String DEVICE_CREDENTIALS_URI = "devicecontrol/deviceCredentials";
	public static final String DEVICE_REQUEST_URI = "devicecontrol/newDeviceRequests";

    private final RestConnector restConnector;
    private final String credentialsUrl;
	private final String requestUrl;

    public DeviceCredentialsApiImpl(PlatformParameters platformParameters, RestConnector restConnector) {
        this.restConnector = restConnector;
        this.credentialsUrl = platformParameters.getHost() + DEVICE_CREDENTIALS_URI;
		this.requestUrl = platformParameters.getHost() + DEVICE_REQUEST_URI;
    }
    
	@Override
	public NewDeviceRequestRepresentation register(String id) {
		NewDeviceRequestRepresentation representation = new NewDeviceRequestRepresentation();
		representation.setId(id);
		return restConnector.post(requestUrl, NEW_DEVICE_REQUEST, representation);
	}

	@Override
	public void delete(NewDeviceRequestRepresentation representation) {
		restConnector.delete(representation.getSelf());
	}

    @Override
    public DeviceCredentialsRepresentation pollCredentials(String deviceId) {
        final DeviceCredentialsRepresentation representation = new DeviceCredentialsRepresentation();
        representation.setId(deviceId);
        return restConnector.post(credentialsUrl, DEVICE_CREDENTIALS, representation);
    }

    @Override
    public DeviceCredentialsRepresentation pollCredentials(String deviceId, int interval, int timeout) {
        PollingStrategy pollingStrategy = new PollingStrategy((long) timeout, SECONDS, (long) interval);
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
                return pollCredentials(deviceId);
            }

        };
        return new AlteringRateResultPoller<DeviceCredentialsRepresentation>(pollingTask, pollingStrategy);
    }
}
