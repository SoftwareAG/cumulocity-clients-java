package com.cumulocity.microservice.platform.api.devicecontrol;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.buffering.Future;
import com.cumulocity.sdk.client.devicecontrol.DeviceControlApi;
import com.cumulocity.sdk.client.devicecontrol.OperationCollection;
import com.cumulocity.sdk.client.devicecontrol.OperationFilter;
import com.cumulocity.sdk.client.notification.Subscriber;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

import static com.cumulocity.microservice.platform.api.client.InternalTrafficDecorator.Builder.internally;

@Service
public class DeviceControlInternalApi {

    @Autowired(required = false)
    private DeviceControlApi deviceControlApi;

    @Autowired(required = false)
    private PlatformImpl platform;

    public OperationRepresentation getOperation(final GId gid) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<OperationRepresentation>() {
            @Override
            public OperationRepresentation call() throws Exception {
                return deviceControlApi.getOperation(gid);
            }
        });
    }

    public OperationRepresentation create(final OperationRepresentation operation) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<OperationRepresentation>() {
            @Override
            public OperationRepresentation call() throws Exception {
                return deviceControlApi.create(operation);
            }
        });
    }

    public OperationRepresentation update(final OperationRepresentation operation) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<OperationRepresentation>() {
            @Override
            public OperationRepresentation call() throws Exception {
                return deviceControlApi.update(operation);
            }
        });
    }

    public Future updateAsync(final OperationRepresentation operation) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<Future>() {
            @Override
            public Future call() throws Exception {
                return deviceControlApi.updateAsync(operation);
            }
        });
    }

    public OperationCollection getOperations() throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<OperationCollection>() {
            @Override
            public OperationCollection call() throws Exception {
                return deviceControlApi.getOperations();
            }
        });
    }

    public OperationCollection getOperationsByFilter(final OperationFilter filter) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<OperationCollection>() {
            @Override
            public OperationCollection call() throws Exception {
                return deviceControlApi.getOperationsByFilter(filter);
            }
        });
    }

    public Subscriber<GId, OperationRepresentation> getNotificationsSubscriber() throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<Subscriber<GId, OperationRepresentation>>() {
            @Override
            public Subscriber<GId, OperationRepresentation> call() throws Exception {
                return deviceControlApi.getNotificationsSubscriber();
            }
        });
    }

    private void checkBeansNotNull() {
        Preconditions.checkNotNull(deviceControlApi, "Bean of type: " + DeviceControlApi.class + " must be in context");
        Preconditions.checkNotNull(platform, "Bean of type: " + PlatformImpl.class + " must be in context");
    }

}
