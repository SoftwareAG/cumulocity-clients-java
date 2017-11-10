package com.cumulocity.agent.server.repository;

import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.client.measurement.MeasurementApi;
import com.cumulocity.sdk.client.measurement.MeasurementCollection;
import com.cumulocity.sdk.client.measurement.MeasurementFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class MeasurementRepository {

    private static final Logger log = LoggerFactory.getLogger(MeasurementRepository.class);

    private final MeasurementApi measurementApi;

    @Autowired
    public MeasurementRepository(MeasurementApi measurementApi) {
        this.measurementApi = measurementApi;
    }

    public MeasurementRepresentation save(MeasurementRepresentation representation) {
        log.debug("Save measurment : {}.", representation);
        if (representation.getId() == null) {
            return measurementApi.create(representation);
        } else {
            throw new UnsupportedOperationException("Unable to update measurment");
        }
    }

    public void save(Collection<MeasurementRepresentation> measurments) {
        for (MeasurementRepresentation measurement : measurments) {
            measurementApi.create(measurement);
        }
    }
    
    public void saveWithoutResponse(MeasurementRepresentation representation) {
        measurementApi.createWithoutResponse(representation);
    }

    public MeasurementCollection getMeasurements() {
        return measurementApi.getMeasurements();
    }

    public MeasurementCollection getMeasurementsByFilter(MeasurementFilter filter) {
        return measurementApi.getMeasurementsByFilter(filter);
    }

}
