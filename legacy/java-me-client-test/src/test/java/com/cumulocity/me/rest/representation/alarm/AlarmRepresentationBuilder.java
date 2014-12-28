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
package com.cumulocity.me.rest.representation.alarm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.cumulocity.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.nsn.cumulocity.model.builder.AbstractObjectBuilder;

public class AlarmRepresentationBuilder extends AbstractObjectBuilder<AlarmRepresentation> {

    private final Set<Object> dynamicProperties = new HashSet<Object>();

    public AlarmRepresentationBuilder withStatus(String value) {
        setFieldValue("status", value);
        return this;
    }

    public AlarmRepresentationBuilder withSeverity(String value) {
        setFieldValue("severity", value);
        return this;
    }

    public AlarmRepresentationBuilder withHistory(AuditRecordCollectionRepresentation value) {
        setFieldValue("history", value);
        return this;
    }

    public AlarmRepresentationBuilder withText(String value) {
        setFieldValue("text", value);
        return this;
    }

    public AlarmRepresentationBuilder withSource(ManagedObjectRepresentation value) {
        setFieldValue("managedObject", value);
        return this;
    }

    public AlarmRepresentationBuilder withType(String value) {
        setFieldValue("type", value);
        return this;
    }

    public AlarmRepresentationBuilder withTime(Date value) {
        setFieldValue("time", value);
        return this;
    }

    public AlarmRepresentationBuilder withCreationTime(Date value) {
        setFieldValue("creationTime", value);
        return this;
    }

    @Override
    protected AlarmRepresentation createDomainObject() {
        AlarmRepresentation alarm = new AlarmRepresentation();
        for (Object object : dynamicProperties) {
            alarm.set(object);
        }
        return alarm;
    }

    public AlarmRepresentationBuilder with(final Object object) {
        dynamicProperties.add(object);
        return this;
    }

}
