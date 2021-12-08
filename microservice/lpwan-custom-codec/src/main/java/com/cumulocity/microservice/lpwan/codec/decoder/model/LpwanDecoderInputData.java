/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.microservice.lpwan.codec.decoder.model;

import com.cumulocity.microservice.customdecoders.api.model.DecoderInputData;
import com.cumulocity.microservice.lpwan.codec.model.DeviceInfo;
import com.cumulocity.model.idtype.GId;
import com.google.common.base.Strings;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.*;

/**
 * The <b>LpwanDecoderInputData</b> class represents the format and content of the request coming in for decoding a device data.
 */
public class LpwanDecoderInputData extends DecoderInputData {
    public static final String SOURCE_DEVICE_EUI_KEY = "sourceDeviceEui";
    public static final String DEVICE_MANUFACTURER_KEY = "deviceManufacturer";
    public static final String DEVICE_MODEL_KEY = "deviceModel";
    public static final String F_PORT_KEY = "fport";
    public static final String UPDATE_TIME_KEY = "updateTime";

    /**
     * Instantiates a new LpwanDecoderInputData.
     *
     * @param sourceDeviceId   the managed object id of the device
     * @param sourceDeviceEui  represents the eui of the device
     * @param sourceDeviceInfo contains the information about the device such as Manufacturer or model of the device
     * @param inputData        represents the actual payload data as a HEX string
     * @param fPort            represents the port number received as an input
     * @param updateTime       represents the Date and Time when the payload is sent
     */
    public LpwanDecoderInputData(@NotBlank String sourceDeviceId,
                                 @NotBlank String sourceDeviceEui,
                                 @NotNull DeviceInfo sourceDeviceInfo,
                                 @NotBlank String inputData,
                                 @Null Integer fPort,
                                 @Null Long updateTime) {

        initializeAndValidate(sourceDeviceId, sourceDeviceEui, sourceDeviceInfo, inputData, fPort, updateTime, null);
    }

