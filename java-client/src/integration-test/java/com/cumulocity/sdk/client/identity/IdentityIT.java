package com.cumulocity.sdk.client.identity;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.idtype.XtId;
import com.cumulocity.rest.representation.identity.ExternalIDCollectionRepresentation;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.common.JavaSdkITBase;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

//TODO inline step definitions (see AlarmIT or InventoryIT)
public class IdentityIT extends JavaSdkITBase {

    private IdentityApi identity;

    @Before
    public void setup() {
        identity = platform.getIdentityApi();
        input = new ArrayList<ExternalIDRepresentation>();
        result1 = new ArrayList<ExternalIDRepresentation>();
        notFound = false;
    }

    @After
    public void tearDown() throws SDKException {
        for (ExternalIDRepresentation e : input) {
            try {
                identity.deleteExternalId(e);
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

    private List<ExternalIDRepresentation> input;

    private List<ExternalIDRepresentation> result1;

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
    public void iHaveManagedObject(long globalId, List<Row> rows) {
        ManagedObjectRepresentation mo = new ManagedObjectRepresentation();
        GId gId = new GId();
        gId.setValue(Long.toString(globalId));
        mo.setId(gId);

        for (Row row : rows) {
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
        result1.add(identity.create(input.get(0)));
    }

    @When("I create all external ids")
    public void iCreateAll() throws SDKException {
        for (ExternalIDRepresentation rep : input) {
            result1.add(identity.create(rep));
        }
    }

    @When("I get the external id")
    public void iGetTheExternalId() throws SDKException {
        result1.clear();
        try {
            XtId id = new XtId(input.get(0).getExternalId());
            id.setType(input.get(0).getType());
            result1.add(identity.getExternalId(id));
        } catch (SDKException e) {
            notFound = (NOT_FOUND == e.getHttpStatus());
        }
    }

    @When("I delete the external id")
    public void iDeleteTheExternalId() throws SDKException {
        ExternalIDRepresentation extIdRep = new ExternalIDRepresentation();
        extIdRep.setExternalId(input.get(0).getExternalId());
        extIdRep.setType(input.get(0).getType());
        identity.deleteExternalId(extIdRep);
    }

    // ------------------------------------------------------------------------
    // Then
    // ------------------------------------------------------------------------

    @Then("I should get back the external id")
    public void shouldGetBackTheExternalId() throws SDKException {
        collection1 = identity.getExternalIdsOfGlobalId(input.get(0).getManagedObject().getId()).get();
        assertEquals(1, collection1.getExternalIds().size());
        assertEquals(input.get(0).getExternalId(), collection1.getExternalIds().get(0).getExternalId());
        assertEquals(input.get(0).getType(), collection1.getExternalIds().get(0).getType());
        assertEquals(input.get(0).getManagedObject().getId().getValue(), collection1.getExternalIds().get(0).getManagedObject().getId()
                .getValue());
    }

    @Then("I should get back all the external ids")
    public void shouldGetBackAllTheIds() throws SDKException {
        collection1 = identity.getExternalIdsOfGlobalId(input.get(0).getManagedObject().getId()).get();
        assertEquals(input.size(), collection1.getExternalIds().size());

        Map<String, ExternalIDRepresentation> result = new HashMap<String, ExternalIDRepresentation>();

        for (int index = 0; index < input.size(); index++) {
            result.put(collection1.getExternalIds().get(index).getExternalId(), collection1.getExternalIds().get(index));
        }

        for (int index = 0; index < input.size(); index++) {
            ExternalIDRepresentation rep = result.get(input.get(index).getExternalId());
            assertNotNull(rep);
            assertEquals(input.get(index).getType(), rep.getType());
            assertEquals(input.get(index).getManagedObject().getId().getValue(), rep.getManagedObject().getId().getValue());
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
