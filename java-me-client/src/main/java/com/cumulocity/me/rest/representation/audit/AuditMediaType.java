package com.cumulocity.me.rest.representation.audit;

import com.cumulocity.me.rest.representation.BaseCumulocityMediaType;

public class AuditMediaType extends BaseCumulocityMediaType {

    public static final AuditMediaType AUDIT_API = new AuditMediaType("auditApi");

    public static final AuditMediaType AUDIT_RECORD = new AuditMediaType("auditRecord");

    public static final AuditMediaType AUDIT_RECORD_COLLECTION = new AuditMediaType("auditRecordCollection");

    public static final String AUDIT_API_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "auditApi+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final String AUDIT_RECORD_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "auditRecord+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final String AUDIT_RECORD_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "auditRecordCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public AuditMediaType(String entity) {
        super(entity);
    }

}
