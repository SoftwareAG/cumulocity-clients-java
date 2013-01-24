/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.me.sdk.client.identity;

import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.model.ID;
import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.me.rest.representation.identity.IdentityMediaType;
import com.cumulocity.me.rest.representation.identity.IdentityRepresentation;
import com.cumulocity.me.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.me.rest.representation.platform.PlatformMediaType;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.TemplateUrlParser;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;

public class IdentityApiImpl implements IdentityApi {

    private static final String GLOBAL_ID = "globalId";

    // FIXME: there's a typo in the rest API
    private static final String EXTERNAL_ID = "externaId";

    private static final String TYPE = "type";

    private final String platformUrl;

    private final RestConnector restConnector;

    private TemplateUrlParser templateUrlParser;

    private final int pageSize;

    private IdentityRepresentation identityRepresentation;

    public IdentityApiImpl(RestConnector restConnector, TemplateUrlParser templateUrlParser, String platformUrl, int pageSize) {
        this.restConnector = restConnector;
        this.templateUrlParser = templateUrlParser;
        this.platformUrl = platformUrl;
        this.pageSize = pageSize;
    }

    private IdentityRepresentation getIdentityRepresentation() throws SDKException {
        if (null == identityRepresentation) {
            createApiRepresentation();
        }
        return identityRepresentation;
    }
    
    private void createApiRepresentation() throws SDKException
    {
        PlatformApiRepresentation platformApiRepresentation =  (PlatformApiRepresentation) restConnector.get(platformUrl,PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class);
        identityRepresentation = platformApiRepresentation.getIdentity();
    }

    public ExternalIDRepresentation getExternalId(ID extId) throws SDKException {
        if (extId == null || extId.getValue() == null || extId.getType() == null) {
            throw new SDKException("XtId without value/type or null");
        }

        Map filter = new HashMap();
        filter.put(TYPE, extId.getType());
        filter.put(EXTERNAL_ID, extId.getValue());
        String extIdUrl = templateUrlParser.replacePlaceholdersWithParams(getIdentityRepresentation().getExternalId(), filter);
        return (ExternalIDRepresentation) restConnector.get(extIdUrl, IdentityMediaType.EXTERNAL_ID, ExternalIDRepresentation.class);
    }

    public void deleteExternalId(ExternalIDRepresentation extIdRep) throws SDKException {
        Map filter = new HashMap();
        filter.put(TYPE, extIdRep.getType());
        filter.put(EXTERNAL_ID, extIdRep.getExternalId());
        String extIdUrl = templateUrlParser.replacePlaceholdersWithParams(getIdentityRepresentation().getExternalId(), filter);

        restConnector.delete(extIdUrl);
    }

    public PagedCollectionResource getExternalIdsOfGlobalId(GId gid) throws SDKException {
        if (gid == null || gid.getValue() == null) {
            throw new SDKException("Cannot determine global id value");
        }

        Map filter = new HashMap();
        filter.put(GLOBAL_ID, gid.getValue());
        String uri = templateUrlParser.replacePlaceholdersWithParams(getIdentityRepresentation().getExternalIdsOfGlobalId(), filter);
        return new ExternalIDCollectionImpl(restConnector, uri, pageSize);
    }

    public ExternalIDRepresentation create(ExternalIDRepresentation representation) throws SDKException {
        if (representation == null || representation.getManagedObject() == null || representation.getManagedObject().getId() == null) {
            throw new SDKException("Cannot determine global id value");
        }

        Map filter = new HashMap();
        filter.put(GLOBAL_ID, representation.getManagedObject().getId().getValue());
        String path = templateUrlParser.replacePlaceholdersWithParams(getIdentityRepresentation().getExternalIdsOfGlobalId(), filter);
        return (ExternalIDRepresentation) restConnector.post(path, IdentityMediaType.EXTERNAL_ID, representation);
    }

}
