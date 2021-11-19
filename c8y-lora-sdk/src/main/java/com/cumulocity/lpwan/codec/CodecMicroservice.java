/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec;

import com.cumulocity.lpwan.codec.model.DeviceInfo;
import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionAddedEvent;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionRemovedEvent;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.cumulocity.model.ID;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.identity.IdentityApi;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.util.*;

import static com.cumulocity.lpwan.codec.util.Constants.*;

/**
 * CodecMicroservice should be implemented by the microservice implementer to advertise the devices it supports.
 */
@Slf4j
public abstract class CodecMicroservice {

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private IdentityApi identityApi;

    @Autowired
    private MicroserviceSubscriptionsService subscriptions;

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
    abstract String getMicroserviceContextPath();

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
                        String supportedDeviceTypeName = supportedDevice.getDeviceTypeName();
                        if (!isDeviceTypeExists(supportedDevice)) {
                            log.info("Creating device type '{}' on codec microservice subscription", supportedDeviceTypeName);
                            String description = String.format("Device protocol that supports device model '%s' manufactured by '%s'", supportedDevice.getModel(), supportedDevice.getManufacturer());
                            ManagedObjectRepresentation deviceTypeMo = new ManagedObjectRepresentation();
                            deviceTypeMo.setName(supportedDeviceTypeName);
                            deviceTypeMo.set(description, DESCRIPTION);
                            deviceTypeMo.set(Collections.EMPTY_MAP, C8Y_IS_DEVICE_TYPE);
                            deviceTypeMo.setType(supportedDevice.getType().getValue());
                            deviceTypeMo.set(supportedDevice.getType().getFieldbusType(), FIELDBUS_TYPE);
                            Map<String, String> lpwanCodecDetails = new HashMap<>(supportedDevice.getAttributes());
                            lpwanCodecDetails.put(CODEC_SERVICE_CONTEXT_PATH, getMicroserviceContextPath());
                            deviceTypeMo.set(lpwanCodecDetails, C8Y_LPWAN_CODEC_DETAILS);
                            try {
                                deviceTypeMo = inventoryApi.create(deviceTypeMo);
                            } catch (Exception e) {
                                log.error("Unable create device type with name '{}'", supportedDeviceTypeName);
                            }

                            //Register the External Id
                            ExternalIDRepresentation deviceTypeExternalId = new ExternalIDRepresentation();
                            deviceTypeExternalId.setExternalId(supportedDeviceTypeName);
                            deviceTypeExternalId.setType(C8Y_SMART_REST_DEVICE_IDENTIFIER);
                            deviceTypeExternalId.setManagedObject(deviceTypeMo);
                            try {
                                identityApi.create(deviceTypeExternalId);
                            } catch (Exception e) {
                                log.error("Unable create External Id for device type with name '{}'", supportedDeviceTypeName);
                            }
                        } else {
                            log.info("Skipping the creation of device type with name '{}' as it already exists", supportedDeviceTypeName);
                        }
                    });
        }
    }

    private boolean isDeviceTypeExists(DeviceInfo deviceInfo) {
        try {
            identityApi.getExternalId(new ID(C8Y_SMART_REST_DEVICE_IDENTIFIER, deviceInfo.getDeviceTypeName()));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

//    /**
//     * This method should delete a device type upon unsubscribing the codec microservice.
//     *
//     * @param event
//     */
//    @EventListener
//    void unregisterDeviceTypes(MicroserviceSubscriptionRemovedEvent event) {
//        log.info("Deleting device types on codec microservice unsubscription");
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
//                            log.error("Unable to delete device type with name '{}'", deviceTypeName);
//                        }
//                    });
//        }
//    }
}
