/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.lns.instance.service;

import com.cumulocity.lpwan.exception.InputDataValidationException;
import com.cumulocity.lpwan.exception.LpwanServiceException;
import com.cumulocity.lpwan.lns.instance.model.LnsInstance;
import com.cumulocity.lpwan.lns.instance.model.LnsInstanceDeserializer;
import com.cumulocity.lpwan.smaple.instance.model.SampleInstance;
import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.option.TenantOptionApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.JsonExpectationsHelper;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class LnsInstanceServiceTest {

    @Mock
    private TenantOptionApi tenantOptionApi;

    @InjectMocks
    private LnsInstanceService lnsInstanceService;

    @Captor
    private ArgumentCaptor<OptionRepresentation> optionRepresentationCaptor;

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final MapType mapType = JSON_MAPPER.getTypeFactory().constructMapType(ConcurrentHashMap.class, String.class, LnsInstance.class);

    // Valid JSON String representing LNS Instances map stored in the tenant options
    //    {
    //        "SampleInstance-1": {
    //                "name": "SampleInstance-1",
    //                "description": "Description for SampleInstance-1",
    //                "user": "user-1",
    //                "password": "password-1"
    //        },
    //        "SampleInstance-2": {
    //                "name": "SampleInstance-2",
    //                "description": "Description for SampleInstance-2",
    //                "user": "user-2",
    //                "password": "password-3"
    //        },
    //        "SampleInstance-3": {
    //                "name": "SampleInstance-3",
    //                "description": "Description for SampleInstance-3",
    //                "user": "user-3",
    //                "password": "password-3"
    //        }
    //    }
    private final String VALID_LNS_INSTANCES_MAP_JSON = "{\"SampleInstance-1\":{\"name\":\"SampleInstance-1\",\"description\":\"Description for SampleInstance-1\",\"user\":\"user-1\",\"password\":\"password-1\"},\"SampleInstance-2\":{\"name\":\"SampleInstance-2\",\"description\":\"Description for SampleInstance-2\",\"user\":\"user-2\",\"password\":\"password-3\"},\"SampleInstance-3\":{\"name\":\"SampleInstance-3\",\"description\":\"Description for SampleInstance-3\",\"user\":\"user-3\",\"password\":\"password-3\"}}";
    private Map<String, LnsInstance> VALID_LNS_INSTANCES_MAP;
    private final String EMPTY_LNS_INSTANCES_MAP_JSON = "{}";

    @Before
    public void setup() throws JsonProcessingException {
        LnsInstanceDeserializer.registerLnsInstanceConcreteClass("sample", SampleInstance.class);

        VALID_LNS_INSTANCES_MAP = JSON_MAPPER.readerWithView(LnsInstance.InternalView.class)
                                             .forType(mapType)
                                             .readValue(VALID_LNS_INSTANCES_MAP_JSON);
    }

    @Test
    public void doLoadLnsInstanceFromTenantOptions_getOption_returns_VALID_lnsInstances_map() throws LpwanServiceException {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(
                lnsInstancesOptionKey.getCategory(),
                lnsInstancesOptionKey.getKey(),
                VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);

        Map<String, LnsInstance> lnsInstancesMap = lnsInstanceService.loadLnsInstancesFromTenantOptions(lnsInstancesOptionKey);

        compare(VALID_LNS_INSTANCES_MAP, lnsInstancesMap);
    }

    @Test
    public void doLoadLnsInstanceFromTenantOptions_getOption_returns_EMPTY_lnsInstances_map() throws LpwanServiceException {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(
                lnsInstancesOptionKey.getCategory(),
                lnsInstancesOptionKey.getKey(),
                EMPTY_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);

        Map<String, LnsInstance> lnsInstancesMap = lnsInstanceService.loadLnsInstancesFromTenantOptions(lnsInstancesOptionKey);

        compare(new ConcurrentHashMap<>(), lnsInstancesMap);
    }

    @Test
    public void doLoadLnsInstanceFromTenantOptions_getOption_returns_NULL_lnsInstances_String() throws LpwanServiceException {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(
                lnsInstancesOptionKey.getCategory(),
                lnsInstancesOptionKey.getKey(),
                null);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);

        Map<String, LnsInstance> lnsInstancesMap = lnsInstanceService.loadLnsInstancesFromTenantOptions(lnsInstancesOptionKey);

        compare(new ConcurrentHashMap<>(), lnsInstancesMap);
    }

    @Test
    public void doLoadLnsInstanceFromTenantOptions_getOption_returns_BLANK_lnsInstances_String() throws LpwanServiceException {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(
                lnsInstancesOptionKey.getCategory(),
                lnsInstancesOptionKey.getKey(),
                " ");

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);

        Map<String, LnsInstance> lnsInstancesMap = lnsInstanceService.loadLnsInstancesFromTenantOptions(lnsInstancesOptionKey);

        compare(new ConcurrentHashMap<>(), lnsInstancesMap);
    }

    @Test
    public void doLoadLnsInstanceFromTenantOptions_getOption_throws_httpStatus_NOT_FOUND_exception() throws LpwanServiceException {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenThrow(new SDKException(HttpStatus.NOT_FOUND.value(), "NOT FOUND"));

        Map<String, LnsInstance> lnsInstancesMap = lnsInstanceService.loadLnsInstancesFromTenantOptions(lnsInstancesOptionKey);

        compare(new ConcurrentHashMap<>(), lnsInstancesMap);
    }

    @Test
    public void doLoadLnsInstanceFromTenantOptions_getOption_throws_some_unexpected_exception() {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenThrow(new SDKException("SOME UNEXPECTED ERROR"));

        LpwanServiceException lpwanServiceException = assertThrows(LpwanServiceException.class, () -> lnsInstanceService.loadLnsInstancesFromTenantOptions(lnsInstancesOptionKey));

        assertEquals(String.format("Error while fetching the tenant option with key '%s'.", lnsInstancesOptionKey), lpwanServiceException.getMessage());
    }

    @Test
    public void doLoadLnsInstanceFromTenantOptions_JSON_MAPPER_throws_JsonProcessingException() {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(
                lnsInstancesOptionKey.getCategory(),
                lnsInstancesOptionKey.getKey(),
                "INVALID JSON");

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);

        LpwanServiceException lpwanServiceException = assertThrows(LpwanServiceException.class, () -> lnsInstanceService.loadLnsInstancesFromTenantOptions(lnsInstancesOptionKey));

        assertEquals(String.format("Error unmarshalling the below JSON string containing LNS instance map stored as a tenant option with key '%s'. \n%s", lnsInstancesOptionKey, "INVALID JSON"), lpwanServiceException.getMessage());
    }

    @Test
    public void doFlushCache_EMPTY_lnsInstances_map() throws LpwanServiceException {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(
                lnsInstancesOptionKey.getCategory(),
                lnsInstancesOptionKey.getKey(),
                null);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);
        when(tenantOptionApi.save(eq(lnsInstancesOptionRepresentation))).thenReturn(lnsInstancesOptionRepresentation);

        lnsInstanceService.flushCache();

        verify(tenantOptionApi).save(optionRepresentationCaptor.capture());
        OptionRepresentation optionRepresentationArgument = optionRepresentationCaptor.getValue();

        assertEquals(OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), EMPTY_LNS_INSTANCES_MAP_JSON), optionRepresentationArgument);
    }

    @Test
    public void doFlushCache_VALID_lnsInstances_map() throws Exception {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(
                lnsInstancesOptionKey.getCategory(),
                lnsInstancesOptionKey.getKey(),
                VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);
        when(tenantOptionApi.save(any())).thenReturn(lnsInstancesOptionRepresentation);

        lnsInstanceService.flushCache();

        verify(tenantOptionApi).save(optionRepresentationCaptor.capture());
        OptionRepresentation optionRepresentationArgument = optionRepresentationCaptor.getValue();

        assertEquals(lnsInstancesOptionKey.getCategory(), optionRepresentationArgument.getCategory());
        assertEquals(lnsInstancesOptionKey.getKey(), optionRepresentationArgument.getKey());
        new JsonExpectationsHelper().assertJsonEqual(VALID_LNS_INSTANCES_MAP_JSON, optionRepresentationArgument.getValue(), true);
    }

    @Test
    public void doFlushCache_getLnsInstances_throws_LpwanServiceException() {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(
                lnsInstancesOptionKey.getCategory(),
                lnsInstancesOptionKey.getKey(),
                "INVALID JSON");

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);

        LpwanServiceException lpwanServiceException = assertThrows(LpwanServiceException.class, () -> lnsInstanceService.flushCache());

        assertEquals(String.format("Unexpected error occurred while accessing the cached LNS instances map with key '%s'.", lnsInstancesOptionKey), lpwanServiceException.getMessage());
    }

    @Test
    public void doFlushCache_tenantOptionApi_throws_SDKException() {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(
                lnsInstancesOptionKey.getCategory(),
                lnsInstancesOptionKey.getKey(),
                EMPTY_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);
        when(tenantOptionApi.save(eq(lnsInstancesOptionRepresentation))).thenThrow(new SDKException("Error while saving."));

        LpwanServiceException lpwanServiceException = assertThrows(LpwanServiceException.class, () -> lnsInstanceService.flushCache());

        assertEquals(String.format("Error saving the below LNS instance map as a tenant option with key '%s'. \n%s", lnsInstancesOptionKey, EMPTY_LNS_INSTANCES_MAP_JSON), lpwanServiceException.getMessage());
    }

    @Test
    public void doGetByName_with_existing_name() throws LpwanServiceException {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);

        LnsInstance sampleInstance_1 = lnsInstanceService.getByName("SampleInstance-1");

        compare(VALID_LNS_INSTANCES_MAP.get("SampleInstance-1"), sampleInstance_1);
    }

    @Test
    public void doGetByName_with_non_existing_name() {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);

        InputDataValidationException notFoundException = assertThrows(InputDataValidationException.class, () -> lnsInstanceService.getByName("SOME_NAME"));
        assertEquals(String.format("LNS instance named '%s' doesn't exist.", "SOME_NAME"), notFoundException.getMessage());
    }

    @Test
    public void doGetByName_with_no_instances_in_tenant_options() {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), EMPTY_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);

        InputDataValidationException notFoundException = assertThrows(InputDataValidationException.class, () -> lnsInstanceService.getByName("SOME_NAME"));
        assertEquals(String.format("LNS instance named '%s' doesn't exist.", "SOME_NAME"), notFoundException.getMessage());
    }

    @Test
    public void doGetByName_inputValidation_null_name() {
        InputDataValidationException inputDataValidationException = Assert.assertThrows(InputDataValidationException.class, () -> lnsInstanceService.getByName(null));
        assertEquals("LNS instance name can't be null or blank.", inputDataValidationException.getMessage());
    }

    @Test
    public void doGetByName_inputValidation_blank_name() {
        InputDataValidationException inputDataValidationException = Assert.assertThrows(InputDataValidationException.class, () -> lnsInstanceService.getByName(""));
        assertEquals("LNS instance name can't be null or blank.", inputDataValidationException.getMessage());
    }

    @Test
    public void doGetAll_non_empty() throws LpwanServiceException {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);

        Collection<LnsInstance> allInstances = lnsInstanceService.getAll();

        compare(VALID_LNS_INSTANCES_MAP, allInstances);
    }

    @Test
    public void doGetAll_empty() throws LpwanServiceException {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), EMPTY_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);

        Collection<LnsInstance> allInstances = lnsInstanceService.getAll();

        compare(new ConcurrentHashMap<>(), allInstances);
    }

    @Test
    public void doCreate_valid_instance() throws Exception {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);
        when(tenantOptionApi.save(any())).thenReturn(null);

        LnsInstance instanceToCreate = SampleInstance.builder()
                                        .name("Sample Instance Name")
                                        .description("Sample Instance Description")
                                        .user("USER NAME")
                                        .password("**********")
                                        .build();
        LnsInstance createdInstance = lnsInstanceService.create(instanceToCreate);

        compare(instanceToCreate, createdInstance);

        verify(tenantOptionApi).save(optionRepresentationCaptor.capture());
        OptionRepresentation optionRepresentationArgument = optionRepresentationCaptor.getValue();

        assertEquals(lnsInstancesOptionKey.getCategory(), optionRepresentationArgument.getCategory());
        assertEquals(lnsInstancesOptionKey.getKey(), optionRepresentationArgument.getKey());


        VALID_LNS_INSTANCES_MAP.put(instanceToCreate.getName(), instanceToCreate);
        Map<String, LnsInstance> actualMapSaved = JSON_MAPPER.readerWithView(LnsInstance.InternalView.class)
                                                            .forType(mapType)
                                                            .readValue(optionRepresentationArgument.getValue());
        compare(VALID_LNS_INSTANCES_MAP, actualMapSaved);
    }

    @Test
    public void doCreate_null_instance() {
        InputDataValidationException inputDataValidationException = assertThrows(InputDataValidationException.class, () -> lnsInstanceService.create(null));
        assertEquals("New LNS instance can't be null.", inputDataValidationException.getMessage());
    }

    @Test
    public void doCreate_invalid_instance() {
        LnsInstance invalidInstanceToCreate = SampleInstance.builder()
                                                .name("Sample Instance Name")
                                                .description("Sample Instance Description")
                                                .user(null)
                                                .password("**********")
                                                .build();

        InputDataValidationException inputDataValidationException = assertThrows(InputDataValidationException.class, () -> lnsInstanceService.create(invalidInstanceToCreate));
        assertEquals("SampleInstance is missing mandatory fields: 'user'", inputDataValidationException.getMessage());
    }

    @Test
    public void doCreate_duplicate_instance() {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);
        when(tenantOptionApi.save(any())).thenReturn(null);

        LnsInstance duplicateInstanceToCreate = SampleInstance.builder()
                .name("SampleInstance-1")
                .description("Sample Instance Description")
                .user("USER NAME")
                .password("**********")
                .build();
        InputDataValidationException inputDataValidationException = assertThrows(InputDataValidationException.class, () -> lnsInstanceService.create(duplicateInstanceToCreate));
        assertEquals(String.format("LNS instance named '%s' already exists.", duplicateInstanceToCreate.getName()), inputDataValidationException.getMessage());
    }

    @Test
    public void doUpdate_with_valid_lnsInstance_with_new_password() throws Exception {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);
        when(tenantOptionApi.save(any())).thenReturn(null);

        LnsInstance instanceToUpdate = SampleInstance.builder()
                                        .name("SampleInstance-1")
                                        .description("Description for SampleInstance-1 (UPDATED)")
                                        .user("user-1 (UPDATED)")
                                        .password("password-1 (UPDATED)")
                                        .build();
        LnsInstance updatedInstance = lnsInstanceService.update(instanceToUpdate.getName(), instanceToUpdate);

        compare(instanceToUpdate, updatedInstance);

        verify(tenantOptionApi).save(optionRepresentationCaptor.capture());
        OptionRepresentation optionRepresentationArgument = optionRepresentationCaptor.getValue();

        assertEquals(lnsInstancesOptionKey.getCategory(), optionRepresentationArgument.getCategory());
        assertEquals(lnsInstancesOptionKey.getKey(), optionRepresentationArgument.getKey());


        VALID_LNS_INSTANCES_MAP.put(instanceToUpdate.getName(), instanceToUpdate);
        Map<String, LnsInstance> actualMapSaved = JSON_MAPPER.readerWithView(LnsInstance.InternalView.class)
                .forType(mapType)
                .readValue(optionRepresentationArgument.getValue());
        compare(VALID_LNS_INSTANCES_MAP, actualMapSaved);
    }

    @Test
    public void doUpdate_with_valid_lnsInstance_with_old_password() throws Exception {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);
        when(tenantOptionApi.save(any())).thenReturn(null);

        SampleInstance instanceToUpdate = SampleInstance.builder()
                .name("SampleInstance-1")
                .description("Description for SampleInstance-1 (UPDATED)")
                .user("user-1 (UPDATED)")
                .password(null) // Password is passed as null, so the old password is kept
                .build();
        LnsInstance updatedInstance = lnsInstanceService.update(instanceToUpdate.getName(), instanceToUpdate);

        instanceToUpdate.setPassword(((SampleInstance)VALID_LNS_INSTANCES_MAP.get(instanceToUpdate.getName())).getPassword()); // Initialize the password with the existing instance's password
        compare(instanceToUpdate, updatedInstance);

        verify(tenantOptionApi).save(optionRepresentationCaptor.capture());
        OptionRepresentation optionRepresentationArgument = optionRepresentationCaptor.getValue();

        assertEquals(lnsInstancesOptionKey.getCategory(), optionRepresentationArgument.getCategory());
        assertEquals(lnsInstancesOptionKey.getKey(), optionRepresentationArgument.getKey());


        VALID_LNS_INSTANCES_MAP.put(instanceToUpdate.getName(), instanceToUpdate);
        Map<String, LnsInstance> actualMapSaved = JSON_MAPPER.readerWithView(LnsInstance.InternalView.class)
                .forType(mapType)
                .readValue(optionRepresentationArgument.getValue());
        compare(VALID_LNS_INSTANCES_MAP, actualMapSaved);
    }

    @Test
    public void doUpdate_rename_lnsInstance() throws Exception {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);
        when(tenantOptionApi.save(any())).thenReturn(null);

        String existingLnsInstanceName = "SampleInstance-1";
        LnsInstance instanceToUpdate = SampleInstance.builder()
                .name("SampleInstance-1 (UPDATED)")
                .description("Description for SampleInstance-1 (UPDATED)")
                .user("user-1 (UPDATED)")
                .password("password-1 (UPDATED)")
                .build();
        LnsInstance updatedInstance = lnsInstanceService.update(existingLnsInstanceName, instanceToUpdate);

        compare(instanceToUpdate, updatedInstance);

        verify(tenantOptionApi).save(optionRepresentationCaptor.capture());
        OptionRepresentation optionRepresentationArgument = optionRepresentationCaptor.getValue();

        assertEquals(lnsInstancesOptionKey.getCategory(), optionRepresentationArgument.getCategory());
        assertEquals(lnsInstancesOptionKey.getKey(), optionRepresentationArgument.getKey());


        VALID_LNS_INSTANCES_MAP.remove(existingLnsInstanceName);
        VALID_LNS_INSTANCES_MAP.put(instanceToUpdate.getName(), instanceToUpdate);
        Map<String, LnsInstance> actualMapSaved = JSON_MAPPER.readerWithView(LnsInstance.InternalView.class)
                .forType(mapType)
                .readValue(optionRepresentationArgument.getValue());
        compare(VALID_LNS_INSTANCES_MAP, actualMapSaved);
    }

    @Test
    public void doUpdate_rename_lnsInstance_with_old_password() throws Exception {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);
        when(tenantOptionApi.save(any())).thenReturn(null);

        String existingLnsInstanceName = "SampleInstance-1";
        SampleInstance instanceToUpdate = SampleInstance.builder()
                .name("SampleInstance-1 (UPDATED)")
                .description("Description for SampleInstance-1 (UPDATED)")
                .user("user-1 (UPDATED)")
                .password(null) // Password is passed as null, so the old password is kept
                .build();
        LnsInstance updatedInstance = lnsInstanceService.update(existingLnsInstanceName, instanceToUpdate);

        instanceToUpdate.setPassword(((SampleInstance)VALID_LNS_INSTANCES_MAP.get(existingLnsInstanceName)).getPassword()); // Initialize the password with the existing instance's password
        compare(instanceToUpdate, updatedInstance);

        verify(tenantOptionApi).save(optionRepresentationCaptor.capture());
        OptionRepresentation optionRepresentationArgument = optionRepresentationCaptor.getValue();

        assertEquals(lnsInstancesOptionKey.getCategory(), optionRepresentationArgument.getCategory());
        assertEquals(lnsInstancesOptionKey.getKey(), optionRepresentationArgument.getKey());


        VALID_LNS_INSTANCES_MAP.remove(existingLnsInstanceName);
        VALID_LNS_INSTANCES_MAP.put(instanceToUpdate.getName(), instanceToUpdate);
        Map<String, LnsInstance> actualMapSaved = JSON_MAPPER.readerWithView(LnsInstance.InternalView.class)
                .forType(mapType)
                .readValue(optionRepresentationArgument.getValue());
        compare(VALID_LNS_INSTANCES_MAP, actualMapSaved);
    }

    @Test
    public void doUpdate_with_null_existingLnsInstanceName() {
        LnsInstance instanceToUpdate = SampleInstance.builder()
                                            .name("SampleInstance-1 (UPDATED)")
                                            .description("Description for SampleInstance-1 (UPDATED)")
                                            .user("user-1 (UPDATED)")
                                            .password(null)
                                            .build();
        InputDataValidationException inputDataValidationException = assertThrows(InputDataValidationException.class, () -> lnsInstanceService.update(null, instanceToUpdate));
        assertEquals("Existing LNS instance name can't be null or blank.", inputDataValidationException.getMessage());
    }

    @Test
    public void doUpdate_with_blank_existingLnsInstanceName() {
        LnsInstance instanceToUpdate = SampleInstance.builder()
                                            .name("SampleInstance-1 (UPDATED)")
                                            .description("Description for SampleInstance-1 (UPDATED)")
                                            .user("user-1 (UPDATED)")
                                            .password(null)
                                            .build();
        InputDataValidationException inputDataValidationException = assertThrows(InputDataValidationException.class, () -> lnsInstanceService.update("  ", instanceToUpdate));
        assertEquals("Existing LNS instance name can't be null or blank.", inputDataValidationException.getMessage());
    }

    @Test
    public void doUpdate_with_nonExisting_existingLnsInstanceName() {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);

        String nonExistingInstanceNameToUpdate = "SampleInstance-5";
        LnsInstance instanceToUpdate = SampleInstance.builder()
                .name("SampleInstance-5 (UPDATED)")
                .description("Description for SampleInstance-5 (UPDATED)")
                .user("user-5 (UPDATED)")
                .password("password-5 (UPDATED)")
                .build();

        InputDataValidationException inputDataValidationException = assertThrows(InputDataValidationException.class, () -> lnsInstanceService.update(nonExistingInstanceNameToUpdate, instanceToUpdate));
        assertEquals(String.format("LNS instance named '%s' doesn't exist.", nonExistingInstanceNameToUpdate), inputDataValidationException.getMessage());
    }

    @Test
    public void doUpdate_with_null_lnsInstanceToUpdate() {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);

        String existingInstanceNameToUpdate = "SampleInstance-1";

        InputDataValidationException inputDataValidationException = assertThrows(InputDataValidationException.class, () -> lnsInstanceService.update(existingInstanceNameToUpdate, null));
        assertEquals("LNS instance to update can't be null.", inputDataValidationException.getMessage());
    }

    @Test
    public void doUpdate_with_invalid_lnsInstanceToUpdate() {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);

        String nonExistingInstanceNameToUpdate = "SampleInstance-1";
        LnsInstance invalidInstanceToUpdate = SampleInstance.builder()
                .name("SampleInstance-1 (UPDATED)")
                .description("Description for SampleInstance-1 (UPDATED)")
                .user(null) // Invalid as user is a mandatory field
                .password("password-5 (UPDATED)")
                .build();

        InputDataValidationException inputDataValidationException = assertThrows(InputDataValidationException.class, () -> lnsInstanceService.update(nonExistingInstanceNameToUpdate, invalidInstanceToUpdate));
        assertEquals("SampleInstance is missing mandatory fields: 'user'", inputDataValidationException.getMessage());
    }

    @Test
    public void doUpdate_with_updated_instance_name_already_present() {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);

        String existingInstanceNameToUpdate = "SampleInstance-1";
        LnsInstance instanceToUpdate = SampleInstance.builder()
                .name("SampleInstance-2") // Already existing instance
                .description("Description for SampleInstance-2 (UPDATED)")
                .user("user-2 (UPDATED)")
                .password("password-2 (UPDATED)")
                .build();

        InputDataValidationException inputDataValidationException = assertThrows(InputDataValidationException.class, () -> lnsInstanceService.update(existingInstanceNameToUpdate, instanceToUpdate));
        assertEquals(String.format("LNS instance named '%s' already exists.", instanceToUpdate.getName()), inputDataValidationException.getMessage());
    }

    @Test
    public void doDelete_with_existing_instanceName() throws LpwanServiceException, JsonProcessingException {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);
        when(tenantOptionApi.save(any())).thenReturn(null);

        String instanceNameToDelete = "SampleInstance-1";
        lnsInstanceService.delete(instanceNameToDelete);

        verify(tenantOptionApi).save(optionRepresentationCaptor.capture());
        OptionRepresentation optionRepresentationArgument = optionRepresentationCaptor.getValue();

        assertEquals(lnsInstancesOptionKey.getCategory(), optionRepresentationArgument.getCategory());
        assertEquals(lnsInstancesOptionKey.getKey(), optionRepresentationArgument.getKey());


        VALID_LNS_INSTANCES_MAP.remove(instanceNameToDelete);
        Map<String, LnsInstance> actualMapSaved = JSON_MAPPER.readerWithView(LnsInstance.InternalView.class)
                                                    .forType(mapType)
                                                    .readValue(optionRepresentationArgument.getValue());
        compare(VALID_LNS_INSTANCES_MAP, actualMapSaved);
    }

    @Test
    public void doDelete_with_null_instanceName() {
        InputDataValidationException inputDataValidationException = assertThrows(InputDataValidationException.class, () -> lnsInstanceService.delete(null));
        assertEquals("LNS instance name to delete can't be null or blank.", inputDataValidationException.getMessage());
    }

    @Test
    public void doDelete_with_blank_instanceName() {
        InputDataValidationException inputDataValidationException = assertThrows(InputDataValidationException.class, () -> lnsInstanceService.delete("  "));
        assertEquals("LNS instance name to delete can't be null or blank.", inputDataValidationException.getMessage());
    }

    @Test
    public void doDelete_with_nonExisting_instanceName() {
        OptionPK lnsInstancesOptionKey = new OptionPK("sample", "credentials.lns.instances.map");
        OptionRepresentation lnsInstancesOptionRepresentation = OptionRepresentation.asOptionRepresentation(lnsInstancesOptionKey.getCategory(), lnsInstancesOptionKey.getKey(), VALID_LNS_INSTANCES_MAP_JSON);

        when(tenantOptionApi.getOption(eq(lnsInstancesOptionKey))).thenReturn(lnsInstancesOptionRepresentation);
        when(tenantOptionApi.save(any())).thenReturn(null);

        String noExistingInstanceNameToDelete = "SampleInstance-5";
        InputDataValidationException inputDataValidationException = assertThrows(InputDataValidationException.class, () -> lnsInstanceService.delete(noExistingInstanceNameToDelete));
        assertEquals(String.format("LNS instance named '%s' doesn't exist.", noExistingInstanceNameToDelete), inputDataValidationException.getMessage());
    }

    private void compare(Map<String, LnsInstance> expected, Collection<LnsInstance> actual) {
        assertEquals(expected.size(), actual.size());
        Map<String, LnsInstance> actualMap = new ConcurrentHashMap<>(actual.size());

        for (LnsInstance oneLnsInstance: actual) {
            actualMap.put(oneLnsInstance.getName(), oneLnsInstance);
        }

        compare(expected, actualMap);
    }

    private void compare(Map<String, LnsInstance> expected, Map<String, LnsInstance> actual) {
        assertNotNull(actual);
        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.size(), actual.size());

        for (Map.Entry<String, LnsInstance> oneExpectedEntry : expected.entrySet()) {
            assertTrue(actual.containsKey(oneExpectedEntry.getKey()));
            compare(oneExpectedEntry.getValue(), actual.get(oneExpectedEntry.getKey()));
        }
    }

    private void compare(LnsInstance expected, LnsInstance actual) {
        assertNotNull(actual);

        assertTrue(expected.getClass().isAssignableFrom(actual.getClass()));
        assertEquals(actual.getClass(), SampleInstance.class);

        SampleInstance expectedTestLnsInstance = (SampleInstance) expected;
        SampleInstance actualTestLnsInstance = (SampleInstance) actual;

        assertEquals(expectedTestLnsInstance.getName(), actualTestLnsInstance.getName());
        assertEquals(expectedTestLnsInstance.getDescription(), actualTestLnsInstance.getDescription());
        assertEquals(expectedTestLnsInstance.getUser(), actualTestLnsInstance.getUser());
        assertEquals(expectedTestLnsInstance.getPassword(), actualTestLnsInstance.getPassword());
    }
}