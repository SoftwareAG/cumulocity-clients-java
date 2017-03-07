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
package com.cumulocity.me.rest.representation.inventory;

import java.util.Date;

import com.cumulocity.me.rest.representation.BaseRepresentationBuilder;
import com.cumulocity.model.builder.IDBuilder;

public class ManagedObjectRepresentationBuilder extends BaseRepresentationBuilder<ManagedObjectRepresentation, ManagedObjectRepresentationBuilder> {

    public ManagedObjectRepresentationBuilder withName(final String value) {
        setFieldValue("name", value);
        return this;
    }
    
    public ManagedObjectRepresentationBuilder withOwner(final String value) {
        setFieldValue("owner", value);
        return this;
    }

    public ManagedObjectRepresentationBuilder withID(final IDBuilder id) {
        setFieldValueBuilder("id", id);
        return this;
    }

    public ManagedObjectRepresentationBuilder withType(final String type) {
        setFieldValue("type", type);
        return this;
    }

    public ManagedObjectRepresentationBuilder withChildAssets(final ManagedObjectReferenceCollectionRepresentation childAssets) {
        setFieldValue("childAssets", childAssets);
        return this;
    }

    public ManagedObjectRepresentationBuilder withChildAdditions(final ManagedObjectReferenceCollectionRepresentation childAdditions) {
        setFieldValue("childAdditions", childAdditions);
        return this;
    }

    public ManagedObjectRepresentationBuilder withChildDevices(final ManagedObjectReferenceCollectionRepresentation childDevices) {
        setFieldValue("childDevices", childDevices);
        return this;
    }

    public ManagedObjectRepresentationBuilder withParents(final ManagedObjectReferenceCollectionRepresentation parents) {
        setFieldValue("parents", parents);
        return this;
    }

    public ManagedObjectRepresentationBuilder withLastUpdated(final Date lastUpdated) {
        setFieldValue("lastUpdated", lastUpdated);
        return this;
    }

    @Override
    protected ManagedObjectRepresentation createDomainObject() {
        return new ManagedObjectRepresentation();
    }
}
