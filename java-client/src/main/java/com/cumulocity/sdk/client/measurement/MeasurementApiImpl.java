/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.measurement;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cumulocity.model.DateConverter;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.util.ExtensibilityConverter;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementCollectionRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementMediaType;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementsApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformMediaType;
import com.cumulocity.sdk.client.*;

public class MeasurementApiImpl implements MeasurementApi {

    private static final String SOURCE = "source";

    private static final String DATE_FROM = "dateFrom";

    private static final String DATE_TO = "dateTo";

    private static final String FRAGMENT_TYPE = "fragmentType";

    private static final String TYPE = "type";

    private final RestConnector restConnector;

    private final String platformUrl;

    private final TemplateUrlParser templateUrlParser;

    private final int pageSize;

    private MeasurementsApiRepresentation measurementsApiRepresentation;

  @Deprecated
   public MeasurementApiImpl(RestConnector restConnector, TemplateUrlParser templateUrlParser, String platformUrl) {
        this.restConnector = restConnector;
        this.templateUrlParser = templateUrlParser;
        this.platformUrl = platformUrl;
        this.pageSize = PlatformParameters.DEFAULT_PAGE_SIZE;
    }

    public MeasurementApiImpl(RestConnector restConnector, TemplateUrlParser templateUrlParser, String platformUrl, int pageSize) {
        this.restConnector = restConnector;
        this.templateUrlParser = templateUrlParser;
        this.platformUrl = platformUrl;
        this.pageSize = pageSize;
    }
  
    private MeasurementsApiRepresentation getMeasurementApiRepresentation() throws SDKException {
        if (null == measurementsApiRepresentation) {
            createApiRepresentation();
        }
        return measurementsApiRepresentation;
    }
    
    private void createApiRepresentation() throws SDKException
    {
        PlatformApiRepresentation platformApiRepresentation =  restConnector.get(platformUrl,PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class);
        measurementsApiRepresentation = platformApiRepresentation.getMeasurement();
    }

    @Override
    public MeasurementRepresentation getMeasurement(GId measurementId) throws SDKException {
        String url = getMeasurementApiRepresentation().getMeasurements().getSelf() + "/" + measurementId.getValue();
        return restConnector.get(url, MeasurementMediaType.MEASUREMENT, MeasurementRepresentation.class);
    }

    @Override
    public void deleteMeasurement(MeasurementRepresentation measurement) throws SDKException {
        String url = getMeasurementApiRepresentation().getMeasurements().getSelf() + "/" + measurement.getId().getValue();
        restConnector.delete(url);
    }

    @Override
    public PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurementsByFilter(MeasurementFilter filter)
            throws SDKException {
        String type = filter.getType();
        Date fromDate = filter.getFromDate();
        Date toDate = filter.getToDate();
        Class<?> fragmentType = filter.getFragmentType();
        ManagedObjectRepresentation source = filter.getSource();

        if (source != null && fromDate != null && toDate != null && fragmentType != null && type != null) {
            return getMeasurementsBySourceAndDateAndFragmentTypeAndType(source, fromDate, toDate, fragmentType, type);
        } else if (fromDate != null && toDate != null && fragmentType != null && type != null) {
            return getMeasurementsByDateAndFragmentTypeAndType(fromDate, toDate, fragmentType, type);
        } else if (fromDate != null && toDate != null && fragmentType != null && source != null) {
            return getMeasurementsBySourceAndDateAndFragmentType(source, fromDate, toDate, fragmentType);
        } else if (source != null && fragmentType != null && type != null) {
            return getMeasurementsBySourceAndFragmentTypeAndType(source, fragmentType, type);
        } else if (fromDate != null && toDate != null && type != null && source != null) {
            return getMeasurementsBySourceAndDateAndType(source, fromDate, toDate, type);
        } else if (fromDate != null && toDate != null && fragmentType != null) {
            return getMeasurementsByDateAndFragmentType(fromDate, toDate, fragmentType);
        } else if (fromDate != null && toDate != null && type != null) {
            return getMeasurementsByDateAndType(fromDate, toDate, type);
        } else if (fromDate != null && toDate != null && source != null) {
            return getMeasurementsBySourceAndDate(source, fromDate, toDate);
        } else if (type != null && fragmentType != null) {
            return getMeasurementsByFragmentTypeAndType(fragmentType, type);
        } else if (source != null && fragmentType != null) {
            return getMeasurementsBySourceAndFragmentType(source, fragmentType);
        } else if (source != null && type != null) {
            return getMeasurementsBySourceAndType(source, type);
        } else if (type != null) {
            return getMeasurementsByType(type);
        } else if (fragmentType != null) {
            return getMeasurementsByFragmentType(fragmentType);
        } else if (fromDate != null && toDate != null) {
            return getMeasurementsByDate(fromDate, toDate);
        } else if (source != null) {
            return getMeasurementsBySource(source);
        } else {
            return getMeasurements();
        }
    }

