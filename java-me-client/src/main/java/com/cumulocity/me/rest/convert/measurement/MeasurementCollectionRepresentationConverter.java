package com.cumulocity.me.rest.convert.measurement;

import com.cumulocity.me.rest.convert.base.BaseCollectionRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.measurement.MeasurementCollectionRepresentation;
import com.cumulocity.me.rest.representation.measurement.MeasurementRepresentation;

public class MeasurementCollectionRepresentationConverter extends BaseCollectionRepresentationConverter {
    
    private static final String PROP_MEASUREMENTS = "measurements";

    protected Class supportedRepresentationType() {
        return MeasurementCollectionRepresentation.class;
    }

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        putList(json, PROP_MEASUREMENTS, $(representation).getMeasurements());
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setMeasurements(getList(json, PROP_MEASUREMENTS, MeasurementRepresentation.class));
    }
    
    private MeasurementCollectionRepresentation $(BaseCumulocityResourceRepresentation baseRepresentation) {
        return (MeasurementCollectionRepresentation) baseRepresentation;
    }
}
