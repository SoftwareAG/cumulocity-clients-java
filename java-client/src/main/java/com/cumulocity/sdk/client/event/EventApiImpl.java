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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cumulocity.model.DateConverter;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.util.ExtensibilityConverter;
import com.cumulocity.rest.representation.event.EventCollectionRepresentation;
import com.cumulocity.rest.representation.event.EventMediaType;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.event.EventsApiRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.QueryURLBuilder;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.TemplateUrlParser;

public class EventApiImpl implements EventApi {

    private static final String SOURCE = "source";

    private static final String DATE_FROM = "dateFrom";

    private static final String DATE_TO = "dateTo";

    private static final String FRAGMENT_TYPE = "fragmentType";

    private static final String TYPE = "type";

    /**
     * TODO: To be marked in the REST API.
     */
    private static final String[] OPTIONAL_PARAMETERS = new String[] { DATE_TO };

    private final String platformApiUrl;

    private final RestConnector restConnector;

    private TemplateUrlParser templateUrlParser;

    private final int pageSize;

    private EventsApiRepresentation eventsApiRepresentation = null;

    @Deprecated
    public EventApiImpl(RestConnector restConnector, TemplateUrlParser templateUrlParser, String platformApiUrl) {
        this.restConnector = restConnector;
        this.templateUrlParser = templateUrlParser;
        this.platformApiUrl = platformApiUrl;
        this.pageSize = PlatformParameters.DEFAULT_PAGE_SIZE;
    }

    public EventApiImpl(RestConnector restConnector, TemplateUrlParser templateUrlParser, String platformApiUrl, int pageSize) {
        this.restConnector = restConnector;
        this.templateUrlParser = templateUrlParser;
        this.platformApiUrl = platformApiUrl;
        this.pageSize = pageSize;
    }

    private EventsApiRepresentation getEventApiRepresentation() throws SDKException {
        if (null == eventsApiRepresentation) {
            createApiRepresentation();
        }
        return eventsApiRepresentation;
    }
    
    private void createApiRepresentation() throws SDKException
    {
        PlatformApiRepresentation platformApiRepresentation =  restConnector.get(platformApiUrl,PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class);
        eventsApiRepresentation = platformApiRepresentation.getEvent();
    }

    @Override
    public EventRepresentation getEvent(GId eventId) throws SDKException {
        String url = getEventApiRepresentation().getEvents().getSelf() + "/" + eventId.getValue();
        return restConnector.get(url, EventMediaType.EVENT, EventRepresentation.class);
    }

    @Override
    public PagedCollectionResource<EventCollectionRepresentation> getEvents() throws SDKException {
        String url = getEventApiRepresentation().getEvents().getSelf();
        return new EventCollectionImpl(restConnector, url, pageSize);
    }

    @Override
    public EventRepresentation create(EventRepresentation representation) throws SDKException {
        return restConnector.post(getEventApiRepresentation().getEvents().getSelf(), EventMediaType.EVENT, representation);
    }

    @Override
    public void delete(EventRepresentation event) throws SDKException {
        String url = getEventApiRepresentation().getEvents().getSelf() + "/" + event.getId().getValue();
        restConnector.delete(url);
    }

    @Override
    public PagedCollectionResource<EventCollectionRepresentation> getEventsByFilter(EventFilter filter) throws SDKException {
        String type = filter.getType();
        Date dateFrom = filter.getFromDate();
        Date dateTo = filter.getToDate();
        Class<?> fragmentType = filter.getFragmentType();
        ManagedObjectRepresentation source = filter.getSource();

        Map<String, String> filterMap = new HashMap<String, String>();
        if (null != source) {
            filterMap.put(SOURCE, source.getId().getValue());
        }
        if (null != dateFrom) {
            filterMap.put(DATE_FROM, DateConverter.date2String(dateFrom));
        }
        if (null != dateTo) {
            filterMap.put(DATE_TO, DateConverter.date2String(dateTo));
        }
        if (null != fragmentType) {
            filterMap.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        }
        if (null != type) {
            filterMap.put(TYPE, type);
        }

        QueryURLBuilder query = new QueryURLBuilder(templateUrlParser, filterMap, getEventApiRepresentation().getURITemplates(),
                OPTIONAL_PARAMETERS);
        String queryUrl = query.build();

        if (null == queryUrl) {
            return getEvents();
        }

        return new EventCollectionImpl(restConnector, queryUrl, pageSize);
    }

}
