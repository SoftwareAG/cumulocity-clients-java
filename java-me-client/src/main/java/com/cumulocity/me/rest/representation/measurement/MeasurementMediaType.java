package com.cumulocity.me.rest.representation.measurement;

import com.cumulocity.me.rest.representation.BaseCumulocityMediaType;

public class MeasurementMediaType extends BaseCumulocityMediaType {

    public static final MeasurementMediaType MEASUREMENT = new MeasurementMediaType("measurement");

    public static final String MEASUREMENT_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "measurement+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final MeasurementMediaType MEASUREMENT_COLLECTION = new MeasurementMediaType("measurementCollection");

    public static final String MEASUREMENT_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "measurementCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final MeasurementMediaType MEASUREMENT_API = new MeasurementMediaType("measurementApi");

    public static final String MEASUREMENT_API_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "measurementApi+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public MeasurementMediaType(String entity) {
        super("application", VND_COM_NSN_CUMULOCITY + entity + "+json;" + VND_COM_NSN_CUMULOCITY_PARAMS);
    }
}
