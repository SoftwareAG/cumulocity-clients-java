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

package com.cumulocity.sdk.client.event;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.event.EventMediaType;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.event.EventsApiRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.UrlProcessor;
import com.cumulocity.sdk.client.buffering.Future;

import java.util.Map;

public class EventApiImpl implements EventApi {

    private final RestConnector restConnector;

    private final int pageSize;

    private EventsApiRepresentation eventsApiRepresentation;
    
    private UrlProcessor urlProcessor;

    public EventApiImpl(RestConnector restConnector, UrlProcessor urlProcessor, EventsApiRepresentation eventsApiRepresentation, int pageSize) {
        this.restConnector = restConnector;
        this.urlProcessor = urlProcessor;
        this.eventsApiRepresentation = eventsApiRepresentation;
        this.pageSize = pageSize;
    }

    private EventsApiRepresentation getEventApiRepresentation() throws SDKException {
        return eventsApiRepresentation;
    }
    
    @Override
    public EventRepresentation getEvent(GId eventId) throws SDKException {
        String url = getSelfUri() + "/" + eventId.getValue();
        return restConnector.get(url, EventMediaType.EVENT, EventRepresentation.class);
    }

    @Override
    public EventCollection getEvents() throws SDKException {
        String url = getSelfUri();
        return new EventCollectionImpl(restConnector, url, pageSize);
    }

    @Override
    public EventRepresentation create(EventRepresentation representation) throws SDKException {
        return restConnector.post(getSelfUri(), EventMediaType.EVENT, representation);
    }
    
    @Override
    public Future createAsync(EventRepresentation representation) throws SDKException {
        return restConnector.postAsync(getSelfUri(), EventMediaType.EVENT, representation);
    }

    @Override
    public void delete(EventRepresentation event) throws SDKException {
        String url = getSelfUri() + "/" + event.getId().getValue();
        restConnector.delete(url);
    }

    @Override
    public void deleteEventsByFilter(EventFilter filter) throws IllegalArgumentException, SDKException {
        if (filter == null) {
            throw new IllegalArgumentException("Event filter is null");
        } else {
            Map<String, String> params = filter.getQueryParams();
            restConnector.delete(urlProcessor.replaceOrAddQueryParam(getSelfUri(), params));
        }
    }

    @Override
    public EventCollection getEventsByFilter(EventFilter filter) throws SDKException {
        if (filter == null) {
            return getEvents();
        }
        Map<String, String> params = filter.getQueryParams();
        return new EventCollectionImpl(restConnector, urlProcessor.replaceOrAddQueryParam(getSelfUri(), params), pageSize);
    }

    private String getSelfUri() throws SDKException {
        return getEventApiRepresentation().getEvents().getSelf();
    }

    @Override
    public EventRepresentation update(EventRepresentation eventRepresentation) throws SDKException {
        String url = getSelfUri() + "/" + eventRepresentation.getId().getValue();
        return restConnector.put(url, EventMediaType.EVENT, eventRepresentation);
    }
}
