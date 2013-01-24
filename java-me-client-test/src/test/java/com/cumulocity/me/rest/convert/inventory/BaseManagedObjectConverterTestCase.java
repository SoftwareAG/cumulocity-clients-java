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
package com.cumulocity.me.rest.convert.inventory;

import static com.cumulocity.me.rest.convert.JSONArrayBuilder.aJSONArray;
import static com.cumulocity.me.rest.convert.JSONObjectBuilder.aJSONObject;
import static com.cumulocity.me.rest.convert.base.BaseResourceRepresentationConverter.PROP_ID;
import static com.cumulocity.me.rest.convert.base.BaseResourceRepresentationConverter.PROP_SELF;
import static com.cumulocity.me.rest.convert.inventory.ManagedObjectReferenceCollectionRepresentationConverter.PROP_REFERENCES;
import static com.cumulocity.me.rest.convert.inventory.ManagedObjectRepresentationConverter.PROP_CHILD_ASSETS;
import static com.cumulocity.me.rest.convert.inventory.ManagedObjectRepresentationConverter.PROP_CHILD_DEVICES;
import static com.cumulocity.me.rest.convert.inventory.ManagedObjectRepresentationConverter.PROP_PARENTS;

import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.convert.BaseConverterTestCase;
import com.cumulocity.me.rest.convert.JSONObjectBuilder;
import com.cumulocity.me.rest.convert.base.BaseRepresentationConverter;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceRepresentationBuilder;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentationBuilder;

public abstract class BaseManagedObjectConverterTestCase extends BaseConverterTestCase {

    static final String ID = "1";
    protected static final String SELF = "http://localhost/me";

    public abstract BaseRepresentationConverter getManagedObjectConverter();
    
    @Override
    public BaseRepresentationConverter getConverter() {
        return getManagedObjectConverter();
    }

    protected GId getSampleGId() {
        GId gid = new GId();
        gid.setName("idname");
        gid.setValue(ID);
        return gid;
    }
    
    protected ManagedObjectReferenceRepresentation getMinimalManagedObjectReferenceRepresentation() {
        return new ManagedObjectReferenceRepresentationBuilder()
            .withMo(minimalManagedObjectBuilder().build()).build();
    }
    
    protected ManagedObjectReferenceRepresentation getSampleManagedObjectReferenceRepresentation() {
        return new ManagedObjectReferenceRepresentationBuilder()
            .withMo(getSampleManagedObjectRepresentation()).build();
    }
    
    protected ManagedObjectRepresentation getSampleManagedObjectRepresentation() {
        ManagedObjectRepresentation representation = minimalManagedObjectBuilder()
                .withSelf(SELF)
                .withID(getSampleGId())
                .withChildAssets(getMinimalMORCR())
                .withChildDevices(getMinimalMORCR())
                .withParents(getMinimalMORCR())
                .build();
        return representation;
    }

    protected ManagedObjectReferenceCollectionRepresentation getMinimalMORCR() {
        ManagedObjectReferenceCollectionRepresentation representation = new ManagedObjectReferenceCollectionRepresentation();
        representation.setSelf(SELF);
        return representation;
    }

    protected ManagedObjectRepresentationBuilder minimalManagedObjectBuilder() {
        return new ManagedObjectRepresentationBuilder().withID(getSampleGId()).withSelf(SELF);
    }

    protected JSONObjectBuilder minimalJsonObjectReferenceRepresentation() {
        return aJSONObject().withProperty(PROP_SELF, SELF).withPropertyBuilder(PROP_REFERENCES, aJSONArray());
    }
    
    protected JSONObjectBuilder minimalJsonObjectRepresentation() {
        return aJSONObject().withProperty(PROP_SELF, SELF);
    }
    
    protected JSONObjectBuilder minimalJsonGidRepresentation() {
        return aJSONObject().withProperty(PROP_ID, ID);
    }

    protected JSONObjectBuilder minimalJsonManagedObjectRepresentation() {
        //return aJSONObject().withPropertyBuilder(PROP_ID, minimalJsonGidRepresentation()).withProperty(PROP_SELF, SELF);
        return aJSONObject().withProperty(PROP_ID, ID).withProperty(PROP_SELF, SELF);
    }
    
    protected JSONObjectBuilder getSampleJsonManagedObjectRepresentation() {
        return minimalJsonManagedObjectRepresentation()
                .withPropertyBuilder(PROP_CHILD_ASSETS, minimalJsonObjectReferenceRepresentation())
                .withPropertyBuilder(PROP_CHILD_DEVICES, minimalJsonObjectReferenceRepresentation())
                .withPropertyBuilder(PROP_PARENTS, minimalJsonObjectReferenceRepresentation());
    }

}
