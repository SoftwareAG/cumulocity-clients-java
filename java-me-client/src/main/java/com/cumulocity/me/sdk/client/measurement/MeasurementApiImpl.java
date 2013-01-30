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

package com.cumulocity.me.sdk.client.measurement;

import java.util.Date;

import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.model.util.ExtensibilityConverter;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.representation.measurement.MeasurementMediaType;
import com.cumulocity.me.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.me.rest.representation.measurement.MeasurementsApiRepresentation;
import com.cumulocity.me.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.me.rest.representation.platform.PlatformMediaType;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.TemplateUrlParser;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;
import com.cumulocity.me.util.DateUtils;

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
    
    private void createApiRepresentation() throws SDKException {
        PlatformApiRepresentation platformApiRepresentation =  (PlatformApiRepresentation) 
                restConnector.get(platformUrl, PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class);
        measurementsApiRepresentation = platformApiRepresentation.getMeasurement();
    }

    public MeasurementRepresentation getMeasurement(GId measurementId) throws SDKException {
        String url = getMeasurementApiRepresentation().getMeasurements().getSelf() + "/" + measurementId.getValue();
        return (MeasurementRepresentation) restConnector.get(url, MeasurementMediaType.MEASUREMENT, MeasurementRepresentation.class);
    }

    public void deleteMeasurement(MeasurementRepresentation measurement) throws SDKException {
        String url = getMeasurementApiRepresentation().getMeasurements().getSelf() + "/" + measurement.getId().getValue();
        restConnector.delete(url);
    }

    public PagedCollectionResource getMeasurementsByFilter(MeasurementFilter filter) throws SDKException {
        String type = filter.getType();
        Date fromDate = filter.getFromDate();
        Date toDate = filter.getToDate();
        Class fragmentType = filter.getFragmentType();
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

    public PagedCollectionResource getMeasurements() throws SDKException {
        String url = getMeasurementApiRepresentation().getMeasurements().getSelf();
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getMeasurementsBySource(ManagedObjectRepresentation source)
            throws SDKException {
        Map filter = new HashMap();
        filter.put(SOURCE, source.getId().getValue());
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForSource();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getMeasurementsByDate(Date dateFrom, Date dateTo)
            throws SDKException {
        Map filter = new HashMap();
        filter.put(DATE_FROM, DateUtils.format(dateFrom));
        filter.put(DATE_TO, DateUtils.format(dateTo));
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForDate();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getMeasurementsByFragmentType(Class fragmentType)
            throws SDKException {
        Map filter = new HashMap();
        filter.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForFragmentType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getMeasurementsByType(String type) throws SDKException {
        Map filter = new HashMap();
        filter.put(TYPE, type);
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getMeasurementsBySourceAndDate(ManagedObjectRepresentation source,
            Date dateFrom, Date dateTo) throws SDKException {
        Map filter = new HashMap();
        filter.put(SOURCE, source.getId().getValue());
        filter.put(DATE_FROM, DateUtils.format(dateFrom));
        filter.put(DATE_TO, DateUtils.format(dateTo));
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForSourceAndDate();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getMeasurementsBySourceAndFragmentType(
            ManagedObjectRepresentation source, Class fragmentType) throws SDKException {
        Map filter = new HashMap();
        filter.put(SOURCE, source.getId().getValue());
        filter.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForSourceAndFragmentType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getMeasurementsBySourceAndType(ManagedObjectRepresentation source,
            String type) throws SDKException {
        Map filter = new HashMap();
        filter.put(SOURCE, source.getId().getValue());
        filter.put(TYPE, type);
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForSourceAndType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getMeasurementsByDateAndFragmentType(Date dateFrom, Date dateTo,
            Class fragmentType) throws SDKException {
        Map filter = new HashMap();
        filter.put(DATE_FROM, DateUtils.format(dateFrom));
        filter.put(DATE_TO, DateUtils.format(dateTo));
        filter.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForDateAndFragmentType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getMeasurementsByDateAndType(Date dateFrom, Date dateTo,
            String type) throws SDKException {
        Map filter = new HashMap();
        filter.put(DATE_FROM, DateUtils.format(dateFrom));
        filter.put(DATE_TO, DateUtils.format(dateTo));
        filter.put(TYPE, type);
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForDateAndType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getMeasurementsByFragmentTypeAndType(Class fragmentType,
            String type) throws SDKException {
        Map filter = new HashMap();
        filter.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        filter.put(TYPE, type);
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForFragmentTypeAndType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getMeasurementsBySourceAndDateAndFragmentType(
            ManagedObjectRepresentation source, Date dateFrom, Date dateTo, Class fragmentType) throws SDKException {
        Map filter = new HashMap();
        filter.put(SOURCE, source.getId().getValue());
        filter.put(DATE_FROM, DateUtils.format(dateFrom));
        filter.put(DATE_TO, DateUtils.format(dateTo));
        filter.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForSourceAndDateAndFragmentType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getMeasurementsBySourceAndDateAndType(
            ManagedObjectRepresentation source, Date dateFrom, Date dateTo, String type) throws SDKException {
        Map filter = new HashMap();
        filter.put(SOURCE, source.getId().getValue());
        filter.put(DATE_FROM, DateUtils.format(dateFrom));
        filter.put(DATE_TO, DateUtils.format(dateTo));
        filter.put(TYPE, type);
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForSourceAndDateAndType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getMeasurementsBySourceAndFragmentTypeAndType(
            ManagedObjectRepresentation source, Class fragmentType, String type) throws SDKException {
        Map filter = new HashMap();
        filter.put(SOURCE, source.getId().getValue());
        filter.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        filter.put(TYPE, type);
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForSourceAndFragmentTypeAndType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getMeasurementsByDateAndFragmentTypeAndType(Date dateFrom,
            Date dateTo, Class fragmentType, String type) throws SDKException {
        Map filter = new HashMap();
        filter.put(DATE_FROM, DateUtils.format(dateFrom));
        filter.put(DATE_TO, DateUtils.format(dateTo));
        filter.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        filter.put(TYPE, type);
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForDateAndFragmentTypeAndType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getMeasurementsBySourceAndDateAndFragmentTypeAndType(
            ManagedObjectRepresentation source, Date dateFrom, Date dateTo, Class fragmentType, String type) throws SDKException {
        Map filter = new HashMap();
        filter.put(SOURCE, source.getId().getValue());
        filter.put(DATE_FROM, DateUtils.format(dateFrom));
        filter.put(DATE_TO, DateUtils.format(dateTo));
        filter.put(FRAGMENT_TYPE, ExtensibilityConverter.classToStringRepresentation(fragmentType));
        filter.put(TYPE, type);
        String urlTemplate = getMeasurementApiRepresentation().getMeasurementsForSourceAndDateAndFragmentTypeAndType();
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new MeasurementCollectionImpl(restConnector, url, pageSize);
    }

    public MeasurementRepresentation create(MeasurementRepresentation measurementRepresentation) throws SDKException {
          return (MeasurementRepresentation) restConnector.post(getSelfUri(), MeasurementMediaType.MEASUREMENT, measurementRepresentation);
    }

    protected String getSelfUri() throws SDKException {
        return getMeasurementApiRepresentation().getMeasurements().getSelf();
    }

}
