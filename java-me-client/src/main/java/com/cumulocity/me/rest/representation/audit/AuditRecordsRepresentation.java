package com.cumulocity.me.rest.representation.audit;

import com.cumulocity.me.rest.representation.BaseCumulocityResourceRepresentation;

public class AuditRecordsRepresentation extends BaseCumulocityResourceRepresentation {

    private String auditRecordsForType;

    private String auditRecordsForUser;

    private String auditRecordsForApplication;
    
    private String auditRecordsForUserAndType;
    
    private String auditRecordsForUserAndApplication;
    
    private String auditRecordsForTypeAndApplication;
    
    private String auditRecordsForTypeAndUserAndApplication;

    private AuditRecordCollectionRepresentation auditRecords;

    public String getAuditRecordsForType() {
        return auditRecordsForType;
    }

    public void setAuditRecordsForType(String auditRecordsForType) {
        this.auditRecordsForType = auditRecordsForType;
    }

    public String getAuditRecordsForUser() {
        return auditRecordsForUser;
    }

    public void setAuditRecordsForUser(String auditRecordsForUser) {
        this.auditRecordsForUser = auditRecordsForUser;
    }

    public String getAuditRecordsForApplication() {
        return auditRecordsForApplication;
    }

    public void setAuditRecordsForApplication(String auditRecordsForApplication) {
        this.auditRecordsForApplication = auditRecordsForApplication;
    }

    public AuditRecordCollectionRepresentation getAuditRecords() {
        return auditRecords;
    }

    public void setAuditRecords(AuditRecordCollectionRepresentation auditRecords) {
        this.auditRecords = auditRecords;
    }

    public String getAuditRecordsForUserAndType() {
        return auditRecordsForUserAndType;
    }

    public void setAuditRecordsForUserAndType(String auditRecordsForUserAndType) {
        this.auditRecordsForUserAndType = auditRecordsForUserAndType;
    }

    public String getAuditRecordsForUserAndApplication() {
        return auditRecordsForUserAndApplication;
    }

    public void setAuditRecordsForUserAndApplication(String auditRecordsForUserAndApplication) {
        this.auditRecordsForUserAndApplication = auditRecordsForUserAndApplication;
    }

    public String getAuditRecordsForTypeAndApplication() {
        return auditRecordsForTypeAndApplication;
    }

    public void setAuditRecordsForTypeAndApplication(String auditRecordsForTypeAndApplication) {
        this.auditRecordsForTypeAndApplication = auditRecordsForTypeAndApplication;
    }

    public String getAuditRecordsForTypeAndUserAndApplication() {
        return auditRecordsForTypeAndUserAndApplication;
    }

    public void setAuditRecordsForTypeAndUserAndApplication(String auditRecordsForTypeAndUserAndApplication) {
        this.auditRecordsForTypeAndUserAndApplication = auditRecordsForTypeAndUserAndApplication;
    }

}
