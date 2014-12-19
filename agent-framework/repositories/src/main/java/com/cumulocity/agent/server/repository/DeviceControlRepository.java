package com.cumulocity.agent.server.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.devicecontrol.DeviceControlApi;
import com.cumulocity.sdk.client.devicecontrol.OperationFilter;
import com.cumulocity.sdk.client.devicecontrol.PagedOperationCollectionRepresentation;
import com.cumulocity.sdk.client.notification.Subscriber;
import com.cumulocity.sdk.client.notification.Subscription;
import com.cumulocity.sdk.client.notification.SubscriptionListener;
import com.google.common.collect.Iterables;

@Component
public class DeviceControlRepository {
	
    private final DeviceControlApi deviceControlApi;

    private Subscriber<GId, OperationRepresentation> subscriber;

    @Autowired
    public DeviceControlRepository(DeviceControlApi deviceControlApi,
            @Qualifier("deviceControlNotificationsSubscriber") Subscriber<GId, OperationRepresentation> subscriber) {
        this.deviceControlApi = deviceControlApi;
        this.subscriber = subscriber;
    }

    public OperationRepresentation findOneByFilter(OperationFilter filter) {
        return Iterables.get(loadByFilter(filter).elements(1), 0);
    }
    
    public OperationRepresentation findById(GId operationId) {
        try {
            return deviceControlApi.getOperation(operationId);
        } catch (SDKException e) {
            if (e.getHttpStatus() == 404) {
                throw new NotFoundException();
            }
            throw e;
        }
    }

    public Iterable<OperationRepresentation> findAllByFilter(OperationFilter filter) {
        return loadByFilter(filter).allPages();
    }
    
    private PagedOperationCollectionRepresentation loadByFilter(OperationFilter filter) {
        return deviceControlApi.getOperationsByFilter(filter).get();
    }

    public Subscription<GId> subscribe(GId deviceId, SubscriptionListener<GId, OperationRepresentation> listener) {
        return subscriber.subscribe(deviceId, listener);
    }

    public void save(OperationRepresentation operation) {
        if (operation.getId() == null) {
            deviceControlApi.create(operation);
        } else {
            deviceControlApi.update(operation);
        }
    }
}
