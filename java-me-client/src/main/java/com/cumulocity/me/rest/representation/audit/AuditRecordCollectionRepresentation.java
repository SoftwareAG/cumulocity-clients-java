package com.cumulocity.me.rest.representation.audit;

import com.cumulocity.me.lang.List;
import com.cumulocity.me.rest.representation.BaseCollectionRepresentation;

public class AuditRecordCollectionRepresentation extends BaseCollectionRepresentation {
    
    private List auditRecords;

    public List getAuditRecords() {
        return auditRecords;
    }

    public void setAuditRecords(List audits) {
        this.auditRecords = audits;
    }
}
