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

package com.cumulocity.me.sdk.client.alarm;

import java.util.Date;

import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.model.event.CumulocityAlarmStatuses;
import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.representation.alarm.AlarmMediaType;
import com.cumulocity.me.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.me.rest.representation.alarm.AlarmsApiRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.me.rest.representation.platform.PlatformMediaType;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.QueryURLBuilder;
import com.cumulocity.me.sdk.client.TemplateUrlParser;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;
import com.cumulocity.me.util.DateUtils;

public class AlarmApiImpl implements AlarmApi {
    
    private static final String PARAMETER_SOURCE = "source";

    private static final String PARAMETER_STATUS = "status";

    private static final String DATE_FROM = "dateFrom";

    private static final String DATE_TO = "dateTo";

    /**
     * TODO: To be marked in the REST API.
     */
    private static final String[] OPTIONAL_PARAMETERS = new String[] { DATE_TO };

    private final String platformApiUri;

    private final RestConnector restConnector;

    private final TemplateUrlParser templateUrlParser;

    private final int pageSize;

    private AlarmsApiRepresentation alarmsApiRepresentation = null;

    public AlarmApiImpl(RestConnector restConnector, TemplateUrlParser templateUrlParser, String platformApiUri, int pageSize) {
        this.restConnector = restConnector;
        this.templateUrlParser = templateUrlParser;
        this.platformApiUri = platformApiUri;
        this.pageSize = pageSize;
    }

    private AlarmsApiRepresentation getAlarmsApiRepresentation() throws SDKException {
        if (null == alarmsApiRepresentation) {
            createApiRepresentation();
        }
        return alarmsApiRepresentation;
    }
    
    private void createApiRepresentation() throws SDKException {
        PlatformApiRepresentation platformApiRepresentation =  (PlatformApiRepresentation) 
                restConnector.get(platformApiUri,PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class);
        alarmsApiRepresentation = platformApiRepresentation.getAlarm();
    }

    public AlarmRepresentation getAlarm(GId alarmId) throws SDKException {
        String url = getAlarmsApiRepresentation().getAlarms().getSelf() + "/" + alarmId.getValue();
        return (AlarmRepresentation) restConnector.get(url, AlarmMediaType.ALARM, AlarmRepresentation.class);
    }

    public AlarmRepresentation updateAlarm(AlarmRepresentation alarmToUpdate) throws SDKException {
        String url = getAlarmsApiRepresentation().getAlarms().getSelf() + "/" + alarmToUpdate.getId().getValue();
        return (AlarmRepresentation) restConnector.put(url, AlarmMediaType.ALARM, prepareForUpdate(alarmToUpdate));
    }

    private AlarmRepresentation prepareForUpdate(AlarmRepresentation alarm) {
        AlarmRepresentation updatable = new AlarmRepresentation();
        updatable.setStatus(alarm.getStatus());
        updatable.setSeverity(alarm.getSeverity());
        updatable.setSource(alarm.getSource());
        updatable.setText(alarm.getText());
        updatable.setAttrs(alarm.getAttrs());
        return updatable;
    }

    public PagedCollectionResource getAlarms() throws SDKException {
        String url = getAlarmsApiRepresentation().getAlarms().getSelf();
        return new AlarmCollectionImpl(restConnector, url, pageSize);
    }

    public AlarmRepresentation create(AlarmRepresentation representation) throws SDKException {
        return (AlarmRepresentation) restConnector.post(getAlarmsApiRepresentation().getAlarms().getSelf(), AlarmMediaType.ALARM, representation);
    }

    public PagedCollectionResource getAlarmsByFilter(AlarmFilter alarmFilter) throws SDKException {
        CumulocityAlarmStatuses status = alarmFilter.getStatus();
        ManagedObjectRepresentation source = alarmFilter.getSource();
        Date dateFrom = alarmFilter.getFromDate();
        Date dateTo = alarmFilter.getToDate();

        Map filter = new HashMap();

        if (null != status) {
            filter.put(PARAMETER_STATUS, status.name());
        }
        if (null != source) {
            filter.put(PARAMETER_SOURCE, source.getId().getValue());
        }
        if (null != dateFrom) {
            filter.put(DATE_FROM, DateUtils.format(dateFrom));
        }
        if (null != dateTo) {
            filter.put(DATE_TO, DateUtils.format(dateTo));
        }

        QueryURLBuilder query = new QueryURLBuilder(templateUrlParser, filter, getAlarmsApiRepresentation().getURITemplates(),
                OPTIONAL_PARAMETERS);
        String queryUrl = query.build();

        if (null == queryUrl) {
            return getAlarms();
        }

        return new AlarmCollectionImpl(restConnector, queryUrl, pageSize);
    }
}
