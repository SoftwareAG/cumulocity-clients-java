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
