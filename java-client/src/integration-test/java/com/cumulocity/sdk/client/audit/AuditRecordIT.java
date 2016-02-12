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
package com.cumulocity.sdk.client.audit;

import static com.cumulocity.model.util.DateTimeUtils.nowLocal;
import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.rest.representation.builder.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.rest.representation.audit.AuditRecordRepresentation;
import com.cumulocity.rest.representation.builder.ManagedObjectRepresentationBuilder;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.common.SystemPropertiesOverrider;
import com.cumulocity.sdk.client.common.TenantCreator;
import com.cumulocity.sdk.client.inventory.InventoryIT;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

//TODO speed up execution time by creating tenant and alarms only once in @BeforeClass
public class AuditRecordIT {

    private static List<ManagedObjectRepresentation> managedObjects = new ArrayList<ManagedObjectRepresentation>();

    private static TenantCreator tenantCreator;
    protected static PlatformImpl platform;

    @BeforeClass
    public static void createTenantWithApplication() throws Exception {
        platform = createPlatform();

    }

    private static PlatformImpl createPlatform() throws IOException {
        Properties cumulocityProps = new Properties();
        cumulocityProps.load(InventoryIT.class.getClassLoader().getResourceAsStream("cumulocity-test.properties"));

        SystemPropertiesOverrider p = new SystemPropertiesOverrider(cumulocityProps);
        return new PlatformImpl(
                p.get("cumulocity.host"),
               new CumulocityCredentials(p.get("cumulocity.tenant"),
                p.get("cumulocity.user"),
                p.get("cumulocity.password"),
                null),
                5);
    }

    @Before
    public void setup() throws Exception {
        tenantCreator = new TenantCreator(platform);
        tenantCreator.createTenant();

        auditRecordsApi = platform.getAuditRecordApi();
        status = OK;
        input = new ArrayList<AuditRecordRepresentation>();
        result1 = new ArrayList<AuditRecordRepresentation>();
        result2 = new ArrayList<AuditRecordRepresentation>();

//    Given I have '3' managed objects
//    And I create all
        for (int i = 0; i < 3; ++i) {
            ManagedObjectRepresentation mo = platform.getInventoryApi().create(aSampleMo().withName("MO" + i).build());
            managedObjects.add(mo);
        }
    }

    @After
    public void removeTenantAndApplication() throws Exception {
        tenantCreator.removeTenant();
    }

    private static ManagedObjectRepresentationBuilder aSampleMo() {
        return anMoRepresentationLike(MO_REPRESENTATION);
    }

    //    Scenario: Create and get audit records
    @Test
    public void createAndGetAuditRecords() throws Exception {
//    Given I have '3' audit records of type 'com.type1' and application 'app1' and user 'user1' for the managed object
        iHaveAuditRecord(3, "com.type1", "app1", "user1");
//    When I create all audit records
        iCreateAll();
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I get all audit records
        iGetAllAuditRecords();
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should I get all the audit records
        shouldGetAllMeasurements();
    }

//
//
//    Scenario: Create and get audit record

    @Test
    public void createAndGetAuditRecord() throws Exception {
//    Given I have '1' audit records of type 'com.type1' and application 'app1' and user 'user1' for the managed object
        iHaveAuditRecord(1, "com.type1", "app1", "user1");
//    When I create all audit records
        iCreateAll();
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I get the audit record with the created id
        iGetWithCreatedId();
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get the audit record
        shouldGetTheMeasurement();
    }

//    Scenario: Query by user

    @Test
    public void queryByUser() throws Exception {
//    Given I have '1' audit records of type 'com.type1' and application 'app1' and user 'user1' for the managed object
        iHaveAuditRecord(1, "com.type1", "app1", "user1");
//    Given I have '3' audit records of type 'com.type1' and application 'app1' and user 'user2' for the managed object
        iHaveAuditRecord(3, "com.type1", "app1", "user2");
//    When I create all audit records
        iCreateAll();
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I query the audit record collection by user 'user2'
        iQueryByUser("user2");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '3' audit records
        iShouldGetNumberOfMeasurements(3);
//    And I query the audit record collection by user 'user1'
        iQueryByUser("user1");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '1' audit records
        iShouldGetNumberOfMeasurements(1);
//    And I query the audit record collection by user 'user3'
        iQueryByUser("user3");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '0' audit records
        iShouldGetNumberOfMeasurements(0);
    }

//
//    Scenario: Query by type

