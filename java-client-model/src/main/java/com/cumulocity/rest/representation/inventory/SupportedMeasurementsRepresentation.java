package com.cumulocity.rest.representation.inventory;

import java.util.List;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import org.svenson.JSONProperty;

public class SupportedMeasurementsRepresentation extends AbstractExtensibleRepresentation {

    private List<String> supportedMeasurements;

    @JSONProperty(value = "c8y_SupportedMeasurements")
    public List<String> getSupportedMeasurements() {
        return supportedMeasurements;
    }

    public void setSupportedMeasurements(List<String> supportedMeasurements) {
        this.supportedMeasurements = supportedMeasurements;
    }
}
