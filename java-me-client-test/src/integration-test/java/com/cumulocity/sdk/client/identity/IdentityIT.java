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
package com.cumulocity.sdk.client.identity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.HashMap;
import com.cumulocity.me.lang.Iterator;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.lang.Map;
import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.model.idtype.XtId;
import com.cumulocity.me.rest.convert.energy.ThreePhaseElectricitySensorConverter;
import com.cumulocity.me.rest.convert.inventory.CoordinateConverter;
import com.cumulocity.me.rest.convert.inventory.SecondFragmentConverter;
import com.cumulocity.me.rest.representation.identity.ExternalIDCollectionRepresentation;
import com.cumulocity.me.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.identity.IdentityApi;
import com.cumulocity.sdk.client.common.JavaSdkITBase;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

//TODO inline step definitions (see AlarmIT or InventoryIT)
public class IdentityIT extends JavaSdkITBase {

    private IdentityApi identity;

    @BeforeClass
    public static void registerConverters() {
        SecondFragmentConverter fragmentConverter = new SecondFragmentConverter();
        platform.getConversionService().register(fragmentConverter);
        platform.getValidationService().register(fragmentConverter);
        
        CoordinateConverter coordinateConverter = new CoordinateConverter();
        platform.getConversionService().register(coordinateConverter);
        platform.getValidationService().register(coordinateConverter);
        
        ThreePhaseElectricitySensorConverter sensorConverter = new ThreePhaseElectricitySensorConverter();
        platform.getConversionService().register(sensorConverter);
        platform.getValidationService().register(sensorConverter);
    }
    
    @Before
    public void setup() {
        identity = platform.getIdentityApi();
        platform.setRequireResponseBody(true);
        input = new ArrayList();
        result1 = new ArrayList();
        notFound = false;
    }

    @After
    public void tearDown() throws SDKException {
        Iterator iterator = input.iterator();
        while (iterator.hasNext()) {
            try {
                identity.deleteExternalId((ExternalIDRepresentation) iterator.next());
            } catch (SDKException e1) {
                if (e1.getHttpStatus() != 404) {
                    throw e1;
                }
            }
        }
    }

    //    Scenario: Create one external id and get all for the global id
    @Test
    public void createExternalIdAndGetAllForTheGlobalId() throws Exception {
        //    Given I have external id for '100' with value 'DN-1' and type 'com.nsn.DN'
        iHaveManagedObject(100, "DN-1", "com.nsn.DN");
        //    When I create the external id
        iCallCreate();
        //    Then I should get back the external id
        shouldGetBackTheExternalId();
    }

    //
    //    Scenario: Create multiple external ids and get all for the global id

    @Test
    public void createMultipleExternalIdsAndGetAllForTheGlobalId() throws Exception {
        //    Given I have the global id '200' with following external ids:
        //            | type | value |
        //            | com.type1 | 1002 |
        //            | com.dn | DN-1 |
        iHaveManagedObject(200, "com.type1", "1002");
        iHaveManagedObject(200, "com.dn", "DN-1");
        //    When I create all external ids
        iCreateAll();
        //    Then I should get back all the external ids
        shouldGetBackAllTheIds();

    }

    //
    //    Scenario: Create one external id and get the external id

    @Test
    public void createOneExternalIdAndGetTheExternalId() throws Exception {
        //    Given I have external id for '100' with value 'DN-1' and type 'com.nsn.DN'
        iHaveManagedObject(100, "DN-1", "com.nsn.DN");
        //    When I create the external id
        iCallCreate();
        //    And I get the external id
        iGetTheExternalId();
        //    Then I should get back the external id
        shouldGetBackTheExternalId();
    }

    //
    //    Scenario: Create one external id, delete it and get the external id

    @Test
    public void createOneExternalIdAndDeleteIdAndGetTheExternalId() throws Exception {
        //    Given I have external id for '100' with value 'DN-1' and type 'com.nsn.DN'
        iHaveManagedObject(100, "DN-1", "com.nsn.DN");
        //    When I create the external id
        iCallCreate();
        //    And I delete the external id
        iDeleteTheExternalId();
        //    And I get the external id
        iGetTheExternalId();
        //    Then External id should not be found
        shouldNotBeFound();
    }

    private static final int NOT_FOUND = 404;

    private List input;

    private List result1;

    private ExternalIDCollectionRepresentation collection1;

    private boolean notFound;

    // ------------------------------------------------------------------------
    // Given
    // ------------------------------------------------------------------------

