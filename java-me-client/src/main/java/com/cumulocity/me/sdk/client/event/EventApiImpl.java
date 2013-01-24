/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.event;

import java.util.Date;

import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.model.util.ExtensibilityConverter;
import com.cumulocity.me.rest.representation.event.EventMediaType;
import com.cumulocity.me.rest.representation.event.EventRepresentation;
import com.cumulocity.me.rest.representation.event.EventsApiRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.me.rest.representation.platform.PlatformMediaType;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.QueryURLBuilder;
import com.cumulocity.me.sdk.client.TemplateUrlParser;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;
import com.cumulocity.me.util.DateUtils;

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
        PlatformApiRepresentation platformApiRepresentation =  (PlatformApiRepresentation) restConnector.get(platformApiUrl,PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class);
        eventsApiRepresentation = platformApiRepresentation.getEvent();
    }

    public EventRepresentation getEvent(GId eventId) throws SDKException {
        String url = getSelfUri() + "/" + eventId.getValue();
        return (EventRepresentation) restConnector.get(url, EventMediaType.EVENT, EventRepresentation.class);
    }

    public PagedCollectionResource getEvents() throws SDKException {
        String url = getSelfUri();
        return new EventCollectionImpl(restConnector, url, pageSize);
    }

    public EventRepresentation create(EventRepresentation representation) throws SDKException {
        return (EventRepresentation) restConnector.post(getSelfUri(), EventMediaType.EVENT, representation);
    }

    private String getSelfUri() {
        return getEventApiRepresentation().getEvents().getSelf();
    }

    public void delete(EventRepresentation event) throws SDKException {
        String url = getSelfUri() + "/" + event.getId().getValue();
        restConnector.delete(url);
    }

    public PagedCollectionResource getEventsByFilter(EventFilter filter) throws SDKException {
        String type = filter.getType();
        Date dateFrom = filter.getFromDate();
        Date dateTo = filter.getToDate();
        Class fragmentType = filter.getFragmentType();
        ManagedObjectRepresentation source = filter.getSource();

        Map filterMap = new HashMap();
        if (null != source) {
            filterMap.put(SOURCE, source.getId().getValue());
        }
        if (null != dateFrom) {
            filterMap.put(DATE_FROM, DateUtils.format(dateFrom));
        }
        if (null != dateTo) {
            filterMap.put(DATE_TO, DateUtils.format(dateTo));
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