    @Test
    public void queryByType() throws Exception {
//    Given I have '1' audit records of type 'com.type1' and application 'app1' and user 'user1' for the managed object
        iHaveAuditRecord(1, "com.type1", "app1", "user1");
//    Given I have '3' audit records of type 'com.type2' and application 'app1' and user 'user1' for the managed object
        iHaveAuditRecord(3, "com.type2", "app1", "user1");
//    When I create all audit records
        iCreateAll();
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I query the audit record collection by type 'com.type2'
        iQueryByType("com.type2");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '3' audit records
        iShouldGetNumberOfMeasurements(3);
//    And I query the audit record collection by type 'com.type1'
        iQueryByType("com.type1");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '1' audit records
        iShouldGetNumberOfMeasurements(1);
//    And I query the audit record collection by type 'com.type3'
        iQueryByType("com.type3");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '0' audit records
        iShouldGetNumberOfMeasurements(0);
    }

//
//
//    Scenario: Query by application

    @Test
    public void queryByApplication() throws Exception {
//    Given I have '1' audit records of type 'com.type1' and application 'app1' and user 'user1' for the managed object
        iHaveAuditRecord(1, "com.type1", "app1", "user1");
//    Given I have '3' audit records of type 'com.type1' and application 'app2' and user 'user1' for the managed object
        iHaveAuditRecord(3, "com.type1", "app2", "user1");
//    When I create all audit records
        iCreateAll();
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I query the audit record collection by application 'app2'
        iQueryByApp("app2");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '3' audit records
        iShouldGetNumberOfMeasurements(3);
//    And I query the audit record collection by application 'app1'
        iQueryByApp("app1");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '1' audit records
        iShouldGetNumberOfMeasurements(1);
//    And I query the audit record collection by application 'app3'
        iQueryByApp("app3");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '0' audit records
        iShouldGetNumberOfMeasurements(0);
    }

//
//    Scenario: Query by user and type

    @Test
    public void queryByUserAndType() throws Exception {
//    Given I have '1' audit records of type 'com.type1' and application 'app1' and user 'user1' for the managed object
        iHaveAuditRecord(1, "com.type1", "app1", "user1");
//    Given I have '3' audit records of type 'com.type1' and application 'app1' and user 'user2' for the managed object
        iHaveAuditRecord(3, "com.type1", "app1", "user2");
//    When I create all audit records
        iCreateAll();
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I query the audit record collection by user 'user2' and type 'com.type1'
        iQueryByUserAndType("user2", "com.type1");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '3' audit records
        iShouldGetNumberOfMeasurements(3);
//    And I query the audit record collection by user 'user1' and type 'com.type1'
        iQueryByUserAndType("user1", "com.type1");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '1' audit records
        iShouldGetNumberOfMeasurements(1);
//    And I query the audit record collection by user 'user3' and type 'com.type1'
        iQueryByUserAndType("user3", "com.type1");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '0' audit records
        iShouldGetNumberOfMeasurements(0);
    }

//
//    Scenario: Query by user and application

    @Test
    public void queryByUserAndApplication() throws Exception {
//    Given I have '1' audit records of type 'com.type1' and application 'app1' and user 'user1' for the managed object
        iHaveAuditRecord(1, "com.type1", "app1", "user1");
//    Given I have '3' audit records of type 'com.type1' and application 'app1' and user 'user2' for the managed object
        iHaveAuditRecord(3, "com.type1", "app1", "user2");
//    When I create all audit records
        iCreateAll();
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I query the audit record collection by user 'user2' and application 'app1'
        iQueryByUserAndApp("user2", "app1");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '3' audit records
        iShouldGetNumberOfMeasurements(3);
//    And I query the audit record collection by user 'user1' and application 'app1'
        iQueryByUserAndApp("user1", "app1");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '1' audit records
        iShouldGetNumberOfMeasurements(1);
//    And I query the audit record collection by user 'user3' and application 'app1'
        iQueryByUserAndApp("user3", "app1");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '0' audit records
        iShouldGetNumberOfMeasurements(0);
    }

//
//
//    Scenario: Query by user, application and type

