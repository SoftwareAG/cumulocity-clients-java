/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.microservice.lpwan.codec;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.lpwan.codec.model.DeviceInfo;
import com.cumulocity.microservice.lpwan.codec.model.LpwanCodecDetails;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionAddedEvent;
import com.cumulocity.model.ID;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjects;
import com.cumulocity.sdk.client.identity.IdentityApi;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * The <b>CodecMicroservice</b> class mainly registers a device type when the codec microservice is subscribed.
 *
 */
@Slf4j
@ComponentScan
public class CodecMicroservice {

    /**
     * {@value #DEVICE_TYPE_DESCRIPTION_FORMAT} The Device type description format.
     */
    static final String DEVICE_TYPE_DESCRIPTION_FORMAT = "Device protocol that supports device model '%s' manufactured by '%s'";

    /**
     * {@value #C8Y_SMART_REST_DEVICE_IDENTIFIER} The device type external id type.
     */
    static final String C8Y_SMART_REST_DEVICE_IDENTIFIER = "c8y_SmartRestDeviceIdentifier";

    /**
     * {@value #C8Y_LPWAN_CODEC_DETAILS} The fragment name to be put into the device type managed object.
     */
    static final String C8Y_LPWAN_CODEC_DETAILS = "c8y_LpwanCodecDetails";

    static final String FIELDBUS_TYPE = "fieldbusType";

    static final String DESCRIPTION = "description";
    /**
     * {@value #C8Y_IS_DEVICE_TYPE} The fragment to mark the managed object as a device type.
     */
    static final String C8Y_IS_DEVICE_TYPE = "c8y_IsDeviceType";
    /**
     * {@value #C8Y_LPWAN_DEVICE_TYPE} The type assigned to the LPWAN agent supported device types.
     */
    static final String C8Y_LPWAN_DEVICE_TYPE = "c8y_LpwanDeviceType";
    /**
     * {@value #LPWAN_FIELDBUS_TYPE} The feildbus type assigned to the LPWAN supoorted device types.
     */
    static final String LPWAN_FIELDBUS_TYPE = "lpwan";

    static final String DEVICE_TYPE_NAME_FORMAT = "%s : %s";

    @Autowired
    private ContextService<Credentials> contextService;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private IdentityApi identityApi;

    @Autowired
    private Codec codec;

    /**
     * This method should create a device type when the codec microservice subscribed.
     *
     * @param event the microservice subscription notification.
     */
    @EventListener
    void registerDeviceTypes(MicroserviceSubscriptionAddedEvent event) {
        contextService.runWithinContext(event.getCredentials(),
                () -> {
                    if (Strings.isNullOrEmpty(codec.getMicroserviceContextPath())) {
                        log.error("CodecMicroservice#getMicroserviceContextPath method is incorrectly implemented. It is returning a null or an empty string. Skipping the Device Type creation for the tenant {}.",
                                event.getCredentials().getTenant());
                        return;
                    }

                    Set<DeviceInfo> supportedDevices = codec.supportsDevices();
                    for (DeviceInfo supportedDevice : supportedDevices) {
                        try {
                            supportedDevice.validate();
                        } catch (IllegalArgumentException e) {
                            log.error("Device manufacturer and model are mandatory fields in the supported device. Skipping the Device Type creation.", e);
                            return;
                        }

                        String supportedDeviceTypeName = formDeviceTypeName(supportedDevice);
                        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails(supportedDevice.getManufacturer(), supportedDevice.getModel(), codec.getMicroserviceContextPath());
                        Optional<ExternalIDRepresentation> deviceType = isDeviceTypeExists(supportedDevice);
                        if (!deviceType.isPresent()) {
                            log.info("Creating device type '{}' on codec microservice subscription", supportedDeviceTypeName);

                            String description = String.format(DEVICE_TYPE_DESCRIPTION_FORMAT, supportedDevice.getModel(), supportedDevice.getManufacturer());
                            ManagedObjectRepresentation deviceTypeMo = new ManagedObjectRepresentation();
                            deviceTypeMo.setName(supportedDeviceTypeName);
                            deviceTypeMo.set(description, DESCRIPTION);
                            deviceTypeMo.set(Collections.EMPTY_MAP, C8Y_IS_DEVICE_TYPE);
                            deviceTypeMo.setType(C8Y_LPWAN_DEVICE_TYPE);
                            deviceTypeMo.set(LPWAN_FIELDBUS_TYPE, FIELDBUS_TYPE);
                            deviceTypeMo.set(lpwanCodecDetails.getAttributes(), C8Y_LPWAN_CODEC_DETAILS);
                            try {
                                deviceTypeMo = inventoryApi.create(deviceTypeMo);

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
                            } catch (Exception e) {
                                log.error("Unable create device type with name '{}'", supportedDeviceTypeName, e);
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
                    }
                });
    }

    private Optional<ExternalIDRepresentation> isDeviceTypeExists(DeviceInfo deviceInfo) {
        try {
            return Optional.of(identityApi.getExternalId(new ID(C8Y_SMART_REST_DEVICE_IDENTIFIER, formDeviceTypeName(deviceInfo))));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * This method forms the device type name.
     *
     * @param deviceInfo carries the device manufacturer name and the device model.
     * @return string device type name
     */
    String formDeviceTypeName(DeviceInfo deviceInfo) {
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
