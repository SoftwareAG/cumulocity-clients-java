package com.cumulocity.sdk.client.audit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.audit.AuditMediaType;
import com.cumulocity.rest.representation.audit.AuditRecordCollectionRepresentation;
import com.cumulocity.rest.representation.audit.AuditRecordRepresentation;
import com.cumulocity.rest.representation.audit.AuditRecordsRepresentation;
import com.cumulocity.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.rest.representation.platform.PlatformMediaType;
import com.cumulocity.sdk.client.PagedCollectionResource;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.TemplateUrlParser;

public class AuditRecordApiImplTest {

    private static final String EXACT_URL = "exactUrl";

    private static final String TEMPLATE_URL = "template_url";

    private static final String AUDIT_RECORDS_URL = "audit_records_url";

    private static final String PLATFORM_API_URL = "path_to_platform_api";

    private static final int DEFAULT_PAGE_SIZE = 555;

    private AuditRecordApi auditRecordApi;

    private AuditRecordsRepresentation auditRecordsApiRepresentation;

    @Mock
    private RestConnector restConnector;

    @Mock
    private TemplateUrlParser templateUrlParser;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        auditRecordsApiRepresentation = new AuditRecordsRepresentation();
        AuditRecordCollectionRepresentation collectionRepresentation = new AuditRecordCollectionRepresentation();
        collectionRepresentation.setSelf(AUDIT_RECORDS_URL);
        auditRecordsApiRepresentation.setAuditRecords(collectionRepresentation);

        auditRecordApi = new AuditRecordApiImpl(restConnector, templateUrlParser, PLATFORM_API_URL, DEFAULT_PAGE_SIZE);

        when(restConnector.get(PLATFORM_API_URL, AuditMediaType.AUDIT_API, AuditRecordsRepresentation.class)).thenReturn(
                auditRecordsApiRepresentation);
        
