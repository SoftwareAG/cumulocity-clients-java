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

import java.util.Map;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.operation.DeviceControlMediaType;
import com.cumulocity.rest.representation.operation.DeviceControlRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.UrlProcessor;
import com.cumulocity.sdk.client.devicecontrol.notification.OperationNotificationSubscriber;
import com.cumulocity.sdk.client.notification.Subscriber;

public class DeviceControlApiImpl implements DeviceControlApi {

    private final RestConnector restConnector;

    private final int pageSize;

    private DeviceControlRepresentation deviceControlRepresentation;

    private final PlatformParameters parameters;
    
    private UrlProcessor urlProcessor;

    public DeviceControlApiImpl(PlatformParameters parameters, RestConnector restConnector, UrlProcessor urlProcessor, DeviceControlRepresentation deviceControlRepresentation, int pageSize) {
        this.parameters = parameters;
        this.restConnector = restConnector;
        this.urlProcessor = urlProcessor;
        this.deviceControlRepresentation = deviceControlRepresentation;
        this.pageSize = pageSize;
    }

    private DeviceControlRepresentation getDeviceControlRepresentation() throws SDKException {
        return deviceControlRepresentation;
    }

    @Override
    public OperationRepresentation getOperation(GId gid) throws SDKException {
        String url = getSelfUri() + "/" + gid.getValue();
        return restConnector.get(url, DeviceControlMediaType.OPERATION, OperationRepresentation.class);
    }

    @Override
    public OperationCollection getOperations() throws SDKException {
        String url = getSelfUri();
        return new OperationCollectionImpl(restConnector, url, pageSize);
    }


    @Override
    public OperationRepresentation create(OperationRepresentation operation) throws SDKException {
        return restConnector.post(getSelfUri(), DeviceControlMediaType.OPERATION, operation);
    }

    @Override
    public OperationRepresentation update(OperationRepresentation operation) throws SDKException {
        String url = getSelfUri() + "/" + operation.getId().getValue();
        return (OperationRepresentation) restConnector.putWithBuffer(url, DeviceControlMediaType.OPERATION, prepareOperationForUpdate(operation));
    }

    private String getSelfUri() throws SDKException {
        return getDeviceControlRepresentation().getOperations().getSelf();
    }

    private OperationRepresentation prepareOperationForUpdate(OperationRepresentation operation) {
        OperationRepresentation toSend = new OperationRepresentation();
        toSend.setStatus(operation.getStatus());
        if (OperationStatus.FAILED.name().equals(operation.getStatus())) {
            toSend.setFailureReason(operation.getFailureReason());
        }
        toSend.setAttrs(operation.getAttrs());
        return toSend;
    }

    @Override
    public OperationCollection getOperationsByFilter(OperationFilter filter) throws SDKException {
        if (filter == null) {
            return getOperations();
        }
        Map<String, String> params = filter.getQueryParams();
        return new OperationCollectionImpl(restConnector, urlProcessor.replaceOrAddQueryParam(getSelfUri(), params), pageSize);
    }

    @Override
    public Subscriber<GId, OperationRepresentation> getNotificationsSubscriber() throws SDKException {
        return new OperationNotificationSubscriber(parameters);
    }
}
