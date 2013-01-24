package com.cumulocity.me.rest.convert.audit;

import com.cumulocity.me.lang.UnsupportedOperationException;
import com.cumulocity.me.rest.convert.base.BaseResourceRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.me.rest.representation.audit.AuditRecordsRepresentation;

public class AuditRecordsRepresentationConverter extends BaseResourceRepresentationConverter {

    private static final String PROP_FOR_TYPE = "auditRecordsForType";
    private static final String PROP_FOR_USER = "auditRecordsForUser";
    private static final String PROP_FOR_APPLICATION = "auditRecordsForApplication";
    private static final String PROP_FOR_USER_AND_TYPE = "auditRecordsForUserAndType";
    private static final String PROP_FOR_USER_AND_APPLICATION = "auditRecordsForUserAndApplication";
    private static final String PROP_FOR_TYPE_AND_APPLICATION = "auditRecordsForTypeAndApplication";
    private static final String PROP_FOR_TYPE_AND_USER_AND_APPLICATION = "auditRecordsForTypeAndUserAndApplication";
    private static final String PROP_AUDIT_RECORDS = "auditRecords";
    
    protected void instanceToJson(BaseCumulocityResourceRepresentation representation, JSONObject json) {
        throw new UnsupportedOperationException();
    }

    protected void instanceFromJson(JSONObject json, BaseCumulocityResourceRepresentation representation) {
        $(representation).setAuditRecordsForType(getString(json, PROP_FOR_TYPE));
        $(representation).setAuditRecordsForUser(getString(json, PROP_FOR_USER));
        $(representation).setAuditRecordsForApplication(getString(json, PROP_FOR_APPLICATION));
        $(representation).setAuditRecordsForUserAndType(getString(json, PROP_FOR_USER_AND_TYPE));
        $(representation).setAuditRecordsForUserAndApplication(getString(json, PROP_FOR_USER_AND_APPLICATION));
        $(representation).setAuditRecordsForTypeAndApplication(getString(json, PROP_FOR_TYPE_AND_APPLICATION));
        $(representation).setAuditRecordsForTypeAndUserAndApplication(getString(json, PROP_FOR_TYPE_AND_USER_AND_APPLICATION));
        $(representation).setAuditRecords((AuditRecordCollectionRepresentation) getObject(json, PROP_AUDIT_RECORDS, AuditRecordCollectionRepresentation.class));
    }

    protected Class supportedRepresentationType() {
        return AuditRecordsRepresentation.class;
    }
    
    private AuditRecordsRepresentation $(BaseCumulocityResourceRepresentation baseRepresentation) {
        return (AuditRecordsRepresentation) baseRepresentation;
    }
}