        PlatformApiRepresentation platformApiRepresentation = new PlatformApiRepresentation();
        platformApiRepresentation.setAudit(auditRecordsApiRepresentation);
        when(restConnector.get(PLATFORM_API_URL, PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class)).thenReturn(
                platformApiRepresentation);
    }

    @Test
    public void shouldGetAuditRecord() throws SDKException {
        // Given 
        GId gid = new GId("123");
        AuditRecordRepresentation auditRep = new AuditRecordRepresentation();
        when(restConnector.get(AUDIT_RECORDS_URL + "/123", AuditMediaType.AUDIT_RECORD, AuditRecordRepresentation.class)).thenReturn(
                auditRep);

        // When
        AuditRecordRepresentation result = auditRecordApi.getAuditRecord(gid);

        //then
        assertThat(result, sameInstance(auditRep));
    }

    @Test
    public void shouldCreateAuditRecord() throws SDKException {
        // Given 
        AuditRecordRepresentation input = new AuditRecordRepresentation();
        AuditRecordRepresentation created = new AuditRecordRepresentation();
        when(restConnector.post(AUDIT_RECORDS_URL, AuditMediaType.AUDIT_RECORD, input)).thenReturn(created);

        // When
        AuditRecordRepresentation result = auditRecordApi.create(input);

        // Then
        assertThat(result, sameInstance(created));
    }

    @Test
    public void shouldReturnAuditRecords() throws Exception {
        // Given
        PagedCollectionResource<AuditRecordCollectionRepresentation> expected = new AuditRecordCollectionImpl(restConnector,
                AUDIT_RECORDS_URL, DEFAULT_PAGE_SIZE);

        // When
        PagedCollectionResource<AuditRecordCollectionRepresentation> result = auditRecordApi.getAuditRecords();

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnAuditRecordsByEmptyFilter() throws Exception {
        // Given
        PagedCollectionResource<AuditRecordCollectionRepresentation> expected = new AuditRecordCollectionImpl(restConnector,
                AUDIT_RECORDS_URL, DEFAULT_PAGE_SIZE);

        // When
        PagedCollectionResource<AuditRecordCollectionRepresentation> result = auditRecordApi
                .getAuditRecordsByFilter(new AuditRecordFilter());

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnAuditRecordsByTypeFilter() throws Exception {
        // Given
        String myType = "myType";
        auditRecordsApiRepresentation.setAuditRecordsForType(TEMPLATE_URL);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, Collections.singletonMap("type", myType))).thenReturn(EXACT_URL);

        PagedCollectionResource<AuditRecordCollectionRepresentation> expected = new AuditRecordCollectionImpl(restConnector, EXACT_URL,
                DEFAULT_PAGE_SIZE);

        // When
        AuditRecordFilter filter = new AuditRecordFilter().byType(myType);
        PagedCollectionResource<AuditRecordCollectionRepresentation> result = auditRecordApi.getAuditRecordsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnAuditRecordsByUserFilter() throws Exception {
        // Given
        String myUser = "myUser";
        auditRecordsApiRepresentation.setAuditRecordsForUser(TEMPLATE_URL);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, Collections.singletonMap("user", myUser))).thenReturn(EXACT_URL);

        PagedCollectionResource<AuditRecordCollectionRepresentation> expected = new AuditRecordCollectionImpl(restConnector, EXACT_URL,
                DEFAULT_PAGE_SIZE);

        // When
        AuditRecordFilter filter = new AuditRecordFilter().byUser(myUser);
        PagedCollectionResource<AuditRecordCollectionRepresentation> result = auditRecordApi.getAuditRecordsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnAuditRecordsByApplicationFilter() throws Exception {
        // Given
        String myApp = "myApp";
        auditRecordsApiRepresentation.setAuditRecordsForApplication(TEMPLATE_URL);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, Collections.singletonMap("application", myApp))).thenReturn(
                EXACT_URL);

        PagedCollectionResource<AuditRecordCollectionRepresentation> expected = new AuditRecordCollectionImpl(restConnector, EXACT_URL,
                DEFAULT_PAGE_SIZE);

        // When
        AuditRecordFilter filter = new AuditRecordFilter().byApplication(myApp);
        PagedCollectionResource<AuditRecordCollectionRepresentation> result = auditRecordApi.getAuditRecordsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnAuditRecordsByTypeAndApplicationFilter() throws SDKException {
        // Given 
        String myType = "type1";
        String myApp = "application1";
        auditRecordsApiRepresentation.setAuditRecordsForTypeAndApplication(TEMPLATE_URL);
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", myType);
        params.put("application", myApp);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<AuditRecordCollectionRepresentation> expected = new AuditRecordCollectionImpl(restConnector, EXACT_URL,
                DEFAULT_PAGE_SIZE);

        // When
        AuditRecordFilter filter = new AuditRecordFilter().byType(myType).byApplication(myApp);
        PagedCollectionResource<AuditRecordCollectionRepresentation> result = auditRecordApi.getAuditRecordsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnAuditRecordsByUserAndApplicationFilter() throws SDKException {
        // Given 
        String myUser = "user1";
        String myApp = "application1";
        auditRecordsApiRepresentation.setAuditRecordsForUserAndApplication(TEMPLATE_URL);
        Map<String, String> params = new HashMap<String, String>();
        params.put("user", myUser);
        params.put("application", myApp);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<AuditRecordCollectionRepresentation> expected = new AuditRecordCollectionImpl(restConnector, EXACT_URL,
                DEFAULT_PAGE_SIZE);

        // When
        AuditRecordFilter filter = new AuditRecordFilter().byUser(myUser).byApplication(myApp);
        PagedCollectionResource<AuditRecordCollectionRepresentation> result = auditRecordApi.getAuditRecordsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnAuditRecordsByTypeAndUserAndStatusFilter() throws Exception {
        // Given 
        String myType = "type1";
        String myUser = "user1";
        String myApplication = "application1";
        auditRecordsApiRepresentation.setAuditRecordsForTypeAndUserAndApplication(TEMPLATE_URL);
        Map<String, String> params = new HashMap<String, String>();
        params.put("user", myUser);
        params.put("type", myType);
        params.put("application", myApplication);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<AuditRecordCollectionRepresentation> expected = new AuditRecordCollectionImpl(restConnector, EXACT_URL,
                DEFAULT_PAGE_SIZE);

        // When
        AuditRecordFilter filter = new AuditRecordFilter().byApplication(myApplication).byType(myType).byUser(myUser);
        PagedCollectionResource<AuditRecordCollectionRepresentation> result = auditRecordApi.getAuditRecordsByFilter(filter);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldReturnAuditRecordsByUserAndTypeFilter() throws SDKException {
        // Given 
        String myUser = "user1";
        String myType = "type1";
        auditRecordsApiRepresentation.setAuditRecordsForUserAndType(TEMPLATE_URL);
        Map<String, String> params = new HashMap<String, String>();
        params.put("user", myUser);
        params.put("type", myType);
        when(templateUrlParser.replacePlaceholdersWithParams(TEMPLATE_URL, params)).thenReturn(EXACT_URL);

        PagedCollectionResource<AuditRecordCollectionRepresentation> expected = new AuditRecordCollectionImpl(restConnector, EXACT_URL,
                DEFAULT_PAGE_SIZE);

        // When
        AuditRecordFilter filter = new AuditRecordFilter().byUser(myUser).byType(myType);
        PagedCollectionResource<AuditRecordCollectionRepresentation> result = auditRecordApi.getAuditRecordsByFilter(filter);

        // Then
        assertThat(result, is(expected));

    }

}
