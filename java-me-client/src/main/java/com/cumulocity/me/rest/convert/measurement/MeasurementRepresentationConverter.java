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
package com.cumulocity.me.rest.convert.measurement;

import com.cumulocity.me.rest.convert.base.BaseExtensibleResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.me.rest.validate.CommandBasedRepresentationValidationContext;
import com.cumulocity.me.rest.validate.RepresentationValidationContext;

public class MeasurementRepresentationConverter extends BaseExtensibleResourceRepresentationConverter {
    
    private static final String PROP_TYPE = "type";
    private static final String PROP_TIME = "time";
    private static final String PROP_SOURCE = "source";

    protected Class supportedRepresentationType() {
        return MeasurementRepresentation.class;
    }

    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        putGId(json, $(representation).getId());
        putString(json, PROP_TYPE, $(representation).getType());
        putDate(json, PROP_TIME, $(representation).getTime());
        putObject(json, PROP_SOURCE, $(representation).getSource());
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setId(getGId(json));
        $(representation).setType(getString(json, PROP_TYPE));
        $(representation).setTime(getDate(json, PROP_TIME));
        $(representation).setSource((ManagedObjectRepresentation) getObject(json, PROP_SOURCE, ManagedObjectRepresentation.class));
    }

    public boolean isValid(CumulocityResourceRepresentation representation, RepresentationValidationContext context) {
        if (CommandBasedRepresentationValidationContext.CREATE.equals(context)) {
            assertNull(PROP_ID, $(representation).getId());
            assertNotNull(PROP_TYPE, $(representation).getType());
            assertNotNull(PROP_TIME, $(representation).getTime());
            assertNotNull(PROP_SOURCE, $(representation).getSource());
        }
        return true;
    }

    private MeasurementRepresentation $(CumulocityResourceRepresentation baseRepresentation) {
        return (MeasurementRepresentation) baseRepresentation;
    }
}
