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

import com.cumulocity.me.rest.convert.base.BaseCollectionRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.BaseResourceRepresentation;
import com.cumulocity.me.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.me.rest.representation.audit.AuditRecordRepresentation;

public class AuditRecordCollectionRepresentationConverter extends BaseCollectionRepresentationConverter{

    private static final String PROP_AUDIT_RECORDS = "auditRecords";
    
    protected void instanceToJson(BaseResourceRepresentation representation, JSONObject json) {
        putList(json, PROP_AUDIT_RECORDS, $(representation).getAuditRecords());
    }

    protected void instanceFromJson(JSONObject json, BaseResourceRepresentation representation) {
        $(representation).setAuditRecords(getList(json, PROP_AUDIT_RECORDS, AuditRecordRepresentation.class));
    }

    protected Class supportedRepresentationType() {
        return AuditRecordCollectionRepresentation.class;
    }
    
    private AuditRecordCollectionRepresentation $(BaseResourceRepresentation representation) {
        return (AuditRecordCollectionRepresentation) representation;
    }

}
