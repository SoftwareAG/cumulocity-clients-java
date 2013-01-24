/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.alarm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cumulocity.model.DateConverter;
import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmCollectionRepresentation;
import com.cumulocity.rest.representation.alarm.AlarmMediaType;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.alarm.AlarmsApiRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.QueryURLBuilder;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.TemplateUrlParser;

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

    @Deprecated
    public AlarmApiImpl(RestConnector restConnector, TemplateUrlParser templateUrlParser, String platfromApiUri) {
        this.restConnector = restConnector;
        this.templateUrlParser = templateUrlParser;
        this.platformApiUri = platfromApiUri;
        this.pageSize = PlatformParameters.DEFAULT_PAGE_SIZE;
    }

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
    
    private void createApiRepresentation() throws SDKException
    {
        PlatformApiRepresentation platformApiRepresentation =  restConnector.get(platformApiUri,PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class);
        alarmsApiRepresentation = platformApiRepresentation.getAlarm();
    }

    @Override
    public AlarmRepresentation getAlarm(GId alarmId) throws SDKException {
        String url = getAlarmsApiRepresentation().getAlarms().getSelf() + "/" + alarmId.getValue();
        return restConnector.get(url, AlarmMediaType.ALARM, AlarmRepresentation.class);
    }

    @Override
    public AlarmRepresentation updateAlarm(AlarmRepresentation alarmToUpdate) throws SDKException {
        String url = getAlarmsApiRepresentation().getAlarms().getSelf() + "/" + alarmToUpdate.getId().getValue();
        return restConnector.put(url, AlarmMediaType.ALARM, prepareForUpdate(alarmToUpdate));
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

    @Override
    public PagedCollectionResource<AlarmCollectionRepresentation> getAlarms() throws SDKException {
        String url = getAlarmsApiRepresentation().getAlarms().getSelf();
        return new AlarmCollectionImpl(restConnector, url, pageSize);
    }

    @Override
    public AlarmRepresentation create(AlarmRepresentation representation) throws SDKException {
        return restConnector.post(getAlarmsApiRepresentation().getAlarms().getSelf(), AlarmMediaType.ALARM, representation);
    }

    @Override
    public PagedCollectionResource<AlarmCollectionRepresentation> getAlarmsByFilter(AlarmFilter alarmFilter) throws SDKException {
        CumulocityAlarmStatuses status = alarmFilter.getStatus();
        ManagedObjectRepresentation source = alarmFilter.getSource();
        Date dateFrom = alarmFilter.getFromDate();
        Date dateTo = alarmFilter.getToDate();

        Map<String, String> filter = new HashMap<String, String>();

        if (null != status) {
            filter.put(PARAMETER_STATUS, status.toString());
        }
        if (null != source) {
            filter.put(PARAMETER_SOURCE, source.getId().getValue());
        }
        if (null != dateFrom) {
            filter.put(DATE_FROM, DateConverter.date2String(dateFrom));
        }
        if (null != dateTo) {
            filter.put(DATE_TO, DateConverter.date2String(dateTo));
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
