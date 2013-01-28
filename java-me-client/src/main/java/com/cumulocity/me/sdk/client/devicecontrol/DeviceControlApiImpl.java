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

package com.cumulocity.me.sdk.client.devicecontrol;

import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.model.operation.OperationStatus;
import com.cumulocity.me.rest.representation.operation.DeviceControlMediaType;
import com.cumulocity.me.rest.representation.operation.DeviceControlRepresentation;
import com.cumulocity.me.rest.representation.operation.OperationRepresentation;
import com.cumulocity.me.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.me.rest.representation.platform.PlatformMediaType;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.TemplateUrlParser;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;

public class DeviceControlApiImpl implements DeviceControlApi {

    private static final String AGENT_ID = "agentId";

    private static final String STATUS = "status";

    private static final String DEVICE_ID = "deviceId";

    private final String platformApiUrl;

    private final RestConnector restConnector;

    private TemplateUrlParser templateUrlParser;

    private final int pageSize;

    private DeviceControlRepresentation deviceControlRepresentation = null;

    public DeviceControlApiImpl(RestConnector restConnector, TemplateUrlParser templateUrlParser, String platformApiUrl, int pageSize) {
        this.restConnector = restConnector;
        this.templateUrlParser = templateUrlParser;
        this.platformApiUrl = platformApiUrl;
        this.pageSize = pageSize;
    }

    private DeviceControlRepresentation getDeviceControlRepresentation() throws SDKException {
        if (null == deviceControlRepresentation) {
            createApiRepresentation();
        }
        return deviceControlRepresentation;
    }

    private void createApiRepresentation() throws SDKException {
        PlatformApiRepresentation platformApiRepresentation = (PlatformApiRepresentation) restConnector.get(platformApiUrl,
                PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class);
        deviceControlRepresentation = platformApiRepresentation.getDeviceControl();
    }

    public OperationRepresentation getOperation(GId gid) throws SDKException {
        String url = getSelfUri() + "/" + gid.getValue();
        return (OperationRepresentation) restConnector.get(url, DeviceControlMediaType.OPERATION, OperationRepresentation.class);
    }

    public PagedCollectionResource getOperations() throws SDKException {
        String url = getSelfUri();
        return new OperationCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getOperationsByStatus(OperationStatus status) throws SDKException {
        String urlTemplate = getDeviceControlRepresentation().getOperationsByStatus();
        Map filter = new HashMap();
        filter.put(STATUS, status.toString());
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new OperationCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getOperationsByAgent(String agentId) throws SDKException {
        String urlTemplate = getDeviceControlRepresentation().getOperationsByAgentId();
        Map filter = new HashMap();
        filter.put(AGENT_ID, agentId);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new OperationCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getOperationsByAgentAndStatus(String agentId, OperationStatus status) throws SDKException {
        String urlTemplate = getDeviceControlRepresentation().getOperationsByAgentIdAndStatus();
        Map filter = new HashMap();
        filter.put(AGENT_ID, agentId);
        filter.put(STATUS, status.toString());
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new OperationCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getOperationsByDevice(String deviceId) throws SDKException {
        String urlTemplate = getDeviceControlRepresentation().getOperationsByDeviceId();
        Map filter = new HashMap();
        filter.put(DEVICE_ID, deviceId);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new OperationCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getOperationsByDeviceAndStatus(String deviceId, OperationStatus status) throws SDKException {
        String urlTemplate = getDeviceControlRepresentation().getOperationsByDeviceIdAndStatus();
        Map filter = new HashMap();
        filter.put(DEVICE_ID, deviceId);
        filter.put(STATUS, status.toString());
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new OperationCollectionImpl(restConnector, url, pageSize);
    }

    public OperationRepresentation create(OperationRepresentation operation) throws SDKException {
        return (OperationRepresentation) restConnector.post(getSelfUri(), DeviceControlMediaType.OPERATION, operation);
    }

    private String getSelfUri() {
        return getDeviceControlRepresentation().getOperations().getSelf();
    }

    public OperationRepresentation update(OperationRepresentation operation) throws SDKException {
        String url = getSelfUri() + "/" + operation.getId().getValue();
        return (OperationRepresentation) restConnector.put(url, DeviceControlMediaType.OPERATION, prepareOperationForUpdate(operation));
    }

    private OperationRepresentation prepareOperationForUpdate(OperationRepresentation operation) {
        OperationRepresentation toSend = new OperationRepresentation();
        toSend.setStatus(operation.getStatus());
        toSend.setAttrs(operation.getAttrs());
        return toSend;
    }

    public PagedCollectionResource getOperationsByFilter(OperationFilter filter) throws SDKException {
        OperationStatus status = filter.getStatus();
        String device = filter.getDevice();
        String agent = filter.getAgent();

        if (status != null && agent != null && device != null) {
            throw new IllegalArgumentException();
        } else if (device != null && agent != null) {
            throw new IllegalArgumentException();
        } else if (status != null && agent != null) {
            return getOperationsByAgentAndStatus(agent, status);
        } else if (status != null && device != null) {
            return getOperationsByDeviceAndStatus(device, status);
        } else if (device != null) {
            return getOperationsByDevice(device);
        } else if (agent != null) {
            return getOperationsByAgent(agent);
        } else if (status != null) {
            return getOperationsByStatus(status);
        } else {
            return getOperations();
        }
    }
}
