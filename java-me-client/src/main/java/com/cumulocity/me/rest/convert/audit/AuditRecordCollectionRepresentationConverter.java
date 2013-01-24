package com.cumulocity.me.rest.convert.audit;

import com.cumulocity.me.rest.convert.base.BaseCollectionRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.me.rest.representation.audit.AuditRecordRepresentation;

public class AuditRecordCollectionRepresentationConverter extends BaseCollectionRepresentationConverter{

    private static final String PROP_AUDIT_RECORDS = "auditRecords";
    
    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        putList(json, PROP_AUDIT_RECORDS, $(representation).getAuditRecords());
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setAuditRecords(getList(json, PROP_AUDIT_RECORDS, AuditRecordRepresentation.class));
    }

    protected Class supportedRepresentationType() {
        return AuditRecordCollectionRepresentation.class;
    }
    
    private AuditRecordCollectionRepresentation $(BaseCumulocityResourceRepresentation representation) {
        return (AuditRecordCollectionRepresentation) representation;
    }

}
