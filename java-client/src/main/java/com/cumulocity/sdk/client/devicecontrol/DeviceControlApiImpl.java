/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.devicecontrol;

import java.util.HashMap;
import java.util.Map;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.operation.DeviceControlMediaType;
import com.cumulocity.rest.representation.operation.DeviceControlRepresentation;
import com.cumulocity.rest.representation.operation.OperationCollectionRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.TemplateUrlParser;

public class DeviceControlApiImpl implements DeviceControlApi {

    private static final String AGENT_ID = "agentId";

    private static final String STATUS = "status";

    private static final String DEVICE_ID = "deviceId";

    private final String platformApiUrl;

    private final RestConnector restConnector;

    private TemplateUrlParser templateUrlParser;

    private final int pageSize;

    private DeviceControlRepresentation deviceControlRepresentation = null;

    @Deprecated
    public DeviceControlApiImpl(RestConnector restConnector, TemplateUrlParser templateUrlParser, String platformApiUrl) {
        this.restConnector = restConnector;
        this.templateUrlParser = templateUrlParser;
        this.platformApiUrl = platformApiUrl;
        this.pageSize = PlatformParameters.DEFAULT_PAGE_SIZE;
    }

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
    
    private void createApiRepresentation() throws SDKException
    {
        PlatformApiRepresentation platformApiRepresentation =  restConnector.get(platformApiUrl,PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class);
        deviceControlRepresentation = platformApiRepresentation.getDeviceControl();
    }


    @Override
    public OperationRepresentation getOperation(GId gid) throws SDKException {
        String url = getDeviceControlRepresentation().getOperations().getSelf() + "/" + gid.getValue();
        return restConnector.get(url, DeviceControlMediaType.OPERATION, OperationRepresentation.class);
    }

    @Override
    public PagedCollectionResource<OperationCollectionRepresentation> getOperations() throws SDKException {
        String url = getDeviceControlRepresentation().getOperations().getSelf();
        return new OperationCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<OperationCollectionRepresentation> getOperationsByStatus(OperationStatus status) throws SDKException {
        String urlTemplate = getDeviceControlRepresentation().getOperationsByStatus();
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(STATUS, status.toString());
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new OperationCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<OperationCollectionRepresentation> getOperationsByAgent(String agentId) throws SDKException {
        String urlTemplate = getDeviceControlRepresentation().getOperationsByAgentId();
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(AGENT_ID, agentId);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new OperationCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<OperationCollectionRepresentation> getOperationsByAgentAndStatus(String agentId, OperationStatus status)
            throws SDKException {
        String urlTemplate = getDeviceControlRepresentation().getOperationsByAgentIdAndStatus();
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(AGENT_ID, agentId);
        filter.put(STATUS, status.toString());
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new OperationCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<OperationCollectionRepresentation> getOperationsByDevice(String deviceId) throws SDKException {
        String urlTemplate = getDeviceControlRepresentation().getOperationsByDeviceId();
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(DEVICE_ID, deviceId);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new OperationCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<OperationCollectionRepresentation> getOperationsByDeviceAndStatus(String deviceId,
            OperationStatus status) throws SDKException {
        String urlTemplate = getDeviceControlRepresentation().getOperationsByDeviceIdAndStatus();
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(DEVICE_ID, deviceId);
        filter.put(STATUS, status.toString());
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new OperationCollectionImpl(restConnector, url, pageSize);
    }

    @Override
    public OperationRepresentation create(OperationRepresentation operation) throws SDKException {
        return restConnector.post(getDeviceControlRepresentation().getOperations().getSelf(), DeviceControlMediaType.OPERATION, operation);
    }

    @Override
    public OperationRepresentation update(OperationRepresentation operation) throws SDKException {
        String url = getDeviceControlRepresentation().getOperations().getSelf() + "/" + operation.getId().getValue();
        return restConnector.put(url, DeviceControlMediaType.OPERATION, prepareOperationForUpdate(operation));
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
    public PagedCollectionResource<OperationCollectionRepresentation> getOperationsByFilter(OperationFilter filter) throws SDKException {
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