    @Given("I have external id for '(\\d+)' with value '([^']*)' and type '([^']*)'")
    public void iHaveManagedObject(long globalId, String extId, String type) {
        ExternalIDRepresentation rep = new ExternalIDRepresentation();
        rep.setExternalId(extId);
        rep.setType(type);
        ManagedObjectRepresentation mo = new ManagedObjectRepresentation();
        GId gId = new GId();
        gId.setValue(Long.toString(globalId));
        mo.setId(gId);
        rep.setManagedObject(mo);
        input.add(rep);
    }

    @Given("I have the global id '(\\d+)' with following external ids:")
    public void iHaveManagedObject(long globalId, List rows) {
        ManagedObjectRepresentation mo = new ManagedObjectRepresentation();
        GId gId = new GId();
        gId.setValue(Long.toString(globalId));
        mo.setId(gId);

        Iterator iterator = rows.iterator();

        while (iterator.hasNext()) {
            Row row = (Row) iterator.next();
            ExternalIDRepresentation rep = new ExternalIDRepresentation();
            rep.setExternalId(row.value);
            rep.setType(row.type);
            rep.setManagedObject(mo);
            input.add(rep);
        }
    }

    // ------------------------------------------------------------------------
    // When
    // ------------------------------------------------------------------------

    @When("I create the external id")
    public void iCallCreate() throws SDKException {
        result1.add(identity.create((ExternalIDRepresentation) input.get(0)));
    }

    @When("I create all external ids")
    public void iCreateAll() throws SDKException {
        Iterator iterator = input.iterator();
        while(iterator.hasNext()) {
            result1.add(identity.create((ExternalIDRepresentation) iterator.next()));
        }
    }

    @When("I get the external id")
    public void iGetTheExternalId() throws SDKException {
        result1 = new ArrayList();
        try {
            XtId id = new XtId(((ExternalIDRepresentation) input.get(0)).getExternalId());
            id.setType(((ExternalIDRepresentation) input.get(0)).getType());
            result1.add(identity.getExternalId(id));
        } catch (SDKException e) {
            notFound = (NOT_FOUND == e.getHttpStatus());
        }
    }

    @When("I delete the external id")
    public void iDeleteTheExternalId() throws SDKException {
        ExternalIDRepresentation extIdRep = new ExternalIDRepresentation();
        extIdRep.setExternalId(((ExternalIDRepresentation) input.get(0)).getExternalId());
        extIdRep.setType(((ExternalIDRepresentation) input.get(0)).getType());
        identity.deleteExternalId(extIdRep);
    }

    // ------------------------------------------------------------------------
    // Then
    // ------------------------------------------------------------------------

    @Then("I should get back the external id")
    public void shouldGetBackTheExternalId() throws SDKException {
        collection1 = (ExternalIDCollectionRepresentation) identity.getExternalIdsOfGlobalId(((ExternalIDRepresentation) input.get(0)).getManagedObject().getId()).get();
        assertEquals(1, collection1.getExternalIds().size());
        assertEquals(((ExternalIDRepresentation) input.get(0)).getExternalId(), ((ExternalIDRepresentation) collection1.getExternalIds().get(0)).getExternalId());
        assertEquals(((ExternalIDRepresentation) input.get(0)).getType(), ((ExternalIDRepresentation) collection1.getExternalIds().get(0)).getType());
        assertEquals(((ExternalIDRepresentation) input.get(0)).getManagedObject().getId().getValue(), ((ExternalIDRepresentation) collection1.getExternalIds().get(0)).getManagedObject().getId()
                .getValue());
    }

    @Then("I should get back all the external ids")
    public void shouldGetBackAllTheIds() throws SDKException {
        collection1 = (ExternalIDCollectionRepresentation) identity.getExternalIdsOfGlobalId(((ExternalIDRepresentation) input.get(0)).getManagedObject().getId()).get();
        assertEquals(input.size(), collection1.getExternalIds().size());

        Map result = new HashMap();

        for (int index = 0; index < input.size(); index++) {
            result.put(((ExternalIDRepresentation) collection1.getExternalIds().get(index)).getExternalId(), collection1.getExternalIds().get(index));
        }

        for (int index = 0; index < input.size(); index++) {
            ExternalIDRepresentation rep = (ExternalIDRepresentation) result.get(((ExternalIDRepresentation) input.get(index)).getExternalId());
            assertNotNull(rep);
            assertEquals(((ExternalIDRepresentation) input.get(index)).getType(), rep.getType());
            assertEquals(((ExternalIDRepresentation) input.get(index)).getManagedObject().getId().getValue(), rep.getManagedObject().getId().getValue());
        }

    }

    @Then("External id should not be found")
    public void shouldNotBeFound() {
        assertTrue(notFound);
    }

    class Row {
        String type;

        String value;
    }

}
