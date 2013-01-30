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

import com.cumulocity.me.lang.HashSet;
import com.cumulocity.me.lang.Iterator;
import com.cumulocity.me.lang.Set;
import com.nsn.cumulocity.model.builder.AbstractObjectBuilder;

public class AuditRecordRepresentationBuilder extends AbstractObjectBuilder<AuditRecordRepresentation> {

    private final Set dynamicProperties = new HashSet();

    public static final AuditRecordRepresentationBuilder aAuditRecordRepresentation() {
        return new AuditRecordRepresentationBuilder();
    }
    
    public AuditRecordRepresentationBuilder withUser(String user) {
        setObjectField("user", user);
        return this;
    }

    public AuditRecordRepresentationBuilder withApplication(String application) {
        setObjectField("application", application);
        return this;
    }

    public AuditRecordRepresentationBuilder withSeverity(String severity) {
        setObjectField("severity", severity);
        return this;
    }

    public AuditRecordRepresentationBuilder withActivity(String activity) {
        setObjectField("activity", activity);
        return this;
    }

    public AuditRecordRepresentationBuilder withChanges(HashSet changes) {
        setObjectField("changes", changes);
        return this;
    }

    @Override
    protected AuditRecordRepresentation createDomainObject() {
        AuditRecordRepresentation audit = new AuditRecordRepresentation();
        Iterator iterator = dynamicProperties.iterator();
        while(iterator.hasNext()) {
            audit.set(iterator.next());
        }
        return audit;
    }

    public AuditRecordRepresentationBuilder with(final Object object) {
        dynamicProperties.add(object);
        return this;
    }

}