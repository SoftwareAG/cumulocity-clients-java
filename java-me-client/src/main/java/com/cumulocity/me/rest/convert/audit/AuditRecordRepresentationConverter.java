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
package com.cumulocity.me.rest.convert.audit;

import com.cumulocity.me.model.audit.Change;
import com.cumulocity.me.rest.convert.event.EventRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseResourceRepresentation;
import com.cumulocity.me.rest.representation.ResourceRepresentation;
import com.cumulocity.me.rest.representation.audit.AuditRecordRepresentation;
import com.cumulocity.me.rest.validate.CommandBasedRepresentationValidationContext;
import com.cumulocity.me.rest.validate.RepresentationValidationContext;

public class AuditRecordRepresentationConverter extends EventRepresentationConverter {

    private static final String PROP_USER = "user";
    private static final String PROP_APPLICATION = "application";
    private static final String PROP_ACTIVITY = "activity";
    private static final String PROP_SEVERITY = "severity";
    private static final String PROP_CHANGES = "changes";
    

    protected Class supportedRepresentationType() {
        return AuditRecordRepresentation.class;
    }
    
    protected void instanceToJson(BaseResourceRepresentation representation, JSONObject json) {
        super.instanceToJson(representation, json);
        putString(json, PROP_USER, $(representation).getUser());
        putString(json, PROP_APPLICATION, $(representation).getApplication());
        putString(json, PROP_ACTIVITY, $(representation).getActivity());
        putString(json, PROP_SEVERITY, $(representation).getSeverity());
        putSet(json, PROP_CHANGES, $(representation).getChanges());
    }
    
    protected void instanceFromJson(JSONObject json, BaseResourceRepresentation representation) {
        super.instanceFromJson(json, representation);
        $(representation).setUser(getString(json, PROP_USER));
        $(representation).setApplication(getString(json, PROP_APPLICATION));
        $(representation).setActivity(getString(json, PROP_ACTIVITY));
        $(representation).setSeverity(getString(json, PROP_SEVERITY));
        $(representation).setChanges(getSet(json, PROP_CHANGES, Change.class));
    }
    
    public boolean isValid(ResourceRepresentation representation, RepresentationValidationContext context) {
        if (CommandBasedRepresentationValidationContext.CREATE.equals(context)) {
            assertNotNull(PROP_ACTIVITY, $(representation).getActivity());
            assertNotNull(PROP_SEVERITY, $(representation).getSeverity());
            assertNull(PROP_CHANGES, $(representation).getChanges());
        }
        return super.isValid(representation, context);
    }

    private AuditRecordRepresentation $(ResourceRepresentation baseRepresentation) {
        return (AuditRecordRepresentation) baseRepresentation;
    }
    
}
