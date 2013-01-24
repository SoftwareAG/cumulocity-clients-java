/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.cumulocity.me.sdk.client.inventory;


import com.cumulocity.me.lang.Collections;
import com.cumulocity.me.lang.Iterator;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.representation.inventory.InventoryMediaType;
import com.cumulocity.me.rest.representation.inventory.InventoryRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectCollectionRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.me.rest.representation.platform.PlatformMediaType;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.TemplateUrlParser;
import com.cumulocity.me.sdk.client.http.RestConnector;
import com.cumulocity.me.sdk.client.page.EmptyPagedCollectionResource;
import com.cumulocity.me.sdk.client.page.PagedCollectionResource;
import com.cumulocity.me.util.StringUtils;

public class InventoryApiImpl implements InventoryApi {

    private static final String TYPE = "type";

    private static final String FRAGMENT_TYPE = "fragmentType";

    private static final String IDS = "ids";

    private final RestConnector restConnector;

    private final TemplateUrlParser templateUrlParser;

    private final String platformUrl;

    private final int pageSize;

    private InventoryRepresentation inventoryRepresentation;

    public InventoryApiImpl(RestConnector restConnector, TemplateUrlParser templateUrlParser, String platformUrl, int pageSize) {
        this.restConnector = restConnector;
        this.templateUrlParser = templateUrlParser;
        this.platformUrl = platformUrl;
        this.pageSize = pageSize;
    }

    public ManagedObjectRepresentation create(ManagedObjectRepresentation representation) {
        return (ManagedObjectRepresentation) restConnector.post(getSelfUri(), InventoryMediaType.MANAGED_OBJECT, representation);
    }

    public ManagedObject getManagedObject(GId globalId) {
        if ((globalId == null) || (globalId.getValue() == null)) {
            throw new SDKException("Cannot determine the Global ID Value");
        }
        String url = StringUtils.ensureTail(getSelfUri(), "/") + globalId.getValue();
        return new ManagedObjectImpl(restConnector, url, pageSize);
    }

    public PagedCollectionResource getManagedObjects() {
        String url = getSelfUri();
        return new ManagedObjectCollectionImpl(restConnector, url, pageSize);
    }

    public PagedCollectionResource getManagedObjectsByFilter(InventoryFilter filter) {
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

    public PagedCollectionResource getManagedObjectsByListOfIds(List ids) {
        if (ids == null || ids.isEmpty()) {
            return new EmptyPagedCollectionResource(ManagedObjectCollectionRepresentation.class);
        }
        String urlTemplate = getInventoryRepresentation().getManagedObjectsForListOfIds();
        Map filter = Collections.singletonMap(IDS, createCommaSeparatedStringFromGids(ids));
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new ManagedObjectCollectionImpl(restConnector, url, pageSize);
    }

    protected String getSelfUri() {
        return getInventoryRepresentation().getManagedObjects().getSelf();
    }

    private PagedCollectionResource getManagedObjectsByType(String type) {
        String urlTemplate = getInventoryRepresentation().getManagedObjectsForType();
        Map filter = Collections.singletonMap(TYPE, type);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new ManagedObjectCollectionImpl(restConnector, url, pageSize);
    }

    private PagedCollectionResource getManagedObjectsByFragmentType(String fragmentType) {
        String urlTemplate = getInventoryRepresentation().getManagedObjectsForFragmentType();
        Map filter = Collections.singletonMap(FRAGMENT_TYPE, fragmentType);
        String url = templateUrlParser.replacePlaceholdersWithParams(urlTemplate, filter);
        return new ManagedObjectCollectionImpl(restConnector, url, pageSize);
    }

    private String createCommaSeparatedStringFromGids(List ids) {
        boolean atLeastOneItemProcessed = false;
        StringBuffer builder = new StringBuffer();

        Iterator i = ids.iterator();
        while (i.hasNext()) {
            GId gid = (GId) i.next();
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

    private InventoryRepresentation getInventoryRepresentation() {
        if (inventoryRepresentation == null) {
            createApiRepresentation();
        }
        return inventoryRepresentation;
    }

    private void createApiRepresentation() {
        PlatformApiRepresentation platformApiRepresentation = (PlatformApiRepresentation) restConnector.get(platformUrl,
                PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class);
        inventoryRepresentation = platformApiRepresentation.getInventory();
    }
}
