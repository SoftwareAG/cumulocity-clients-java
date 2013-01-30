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