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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.cumulocity.rest.representation.devicebootstrap.NewDeviceRequestRepresentation;
import com.cumulocity.rest.representation.operation.DeviceControlMediaType;
import com.cumulocity.sdk.client.ResponseParser;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.common.HttpClientFactory;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class DeviceCredentialsIT extends JavaSdkITBase {

    private static final String NEW_DEVICE_REQUEST_URI = "devicecontrol/newDeviceRequests";
    
    private DeviceCredentialsApiImpl deviceCredentialsResource;
    private ResponseParser responseParser;
    private RestConnector restConnector;
    
    @Before
    public void setUp() throws Exception {
        deviceCredentialsResource = (DeviceCredentialsApiImpl) platform.getDeviceCredentialsApi();
        responseParser = new ResponseParser();
        restConnector = new RestConnector(platform, responseParser);
    }

    @Test
    @Ignore
    public void hello() throws Exception {
        createNewDeviceRequest("2000");
        NewDeviceRequestRepresentation newDeviceRequest = getNewDeviceRequest("2000");
        System.out.println(newDeviceRequest);
        deviceCredentialsResource.hello("2000");
    }
    
    public void createNewDeviceRequest(String deviceId) {
        NewDeviceRequestRepresentation representation = new NewDeviceRequestRepresentation();
        representation.setId(deviceId);
        String path = platform.getHost() + NEW_DEVICE_REQUEST_URI;
        System.out.println("create new device request PATH = " + path);
        restConnector.post(path, DeviceControlMediaType.NEW_DEVICE_REQUEST, representation);
    }
    
    public NewDeviceRequestRepresentation getNewDeviceRequest(String deviceId) {
        Client httpClient = new HttpClientFactory().createClient();
        try {
            return getNewDeviceRequest(httpClient, deviceId);
        } finally {
            httpClient.destroy();
        }
    }
    
    //    private ClientResponse postNewDeviceRequest(Client httpClient, String deviceId) {
//        String host = platform.getHost();
//        WebResource.Builder resource = httpClient
//        .resource(host + NEW_DEVICE_REQUEST_URI)
//        .type(DeviceControlMediaType.NEW_DEVICE_REQUEST);
//        String json = "{ \"id\": \"" + deviceId + "\"}";
//        return resource.post(ClientResponse.class, json);
//    }
    
    private NewDeviceRequestRepresentation getNewDeviceRequest(Client httpClient, String deviceId) {
        String host = platform.getHost();
        WebResource.Builder resource = httpClient
                .resource(host + NEW_DEVICE_REQUEST_URI + "/" + deviceId)
                .type(DeviceControlMediaType.NEW_DEVICE_REQUEST);
        ClientResponse clientResponse = resource.get(ClientResponse.class);
        return responseParser.parse(clientResponse, 200, NewDeviceRequestRepresentation.class);
    }


}
