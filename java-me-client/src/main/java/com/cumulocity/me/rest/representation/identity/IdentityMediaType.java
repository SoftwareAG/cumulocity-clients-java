package com.cumulocity.me.rest.representation.identity;

import com.cumulocity.me.rest.representation.BaseCumulocityMediaType;

public class IdentityMediaType extends BaseCumulocityMediaType {

    public static final IdentityMediaType EXTERNAL_ID = new IdentityMediaType("externalId");

    public static final String EXTERNAL_ID_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "externalId+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final IdentityMediaType EXTERNAL_ID_COLLECTION = new IdentityMediaType("externalIdCollection");

    public static final String EXTERNAL_ID_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "externalIdCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final IdentityMediaType IDENTITY_API = new IdentityMediaType("identityApi");

    public static final String IDENTITY_API_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "identityApi+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public IdentityMediaType(String entity) {
        super("application", VND_COM_NSN_CUMULOCITY + entity + "+json;" + VND_COM_NSN_CUMULOCITY_PARAMS);
    }

}
