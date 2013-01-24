package com.cumulocity.me.rest.representation.audit;

import com.nsn.cumulocity.model.builder.AbstractObjectBuilder;

public class AuditRecordsRepresentationBuilder extends AbstractObjectBuilder<AuditRecordsRepresentation> {

    public static final AuditRecordsRepresentationBuilder aAuditRecordsRepresentation() {
        return new AuditRecordsRepresentationBuilder();
    }
    
    public AuditRecordsRepresentationBuilder withAuditRecordsForType(String auditRecordsForType) {
        setObjectField("auditRecordsForType", auditRecordsForType);
        return this;
    }
    
    public AuditRecordsRepresentationBuilder withAuditRecordsForUser(String auditRecordsForUser) {
        setObjectField("auditRecordsForUser", auditRecordsForUser);
        return this;
    }
    
    public AuditRecordsRepresentationBuilder withAuditRecordsForApplication(String auditRecordsForApplication) {
        setObjectField("auditRecordsForApplication", auditRecordsForApplication);
        return this;
    }
    
    public AuditRecordsRepresentationBuilder withAuditRecordsForUserAndType(String auditRecordsForUserAndType) {
        setObjectField("auditRecordsForUserAndType", auditRecordsForUserAndType);
        return this;
    }
    
    public AuditRecordsRepresentationBuilder withAuditRecordsForUserAndApplication(String auditRecordsForUserAndApplication) {
        setObjectField("auditRecordsForUserAndApplication", auditRecordsForUserAndApplication);
        return this;
    }
    
    public AuditRecordsRepresentationBuilder withAuditRecordsForTypeAndApplication(String auditRecordsForTypeAndApplication) {
        setObjectField("auditRecordsForTypeAndApplication", auditRecordsForTypeAndApplication);
        return this;
    }
    public AuditRecordsRepresentationBuilder withAuditRecordsForTypeAndUserAndApplication(String auditRecordsForTypeAndUserAndApplication) {
        setObjectField("auditRecordsForTypeAndUserAndApplication", auditRecordsForTypeAndUserAndApplication);
        return this;
    }
    
    public AuditRecordsRepresentationBuilder withAuditRecords(AuditRecordCollectionRepresentation auditRecords) {
        setObjectField("auditRecords", auditRecords);
        return this;
    }
    
    @Override
    protected AuditRecordsRepresentation createDomainObject() {
        return new AuditRecordsRepresentation();
    }

}