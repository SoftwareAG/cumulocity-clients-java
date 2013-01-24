package com.cumulocity.me.rest.convert;

import org.junit.Before;

import com.cumulocity.me.rest.RepresentationServicesFactory;
import com.cumulocity.me.rest.convert.base.BaseRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.rest.validate.RepresentationValidationContext;

public abstract class BaseConverterTestCase {

    public abstract BaseRepresentationConverter getConverter();
    
    @Before
    public void setUp() {
        getConverter().setJsonConversionService(RepresentationServicesFactory.getInstance().getConversionService());
    }
    
    @SuppressWarnings("unchecked")
    protected <T> T fromJson(JSONObject json) {
        return (T) getConverter().fromJson(json);
    }

    protected JSONObject toJson(CumulocityResourceRepresentation representation) {
        return getConverter().toJson(representation);
    }
    
    protected boolean isValid(CumulocityResourceRepresentation representation, RepresentationValidationContext context) {
        return getConverter().isValid(representation, context);
    }
    
}
