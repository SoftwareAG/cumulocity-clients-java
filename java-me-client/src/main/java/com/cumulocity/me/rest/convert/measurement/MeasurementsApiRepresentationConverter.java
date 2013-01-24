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
