package com.cumulocity.rest.representation.measurement;

import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class MeasurementCollectionRepresentation extends BaseCollectionRepresentation<MeasurementRepresentation> {

    private List<MeasurementRepresentation> measurements;

    public void setMeasurements(List<MeasurementRepresentation> measurements) {
        this.measurements = measurements;
    }

    @JSONTypeHint(MeasurementRepresentation.class)
    public List<MeasurementRepresentation> getMeasurements() {
        return measurements;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<MeasurementRepresentation> iterator() {
        return measurements.iterator();
    }
}
