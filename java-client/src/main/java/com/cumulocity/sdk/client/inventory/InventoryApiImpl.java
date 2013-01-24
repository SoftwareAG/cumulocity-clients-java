/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.inventory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.InventoryMediaType;
import com.cumulocity.rest.representation.inventory.InventoryRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectCollectionRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformMediaType;
import com.cumulocity.sdk.client.EmptyPagedCollectionResource;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.TemplateUrlParser;

public class InventoryApiImpl implements InventoryApi {

    private static final String TYPE = "type";

    private static final String FRAGMENT_TYPE = "fragmentType";

    private static final String IDS = "ids";

    private final RestConnector restConnector;

    private final String platformUrl;

    private TemplateUrlParser templateUrlParser;

    private final int pageSize;

    private InventoryRepresentation inventoryRepresentation;

    @Deprecated
    public InventoryApiImpl(RestConnector restConnector, TemplateUrlParser templateUrlParser, String platformUrl) {
        this.restConnector = restConnector;
        this.templateUrlParser = templateUrlParser;
        this.platformUrl = platformUrl;
        this.pageSize = PlatformParameters.DEFAULT_PAGE_SIZE;
    }

    public InventoryApiImpl(RestConnector restConnector, TemplateUrlParser templateUrlParser, String platformUrl, int pageSize) {
        this.restConnector = restConnector;
        this.templateUrlParser = templateUrlParser;
        this.platformUrl = platformUrl;
        this.pageSize = pageSize;
    }

    @Override
    public ManagedObjectRepresentation create(ManagedObjectRepresentation representation) throws SDKException {
        return restConnector.post(getSelfUri(), InventoryMediaType.MANAGED_OBJECT, representation);
    }

    @Override
    public ManagedObject getManagedObject(GId globalId) throws SDKException {
        if ((globalId == null) || (globalId.getValue() == null)) {
            throw new SDKException("Cannot determine the Global ID Value");
        }
        String url = getSelfUri() + "/" + globalId.getValue();
        return new ManagedObjectImpl(restConnector, url,pageSize);
    }

    @Override
    public PagedCollectionResource<ManagedObjectCollectionRepresentation> getManagedObjects() throws SDKException {
        String url = getSelfUri();
        return new ManagedObjectCollectionImpl(restConnector, url, pageSize);
    }

    @Override
    public PagedCollectionResource<ManagedObjectCollectionRepresentation> getManagedObjectsByFilter(InventoryFilter filter)
            throws SDKException {
        String type = filter.getType();
        String fragmentType = filter.getFragmentType();

        if (type != null && fragmentType != null) {
            throw new IllegalArgumentException();
        } else if (type != null) {
            return getManagedObjectsByType(type);
        } else if (fragmentType != null) {
            return getManagedObjectsByFragmentType(fragmentType);
        } else {
            return getManagedObjects();
        }
    }

    @Override
    public PagedCollectionResource<ManagedObjectCollectionRepresentation> getManagedObjectsByListOfIds(List<GId> ids) throws SDKException {
        if(ids == null || ids.size() == 0) {
            return new EmptyPagedCollectionResource<ManagedObjectCollectionRepresentation>(ManagedObjectCollectionRepresentation.class);
        }
        String urlTemplate = getInventoryRepresentation().getManagedObjectsForListOfIds();
        Map<String, String> filter = Collections.singletonMap(IDS, createCommaSeparatedStringFromGids(ids));
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new ManagedObjectCollectionImpl(restConnector, url, pageSize);
    }

    protected String getSelfUri() throws SDKException {
        return getInventoryRepresentation().getManagedObjects().getSelf();
    }

    private PagedCollectionResource<ManagedObjectCollectionRepresentation> getManagedObjectsByType(String type) throws SDKException {
        String urlTemplate = getInventoryRepresentation().getManagedObjectsForType();
        Map<String, String> filter = Collections.singletonMap(TYPE, type);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new ManagedObjectCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource<ManagedObjectCollectionRepresentation> getManagedObjectsByFragmentType(String fragmentType)
            throws SDKException {
        String urlTemplate = getInventoryRepresentation().getManagedObjectsForFragmentType();
        Map<String, String> filter = Collections.singletonMap(FRAGMENT_TYPE, fragmentType);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new ManagedObjectCollectionImpl(restConnector, url, pageSize);
    }

    private String createCommaSeparatedStringFromGids(List<GId> ids) {
        boolean atLeastOneItemProcessed = false;
        StringBuilder builder = new StringBuilder();

        for (GId gid : ids) {
            atLeastOneItemProcessed = true;
            builder.append(gid.getValue());
            builder.append(",");
        }

        // remove last comma if needed
        if (atLeastOneItemProcessed) {
            builder.deleteCharAt(builder.length() - 1);
        }

        return builder.toString();
    }

    private InventoryRepresentation getInventoryRepresentation() throws SDKException {
        if (null == inventoryRepresentation) {
            createApiRepresentation();
        }
        return inventoryRepresentation;
    }
    
    private void createApiRepresentation() throws SDKException
    {
        PlatformApiRepresentation platformApiRepresentation =  restConnector.get(platformUrl,PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class);
        inventoryRepresentation = platformApiRepresentation.getInventory();
    }
}
