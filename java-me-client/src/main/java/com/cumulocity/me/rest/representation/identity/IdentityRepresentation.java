package com.cumulocity.me.rest.representation.identity;

import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;

public class IdentityRepresentation extends BaseCumulocityResourceRepresentation {

    private String externalId;

    private String externalIdsOfGlobalId;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExternalIdsOfGlobalId() {
        return externalIdsOfGlobalId;
    }

    public void setExternalIdsOfGlobalId(String externalIdsOfGlobalId) {
        this.externalIdsOfGlobalId = externalIdsOfGlobalId;
    }
}
