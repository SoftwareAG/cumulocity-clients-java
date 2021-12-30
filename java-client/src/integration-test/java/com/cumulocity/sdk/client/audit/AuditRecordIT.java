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
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import org.joda.time.DateTime;
import org.junit.jupiter.api.*;

import java.util.*;

import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.rest.representation.builder.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AuditRecordIT extends JavaSdkITBase{

    private static final List<ManagedObjectRepresentation> managedObjects = new ArrayList<>();

    private static final int OK = 200;

    private List<AuditRecordRepresentation> input;
    private List<AuditRecordRepresentation> result1;
    private List<AuditRecordRepresentation> result2;
    private AuditRecordCollectionRepresentation collection1;
    private AuditRecordApi auditRecordsApi;

    private int status;

    @BeforeAll
    public static void createManagedObjects() {
        for (int i = 0; i < 3; ++i) {
            ManagedObjectRepresentation mo = platform.getInventoryApi().create(aSampleMo().withName("MO" + i).build());
            managedObjects.add(mo);
        }
    }

    @BeforeEach
    public void setup() throws Exception {
        auditRecordsApi = platform.getAuditRecordApi();
        status = OK;
        input = new ArrayList<>();
        result1 = new ArrayList<>();
        result2 = new ArrayList<>();
    }

    @Test
    public void createAndGetAuditRecords() {
        // given
        iHaveAuditRecord(3, "com.type0", "app0", "user0");
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
        iHaveAuditRecord(1, "com.type2", "app2", "user2");
        iHaveAuditRecord(3, "com.type2", "app2", "user3");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        // when
        iQueryByUser("user3");
        // then
        shouldBeSuccess();
        // when
        iShouldGetNumberOfAudits(3);
        iQueryByUser("user2");
        // then
        shouldBeSuccess();
        // when
        iShouldGetNumberOfAudits(1);
        iQueryByUser("user4");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(0);
    }

    @Test
    public void queryByType() {
        // given
        iHaveAuditRecord(1, "com.type5", "app5", "user5");
        iHaveAuditRecord(3, "com.type6", "app5", "user5");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        // when
        iQueryByType("com.type6");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(3);
        // when
        iQueryByType("com.type5");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(1);
        // when
        iQueryByType("com.type7");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(0);
    }

    @Test
    public void queryByApplication() {
        // given
        iHaveAuditRecord(1, "com.type8", "app8", "user8");
        iHaveAuditRecord(3, "com.type8", "app9", "user8");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        // when
        iQueryByApp("app9");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(3);
        // when
        iQueryByApp("app8");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(1);
        // when
        iQueryByApp("app10");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(0);
    }

    @Test
    public void queryByUserAndType() {
        // given
        iHaveAuditRecord(1, "com.type11", "app11", "user11");
        iHaveAuditRecord(3, "com.type11", "app11", "user12");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        // when
        iQueryByUserAndType("user12", "com.type11");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(3);
        // when
        iQueryByUserAndType("user11", "com.type11");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(1);
        // when
        iQueryByUserAndType("user13", "com.type11");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(0);
    }

    @Test
    public void queryByUserAndApplication() {
        // given
        iHaveAuditRecord(1, "com.type14", "app14", "user14");
        iHaveAuditRecord(3, "com.type14", "app14", "user15");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        // when
        iQueryByUserAndApp("user15", "app14");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(3);
        // when
        iQueryByUserAndApp("user14", "app14");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(1);
        // when
        iQueryByUserAndApp("user16", "app14");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(0);
    }

    @Test
    public void queryByUserApplicationAndType() {
        // given
        iHaveAuditRecord(1, "com.type17", "app17", "user17");
        iHaveAuditRecord(3, "com.type17", "app17", "user18");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        iQueryByUserAndAppAndType("user18", "app17", "com.type17");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(3);
        // when
        iQueryByUserAndAppAndType("user17", "app17", "com.type17");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(1);
        // when
        iQueryByUserAndAppAndType("user19", "app17", "com.type17");
        // then
        shouldBeSuccess();
        iShouldGetNumberOfAudits(0);
    }

    @Test
    public void queryToTestThePagingWithUser() {
        // given
        iHaveAuditRecord(10, "com.type20", "app20", "user20");
        iHaveAuditRecord(10, "com.type20", "app20", "user21");
        // when
        iCreateAll();
        // then
        shouldBeSuccess();
        // when
        iQueryByUser("user21");
        // then
        shouldBeSuccess();
    }

    @Test
    public void queryToTestThePagingToGetAllAuditRecords() {
        // given
        iHaveAuditRecord(10, "com.type22", "app22", "user22");
        iHaveAuditRecord(10, "com.type22", "app22", "user23");
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
