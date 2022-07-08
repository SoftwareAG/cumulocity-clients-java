package com.cumulocity.model.builder;

import com.cumulocity.model.idtype.GId;

public class GIdBuilder extends IDBuilder {

	public GIdBuilder withType(final String type) {
		setFieldValue("type", type);
		return this;
	}

	public GIdBuilder withValue(final String value) {
		setFieldValue("value", value);
		return this;
	}

	@Override
	protected GId createDomainObject() {
		return new GId();
	}

    @Override
    public GId build() {
        return (GId) super.build();
    }
}
