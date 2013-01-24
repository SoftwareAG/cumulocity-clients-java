/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.alarm;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.alarm.AlarmCollectionRepresentation;
import com.cumulocity.rest.representation.alarm.AlarmMediaType;
import com.cumulocity.sdk.client.PagedCollectionResourceImpl;
import com.cumulocity.sdk.client.RestConnector;

public class AlarmCollectionImpl extends PagedCollectionResourceImpl<AlarmCollectionRepresentation> {
    @Override
    protected CumulocityMediaType getMediaType() {
        return AlarmMediaType.ALARM_COLLECTION;
    }

    @Override
    protected Class<AlarmCollectionRepresentation> getResponseClass() {
        return AlarmCollectionRepresentation.class;
    }

    @Deprecated
    public AlarmCollectionImpl(RestConnector restConnector, String url) {
        super(restConnector, url);
    }

    public AlarmCollectionImpl(RestConnector restConnector, String url, int pageSize) {
        super(restConnector, url, pageSize);
    }
}
