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
