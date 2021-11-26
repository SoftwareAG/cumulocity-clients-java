/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec;

import com.cumulocity.lpwan.codec.model.DeviceInfo;
import com.cumulocity.lpwan.codec.model.LpwanCodecDetails;
import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionAddedEvent;
import com.cumulocity.model.ID;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjects;
import com.cumulocity.sdk.client.identity.IdentityApi;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static com.cumulocity.lpwan.codec.util.Constants.*;

/**
 * CodecMicroservice should be implemented by the microservice implementer to advertise the devices it supports.
 */
@Slf4j
public abstract class CodecMicroservice {

    private static final String DEVICE_TYPE_NAME_FORMAT = "%s : %s";

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private IdentityApi identityApi;

    @Autowired
    private ContextService<Credentials> contextService;

    /**
     * This method should return a set of uniquely supported devices w.r.t the device manufacturer and the device model.
     *
     * @return Set<DeviceInfo>
     */
    public Set<DeviceInfo> supportsDevices() {
        throw new UnsupportedOperationException("Needs implementation for supportsDevices()");
    }

    /**
     * @return
     */
    public abstract String getMicroserviceContextPath();

    /**
     * This method should register a device type upon subscribing the codec microservice.
     *
     * @param event
     */
    @EventListener
    void registerDeviceTypes(MicroserviceSubscriptionAddedEvent event) {
        Set<DeviceInfo> supportedDevices = supportsDevices();
        for (DeviceInfo supportedDevice : supportedDevices) {
            contextService.runWithinContext(event.getCredentials(),
                    () -> {
                        try {
                            supportedDevice.validate();
                        } catch (IllegalArgumentException e) {
                            log.error("Device manufacturer and model are mandatory fields in the supported device.", e);
                            return;
                        }

                        String supportedDeviceTypeName = formDeviceTypeName(supportedDevice);
                        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails(supportedDevice.getManufacturer(), supportedDevice.getModel(), getMicroserviceContextPath());
                        Optional<ExternalIDRepresentation> deviceType = isDeviceTypeExists(supportedDevice);
                        if (!deviceType.isPresent()) {
                            log.info("Creating device type '{}' on codec microservice subscription", supportedDeviceTypeName);

                            String description = String.format("Device protocol that supports device model '%s' manufactured by '%s'", supportedDevice.getModel(), supportedDevice.getManufacturer());
                            ManagedObjectRepresentation deviceTypeMo = new ManagedObjectRepresentation();
                            deviceTypeMo.setName(supportedDeviceTypeName);
                            deviceTypeMo.set(description, DESCRIPTION);
                            deviceTypeMo.set(Collections.EMPTY_MAP, C8Y_IS_DEVICE_TYPE);
                            deviceTypeMo.setType(C8Y_LPWAN_DEVICE_TYPE);
                            deviceTypeMo.set(LPWAN_FIELDBUS_TYPE, FIELDBUS_TYPE);
                            deviceTypeMo.set(lpwanCodecDetails.getAttributes(), C8Y_LPWAN_CODEC_DETAILS);
                            try {
                                deviceTypeMo = inventoryApi.create(deviceTypeMo);
                            } catch (Exception e) {
                                log.error("Unable create device type with name '{}'", supportedDeviceTypeName, e);
                            }

                            //Register the External Id
                            ExternalIDRepresentation deviceTypeExternalId = new ExternalIDRepresentation();
                            deviceTypeExternalId.setExternalId(supportedDeviceTypeName);
                            deviceTypeExternalId.setType(C8Y_SMART_REST_DEVICE_IDENTIFIER);
                            deviceTypeExternalId.setManagedObject(deviceTypeMo);
                            try {
                                identityApi.create(deviceTypeExternalId);
                            } catch (Exception e) {
                                log.error("Unable create External Id for device type with name '{}'", supportedDeviceTypeName, e);
                            }

                            log.info("Created device type '{}' on codec microservice subscription", supportedDeviceTypeName);
                        } else {
                            log.info("Updating the device type with name '{}' as it already exists", supportedDeviceTypeName);

                            ManagedObjectRepresentation deviceTypeMo = ManagedObjects.asManagedObject(deviceType.get().getManagedObject().getId());
                            deviceTypeMo.set(lpwanCodecDetails.getAttributes(), C8Y_LPWAN_CODEC_DETAILS);
                            try {
                                inventoryApi.update(deviceTypeMo);
                            } catch (Exception e) {
                                log.error("Unable update device type with name '{}'", supportedDeviceTypeName, e);
                            }

                            log.info("Updated the device type with name '{}' as it already exists", supportedDeviceTypeName);
                        }
                    });
        }
    }

    private Optional<ExternalIDRepresentation> isDeviceTypeExists(DeviceInfo deviceInfo) {
        try {
            return Optional.of(identityApi.getExternalId(new ID(C8Y_SMART_REST_DEVICE_IDENTIFIER, formDeviceTypeName(deviceInfo))));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String formDeviceTypeName(DeviceInfo deviceInfo) {
        return String.format(DEVICE_TYPE_NAME_FORMAT, deviceInfo.getManufacturer(), deviceInfo.getModel());
    }

//    /**
//     * This method should delete a device type upon unsubscribing the codec microservice.
//     *
//     * @param event
//     */
//    @EventListener
//    void unregisterDeviceTypes(MicroserviceSubscriptionRemovedEvent event) {
//        log.info("Deleting device types as codec microservice is unsubscribed.");
//        Set<DeviceInfo> supportedDevices = supportsDevices();
//        Optional<MicroserviceCredentials> credentials = subscriptions.getCredentials(event.getTenant());
//        for (DeviceInfo supportedDevice : supportedDevices) {
//            contextService.runWithinContext(credentials.get(),
//                    () -> {
//                        String deviceTypeName = supportedDevice.getDeviceTypeName();
//                        try {
//                            GId deviceTypeId = identityApi.getExternalId(new ID(C8Y_SMART_REST_DEVICE_IDENTIFIER, deviceTypeName)).getManagedObject().getId();
//                            inventoryApi.delete(deviceTypeId);
//                        } catch (Exception e) {
//                            log.error("Unable to delete device type with name '{}'", deviceTypeName, e);
//                        }
//                    });
//        }
//    }
}
