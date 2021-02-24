package com.cumulocity.lpwan.devicetype.service;

import c8y.LpwanDevice;
import com.cumulocity.lpwan.devicetype.model.DeviceType;
import com.cumulocity.lpwan.payload.exception.DeviceTypeObjectNotFoundException;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DeviceTypePayloadConfigurer {

    protected final InventoryApi inventoryApi;

    protected final DeviceTypeMapper deviceTypeMapper;

    private LoadingCache<ImmutablePair<String, GId>, Optional<DeviceType>> deviceTypes = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<ImmutablePair<String, GId>, Optional<DeviceType>>() {
                        @Override
                        public Optional<DeviceType> load(ImmutablePair<String, GId> tenantAnddeviceTypePair) throws Exception {
                            ManagedObjectRepresentation deviceTypeObject = inventoryApi.get(tenantAnddeviceTypePair.getValue());
                            if (deviceTypeObject == null) {
                                return Optional.absent();
                            }
                            return Optional.fromNullable(deviceTypeMapper.convertManagedObjectToDeviceType(deviceTypeObject));
                        }
                    }
            );

    public DeviceTypePayloadConfigurer(InventoryApi inventoryApi, DeviceTypeMapper deviceTypeMapper) {
        this.inventoryApi = inventoryApi;
        this.deviceTypeMapper = deviceTypeMapper;
    }

    public DeviceType getDeviceTypeObject(String tenantId, ManagedObjectRepresentation device) throws DeviceTypeObjectNotFoundException {
        try {
            LpwanDevice lpwanDevice = device.get(LpwanDevice.class);
            if (lpwanDevice == null) {
                throw new DeviceTypeObjectNotFoundException("LpwanDevice fragment is missing for device " + device.getId());
            } else if (StringUtils.isBlank(lpwanDevice.getType())) {
                throw new DeviceTypeObjectNotFoundException("LpwanDevice.type field is missing for device " + device.getId());
            } else {
                GId deviceTypeId = LpwanDeviceTypeUtil.getTypeId(lpwanDevice);
                if (deviceTypeId == null) {
                    throw new DeviceTypeObjectNotFoundException("Device type id could not be parsed" + lpwanDevice.getType());
                }
                ImmutablePair<String, GId> tenantAndDeviceTypePair = new ImmutablePair<>(tenantId, deviceTypeId);
                Optional<DeviceType> deviceType = deviceTypes.get(tenantAndDeviceTypePair);
                if (deviceType.isPresent()) {
                    return deviceType.get();
                } else {
                    throw new DeviceTypeObjectNotFoundException("Device type managed object not found for " + deviceTypeId);
                }
            }
        } catch (ExecutionException e) {
            log.error(Throwables.getStackTraceAsString(e));
            throw new DeviceTypeObjectNotFoundException("Error retrieving device type");
        }
    }

    public void refreshDeviceTypeCache(String tenantId, LpwanDevice lpwanDevice) {
        GId deviceTypeId = LpwanDeviceTypeUtil.getTypeId(lpwanDevice);
        ImmutablePair<String, GId> tenantAndDeviceTypePair = new ImmutablePair<>(tenantId, deviceTypeId);
        deviceTypes.refresh(tenantAndDeviceTypePair);
    }
}
