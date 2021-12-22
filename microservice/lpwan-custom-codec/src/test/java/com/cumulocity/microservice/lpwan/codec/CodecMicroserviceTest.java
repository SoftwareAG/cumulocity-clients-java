/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.microservice.lpwan.codec;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.lpwan.codec.model.DeviceInfo;
import com.cumulocity.microservice.lpwan.codec.model.LpwanCodecDetails;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionAddedEvent;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.application.ApplicationApi;
import com.cumulocity.microservice.subscription.repository.application.CurrentApplicationApi;
import com.cumulocity.model.ID;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjects;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.identity.IdentityApi;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.cumulocity.microservice.lpwan.codec.CodecMicroservice.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class CodecMicroserviceTest {
    @Mock
    private ContextService<Credentials> contextService;

    @Mock
    private ApplicationApi applicationApi;

    @Mock
    private InventoryApi inventoryApi;

    @Mock
    private IdentityApi identityApi;

    @Mock
    Codec codec;

    @Mock
    private PlatformProperties platformProperties;

    @Mock
    private Callable<String> mockCallable;

    private DeviceInfo deviceInfo_1 = new DeviceInfo("Manufacturer_1", "Model_1");
    private String deviceInfo_1_deviceTypeName = String.format(DEVICE_TYPE_NAME_FORMAT, deviceInfo_1.getManufacturer(), deviceInfo_1.getModel());
    private DeviceInfo deviceInfo_2 = new DeviceInfo("Manufacturer_2", "Model_2");
    private String deviceInfo_2_deviceTypeName = String.format(DEVICE_TYPE_NAME_FORMAT, deviceInfo_2.getManufacturer(), deviceInfo_2.getModel());

    private DeviceInfo deviceInfo_invalid = new DeviceInfo(null, "Model_1");

    private MicroserviceCredentials credentials = new MicroserviceCredentials("tenant", "username", "password", null, null, null, "appKey");
    private MicroserviceSubscriptionAddedEvent microserviceSubscriptionAddedEvent = new MicroserviceSubscriptionAddedEvent(credentials);

    @Captor
    private ArgumentCaptor<ID> idCaptor;

    @Captor
    private ArgumentCaptor<ExternalIDRepresentation> externalIDRepresentationCaptor;

    @Captor
    private ArgumentCaptor<ManagedObjectRepresentation> managedObjectRepresentationCaptor;

    @InjectMocks
    CodecMicroservice codecMicroservice;


    @Before
    public void setup() {
        reset(contextService, identityApi, inventoryApi);
    }

    @Test
    public void doRegisterDeviceTypes_BothCreateAndUpdate_Success() throws Exception {
        setupIdentityApi(deviceInfo_1, deviceInfo_1_deviceTypeName, false, false);
        setupInventoryApi(deviceInfo_1, deviceInfo_1_deviceTypeName,false, false);

        setupIdentityApi(deviceInfo_2, deviceInfo_2_deviceTypeName, true, false);
        setupInventoryApi(deviceInfo_2, deviceInfo_2_deviceTypeName, true, false);

        setup_codec_with_2_valid_devices();

        invokeRegisterDeviceTypes(codecMicroservice);

        verify(identityApi, times(2)).getExternalId(idCaptor.capture());
        List<ID> allIds = idCaptor.getAllValues();
        ID deviceInfo_1_ID = allIds.get(0);
        ID deviceInfo_2_ID = allIds.get(1);

        assertEquals(C8Y_SMART_REST_DEVICE_IDENTIFIER, deviceInfo_1_ID.getType());
        assertEquals(C8Y_SMART_REST_DEVICE_IDENTIFIER, deviceInfo_2_ID.getType());

        assertTrue(deviceInfo_1_ID.getValue().equals(deviceInfo_1_deviceTypeName) || deviceInfo_1_ID.getValue().equals(deviceInfo_2_deviceTypeName));
        assertTrue(deviceInfo_2_ID.getValue().equals(deviceInfo_1_deviceTypeName) || deviceInfo_2_ID.getValue().equals(deviceInfo_2_deviceTypeName));

        verify(identityApi).create(externalIDRepresentationCaptor.capture());
        ExternalIDRepresentation deviceInfo_1_externalId = externalIDRepresentationCaptor.getValue();
        assertEquals(C8Y_SMART_REST_DEVICE_IDENTIFIER, deviceInfo_1_externalId.getType());
        assertEquals(deviceInfo_1_deviceTypeName, deviceInfo_1_externalId.getExternalId());

        verify(inventoryApi).create(managedObjectRepresentationCaptor.capture());
        List<ManagedObjectRepresentation> allMOs = managedObjectRepresentationCaptor.getAllValues();
        ManagedObjectRepresentation deviceInfo_1_DeviceType_MO = allMOs.get(0);
        assertEquals(deviceInfo_1_deviceTypeName, deviceInfo_1_DeviceType_MO.getName());
        assertEquals(String.format(DEVICE_TYPE_DESCRIPTION_FORMAT, deviceInfo_1.getModel(), deviceInfo_1.getManufacturer()), deviceInfo_1_DeviceType_MO.get(DESCRIPTION));
        assertEquals(Collections.EMPTY_MAP, deviceInfo_1_DeviceType_MO.get(C8Y_IS_DEVICE_TYPE));
        assertEquals(C8Y_LPWAN_DEVICE_TYPE, deviceInfo_1_DeviceType_MO.getType());
        assertEquals(LPWAN_FIELDBUS_TYPE, deviceInfo_1_DeviceType_MO.get(FIELDBUS_TYPE));

        DeviceInfo deviceInfo_1_supportedDevice = new DeviceInfo(deviceInfo_1.getManufacturer(), deviceInfo_1.getModel());
        LpwanCodecDetails deviceInfo_1_LpwanCodecDetails = new LpwanCodecDetails("testServiceContextPath", deviceInfo_1_supportedDevice);
        assertEquals(deviceInfo_1_LpwanCodecDetails.getAttributes(), deviceInfo_1_DeviceType_MO.get(C8Y_LPWAN_CODEC_DETAILS));

        verify(inventoryApi).update(managedObjectRepresentationCaptor.capture());
        ManagedObjectRepresentation deviceInfo_2_DeviceType_MO = managedObjectRepresentationCaptor.getValue();
        assertEquals(deviceInfo_2_deviceTypeName + "_ID", deviceInfo_2_DeviceType_MO.getId().getValue());

        DeviceInfo deviceInfo_2_supportedDevice = new DeviceInfo(deviceInfo_2.getManufacturer(), deviceInfo_2.getModel());
        LpwanCodecDetails deviceInfo_2_LpwanCodecDetails = new LpwanCodecDetails("testServiceContextPath", deviceInfo_2_supportedDevice);
        assertEquals(deviceInfo_2_LpwanCodecDetails.getAttributes(), deviceInfo_2_DeviceType_MO.get(C8Y_LPWAN_CODEC_DETAILS));
    }

    @Test
    public void doRegisterDeviceTypes_BothCreateAndUpdate_InventoryApiThrowsException_Failure() throws Exception {
        setupIdentityApi(deviceInfo_1, deviceInfo_1_deviceTypeName, false, false);
        setupInventoryApi(deviceInfo_1, deviceInfo_1_deviceTypeName,false, true);

        setupIdentityApi(deviceInfo_2, deviceInfo_2_deviceTypeName, true, false);
        setupInventoryApi(deviceInfo_2, deviceInfo_2_deviceTypeName, true, true);

        setup_codec_with_2_valid_devices();
        invokeRegisterDeviceTypes(codecMicroservice);

        verify(identityApi, times(2)).getExternalId(idCaptor.capture());

        verify(identityApi, never()).create(externalIDRepresentationCaptor.capture());

        verify(inventoryApi).create(managedObjectRepresentationCaptor.capture());

        verify(inventoryApi).update(managedObjectRepresentationCaptor.capture());
    }

    @Test
    public void doRegisterDeviceTypes_BothCreateAndUpdate_IdentityApiThrowsException_Failure() throws Exception {
        setupIdentityApi(deviceInfo_1, deviceInfo_1_deviceTypeName, false, true);
        setupInventoryApi(deviceInfo_1, deviceInfo_1_deviceTypeName,false, false);

        setupIdentityApi(deviceInfo_2, deviceInfo_2_deviceTypeName, true, true);
        setupInventoryApi(deviceInfo_2, deviceInfo_2_deviceTypeName, true, false);

        setup_codec_with_2_valid_devices();
        invokeRegisterDeviceTypes(codecMicroservice);

        verify(identityApi, times(2)).getExternalId(idCaptor.capture());

        verify(identityApi).create(externalIDRepresentationCaptor.capture());

        verify(inventoryApi).create(managedObjectRepresentationCaptor.capture());

        verify(inventoryApi).update(managedObjectRepresentationCaptor.capture());
    }

    @Test
    public void doRegisterDeviceTypes_BothCreateAndUpdate_InvalidDeviceInfo_Failure() throws Exception {
        setupIdentityApi(deviceInfo_1, deviceInfo_1_deviceTypeName, false, false);
        setupInventoryApi(deviceInfo_1, deviceInfo_1_deviceTypeName, false, false);

        setupIdentityApi(deviceInfo_invalid, "deviceInfo_invalid_deviceTypeName", false, false);
        setupInventoryApi(deviceInfo_invalid, "deviceInfo_invalid_deviceTypeName",false, false);

        setupIdentityApi(deviceInfo_2, deviceInfo_2_deviceTypeName, true, false);
        setupInventoryApi(deviceInfo_2, deviceInfo_2_deviceTypeName, true, false);

        setup_codec_with_invalidDeviceInfo();
        invokeRegisterDeviceTypes(codecMicroservice);

        verify(identityApi, times(2)).getExternalId(idCaptor.capture());

        verify(identityApi).create(externalIDRepresentationCaptor.capture());

        verify(inventoryApi).create(managedObjectRepresentationCaptor.capture());

        verify(inventoryApi).update(managedObjectRepresentationCaptor.capture());
    }

    private void setup_codec_with_2_valid_devices() {
//        when(codec.getMicroserviceContextPath()).thenReturn("testServiceContextPath");
        when(contextService.callWithinContext(eq(credentials),any(Callable.class))).thenReturn("testServiceContextPath");
        when(codec.supportsDevices()).thenReturn(Stream.of(new DeviceInfo[] {deviceInfo_1, deviceInfo_2})
                .collect(Collectors.toCollection(HashSet::new)));
    }

    private void setup_codec_with_invalidContextPath() {
//        when(codec.getMicroserviceContextPath()).thenReturn(null);
        when(contextService.callWithinContext(eq(credentials),any(Callable.class))).thenReturn(null);
        when(codec.supportsDevices()).thenReturn(Stream.of(new DeviceInfo[] {deviceInfo_1, deviceInfo_2})
                .collect(Collectors.toCollection(HashSet::new)));
    }

    private void setup_codec_with_invalidDeviceInfo() {
//        when(codec.getMicroserviceContextPath()).thenReturn("testServiceContextPath");
        when(contextService.callWithinContext(eq(credentials),any(Callable.class))).thenReturn("testServiceContextPath");
        when(codec.supportsDevices()).thenReturn(Stream.of(new DeviceInfo[] {deviceInfo_1, deviceInfo_invalid, deviceInfo_2})
                .collect(Collectors.toCollection(HashSet::new)));
    }

    private void invokeRegisterDeviceTypes(CodecMicroservice codecMicroservice) throws Exception {

        CurrentApplicationApi currentApplicationApi = mock(CurrentApplicationApi.class);
        ApplicationRepresentation applicationRepresentation = mock(ApplicationRepresentation.class);
        when(platformProperties.getMicroserviceBoostrapUser()).thenReturn(credentials);
        when(applicationApi.currentApplication()).thenReturn(currentApplicationApi);
        when(currentApplicationApi.get()).thenReturn(applicationRepresentation);
        when(applicationRepresentation.getContextPath()).thenReturn("testServiceContextPath");

//        when(contextService.callWithinContext(eq(credentials),any(Callable.class))).thenReturn("testServiceContextPath");
        codecMicroservice.registerDeviceTypes(microserviceSubscriptionAddedEvent);

        ArgumentCaptor<Callable> callableArgumentCaptor = ArgumentCaptor.forClass(Callable.class);
        verify(contextService).callWithinContext(eq(credentials), callableArgumentCaptor.capture());
        callableArgumentCaptor.getValue().call();

        ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(contextService).runWithinContext(eq(credentials), taskCaptor.capture());
        taskCaptor.getValue().run();
    }

    private void setupIdentityApi(DeviceInfo deviceInfo, String deviceTypeName, boolean isDeviceTypeExist, boolean throwException) {
        ID id = new ID(C8Y_SMART_REST_DEVICE_IDENTIFIER, deviceTypeName);

        ExternalIDRepresentation externalIDRepresentation = new ExternalIDRepresentation();
        externalIDRepresentation.setExternalId(deviceTypeName);
        externalIDRepresentation.setType(C8Y_SMART_REST_DEVICE_IDENTIFIER);
        externalIDRepresentation.setManagedObject(ManagedObjects.asManagedObject(GId.asGId(deviceTypeName + "_ID")));

        // Setup for IdentityApi#getExternalId() method
        if(!isDeviceTypeExist) {
            when(identityApi.getExternalId(eq(id))).thenThrow(new SDKException("Simulated error in IdentityApi"));
        }
        else {
            when(identityApi.getExternalId(eq(id))).thenReturn(externalIDRepresentation);
        }

        // Setup for IdentityApi#create() methods
        if(!isDeviceTypeExist) {
            if(!throwException) {
                when(identityApi.create(any(ExternalIDRepresentation.class))).thenReturn(null);
            }
            else {
                when(identityApi.create(any(ExternalIDRepresentation.class))).thenThrow(new SDKException("Simulated error in IdentityApi"));
            }
        }
    }

    private void setupInventoryApi(DeviceInfo deviceInfo, String deviceTypeName, boolean isDeviceTypeExist, boolean throwException) {
        if(!isDeviceTypeExist) {
            if(!throwException) {
                when(inventoryApi.create(any(ManagedObjectRepresentation.class))).thenReturn(ManagedObjects.asManagedObject(GId.asGId(deviceTypeName + "_ID")));
            }
            else {
                when(inventoryApi.create(any(ManagedObjectRepresentation.class))).thenThrow(new SDKException("Simulated error in InventoryApi"));
            }
        }
        else {
            if(!throwException) {
                when(inventoryApi.update(any(ManagedObjectRepresentation.class))).thenReturn(ManagedObjects.asManagedObject(GId.asGId(deviceTypeName + "_ID")));
            }
            else {
                when(inventoryApi.update(any(ManagedObjectRepresentation.class))).thenThrow(new SDKException("Simulated error in InventoryApi"));
            }
        }
    }
}