package com.cumulocity.model.builder;

import com.cumulocity.model.ID;
import com.nsn.cumulocity.model.builder.AbstractObjectBuilder;

public class IDBuilder extends AbstractObjectBuilder<ID> {

    public IDBuilder withType(final String type) {
        setFieldValue("type", type);
        return this;
    }

    public IDBuilder withValue(final String value) {
        setFieldValue("value", value);
        return this;
    }
    
    public IDBuilder withName(final String name) {
        setFieldValue("name", name);
        return this;
    }

    @Override
    protected ID createDomainObject() {
        return new ID();
    }
}
