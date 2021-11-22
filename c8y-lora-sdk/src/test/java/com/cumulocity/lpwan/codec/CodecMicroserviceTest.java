/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec;

import com.cumulocity.lpwan.codec.CodecMicroservice;
import com.cumulocity.lpwan.codec.model.DeviceInfo;
import com.cumulocity.lpwan.codec.model.DeviceTypeEnum;
import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionAddedEvent;
import com.cumulocity.model.ID;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.identity.IdentityApi;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.cumulocity.lpwan.codec.util.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class CodecMicroserviceTest extends TestCase {
    @Mock
    private MicroserviceSubscriptionAddedEvent microserviceSubscriptionAddedEvent;

    @Mock
    private ContextService<Credentials> contextService;

    @Mock
    private InventoryApi inventoryApi;

    @Mock
    private IdentityApi identityApi;

    @Mock
    ExternalIDRepresentation externalIDRepresentation;

    private CodecMicroservice codecMicroservice;


    @Before
    public void setUp(){
        codecMicroservice = new CodecMicroservice() {
            @Override
            String getMicroserviceContextPath() {
                return "dummyContextPath";
            }

            @Override
            public Set<DeviceInfo> supportsDevices() {
                DeviceInfo deviceInfo = new DeviceInfo("Dummy_Manufacturer", "Dummy_Model", DeviceTypeEnum.LORA);
                return Stream.of(deviceInfo).collect(Collectors.toCollection(HashSet::new));
            }
        };

        ReflectionTestUtils.setField(codecMicroservice,"inventoryApi",inventoryApi);
        ReflectionTestUtils.setField(codecMicroservice,"identityApi",identityApi);
        ReflectionTestUtils.setField(codecMicroservice,"contextService",contextService);
    }

    @Test
    public void shouldTestCreateDeviceType(){
        setUpIsDeviceTypeExists(false);

        ArgumentCaptor<ManagedObjectRepresentation> moArgumentCaptor = ArgumentCaptor.forClass(ManagedObjectRepresentation.class);
        verify(inventoryApi).create(moArgumentCaptor.capture());
        ManagedObjectRepresentation deviceTypeMo = moArgumentCaptor.getValue();
        Assert.assertEquals(deviceTypeMo.getType(), DeviceTypeEnum.LORA.getValue());
        Assert.assertEquals(deviceTypeMo.get("fieldbusType"), DeviceTypeEnum.LORA.getFieldbusType());
        Map<String, String> codecDeviceDetails = (Map<String, String>) deviceTypeMo.get(C8Y_LPWAN_CODEC_DETAILS);
        Assert.assertEquals(codecDeviceDetails.get(DEVICE_MANUFACTURER), codecMicroservice.supportsDevices().stream().findFirst().get().getAttributes().get(DEVICE_MANUFACTURER));
        Assert.assertEquals(codecDeviceDetails.get(DEVICE_MODEL), codecMicroservice.supportsDevices().stream().findFirst().get().getAttributes().get(DEVICE_MODEL));
        Assert.assertEquals(deviceTypeMo.getName(), codecMicroservice.supportsDevices().stream().findFirst().get().getDeviceTypeName());

        ArgumentCaptor<ExternalIDRepresentation> extIdArgumentCaptor = ArgumentCaptor.forClass(ExternalIDRepresentation.class);
        verify(identityApi).create(extIdArgumentCaptor.capture());
        ExternalIDRepresentation deviceTypeExtId = extIdArgumentCaptor.getValue();
        Assert.assertEquals(deviceTypeExtId.getType(), C8Y_SMART_REST_DEVICE_IDENTIFIER);
        Assert.assertEquals(deviceTypeExtId.getExternalId(), codecMicroservice.supportsDevices().stream().findFirst().get().getDeviceTypeName());
    }

    @Test
    public void shouldTestDeviceTypeIsAlreadyCreated(){
        setUpIsDeviceTypeExists(true);

        verify(inventoryApi,never()).create(any(ManagedObjectRepresentation.class));
        verify(identityApi,never()).create(any(ExternalIDRepresentation.class));
    }

    private void setUpIsDeviceTypeExists(boolean isDeviceTypeExists) {
        MicroserviceCredentials credentials = new MicroserviceCredentials("tenant", "username", "password", null, null, null, "appKey");
        when(microserviceSubscriptionAddedEvent.getCredentials()).thenReturn(credentials);
        if(isDeviceTypeExists) {
            when(identityApi.getExternalId(any(ID.class))).thenReturn(externalIDRepresentation);
        } else{
            when(identityApi.getExternalId(any(ID.class))).thenThrow(new SDKException(HttpStatus.NOT_FOUND.toString()));
        }

        codecMicroservice.registerDeviceTypes(microserviceSubscriptionAddedEvent);

        ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(contextService).runWithinContext(eq(credentials), taskCaptor.capture());
        Runnable task = taskCaptor.getValue();
        task.run();
    }
}