package com.cumulocity.me.rest.convert.identity;

import com.cumulocity.me.rest.convert.base.BaseCollectionRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.identity.ExternalIDCollectionRepresentation;
import com.cumulocity.me.rest.representation.identity.ExternalIDRepresentation;

public class ExternalIDCollectionRepresentationConverter extends BaseCollectionRepresentationConverter {

    private static final String PROP_EXTERNAL_IDS = "externalIds";
    
    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        putList(json, PROP_EXTERNAL_IDS, $(representation).getExternalIds());
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setExternalIds(getList(json, PROP_EXTERNAL_IDS, ExternalIDRepresentation.class));
    }

    protected Class supportedRepresentationType() {
        return ExternalIDCollectionRepresentation.class;
    }
    
    private ExternalIDCollectionRepresentation $(BaseCumulocityResourceRepresentation baseRepresentation) {
        return (ExternalIDCollectionRepresentation) baseRepresentation;
    }

}
