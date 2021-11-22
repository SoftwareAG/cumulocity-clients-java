/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.service;

import com.cumulocity.lpwan.codec.Decoder;
import com.cumulocity.lpwan.codec.exception.DecoderException;
import com.cumulocity.lpwan.codec.model.DecodePayload;
import com.cumulocity.lpwan.codec.model.DecodeResponse;
import com.cumulocity.lpwan.codec.model.DeviceInfo;
import com.cumulocity.lpwan.codec.model.DeviceTypeEnum;
import com.cumulocity.microservice.context.inject.TenantScope;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.cumulocity.lpwan.codec.util.Constants.*;

@Service
@Slf4j
@TenantScope
public class CodecService {

    @Autowired
    private Decoder decoder;

    @Autowired
    private InventoryApi inventoryApi;

//    private LoadingCache<GId, ManagedObjectRepresentation> devicesCache = CacheBuilder.newBuilder()
//            .expireAfterWrite(10, TimeUnit.MINUTES)
//            .build(
//                    new CacheLoader<>() {
//                        @Override
//                        public ManagedObjectRepresentation load(GId deviceMoId) {
//                            try {
//                                return inventoryApi.get(deviceMoId);
//                            } catch (Exception e) {
//                                return null;
//                            }
//                        }
//                    }
//            );
//
//    private LoadingCache<GId, ManagedObjectRepresentation> deviceTypesCache = CacheBuilder.newBuilder()
//            //The 'expireAfterWrite' is not set intentionally as the device types are never updated by the user.
//            .build(
//                    new CacheLoader<>() {
//                        @Override
//                        public ManagedObjectRepresentation load(GId deviceTypeMoId) {
//                            try {
//                                return inventoryApi.get(deviceTypeMoId);
//                            } catch (Exception e) {
//                                return null;
//                            }
//                        }
//                    }
//            );

    /**
     * This method should decode the payload received by a particular device.
     *
     * @param payload
     * @return DecodedData
     */
    public DecodeResponse decode(DecodePayload payload) throws DecoderException {
        log.debug("Forwarding decoding request for the device with Id {} with payload {}", payload.getDeviceMoId(), payload.getPayload());

//        // Fetch device mo
//        ManagedObjectRepresentation deviceMo = null;
//        try {
//            deviceMo = devicesCache.get(GId.asGId(payload.getDeviceMoId()));
//            if (deviceMo == null) {
//                throw new DecoderException("Unable find the device with id {}", payload.getDeviceMoId());
//            }
//        } catch (ExecutionException e) {
//            throw new DecoderException("Unable find the device with id {}", e, payload.getDeviceMoId());
//        }
//
//        // Fetch the device type mo from the details in device mo
//        Map<String, String> deviceTypeDetails = (Map<String, String>) deviceMo.get(C8Y_LPWAN_DEVICE);
//        String deviceTypeUri = deviceTypeDetails.get("type");
//        String deviceTypeId = deviceTypeUri.substring(deviceTypeUri.lastIndexOf('/') + 1);
//        ManagedObjectRepresentation deviceTypeMo = null;
//        try {
//            deviceTypeMo = deviceTypesCache.get(GId.asGId(deviceTypeId));
//            if (deviceTypeMo == null) {
//                throw new DecoderException("Unable find the device type with id {}", deviceTypeId);
//            }
//        } catch (ExecutionException e) {
//            throw new DecoderException("Unable find the device type with id {}", e, deviceTypeId);
//        }
//
//        // From the device type mo create a deviceinfo.
//        Map<String, String> lpawanCodecDeviceInfo = (Map<String, String>) deviceTypeMo.get(C8Y_LPWAN_CODEC_DETAILS);
//        DeviceInfo deviceInfo = new DeviceInfo(lpawanCodecDeviceInfo.get(DEVICE_MANUFACTURER), lpawanCodecDeviceInfo.get(DEVICE_MODEL), DeviceTypeEnum.valueOf(lpawanCodecDeviceInfo.get(FIELDBUS_TYPE).toUpperCase()));

        return decoder.decode(payload);
    }
}
