/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cumulocity.sdk.client.devicecontrol;

import static com.cumulocity.rest.representation.operation.DeviceControlMediaType.NEW_DEVICE_REQUEST;
import static org.fest.assertions.Assertions.assertThat;

import java.util.Timer;
import java.util.TimerTask;

import org.junit.Before;
import org.junit.Test;

import com.cumulocity.rest.representation.devicebootstrap.DeviceCredentialsRepresentation;
import com.cumulocity.rest.representation.devicebootstrap.NewDeviceRequestRepresentation;
import com.cumulocity.sdk.client.ResponseParser;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.common.JavaSdkITBase;

public class DeviceCredentialsIT extends JavaSdkITBase {

    private DeviceCredentialsApiImpl deviceCredentialsResource;
    private ResponseParser responseParser;
    private RestConnector restConnector;

    @Before
    public void setUp() throws Exception {
        deviceCredentialsResource = (DeviceCredentialsApiImpl) bootstrapPlatform.getDeviceCredentialsApi();
        responseParser = new ResponseParser();
        restConnector = new RestConnector(platform, responseParser);
    }
    
    @Test
    public void shouldNewDeviceRequestTransposeToPendingAcceptanceOnHello() throws Exception {
        final String deviceId = "1000";
        createNewDeviceRequest(deviceId);
        NewDeviceRequestRepresentation newDeviceRequest = getNewDeviceRequest(deviceId);
        assertThat(newDeviceRequest.getStatus()).isEqualTo("WAITING_FOR_CONNECTION");

        deviceCredentialsResource.hello(deviceId);

        newDeviceRequest = getNewDeviceRequest(deviceId);
        assertThat(newDeviceRequest.getStatus()).isEqualTo("PENDING_ACCEPTANCE");
    }

    @Test
    public void shouldReturnCredentialsIfDeviceAccepted() throws Exception {
        final String deviceId = "2000";
        final int pollInterval = 2;
        createNewDeviceRequest(deviceId);
        deviceCredentialsResource.hello(deviceId);
        acceptNewDeviceRequest(deviceId);

        DeviceCredentialsRepresentation credentials = deviceCredentialsResource.pollCredentials(deviceId, pollInterval, 100);

        assertThat(credentials.getTenantId()).isEqualTo(platform.getTenantId());
        assertThat(credentials.getUsername()).isEqualTo("device_" + deviceId);
        assertThat(credentials.getPassword()).isNotEmpty();
    }

    @Test
    public void shouldPollCredentialsUntilDeviceAccepted() throws Exception {
        final String deviceId = "3000";
        final int pollInterval = 2;
        createNewDeviceRequest(deviceId);
        deviceCredentialsResource.hello(deviceId);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                acceptNewDeviceRequest(deviceId);
            }
        }, pollInterval * 2);

        DeviceCredentialsRepresentation credentials = deviceCredentialsResource.pollCredentials(deviceId, pollInterval, 100);
        assertThat(credentials).isNotNull();
        assertThat(credentials.getTenantId()).isEqualTo(platform.getTenantId());
        assertThat(credentials.getUsername()).isEqualTo("device_" + deviceId);
        assertThat(credentials.getPassword()).isNotEmpty();

    }

    private void createNewDeviceRequest(String deviceId) {
        NewDeviceRequestRepresentation representation = new NewDeviceRequestRepresentation();
        representation.setId(deviceId);
        restConnector.post(newDeviceRequestsUri(), NEW_DEVICE_REQUEST, representation);
    }

    private void acceptNewDeviceRequest(String deviceId) {
        NewDeviceRequestRepresentation representation = new NewDeviceRequestRepresentation();
        representation.setStatus("ACCEPTED");
        restConnector.put(newDeviceRequestUri(deviceId), NEW_DEVICE_REQUEST, representation);
    }

    private NewDeviceRequestRepresentation getNewDeviceRequest(String deviceId) {
        return restConnector.get(newDeviceRequestUri(deviceId), NEW_DEVICE_REQUEST, NewDeviceRequestRepresentation.class);
    }
    
    private String newDeviceRequestsUri() {
        return platform.getHost() + "devicecontrol/newDeviceRequests";
    }
    
    private String newDeviceRequestUri(String deviceId) {
        return newDeviceRequestsUri() + "/" + deviceId;
    }
}