    @Test
    public void queryByUserApplicationAndType() throws Exception {
//    Given I have '1' audit records of type 'com.type1' and application 'app1' and user 'user1' for the managed object
        iHaveAuditRecord(1, "com.type1", "app1", "user1");
//    Given I have '3' audit records of type 'com.type1' and application 'app1' and user 'user2' for the managed object
        iHaveAuditRecord(3, "com.type1", "app1", "user2");
//    When I create all audit records
        iCreateAll();
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I query the audit record collection by user 'user2' and application 'app1' and type 'com.type1'
        iQueryByUserAndAppAndType("user2", "app1", "com.type1");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '3' audit records
        iShouldGetNumberOfMeasurements(3);
//    And I query the audit record collection by user 'user1' and application 'app1' and type 'com.type1'
        iQueryByUserAndAppAndType("user1", "app1", "com.type1");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '1' audit records
        iShouldGetNumberOfMeasurements(1);
//    And I query the audit record collection by user 'user3' and application 'app1' and type 'com.type1'
        iQueryByUserAndAppAndType("user3", "app1", "com.type1");
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '0' audit records
        iShouldGetNumberOfMeasurements(0);
    }

//
//    Scenario: Query to test the paging with user

    @Test
    public void queryToTestThePagingWithUser() throws Exception {
//    Given I have '10' audit records of type 'com.type1' and application 'app1' and user 'user1' for the managed object
        iHaveAuditRecord(10, "com.type1", "app1", "user1");
//    Given I have '10' audit records of type 'com.type1' and application 'app1' and user 'user2' for the managed object
        iHaveAuditRecord(10, "com.type1", "app1", "user2");
//    When I create all audit records
        iCreateAll();
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I query the audit record collection by user 'user2'
        iQueryByUser("user2");
//    Then Audit record response status should be success
        shouldBeSuccess();
    }

//
//    Scenario: Query to test the paging to get all AuditRecords

    @Test
    public void queryToTestThePagingToGetAllAuditRecords() throws Exception {
//    Given I have '10' audit records of type 'com.type1' and application 'app1' and user 'user1' for the managed object
        iHaveAuditRecord(10, "com.type1", "app1", "user1");
//    Given I have '10' audit records of type 'com.type1' and application 'app1' and user 'user2' for the managed object
        iHaveAuditRecord(10, "com.type1", "app1", "user2");
//    When I create all audit records
        iCreateAll();
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I get all audit records
        iGetAllAuditRecords();
//    Then Audit record response status should be success
        shouldBeSuccess();
//    And I should get '4' pages of audit records
        iShouldGetNPages(4);
//    Then I should getNextPage
        iShouldGetNextPages();
//    And I should get current page as '2'
        shouldBeCurrentPage(2);
//    Then I should getPreviousPage
        iShouldGetPreviousPages();
//    And I should get current page as '1'
        shouldBeCurrentPage(1);
    }

//

    private static final int OK = 200;

    private List<AuditRecordRepresentation> input;

    private List<AuditRecordRepresentation> result1;

    private List<AuditRecordRepresentation> result2;

    private AuditRecordCollectionRepresentation collection1;

    private AuditRecordApi auditRecordsApi;

    private int status;

    // ------------------------------------------------------------------------
    // Given
    // ------------------------------------------------------------------------

    @Given("I have '(\\d+)' audit records of type '([^']*)' and application '([^']*)' and user '([^']*)' for the managed object$")
    public void iHaveAuditRecord(int n, String type, String application, String user) {
        for (int i = 0; i < n; i++) {
            AuditRecordRepresentation rep = new AuditRecordRepresentation();
            rep.setType(type);
            rep.setTime(nowLocal());
            rep.setSource(managedObjects.get(0));
            rep.setActivity("Some Activity");
            rep.setApplication(application);
            rep.setUser(user);
            rep.setSeverity("major");
            rep.setText("text");
            input.add(rep);
        }
    }

    // ------------------------------------------------------------------------
    // When
    // ------------------------------------------------------------------------

