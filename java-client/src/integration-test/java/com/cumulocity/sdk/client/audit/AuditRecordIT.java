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

import com.cumulocity.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.rest.representation.audit.AuditRecordRepresentation;
import com.cumulocity.rest.representation.builder.ManagedObjectRepresentationBuilder;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import com.cumulocity.sdk.client.common.TenantCreator;
import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.rest.representation.builder.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

//TODO speed up execution time by creating tenant and alarms only once in @BeforeAll
public class AuditRecordIT {

    private static List<ManagedObjectRepresentation> managedObjects = new ArrayList<ManagedObjectRepresentation>();

    private static TenantCreator tenantCreator;
    protected static PlatformImpl platform;

    private static final int OK = 200;

    private List<AuditRecordRepresentation> input;
    private List<AuditRecordRepresentation> result1;
    private List<AuditRecordRepresentation> result2;
    private AuditRecordCollectionRepresentation collection1;
    private AuditRecordApi auditRecordsApi;

    private int status;

    @BeforeAll
    public static void createTenantWithApplication() throws Exception {
        platform = JavaSdkITBase.createPlatform(false);
    }

    @BeforeEach
    public void setup() throws Exception {
        tenantCreator = new TenantCreator(platform);
        tenantCreator.createTenant();

        auditRecordsApi = platform.getAuditRecordApi();
        status = OK;
        input = new ArrayList<>();
        result1 = new ArrayList<>();
        result2 = new ArrayList<>();

        for (int i = 0; i < 3; ++i) {
            ManagedObjectRepresentation mo = platform.getInventoryApi().create(aSampleMo().withName("MO" + i).build());
            managedObjects.add(mo);
        }
    }

    @AfterEach
    public void removeTenantAndApplication() {
        tenantCreator.removeTenant();
    }

    @Test
    public void createAndGetAuditRecords() {
        // given
        iHaveAuditRecord(3, "com.type1", "app1", "user1");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        // when
        iGetAllAuditRecords();
        // then
        shouldBeSuccess();
        iShouldGetAtLeastNumberOfAudits(3);
    }

    @Test
    public void createAndGetAuditRecord() {
        // given
        iHaveAuditRecord(1, "com.type1", "app1", "user1");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        // when
        iGetWithCreatedId();
        // then
        shouldBeSuccess();
        shouldGetTheAuditRecord();
    }

    @Test
    public void queryByUser() {
        // given
        iHaveAuditRecord(1, "com.type1", "app1", "user1");
        iHaveAuditRecord(3, "com.type1", "app1", "user2");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        // when
        iQueryByUser("user2");
        // then
        shouldBeSuccess();
        // when
        iShouldGetNumberOfAudits(3);
        iQueryByUser("user1");
        // then
        shouldBeSuccess();
        // when
        iShouldGetNumberOfAudits(1);
        iQueryByUser("user3");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(0);
    }

    @Test
    public void queryByType() {
        // given
        iHaveAuditRecord(1, "com.type1", "app1", "user1");
        iHaveAuditRecord(3, "com.type2", "app1", "user1");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        // when
        iQueryByType("com.type2");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(3);
        // when
        iQueryByType("com.type1");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(1);
        // when
        iQueryByType("com.type3");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(0);
    }

    @Test
    public void queryByApplication() {
        // given
        iHaveAuditRecord(1, "com.type1", "app1", "user1");
        iHaveAuditRecord(3, "com.type1", "app2", "user1");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        // when
        iQueryByApp("app2");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(3);
        // when
        iQueryByApp("app1");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(1);
        // when
        iQueryByApp("app3");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(0);
    }

    @Test
    public void queryByUserAndType() {
        // given
        iHaveAuditRecord(1, "com.type1", "app1", "user1");
        iHaveAuditRecord(3, "com.type1", "app1", "user2");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        // when
        iQueryByUserAndType("user2", "com.type1");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(3);
        // when
        iQueryByUserAndType("user1", "com.type1");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(1);
        // when
        iQueryByUserAndType("user3", "com.type1");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(0);
    }

    @Test
    public void queryByUserAndApplication() throws Exception {
        // given
        iHaveAuditRecord(1, "com.type1", "app1", "user1");
        iHaveAuditRecord(3, "com.type1", "app1", "user2");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        // when
        iQueryByUserAndApp("user2", "app1");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(3);
        // when
        iQueryByUserAndApp("user1", "app1");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(1);
        // when
        iQueryByUserAndApp("user3", "app1");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(0);
    }

