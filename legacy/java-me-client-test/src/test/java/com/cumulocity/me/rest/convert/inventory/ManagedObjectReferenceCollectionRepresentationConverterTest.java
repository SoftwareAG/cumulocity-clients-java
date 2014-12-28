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
import static com.cumulocity.me.rest.convert.inventory.ManagedObjectReferenceCollectionRepresentationConverter.PROP_REFERENCES;
import static com.cumulocity.me.rest.convert.inventory.ManagedObjectReferenceRepresentationConverter.PROP_MANAGED_OBJECT;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.rest.convert.JSONObjectBuilder;
import com.cumulocity.me.rest.convert.JSONObjectMatcher;
import com.cumulocity.me.rest.convert.base.BaseRepresentationConverter;
import com.cumulocity.me.rest.json.JSONArray;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceCollectionRepresentation;

public class ManagedObjectReferenceCollectionRepresentationConverterTest extends BaseManagedObjectConverterTestCase {

    ManagedObjectReferenceCollectionRepresentationConverter converter = new ManagedObjectReferenceCollectionRepresentationConverter();
    
    @Test
    public void shouldConvertToEmptyReferencesJson() throws Exception {
        JSONObject expectedJSON = aJSONObject().withProperty(PROP_REFERENCES, aJSONArray().build()).build();

        JSONObject outputJSON = toJson(new ManagedObjectReferenceCollectionRepresentation());
        
        assertThat(outputJSON.toString()).isEqualTo(expectedJSON.toString());
    }
    
    @Test
    public void shouldConvertToJson() throws Exception {
        ManagedObjectReferenceCollectionRepresentation representation = new ManagedObjectReferenceCollectionRepresentation();
        representation.setReferences(getListOfReferences());
        JSONObject expectedJSON = aJSONObject().withProperty(PROP_REFERENCES, getJsonArrayReferenceRepresentation()).build();
        
        JSONObject outputJSON = toJson(representation);
        
        assertThat(outputJSON.toString()).isEqualTo(expectedJSON.toString());
    }
    
    @Test
    public void shouldConvertFromJson() throws Exception {
        JSONObject json = aJSONObject().withProperty(PROP_REFERENCES, getJsonArrayReferenceRepresentation()).build();
        
        ManagedObjectReferenceCollectionRepresentation representation = fromJson(json);

        assertThat(representation.getReferences().size()).isEqualTo(getListOfReferences().size());
    }

    private JSONArray getJsonArrayReferenceRepresentation() {
        JSONArray jsonArray = aJSONArray()
                .withPropertyBuilder(getMinimalJsonManagedObjectReferenceRepresentation())
                .withPropertyBuilder(getMinimalJsonManagedObjectReferenceRepresentation()).build();
        return jsonArray;
    }

    @Override
    public BaseRepresentationConverter getManagedObjectConverter() {
        return converter;
    }
    
    private List getListOfReferences() {
        List list = new ArrayList();
        list.add(getMinimalManagedObjectReferenceRepresentation());
        list.add(getMinimalManagedObjectReferenceRepresentation());
        return list;
    }
    
    protected JSONObjectBuilder getMinimalJsonManagedObjectReferenceRepresentation() {
        return aJSONObject().withProperty(PROP_MANAGED_OBJECT, minimalJsonManagedObjectRepresentation().build());
    }
}
