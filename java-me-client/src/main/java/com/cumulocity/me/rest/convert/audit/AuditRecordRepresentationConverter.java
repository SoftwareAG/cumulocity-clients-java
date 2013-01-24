package com.cumulocity.me.rest.convert.audit;

import com.cumulocity.me.model.audit.Change;
import com.cumulocity.me.rest.convert.event.EventRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
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
    
    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        super.instanceToJson(representation, json);
        putString(json, PROP_USER, $(representation).getUser());
        putString(json, PROP_APPLICATION, $(representation).getApplication());
        putString(json, PROP_ACTIVITY, $(representation).getActivity());
        putString(json, PROP_SEVERITY, $(representation).getSeverity());
        putSet(json, PROP_CHANGES, $(representation).getChanges());
    }
    
    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        super.instanceFromJson(json, representation);
        $(representation).setUser(getString(json, PROP_USER));
        $(representation).setApplication(getString(json, PROP_APPLICATION));
        $(representation).setActivity(getString(json, PROP_ACTIVITY));
        $(representation).setSeverity(getString(json, PROP_SEVERITY));
        $(representation).setChanges(getSet(json, PROP_CHANGES, Change.class));
    }
    
    public boolean isValid(CumulocityResourceRepresentation representation, RepresentationValidationContext context) {
        if (CommandBasedRepresentationValidationContext.CREATE.equals(context)) {
            assertNotNull(PROP_ACTIVITY, $(representation).getActivity());
            assertNotNull(PROP_SEVERITY, $(representation).getSeverity());
            assertNull(PROP_CHANGES, $(representation).getChanges());
        }
        return super.isValid(representation, context);
    }

    private AuditRecordRepresentation $(CumulocityResourceRepresentation baseRepresentation) {
        return (AuditRecordRepresentation) baseRepresentation;
    }
    
}
