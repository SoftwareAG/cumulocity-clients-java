package com.cumulocity.me.rest.representation.measurement;

import com.cumulocity.me.lang.List;
import com.cumulocity.me.rest.representation.BaseCollectionRepresentation;

public class MeasurementCollectionRepresentation extends BaseCollectionRepresentation {

    private List measurements;

    public void setMeasurements(List measurements) {
        this.measurements = measurements;
    }

    public List getMeasurements() {
        return measurements;
    }
}
