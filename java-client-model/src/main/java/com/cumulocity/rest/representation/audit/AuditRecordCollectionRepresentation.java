package com.cumulocity.rest.representation.audit;

import java.util.Iterator;
import java.util.List;

import lombok.EqualsAndHashCode;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

@EqualsAndHashCode
public class AuditRecordCollectionRepresentation extends BaseCollectionRepresentation<AuditRecordRepresentation> {

    private List<AuditRecordRepresentation> auditRecords;

    public AuditRecordCollectionRepresentation() {}

    public AuditRecordCollectionRepresentation(List<AuditRecordRepresentation> auditRecords) {
        this.auditRecords = auditRecords;
    }

    @JSONTypeHint(AuditRecordRepresentation.class)
    public List<AuditRecordRepresentation> getAuditRecords() {
        return auditRecords;
    }

    public void setAuditRecords(List<AuditRecordRepresentation> audits) {
        this.auditRecords = audits;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<AuditRecordRepresentation> iterator() {
        return auditRecords.iterator();
    }
}
