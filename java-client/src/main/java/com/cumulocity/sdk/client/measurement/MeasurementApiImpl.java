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

package com.cumulocity.sdk.client.measurement;

import java.util.Map;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.measurement.MeasurementCollectionRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementMediaType;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementsApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.UrlProcessor;

public class MeasurementApiImpl implements MeasurementApi {

    private final RestConnector restConnector;

    private final String platformUrl;

    private final int pageSize;

    private MeasurementsApiRepresentation measurementsApiRepresentation;
    
    private UrlProcessor urlProcessor;

    public MeasurementApiImpl(RestConnector restConnector, UrlProcessor urlProcessor, String platformUrl, int pageSize) {
        this.restConnector = restConnector;
        this.urlProcessor = urlProcessor;
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
        if (filter == null) {
            return getMeasurements();
        }
        Map<String, String> params = filter.getQueryParams();
        return new MeasurementCollectionImpl(restConnector, urlProcessor.replaceOrAddQueryParam(getSelfUri(), params), pageSize);
    }

    @Override
    public PagedCollectionResource<MeasurementCollectionRepresentation> getMeasurements() throws SDKException {
        String url = getMeasurementApiRepresentation().getMeasurements().getSelf();
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