    /**
     * Instantiates a new LpwanDecoderInputData.
     *
     * @param inputData      represents the actual payload data as a HEX string
     * @param sourceDeviceId the managed object id of the device
     * @param args           represents the properties
     */
    public LpwanDecoderInputData(@NotBlank String inputData,
                                 @NotNull GId sourceDeviceId,
                                 @NotNull Map<String, String> args) {

        String sourceDeviceIdString = null;
        if (Objects.nonNull(sourceDeviceId)) {
            sourceDeviceIdString = sourceDeviceId.getValue();
        }

        String sourceDeviceEui = null;
        DeviceInfo sourceDeviceInfo = null;
        Integer fPort = null;
        Long updateTime = null;

        if (Objects.nonNull(args)) {
            sourceDeviceEui = args.get(SOURCE_DEVICE_EUI_KEY);
            sourceDeviceInfo = new DeviceInfo(args.get(DEVICE_MANUFACTURER_KEY), args.get(DEVICE_MODEL_KEY));

            String updateTimeString = args.get(UPDATE_TIME_KEY);
            if (!Strings.isNullOrEmpty(updateTimeString)) {
                try {
                    updateTime = Long.valueOf(updateTimeString);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(e);
                }
            }

            String fPortString = args.get(F_PORT_KEY);
            if (!Strings.isNullOrEmpty(fPortString)) {
                try {
                    fPort = Integer.valueOf(fPortString);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }

        initializeAndValidate(sourceDeviceIdString, sourceDeviceEui, sourceDeviceInfo, inputData, fPort, updateTime, args);
    }

    private void initializeAndValidate(@NotBlank String sourceDeviceId,
                                       @NotBlank String sourceDeviceEui,
                                       @NotNull DeviceInfo sourceDeviceInfo,
                                       @NotBlank String inputData,
                                       @Null Integer fPort,
                                       @Null Long updateTime,
                                       @Null Map<String, String> inputArguments) {

        setSourceDeviceId(sourceDeviceId);
        setSourceDeviceEui(sourceDeviceEui);
        setSourceDeviceInfo(sourceDeviceInfo);
        setValue(inputData);
        setFPort(fPort);
        setUpdateTime(updateTime);

        if (Objects.nonNull(inputArguments)) {
            getInputArguments().putAll(inputArguments);
        }

        validate();
    }

    /**
     * Gets the source device eui
     *
     * @return the source device eui
     */
    public @NotBlank String getSourceDeviceEui() {
        return getInputArguments().get(SOURCE_DEVICE_EUI_KEY);
    }

    private void setSourceDeviceEui(@NotBlank String sourceDeviceEui) {
        getInputArguments().put(SOURCE_DEVICE_EUI_KEY, sourceDeviceEui);
    }

    /**
     * Gets the source device info that contains the information about the device such as Manufacturer or model of the device
     *
     * @return the source device info
     */
    public @NotNull DeviceInfo getSourceDeviceInfo() {
        return new DeviceInfo(getInputArguments().get(DEVICE_MANUFACTURER_KEY), getInputArguments().get(DEVICE_MODEL_KEY));
    }

    private void setSourceDeviceInfo(@NotNull DeviceInfo sourceDeviceInfo) {
        if (Objects.nonNull(sourceDeviceInfo)) {
            getInputArguments().put(DEVICE_MANUFACTURER_KEY, sourceDeviceInfo.getManufacturer());
            getInputArguments().put(DEVICE_MODEL_KEY, sourceDeviceInfo.getModel());
        }
    }

    /**
     * Gets update time.
     *
     * @return the update time
     */
    public @Null Long getUpdateTime() {
        String updateTimeString = getInputArguments().get(UPDATE_TIME_KEY);
        if (!Strings.isNullOrEmpty(updateTimeString)) {
            return Long.valueOf(updateTimeString);
        }

        return null;
    }

    private void setUpdateTime(@Null Long updateTime) {
        if (Objects.nonNull(updateTime)) {
            getInputArguments().put(UPDATE_TIME_KEY, updateTime.toString());
        }
    }

    /**
     * Gets f-port.
     *
     * @return the f-port
     */
    public @Null Integer getFPort() {
        String fPortString = getInputArguments().get(F_PORT_KEY);
        if (!Strings.isNullOrEmpty(fPortString)) {
            return Integer.valueOf(fPortString);
        }

        return null;
    }

    private void setFPort(@Null Integer fPort) {
        if (Objects.nonNull(fPort)) {
            getInputArguments().put(F_PORT_KEY, fPort.toString());
        }
    }

    private @NotNull Map<String, String> getInputArguments() {
        Map<String, String> args = super.getArgs();
        if (Objects.isNull(args)) {
            args = new HashMap<>();
            setArgs(args);
        }

        return args;
    }

    /**
     * This method validates the object fields.
     *
     * @throws IllegalArgumentException if the field marked with <b>@NotNull</b> or <b>@NotBlank</b> are either null or blank.
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/lang/IllegalArgumentException.html">IllegalArgumentException</a>
     */
    private void validate() {
        List<String> missingParameters = new ArrayList<>();

        if (Strings.isNullOrEmpty(getSourceDeviceId())) {
            missingParameters.add("'sourceDeviceId'");
        }

        if (Strings.isNullOrEmpty(getSourceDeviceEui())) {
            missingParameters.add("'sourceDeviceEui'");
        }

        if (Strings.isNullOrEmpty(getValue())) {
            missingParameters.add("'inputData'");
        }

        if (Objects.isNull(getSourceDeviceInfo())) {
            missingParameters.add("'sourceDeviceInfo'");
        }
        else {
            try {
                getSourceDeviceInfo().validate();
            } catch (IllegalArgumentException e) {
                missingParameters.add("'manufacturer and/or model'");
            }
        }

        if (!missingParameters.isEmpty()) {
            throw new IllegalArgumentException("DecoderInputData is missing mandatory fields: " + String.join(", ", missingParameters));
        }
    }
}