    @When("I create all audit records$")
    public void iCreateAll() throws SDKException {
        try {
            for (AuditRecordRepresentation rep : input) {
                result1.add(auditRecordsApi.create(rep));
            }
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

    @When("I get all audit records$")
    public void iGetAllAuditRecords() throws SDKException {
        try {
            collection1 = auditRecordsApi.getAuditRecords().get();
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

    @When("I get the audit record with the created id$")
    public void iGetWithCreatedId() throws SDKException {
        try {
            AuditRecordRepresentation auditRecordRepresentation = auditRecordsApi.getAuditRecord(result1.get(0).getId());
            assertThat(auditRecordRepresentation, is(notNullValue()));
            result2.add(auditRecordRepresentation);
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

    @When("I query the audit record collection by user '([^']*)'$")
    public void iQueryByUser(String user) throws SDKException {
        try {
            AuditRecordFilter filter = new AuditRecordFilter().byUser(user);
            collection1 = auditRecordsApi.getAuditRecordsByFilter(filter).get();
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

    @When("I query the audit record collection by type '([^']*)'$")
    public void iQueryByType(String type) throws SDKException {
        try {
            AuditRecordFilter filter = new AuditRecordFilter().byType(type);
            collection1 = auditRecordsApi.getAuditRecordsByFilter(filter).get();
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

    @When("I query the audit record collection by application '([^']*)'$")
    public void iQueryByApp(String app) throws SDKException {
        try {
            AuditRecordFilter filter = new AuditRecordFilter().byApplication(app);
            collection1 = auditRecordsApi.getAuditRecordsByFilter(filter).get();
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

    @When("I query the audit record collection by user '([^']*)' and type '([^']*)'$")
    public void iQueryByUserAndType(String user, String type) throws SDKException {
        try {
            AuditRecordFilter filter = new AuditRecordFilter().byUser(user).byType(type);
            collection1 = auditRecordsApi.getAuditRecordsByFilter(filter).get();
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

    @When("I query the audit record collection by user '([^']*)' and application '([^']*)'$")
    public void iQueryByUserAndApp(String user, String application) throws SDKException {
        try {
            AuditRecordFilter filter = new AuditRecordFilter().byUser(user).byApplication(application);
            collection1 = auditRecordsApi.getAuditRecordsByFilter(filter).get();
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

    @When("I query the audit record collection by user '([^']*)' and application '([^']*)' and type '([^']*)'$")
    public void iQueryByUserAndAppAndType(String user, String application, String type) throws SDKException {
        try {
            AuditRecordFilter filter = new AuditRecordFilter().byUser(user).byType(type).byApplication(application);
            collection1 = auditRecordsApi.getAuditRecordsByFilter(filter).get();
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

    @When("I query the audit record collection by type '([^']*)' and application '([^']*)'$")
    public void iQueryByTypeAndApp(String type, String application) throws SDKException {
        try {
            AuditRecordFilter filter = new AuditRecordFilter().byType(type).byApplication(application);
            collection1 = auditRecordsApi.getAuditRecordsByFilter(filter).get();
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

    // ------------------------------------------------------------------------
    // Then
    // ------------------------------------------------------------------------

    @Then("Audit record response status should be success$")
    public void shouldBeSuccess() {
        assertThat(status, is(lessThan(300)));
    }

    @Then("I should get the audit record$")
    public void shouldGetTheMeasurement() {
        assertThat(result1.get(0).getId(), is(equalTo(result2.get(0).getId())));
    }

    @Then("I should get '(\\d+)' audit records$")
    public void iShouldGetNumberOfMeasurements(int count) {
        assertThat(collection1.getAuditRecords().size(), is(equalTo(count)));
    }

    @Then("I should I get all the audit records$")
    public void shouldGetAllMeasurements() {
        assertThat(collection1.getAuditRecords().size(), is(equalTo(result1.size())));

        Map<GId, AuditRecordRepresentation> map = new HashMap<GId, AuditRecordRepresentation>();

        for (AuditRecordRepresentation rep : result1) {
            map.put(rep.getId(), rep);
        }

        for (AuditRecordRepresentation rep : collection1.getAuditRecords()) {
            AuditRecordRepresentation orig = map.get(rep.getId());
            assertThat(orig, is(notNullValue()));
        }
    }

    @Then("I should get '(\\d+)' pages of audit records$")
    public void iShouldGetNPages(int count) {
        assertThat(collection1.getPageStatistics().getTotalPages(), is(equalTo(count)));
    }

    @Then("I should getNextPage$")
    public void iShouldGetNextPages() throws SDKException {
        collection1 = auditRecordsApi.getAuditRecords().getNextPage(collection1);
    }

    @Then("I should get current page as '(\\d+)'$")
    public void shouldBeCurrentPage(int pageNum) throws SDKException {
        assertThat(collection1.getPageStatistics().getCurrentPage(), is(equalTo(pageNum)));
    }

    @Then("I should getPreviousPage$")
    public void iShouldGetPreviousPages() throws SDKException {
        collection1 = auditRecordsApi.getAuditRecords().getPreviousPage(collection1);
    }

}

