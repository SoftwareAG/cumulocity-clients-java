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
package com.cumulocity.me.rest.convert.identity;

import com.cumulocity.me.rest.convert.base.BaseResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.validate.CommandBasedRepresentationValidationContext;
import com.cumulocity.me.rest.validate.RepresentationValidationContext;

public class ExternalIDRepresentationConverter extends BaseResourceRepresentationConverter {

    public static final String PROP_TYPE = "type";
    public static final String PROP_EXTERNAL_ID = "externalId";
    public static final String PROP_MANAGED_OBJECT = "managedObject";

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        putString(json, PROP_TYPE, $(representation).getType());
        putString(json, PROP_EXTERNAL_ID, $(representation).getExternalId());
        putObject(json, PROP_MANAGED_OBJECT, $(representation).getManagedObject());
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setType(getString(json, PROP_TYPE));
        $(representation).setExternalId(getString(json, PROP_EXTERNAL_ID));
        $(representation).setManagedObject((ManagedObjectRepresentation)getObject(json, PROP_MANAGED_OBJECT, ManagedObjectRepresentation.class));
    }

    public boolean isValid(CumulocityResourceRepresentation representation, RepresentationValidationContext context) {
        if (CommandBasedRepresentationValidationContext.CREATE.equals(context)) {
            assertNotNull(PROP_TYPE, $(representation).getType());
            assertNotNull(PROP_EXTERNAL_ID, $(representation).getExternalId());
        }
        return true;
    }
    
    protected Class supportedRepresentationType() {
        return ExternalIDRepresentation.class;
    }
    
    private ExternalIDRepresentation $(CumulocityResourceRepresentation baseRepresentation) {
        return (ExternalIDRepresentation) baseRepresentation;
    }

}
