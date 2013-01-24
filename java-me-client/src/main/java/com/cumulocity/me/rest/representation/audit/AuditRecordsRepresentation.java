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
