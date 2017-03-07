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
package com.cumulocity.me.rest.convert.inventory;

import com.cumulocity.me.rest.convert.base.BaseExtensibleResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONException;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseResourceRepresentation;
import com.cumulocity.me.rest.representation.ResourceRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.validate.CommandBasedRepresentationValidationContext;
import com.cumulocity.me.rest.validate.RepresentationValidationContext;

public class ManagedObjectRepresentationConverter extends BaseExtensibleResourceRepresentationConverter {

    public static final String PROP_TYPE = "type";
    public static final String PROP_NAME = "name";
    public static final String PROP_OWNER = "owner";
    public static final String PROP_CHILD_DEVICES = "childDevices";
    public static final String PROP_CHILD_ASSETS = "childAssets";
    public static final String PROP_CHILD_ADDITIONS = "childAdditions";
    public static final String PROP_PARENTS = "parents";
    public static final String PROP_LAST_UPDATED = "lastUpdated";

    protected Class supportedRepresentationType() {
        return ManagedObjectRepresentation.class;
    }

    protected void instanceToJson(BaseResourceRepresentation representation, JSONObject json) throws JSONException {
        putGId(json, $(representation).getId());
        putString(json, PROP_TYPE, $(representation).getType());
        putString(json, PROP_NAME, $(representation).getName());
        putString(json, PROP_OWNER,$(representation).getOwner());
        putObject(json, PROP_CHILD_DEVICES, $(representation).getChildDevices());
        putObject(json, PROP_CHILD_ASSETS, $(representation).getChildAssets());
        putObject(json, PROP_CHILD_ADDITIONS, $(representation).getChildAdditions());
        putObject(json, PROP_PARENTS, $(representation).getParents());
        putDate(json, PROP_LAST_UPDATED, $(representation).getLastUpdated());
    }

    protected void instanceFromJson(JSONObject json, BaseResourceRepresentation representation) throws JSONException {
        $(representation).setId(getGId(json));
        $(representation).setType(getString(json, PROP_TYPE));
        $(representation).setName(getString(json, PROP_NAME));
        $(representation).setOwner(getString(json, PROP_OWNER));
        $(representation).setChildAssets((ManagedObjectReferenceCollectionRepresentation) getObject(json, PROP_CHILD_ASSETS,
                ManagedObjectReferenceCollectionRepresentation.class));
        $(representation).setChildDevices((ManagedObjectReferenceCollectionRepresentation) getObject(json, PROP_CHILD_DEVICES, 
                ManagedObjectReferenceCollectionRepresentation.class));
        $(representation).setChildAdditions((ManagedObjectReferenceCollectionRepresentation) getObject(json, PROP_CHILD_ADDITIONS,
                ManagedObjectReferenceCollectionRepresentation.class));
        $(representation).setParents((ManagedObjectReferenceCollectionRepresentation) getObject(json, PROP_PARENTS,
                ManagedObjectReferenceCollectionRepresentation.class));
        $(representation).setLastUpdated(getDate(json, PROP_LAST_UPDATED));
    }

    public boolean isValid(ResourceRepresentation representation, RepresentationValidationContext context) {
        if (CommandBasedRepresentationValidationContext.CREATE.equals(context) ||
                CommandBasedRepresentationValidationContext.UPDATE.equals(context)) {
           
            assertNull(PROP_ID, $(representation).getId());
            assertNull(PROP_LAST_UPDATED, $(representation).getLastUpdated());
        }
        return true;
    }

    private ManagedObjectRepresentation $(ResourceRepresentation baseRepresentation) {
        return (ManagedObjectRepresentation) baseRepresentation;
    }
}
