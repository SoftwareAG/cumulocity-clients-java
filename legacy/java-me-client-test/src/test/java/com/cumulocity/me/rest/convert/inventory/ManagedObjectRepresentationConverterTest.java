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

import static com.cumulocity.me.rest.convert.inventory.ManagedObjectRepresentationConverter.PROP_CHILD_ASSETS;
import static com.cumulocity.me.rest.convert.inventory.ManagedObjectRepresentationConverter.PROP_CHILD_DEVICES;
import static com.cumulocity.me.rest.convert.inventory.ManagedObjectRepresentationConverter.PROP_LAST_UPDATED;
import static com.cumulocity.me.rest.convert.inventory.ManagedObjectRepresentationConverter.PROP_PARENTS;
import static com.cumulocity.me.rest.validate.CommandBasedRepresentationValidationContext.CREATE;
import static com.cumulocity.me.rest.validate.CommandBasedRepresentationValidationContext.UPDATE;
import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;

import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.rest.convert.base.BaseRepresentationConverter;
import com.cumulocity.me.rest.json.JSONObject;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentationBuilder;
import com.cumulocity.me.rest.validate.RepresentationValidationException;
import com.cumulocity.me.util.DateUtils;

public class ManagedObjectRepresentationConverterTest extends BaseManagedObjectConverterTestCase {

    ManagedObjectRepresentationConverter converter = new ManagedObjectRepresentationConverter();

    Date lastUpdated = new Date();

    @Test
    public void shouldConvertMinimalMoFromJson() throws Exception {
        JSONObject json = minimalJsonManagedObjectRepresentation().build();

        ManagedObjectRepresentation representation = fromJson(json);

        assertThat(representation.getId()).isEqualTo(new GId(ID));
        assertThat(representation.getSelf()).isEqualTo(SELF);
    }

    @Test
    public void shouldConvertChildAssetsAndDevicesFromJson() throws Exception {
        JSONObject json = minimalJsonManagedObjectRepresentation()
                .withPropertyBuilder(PROP_CHILD_ASSETS, minimalJsonObjectRepresentation())
                .withPropertyBuilder(PROP_CHILD_DEVICES, minimalJsonObjectRepresentation())
                .withPropertyBuilder(PROP_PARENTS, minimalJsonObjectRepresentation()).build();

        ManagedObjectRepresentation representation = fromJson(json);

        assertThat(representation.getChildAssets()).isNotNull();
        assertThat(representation.getChildDevices()).isNotNull();
        assertThat(representation.getParents()).isNotNull();
    }

    @Test
    public void shouldConvertDateFromJson() throws Exception {
        String stringDate = DateUtils.format(lastUpdated);
        JSONObject json = minimalJsonManagedObjectRepresentation().withProperty(PROP_LAST_UPDATED, stringDate).build();

        ManagedObjectRepresentation representation = fromJson(json);

        assertThat(representation.getLastUpdated()).isEqualTo(lastUpdated);
    }

    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNullIdInCreateContext() throws Exception {
        ManagedObjectRepresentation representation = new ManagedObjectRepresentationBuilder().withID(getSampleGId()).build();

        isValid(representation, CREATE);
    }
    
    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNullDateInCreateContext() throws Exception {
        ManagedObjectRepresentation representation = new ManagedObjectRepresentationBuilder().withLastUpdated(lastUpdated).build();

        isValid(representation, CREATE);
    }
    
    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNullIdInUpdateContext() throws Exception {
        ManagedObjectRepresentation representation = new ManagedObjectRepresentationBuilder().withID(getSampleGId()).build();

        isValid(representation, UPDATE);
    }
    
    @Test(expected = RepresentationValidationException.class)
    public void shouldRequireNullDateInUpdateContext() throws Exception {
        ManagedObjectRepresentation representation = new ManagedObjectRepresentationBuilder().withLastUpdated(lastUpdated).build();

        isValid(representation, UPDATE);
    }

    @Test
    public void shouldConvertDateToJson() throws Exception {
        ManagedObjectRepresentation representation = minimalManagedObjectBuilder().withLastUpdated(lastUpdated).build();
        String stringDate = DateUtils.format(lastUpdated);
        JSONObject expectedJSON = minimalJsonManagedObjectRepresentation().withProperty(PROP_LAST_UPDATED, stringDate).build();

        JSONObject outputJSON = toJson(representation);
        System.out.println(outputJSON);
        System.out.println(expectedJSON);
        assertThat(outputJSON.toString()).isEqualTo(expectedJSON.toString());
    }

    @Test
    public void shouldConvertChildAssetsAndDevicesToJson() throws Exception {
        ManagedObjectRepresentation representation = minimalManagedObjectBuilder()
                .withChildAssets(getMinimalMORCR())
                .withChildDevices(getMinimalMORCR())
                .withParents(getMinimalMORCR())
                .build();
        JSONObject expectedJSON = minimalJsonManagedObjectRepresentation()
                .withPropertyBuilder(PROP_CHILD_ASSETS, minimalJsonObjectReferenceRepresentation())
                .withPropertyBuilder(PROP_CHILD_DEVICES, minimalJsonObjectReferenceRepresentation())
                .withPropertyBuilder(PROP_PARENTS, minimalJsonObjectReferenceRepresentation()).build();

        JSONObject outputJSON = toJson(representation);
        
        assertThat(outputJSON.toString()).isEqualTo(expectedJSON.toString());
    }

    @Override
    public BaseRepresentationConverter getManagedObjectConverter() {
        return converter;
    }
}
