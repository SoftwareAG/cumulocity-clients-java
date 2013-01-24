package com.cumulocity.me.rest.convert.alarm;

import com.cumulocity.me.rest.convert.event.EventRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.me.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.me.rest.validate.CommandBasedRepresentationValidationContext;
import com.cumulocity.me.rest.validate.RepresentationValidationContext;

public class AlarmRepresentationConverter extends EventRepresentationConverter {

    private static final String PROP_STATUS = "status";
    private static final String PROP_SEVERITY = "severity";
    private static final String PROP_HISTORY = "history";

    protected Class supportedRepresentationType() {
        return AlarmRepresentation.class;
    }
    
    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        super.instanceToJson(representation, json);
        putString(json, PROP_STATUS, $(representation).getStatus());
        putString(json, PROP_SEVERITY, $(representation).getSeverity());
        putObject(json, PROP_HISTORY, $(representation).getHistory());
    }
    
    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        super.instanceFromJson(json, representation);
        $(representation).setStatus(getString(json, PROP_STATUS));
        $(representation).setSeverity(getString(json, PROP_SEVERITY));
        $(representation).setHistory((AuditRecordCollectionRepresentation) getObject(json, PROP_HISTORY, AuditRecordCollectionRepresentation.class));
    }
    
    public boolean isValid(CumulocityResourceRepresentation representation, RepresentationValidationContext context) {
        if (CommandBasedRepresentationValidationContext.CREATE.equals(context)) {
            assertNotNull(PROP_STATUS, $(representation).getStatus());
            assertNotNull(PROP_SEVERITY, $(representation).getSeverity());
        }
        if (CommandBasedRepresentationValidationContext.UPDATE.equals(context)) {
            assertNull(PROP_HISTORY, $(representation).getHistory());
        }
        return super.isValid(representation, context);
    }

    private AlarmRepresentation $(CumulocityResourceRepresentation baseRepresentation) {
        return (AlarmRepresentation) baseRepresentation;
    }
}