    @Test
    public void queryByUserApplicationAndType() {
        // given
        iHaveAuditRecord(1, "com.type1", "app1", "user1");
        iHaveAuditRecord(3, "com.type1", "app1", "user2");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        iQueryByUserAndAppAndType("user2", "app1", "com.type1");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(3);
        // when
        iQueryByUserAndAppAndType("user1", "app1", "com.type1");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(1);
        // when
        iQueryByUserAndAppAndType("user3", "app1", "com.type1");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(0);
    }

    @Test
    public void queryToTestThePagingWithUser() {
        // given
        iHaveAuditRecord(10, "com.type1", "app1", "user1");
        iHaveAuditRecord(10, "com.type1", "app1", "user2");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        // when
        iQueryByUser("user2");
        // then
        shouldBeSuccess();
    }

    @Test
    public void queryToTestThePagingToGetAllAuditRecords() {
        // given
        iHaveAuditRecord(10, "com.type1", "app1", "user1");
        iHaveAuditRecord(10, "com.type1", "app1", "user2");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        // when
        iGetAllAuditRecords();
        // then
        shouldBeSuccess();
    }

    // ------------------------------------------------------------------------
    // Given
    // ------------------------------------------------------------------------

    public void iHaveAuditRecord(int n, String type, String application, String user) {
        for (int i = 0; i < n; i++) {
            AuditRecordRepresentation rep = new AuditRecordRepresentation();
            rep.setType(type);
            rep.setDateTime(new DateTime());
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

    public void iGetAllAuditRecords() throws SDKException {
        try {
            collection1 = auditRecordsApi.getAuditRecords().get();
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

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

    public void iQueryByUser(String user) throws SDKException {
        try {
            AuditRecordFilter filter = new AuditRecordFilter().byUser(user);
            collection1 = auditRecordsApi.getAuditRecordsByFilter(filter).get();
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

    public void iQueryByType(String type) throws SDKException {
        try {
            AuditRecordFilter filter = new AuditRecordFilter().byType(type);
            collection1 = auditRecordsApi.getAuditRecordsByFilter(filter).get();
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

    public void iQueryByApp(String app) throws SDKException {
        try {
            AuditRecordFilter filter = new AuditRecordFilter().byApplication(app);
            collection1 = auditRecordsApi.getAuditRecordsByFilter(filter).get();
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

    public void iQueryByUserAndType(String user, String type) throws SDKException {
        try {
            AuditRecordFilter filter = new AuditRecordFilter().byUser(user).byType(type);
            collection1 = auditRecordsApi.getAuditRecordsByFilter(filter).get();
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

    public void iQueryByUserAndApp(String user, String application) throws SDKException {
        try {
            AuditRecordFilter filter = new AuditRecordFilter().byUser(user).byApplication(application);
            collection1 = auditRecordsApi.getAuditRecordsByFilter(filter).get();
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

    public void iQueryByUserAndAppAndType(String user, String application, String type) throws SDKException {
        try {
            AuditRecordFilter filter = new AuditRecordFilter().byUser(user).byType(type).byApplication(application);
            collection1 = auditRecordsApi.getAuditRecordsByFilter(filter).get();
        } catch (SDKException ex) {
            ex.printStackTrace();
            status = ex.getHttpStatus();
        }
    }

    // ------------------------------------------------------------------------
    // Then
    // ------------------------------------------------------------------------

    public void shouldBeSuccess() {
        assertThat(status, is(lessThan(300)));
    }

    public void shouldGetTheAuditRecord() {
        assertThat(result1.get(0).getId(), is(equalTo(result2.get(0).getId())));
    }

    public void iShouldGetNumberOfAudits(int count) {
        assertThat(collection1.getAuditRecords().size(), is(equalTo(count)));
    }

    public void iShouldGetAtLeastNumberOfAudits(int count) {
        assertThat(collection1.getAuditRecords().size(), is(greaterThanOrEqualTo(count)));
    }

    private static ManagedObjectRepresentationBuilder aSampleMo() {
        return anMoRepresentationLike(MO_REPRESENTATION);
    }

}

