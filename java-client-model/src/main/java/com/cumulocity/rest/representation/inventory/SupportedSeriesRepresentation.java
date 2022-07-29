package com.cumulocity.rest.representation.inventory;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import org.svenson.JSONProperty;

import java.util.List;

public class SupportedSeriesRepresentation extends AbstractExtensibleRepresentation {

    private List<String> supportedSeries;

    @JSONProperty(value = "c8y_SupportedSeries")
    public List<String> getSupportedSeries() {
        return supportedSeries;
    }

    public void setSupportedSeries(List<String> supportedSeries) {
        this.supportedSeries = supportedSeries;
    }
}
