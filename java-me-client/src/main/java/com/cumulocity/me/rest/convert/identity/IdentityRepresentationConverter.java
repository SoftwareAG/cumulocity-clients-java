package com.cumulocity.me.rest.convert.identity;

import com.cumulocity.me.lang.UnsupportedOperationException;
import com.cumulocity.me.rest.convert.base.BaseResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.identity.IdentityRepresentation;

public class IdentityRepresentationConverter extends BaseResourceRepresentationConverter {

    private static final String PROP_EXTERNAL_IDS_OF_GLOBAL_ID = "externalIdsOfGlobalId";

    private static final String PROP_EXTERNAL_ID = "externalId";

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        throw new UnsupportedOperationException();
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setExternalId(getString(json, PROP_EXTERNAL_ID));
        $(representation).setExternalIdsOfGlobalId(getString(json, PROP_EXTERNAL_IDS_OF_GLOBAL_ID));
    }

    protected Class supportedRepresentationType() {
        return IdentityRepresentation.class;
    }

    private IdentityRepresentation $(BaseCumulocityResourceRepresentation baseRepresentation) {
        return (IdentityRepresentation) baseRepresentation;
    }

}