    @Override
    public PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurements() throws SDKException {
        String url = getMeasurementApiRepresentation().getMeasurements().getSelf();
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurementsBySource(ManagedObjectRepresentation source)
            throws SDKException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(SOURCE, source.getId().getValue());
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForSource();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurementsByDate(Date dateFrom, Date dateTo)
            throws SDKException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(DATE_FROM, DateConverter.date2String(dateFrom));
        filter.put(DATE_TO, DateConverter.date2String(dateTo));
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForDate();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurementsByFragmentType(Class<?> fragmentType)
            throws SDKException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForFragmentType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurementsByType(String type) throws SDKException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(TYPE, type);
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurementsBySourceAndDate(ManagedObjectRepresentation source,
            Date dateFrom, Date dateTo) throws SDKException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(SOURCE, source.getId().getValue());
        filter.put(DATE_FROM, DateConverter.date2String(dateFrom));
        filter.put(DATE_TO, DateConverter.date2String(dateTo));
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForSourceAndDate();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurementsBySourceAndFragmentType(
            ManagedObjectRepresentation source, Class<?> fragmentType) throws SDKException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(SOURCE, source.getId().getValue());
        filter.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForSourceAndFragmentType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurementsBySourceAndType(ManagedObjectRepresentation source,
            String type) throws SDKException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(SOURCE, source.getId().getValue());
        filter.put(TYPE, type);
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForSourceAndType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurementsByDateAndFragmentType(Date dateFrom, Date dateTo,
            Class<?> fragmentType) throws SDKException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(DATE_FROM, DateConverter.date2String(dateFrom));
        filter.put(DATE_TO, DateConverter.date2String(dateTo));
        filter.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForDateAndFragmentType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurementsByDateAndType(Date dateFrom, Date dateTo,
            String type) throws SDKException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(DATE_FROM, DateConverter.date2String(dateFrom));
        filter.put(DATE_TO, DateConverter.date2String(dateTo));
        filter.put(TYPE, type);
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForDateAndType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurementsByFragmentTypeAndType(Class<?> fragmentType,
            String type) throws SDKException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        filter.put(TYPE, type);
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForFragmentTypeAndType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurementsBySourceAndDateAndFragmentType(
            ManagedObjectRepresentation source, Date dateFrom, Date dateTo, Class<?> fragmentType) throws SDKException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(SOURCE, source.getId().getValue());
        filter.put(DATE_FROM, DateConverter.date2String(dateFrom));
        filter.put(DATE_TO, DateConverter.date2String(dateTo));
        filter.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForSourceAndDateAndFragmentType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurementsBySourceAndDateAndType(
            ManagedObjectRepresentation source, Date dateFrom, Date dateTo, String type) throws SDKException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(SOURCE, source.getId().getValue());
        filter.put(DATE_FROM, DateConverter.date2String(dateFrom));
        filter.put(DATE_TO, DateConverter.date2String(dateTo));
        filter.put(TYPE, type);
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForSourceAndDateAndType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurementsBySourceAndFragmentTypeAndType(
            ManagedObjectRepresentation source, Class<?> fragmentType, String type) throws SDKException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(SOURCE, source.getId().getValue());
        filter.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        filter.put(TYPE, type);
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForSourceAndFragmentTypeAndType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurementsByDateAndFragmentTypeAndType(Date dateFrom,
            Date dateTo, Class<?> fragmentType, String type) throws SDKException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(DATE_FROM, DateConverter.date2String(dateFrom));
        filter.put(DATE_TO, DateConverter.date2String(dateTo));
        filter.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        filter.put(TYPE, type);
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForDateAndFragmentTypeAndType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurementsBySourceAndDateAndFragmentTypeAndType(
            ManagedObjectRepresentation source, Date dateFrom, Date dateTo, Class<?> fragmentType, String type) throws SDKException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(SOURCE, source.getId().getValue());
        filter.put(DATE_FROM, DateConverter.date2String(dateFrom));
        filter.put(DATE_TO, DateConverter.date2String(dateTo));
        filter.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        filter.put(TYPE, type);
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForSourceAndDateAndFragmentTypeAndType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    @Override
    public MeasurementRepresentation create(MeasurementRepresentation measurementRepresentation) throws SDKException {
          return restConnector.post(getSelfUri(), MeasurementMediaType.MEASUREMENT, measurementRepresentation);
    }

    protected String getSelfUri() throws SDKException {
        return getMeasurementApiRepresentation().getMeasurements().getSelf();
    }

}
