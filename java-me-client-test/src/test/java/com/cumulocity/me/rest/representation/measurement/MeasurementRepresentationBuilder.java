package com.cumulocity.me.rest.representation.measurement;

import java.util.Date;

import com.cumulocity.me.rest.representation.BaseRepresentationBuilder;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;

public class MeasurementRepresentationBuilder extends BaseRepresentationBuilder<MeasurementRepresentation, MeasurementRepresentationBuilder> {

    public static final MeasurementRepresentationBuilder aMeasurementRepresentation() {
        return new MeasurementRepresentationBuilder();
    }
    
    public MeasurementRepresentationBuilder withType(String type) {
        setObjectField("type", type);
        return this;
    }
    
    public MeasurementRepresentationBuilder withTime(Date time) {
        setObjectField("time", time);
        return this;
    }
    
    public MeasurementRepresentationBuilder withSource(ManagedObjectRepresentation source) {
        setObjectField("source", source);
        return this;
    }
    
    @Override
    protected MeasurementRepresentation createDomainObject() {
        return new MeasurementRepresentation();
    }
}
