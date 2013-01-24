/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.alarm;

import com.cumulocity.me.rest.representation.CumulocityMediaType;
import com.cumulocity.me.rest.representation.alarm.AlarmCollectionRepresentation;
import com.cumulocity.me.rest.representation.alarm.AlarmMediaType;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.sdk.client.page.PagedCollectionResourceImpl;

public class AlarmCollectionImpl extends PagedCollectionResourceImpl {
    
    protected CumulocityMediaType getMediaType() {
        return AlarmMediaType.ALARM_COLLECTION;
    }

    protected Class getResponseClass() {
        return AlarmCollectionRepresentation.class;
    }

    public AlarmCollectionImpl(RestConnector restConnector, String url) {
        super(restConnector, url);
    }

    public AlarmCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }
}
