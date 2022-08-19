package com.cumulocity.rest.representation.identity;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;

public class IdentityRepresentation extends AbstractExtensibleRepresentation {

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
