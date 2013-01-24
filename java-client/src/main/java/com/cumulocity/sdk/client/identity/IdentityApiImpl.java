/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.identity;

import java.util.HashMap;
import java.util.Map;

import com.cumulocity.model.ID;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.identity.ExternalIDCollectionRepresentation;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.identity.IdentityMediaType;
import com.cumulocity.rest.representation.identity.IdentityRepresentation;
import com.cumulocity.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.TemplateUrlParser;

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

    @Deprecated
    public IdentityApiImpl(RestConnector restConnector, TemplateUrlParser templateUrlParser, String platformUrl) {
        this.restConnector = restConnector;
        this.templateUrlParser = templateUrlParser;
        this.platformUrl = platformUrl;
        this.pageSize = PlatformParameters.DEFAULT_PAGE_SIZE;
    }

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
        PlatformApiRepresentation platformApiRepresentation =  restConnector.get(platformUrl,PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class);
        identityRepresentation = platformApiRepresentation.getIdentity();
    }

    @Override
    public ExternalIDRepresentation getExternalId(ID extId) throws SDKException {
        if (extId == null || extId.getValue() == null || extId.getType() == null) {
            throw new SDKException("XtId without value/type or null");
        }

        Map<String, String> filter = new HashMap<String, String>();
        filter.put(TYPE, extId.getType());
        filter.put(EXTERNAL_ID, extId.getValue());
        String extIdUrl = templateUrlParser.replacePlaceholdersWithParams(getIdentityRepresentation().getExternalId(), filter);
        return restConnector.get(extIdUrl, IdentityMediaType.EXTERNAL_ID, ExternalIDRepresentation.class);
    }

    @Override
    public void deleteExternalId(ExternalIDRepresentation extIdRep) throws SDKException {
        Map<String, String> filter = new HashMap<String, String>();
        filter.put(TYPE, extIdRep.getType());
        filter.put(EXTERNAL_ID, extIdRep.getExternalId());
        String extIdUrl = templateUrlParser.replacePlaceholdersWithParams(getIdentityRepresentation().getExternalId(), filter);

        restConnector.delete(extIdUrl);
    }

    @Override
    public PagedCollectionResource<ExternalIDCollectionRepresentation> getExternalIdsOfGlobalId(GId gid) throws SDKException {
        if (gid == null || gid.getValue() == null) {
            throw new SDKException("Cannot determine global id value");
        }

        Map<String, String> filter = new HashMap<String, String>();
        filter.put(GLOBAL_ID, gid.getValue());
        String uri = templateUrlParser.replacePlaceholdersWithParams(getIdentityRepresentation().getExternalIdsOfGlobalId(), filter);
        return new ExternalIDCollectionImpl(restConnector, uri, pageSize);
    }

    @Override
    public ExternalIDRepresentation create(ExternalIDRepresentation representation) throws SDKException {
        if (representation == null || representation.getManagedObject() == null || representation.getManagedObject().getId() == null) {
            throw new SDKException("Cannot determine global id value");
        }

        Map<String, String> filter = new HashMap<String, String>();
        filter.put(GLOBAL_ID, representation.getManagedObject().getId().getValue());
        String path = templateUrlParser.replacePlaceholdersWithParams(getIdentityRepresentation().getExternalIdsOfGlobalId(), filter);
        return restConnector.post(path, IdentityMediaType.EXTERNAL_ID, representation);
    }

}
