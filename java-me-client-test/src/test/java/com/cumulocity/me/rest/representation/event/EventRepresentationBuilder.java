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
package com.cumulocity.me.rest.representation.event;

import java.util.Date;

import com.cumulocity.me.rest.representation.BaseRepresentationBuilder;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;

public class EventRepresentationBuilder extends BaseRepresentationBuilder<EventRepresentation, EventRepresentationBuilder> {

    public static final EventRepresentationBuilder aEventRepresentation() {
        return new EventRepresentationBuilder();
    }
    
    public EventRepresentationBuilder withType(String type) {
        setFieldValue("type", type);
        return this;
    }
    
    public EventRepresentationBuilder withTime(Date time) {
        setFieldValue("time", time);
        return this;
    }
    
    public EventRepresentationBuilder withCreationTime(Date creationTime) {
        setFieldValue("creationTime", creationTime);
        return this;
    }
    
    public EventRepresentationBuilder withText(String text) {
        setFieldValue("text", text);
        return this;
    }
    
    public EventRepresentationBuilder withSource(ManagedObjectRepresentation source) {
        setFieldValue("managedObject", source);
        return this;
    }
    
    @Override
    protected EventRepresentation createDomainObject() {
        return new EventRepresentation();
    }
}
