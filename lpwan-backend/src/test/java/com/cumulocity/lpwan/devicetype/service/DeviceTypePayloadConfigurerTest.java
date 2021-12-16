package com.cumulocity.lpwan.devicetype.service;

import c8y.LpwanDevice;
import c8y.TypeExternalId;
import com.cumulocity.lpwan.devicetype.model.DeviceType;
import com.cumulocity.lpwan.payload.exception.DeviceTypeObjectNotFoundException;
import com.cumulocity.model.ID;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjects;
import com.cumulocity.sdk.client.identity.IdentityApi;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static com.cumulocity.lpwan.devicetype.service.DeviceTypePayloadConfigurer.C8Y_SMART_REST_DEVICE_IDENTIFIER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceTypePayloadConfigurerTest {

    @Mock
    IdentityApi identityApi;

    @Mock
    private InventoryApi inventoryApi;

    @Mock
    private DeviceTypeMapper deviceTypeMapper;

    private DeviceTypePayloadConfigurer deviceTypePayloadConfigurer;

    private String tenantId = "tenantA";

    @BeforeEach
    public void setup() {
        deviceTypePayloadConfigurer = new DeviceTypePayloadConfigurer(identityApi, inventoryApi, deviceTypeMapper);
    }

    @Test
    public void shouldThrowExceptionWhenLpwanFragmentNotFound() {
        ManagedObjectRepresentation managedObject = new ManagedObjectRepresentation();

        Throwable thrown = catchThrowable(() -> deviceTypePayloadConfigurer.getDeviceTypeObject(tenantId, managedObject));

        assertThat(thrown).isInstanceOf(DeviceTypeObjectNotFoundException.class);
    }

    @Test
    public void shouldThrowExceptionWhenLpwanTypeNotFound() {
        ManagedObjectRepresentation managedObject = new ManagedObjectRepresentation();
        LpwanDevice lpwanDevice = new LpwanDevice();
        managedObject.set(lpwanDevice);

        Throwable thrown = catchThrowable(() -> deviceTypePayloadConfigurer.getDeviceTypeObject(tenantId, managedObject));

        assertThat(thrown).isInstanceOf(DeviceTypeObjectNotFoundException.class);
    }

    @Test
    public void shouldThrowExceptionWhenLpwanTypeNotCorrect() {
        ManagedObjectRepresentation managedObject = new ManagedObjectRepresentation();
        LpwanDevice lpwanDevice = new LpwanDevice();
        lpwanDevice.setType("wrong-syntax-for-device-type-path");
        managedObject.set(lpwanDevice);

        Throwable thrown = catchThrowable(() -> deviceTypePayloadConfigurer.getDeviceTypeObject(tenantId, managedObject));

        assertThat(thrown).isInstanceOf(DeviceTypeObjectNotFoundException.class);
    }

    @Test
    public void shouldThrowExceptionWhenTypeInventoryObjectNotFound() {
        ManagedObjectRepresentation managedObject = new ManagedObjectRepresentation();
        LpwanDevice lpwanDevice = new LpwanDevice();
        lpwanDevice.setType("inventory/managedObjects/18002");
        managedObject.set(lpwanDevice);

        Throwable thrown = catchThrowable(() -> deviceTypePayloadConfigurer.getDeviceTypeObject(tenantId, managedObject));

        assertThat(thrown).isInstanceOf(DeviceTypeObjectNotFoundException.class);
        verify(inventoryApi).get(eq(new GId("18002")));
    }

    @Test
    public void shouldGetAndConvertDevice() throws DeviceTypeObjectNotFoundException {
        String deviceId = "18002";
        ManagedObjectRepresentation dummyDeviceTypeObject = getDeviceMo(deviceId, deviceId, false);

        ArgumentCaptor<ManagedObjectRepresentation> captor = ArgumentCaptor.forClass(ManagedObjectRepresentation.class);
        verify(inventoryApi).update(captor.capture());
        ManagedObjectRepresentation updatedDeviceMo = captor.getValue();
        LpwanDevice lpwanDevice1 = updatedDeviceMo.get(LpwanDevice.class);
        Assert.assertTrue(Objects.nonNull(lpwanDevice1.getTypeExternalId()));
        Assert.assertEquals(lpwanDevice1.getTypeExternalId().getType(), C8Y_SMART_REST_DEVICE_IDENTIFIER);

        verify(identityApi,never()).getExternalId(any(ID.class));
        verify(inventoryApi).get(eq(new GId(deviceId)));
        verify(deviceTypeMapper).convertManagedObjectToDeviceType(dummyDeviceTypeObject);

    }

    @Test
    public void shouldGetAndConvertDevice_WithTypeExternalId() throws DeviceTypeObjectNotFoundException {
        String deviceId = "18002";
        ManagedObjectRepresentation dummyDeviceTypeObject = getDeviceMo(deviceId, deviceId, true);

        ArgumentCaptor<ID> externalIdCaptor = ArgumentCaptor.forClass(ID.class);
        verify(identityApi).getExternalId(externalIdCaptor.capture());
        ID id = externalIdCaptor.getValue();
        Assert.assertEquals(id.getType(), "c8y_SmartRestDeviceIdentifier");
        Assert.assertEquals(id.getValue(), "DeviceType_ExternalId");

        verify(inventoryApi,never()).update(any(ManagedObjectRepresentation.class));
        verify(inventoryApi).get(eq(new GId(deviceId)));
        verify(deviceTypeMapper).convertManagedObjectToDeviceType(dummyDeviceTypeObject);
    }

    @Test
    public void shouldGetAndConvertDevice_WithTypeExternalId_WithUpdatedGId() throws DeviceTypeObjectNotFoundException {
        String oldDeviceId = "18002";
        String newDeviceId = "18003";
        ManagedObjectRepresentation dummyDeviceTypeObject = getDeviceMo(oldDeviceId, newDeviceId, true);

        ArgumentCaptor<ManagedObjectRepresentation> deviceMoCaptor = ArgumentCaptor.forClass(ManagedObjectRepresentation.class);
        verify(inventoryApi).update(deviceMoCaptor.capture());
        ManagedObjectRepresentation updatedDeviceMo = deviceMoCaptor.getValue();
        LpwanDevice lpwanDevice1 = updatedDeviceMo.get(LpwanDevice.class);
        Assert.assertEquals(lpwanDevice1.getType(), "inventory/managedObjects/"+newDeviceId);

        verify(deviceTypeMapper).convertManagedObjectToDeviceType(dummyDeviceTypeObject);
    }

    @Test
    public void shouldCacheDeviceType() throws DeviceTypeObjectNotFoundException {
        ManagedObjectRepresentation dummyDeviceTypeObject = new ManagedObjectRepresentation();
        when(inventoryApi.get(any(GId.class))).thenReturn(dummyDeviceTypeObject);

        when(deviceTypeMapper.convertManagedObjectToDeviceType(dummyDeviceTypeObject)).thenReturn(new DeviceType());

        ManagedObjectRepresentation managedObject = new ManagedObjectRepresentation();
        LpwanDevice lpwanDevice = new LpwanDevice();
        lpwanDevice.setType("inventory/managedObjects/18002");
        managedObject.set(lpwanDevice);

        deviceTypePayloadConfigurer.getDeviceTypeObject(tenantId, managedObject);
        deviceTypePayloadConfigurer.getDeviceTypeObject(tenantId, managedObject);

        verify(inventoryApi, times(1)).get(eq(new GId("18002")));
        verify(deviceTypeMapper, times(1)).convertManagedObjectToDeviceType(dummyDeviceTypeObject);

    }

    @Test
    public void shouldRefreshCacheDeviceType() throws DeviceTypeObjectNotFoundException {
        ManagedObjectRepresentation dummyDeviceTypeObject = new ManagedObjectRepresentation();
        when(inventoryApi.get(any(GId.class))).thenReturn(dummyDeviceTypeObject);

        when(deviceTypeMapper.convertManagedObjectToDeviceType(dummyDeviceTypeObject)).thenReturn(new DeviceType());

        ManagedObjectRepresentation managedObject = new ManagedObjectRepresentation();
        LpwanDevice lpwanDevice = new LpwanDevice();
        lpwanDevice.setType("inventory/managedObjects/18002");
        managedObject.set(lpwanDevice);

        deviceTypePayloadConfigurer.refreshDeviceTypeCache(tenantId, lpwanDevice);

        verify(inventoryApi, times(1)).get(eq(new GId("18002")));
        verify(deviceTypeMapper, times(1)).convertManagedObjectToDeviceType(dummyDeviceTypeObject);

    }

    private ManagedObjectRepresentation getDeviceMo(String oldDeviceId, String newDeviceId, boolean isTypeExternalIdSet) throws DeviceTypeObjectNotFoundException {
        ManagedObjectRepresentation dummyDeviceTypeObject = new ManagedObjectRepresentation();
        when(inventoryApi.get(any(GId.class))).thenReturn(dummyDeviceTypeObject);

        when(deviceTypeMapper.convertManagedObjectToDeviceType(dummyDeviceTypeObject)).thenReturn(new DeviceType());

        ManagedObjectRepresentation managedObject = new ManagedObjectRepresentation();
        LpwanDevice lpwanDevice = new LpwanDevice();
        lpwanDevice.setType("inventory/managedObjects/"+oldDeviceId);
        managedObject.set(lpwanDevice);

        if(isTypeExternalIdSet) {
            TypeExternalId typeExternalId = new TypeExternalId();
            typeExternalId.setExternalId("DeviceType_ExternalId");
            typeExternalId.setType("c8y_SmartRestDeviceIdentifier");

            lpwanDevice.setTypeExternalId(typeExternalId);

            ExternalIDRepresentation externalIDRepresentation = new ExternalIDRepresentation();
            externalIDRepresentation.setManagedObject(ManagedObjects.asManagedObject(GId.asGId(newDeviceId)));
            when(identityApi.getExternalId(any(ID.class))).thenReturn(externalIDRepresentation);
        }

        deviceTypePayloadConfigurer.getDeviceTypeObject(tenantId, managedObject);
        return dummyDeviceTypeObject;
    }
}
