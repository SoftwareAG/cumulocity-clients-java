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
package com.cumulocity.me.rest.convert.measurement;

import com.cumulocity.me.lang.UnsupportedOperationException;
import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.convert.base.BaseResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.measurement.MeasurementCollectionRepresentation;
import com.cumulocity.me.rest.representation.measurement.MeasurementsApiRepresentation;

public class MeasurementsApiRepresentationConverter extends BaseResourceRepresentationConverter {
    
    protected Class supportedRepresentationType() {
        return MeasurementsApiRepresentation.class;
    }

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        throw new UnsupportedOperationException();
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setMeasurements((MeasurementCollectionRepresentation) 
                getObject(json, "measurements", MeasurementCollectionRepresentation.class));
        $(representation).setMeasurementsForSource(getString(json, "measurementsForSource"));
        $(representation).setMeasurementsForDate(getString(json, "measurementsForDate"));
        $(representation).setMeasurementsForFragmentType(getString(json, "measurementsForFragmentType"));
        $(representation).setMeasurementsForType(getString(json, "measurementsForType"));
        $(representation).setMeasurementsForSourceAndDate(getString(json, "measurementsForSourceAndDate"));
        $(representation).setMeasurementsForSourceAndFragmentType(getString(json, "measurementsForSourceAndFragmentType"));
        $(representation).setMeasurementsForSourceAndType(getString(json, "measurementsForSourceAndType"));
        $(representation).setMeasurementsForDateAndFragmentType(getString(json, "measurementsForDateAndFragmentType"));
        $(representation).setMeasurementsForDateAndType(getString(json, "measurementsForDateAndType"));
        $(representation).setMeasurementsForFragmentTypeAndType(getString(json, "measurementsForFragmentTypeAndType"));
        $(representation).setMeasurementsForSourceAndDateAndFragmentType(getString(json, "measurementsForSourceAndDateAndFragmentType"));
        $(representation).setMeasurementsForSourceAndDateAndType(getString(json, "measurementsForSourceAndDateAndType"));
        $(representation).setMeasurementsForSourceAndFragmentTypeAndType(getString(json, "measurementsForSourceAndFragmentTypeAndType"));
        $(representation).setMeasurementsForDateAndFragmentTypeAndType(getString(json, "measurementsForDateAndFragmentTypeAndType"));
        $(representation).setMeasurementsForSourceAndDateAndFragmentTypeAndType(getString(json, "measurementsForSourceAndDateAndFragmentTypeAndType"));
    }

    private MeasurementsApiRepresentation $(BaseCumulocityResourceRepresentation baseRepresentation) {
        return (MeasurementsApiRepresentation) baseRepresentation;
    }

    protected void putGId(JSONObject json, String propDeviceId, GId id) {
        if (id == null) {
            return;
        }
        json.put(propDeviceId, id.getValue());
    }
}
