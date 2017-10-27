package com.cumulocity.microservice.platform.api.event;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.buffering.Future;
import com.cumulocity.sdk.client.event.EventApi;
import com.cumulocity.sdk.client.event.EventCollection;
import com.cumulocity.sdk.client.event.EventFilter;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

import static com.cumulocity.microservice.platform.api.client.InternalTrafficDecorator.Builder.internally;

@Service
public class EventInternalApi {

    @Autowired(required = false)
    private EventApi eventApi;

    @Autowired(required = false)
    private PlatformImpl platform;

    public EventRepresentation getEvent(final GId gid) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<EventRepresentation>() {
            @Override
            public EventRepresentation call() throws Exception {
                return eventApi.getEvent(gid);
            }
        });
    }

    public EventRepresentation create(final EventRepresentation event) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<EventRepresentation>() {
            @Override
            public EventRepresentation call() throws Exception {
                return eventApi.create(event);
            }
        });
    }

    public Future createAsync(final EventRepresentation event) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<Future>() {
            @Override
            public Future call() throws Exception {
                return eventApi.createAsync(event);
            }
        });
    }

    public void delete(final EventRepresentation event) throws SDKException {
        checkBeansNotNull();
        internally().onPlatform(platform).doAction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                eventApi.delete(event);
                return null;
            }
        });
    }

    public EventCollection getEvents() throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<EventCollection>() {
            @Override
            public EventCollection call() throws Exception {
                return eventApi.getEvents();
            }
        });
    }

    public EventCollection getEventsByFilter(final EventFilter filter) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<EventCollection>() {
            @Override
            public EventCollection call() throws Exception {
                return eventApi.getEventsByFilter(filter);
            }
        });
    }

    public EventRepresentation update(final EventRepresentation eventRepresentation) throws SDKException {
        checkBeansNotNull();
        return internally().onPlatform(platform).doAction(new Callable<EventRepresentation>() {
            @Override
            public EventRepresentation call() throws Exception {
                return eventApi.update(eventRepresentation);
            }
        });
    }

    private void checkBeansNotNull() {
        Preconditions.checkNotNull(eventApi, "Bean of type: " + EventApi.class + " must be in context");
        Preconditions.checkNotNull(platform, "Bean of type: " + PlatformImpl.class + " must be in context");
    }

}
