package com.cumulocity.lpwan.devicetype.service;

import c8y.LpwanDevice;
import com.cumulocity.lpwan.devicetype.model.DeviceType;
import com.cumulocity.lpwan.payload.exception.DeviceTypeObjectNotFoundException;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceTypePayloadConfigurerTest {

    @Mock
    private InventoryApi inventoryApi;

    @Mock
    private DeviceTypeMapper deviceTypeMapper;

    private DeviceTypePayloadConfigurer deviceTypePayloadConfigurer;

    private String tenantId = "tenantA";

    @BeforeEach
    public void setup() {
        deviceTypePayloadConfigurer = new DeviceTypePayloadConfigurer(inventoryApi, deviceTypeMapper);
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
        ManagedObjectRepresentation dummyDeviceTypeObject = new ManagedObjectRepresentation();
        when(inventoryApi.get(any(GId.class))).thenReturn(dummyDeviceTypeObject);

        when(deviceTypeMapper.convertManagedObjectToDeviceType(dummyDeviceTypeObject)).thenReturn(new DeviceType());

        ManagedObjectRepresentation managedObject = new ManagedObjectRepresentation();
        LpwanDevice lpwanDevice = new LpwanDevice();
        lpwanDevice.setType("inventory/managedObjects/18002");
        managedObject.set(lpwanDevice);

        deviceTypePayloadConfigurer.getDeviceTypeObject(tenantId, managedObject);

        verify(inventoryApi).get(eq(new GId("18002")));
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
}
