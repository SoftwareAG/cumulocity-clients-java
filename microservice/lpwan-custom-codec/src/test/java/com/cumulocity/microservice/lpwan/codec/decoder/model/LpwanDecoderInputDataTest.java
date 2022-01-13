package com.cumulocity.microservice.lpwan.codec.decoder.model;

import com.cumulocity.microservice.lpwan.codec.model.DeviceInfo;
import com.cumulocity.model.idtype.GId;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LpwanDecoderInputDataTest {

    @Test
    void doCreateLpwanDecoderInputData_FromRawData_Success() {
        long currentTimeMillis = System.currentTimeMillis();
        DeviceInfo sourceDeviceInfo = new DeviceInfo("MANUFACTURER_1", "MODEL_1");
        LpwanDecoderInputData inputData = new LpwanDecoderInputData("SOURCE_ID",
                "EUI_ID",
                sourceDeviceInfo,
                "HEX INPUT",
                Integer.valueOf(999),
                currentTimeMillis);

        assertEquals("SOURCE_ID", inputData.getSourceDeviceId());
        assertEquals("EUI_ID", inputData.getSourceDeviceEui());
        assertEquals(sourceDeviceInfo, inputData.getSourceDeviceInfo());
        assertEquals("HEX INPUT", inputData.getValue());
        assertEquals(999, inputData.getFport());
        assertEquals(currentTimeMillis, inputData.getUpdateTime());
    }

    @Test
    void doCreateLpwanDecoderInputData_FromRawData_MissingParameters_Fail() {
        long currentTimeMillis = System.currentTimeMillis();
        DeviceInfo sourceDeviceInfo = new DeviceInfo(null, null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new LpwanDecoderInputData(null,
                                null,
                                sourceDeviceInfo,
                                null,
                                null,
                                null));

        assertEquals("DecoderInputData is missing mandatory fields: 'sourceDeviceId', 'sourceDeviceEui', 'inputData', 'manufacturer and/or model'", exception.getMessage());
    }

    @Test
    void doCreateLpwanDecoderInputData_FromRawData_MissingParameters_Fail_2() {
        long currentTimeMillis = System.currentTimeMillis();
        DeviceInfo sourceDeviceInfo = null;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new LpwanDecoderInputData(null,
                        null,
                        sourceDeviceInfo,
                        null,
                        null,
                        null));

        assertEquals("DecoderInputData is missing mandatory fields: 'sourceDeviceId', 'sourceDeviceEui', 'inputData', 'manufacturer and/or model'", exception.getMessage());
    }

    @Test
    void doCreateLpwanDecoderInputData_FromMap_Success() {
        long currentTimeMillis = System.currentTimeMillis();

        HashMap<String, String> args = new HashMap<>();
        args.put(LpwanDecoderInputData.SOURCE_DEVICE_EUI_KEY, "EUI_ID");
        args.put(DeviceInfo.DEVICE_MANUFACTURER, "MANUFACTURER_1");
        args.put(DeviceInfo.DEVICE_MODEL, "MODEL_1");
        args.put(LpwanDecoderInputData.FPORT_KEY, "999");
        args.put(LpwanDecoderInputData.UPDATE_TIME_KEY, String.valueOf(currentTimeMillis));

        LpwanDecoderInputData inputData = new LpwanDecoderInputData("HEX INPUT", GId.asGId("SOURCE_ID"), args);

        assertEquals("SOURCE_ID", inputData.getSourceDeviceId());
        assertEquals("EUI_ID", inputData.getSourceDeviceEui());
        assertEquals("MANUFACTURER_1", inputData.getSourceDeviceInfo().getDeviceManufacturer());
        assertEquals("MODEL_1", inputData.getSourceDeviceInfo().getDeviceModel());
        assertEquals("HEX INPUT", inputData.getValue());
        assertEquals(999, inputData.getFport());
        assertEquals(currentTimeMillis, inputData.getUpdateTime());
        assertEquals(args, inputData.getArgs());
    }

    @Test
    void doCreateLpwanDecoderInputData_FromInvalidInput_Fail() {
        long currentTimeMillis = System.currentTimeMillis();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                        new LpwanDecoderInputData("HEX INPUT", GId.asGId("SOURCE_ID"), Collections.EMPTY_MAP));

        assertEquals("DecoderInputData is missing mandatory fields: 'sourceDeviceEui', 'manufacturer and/or model'", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () ->
                new LpwanDecoderInputData("HEX INPUT", GId.asGId("SOURCE_ID"), null));

        assertEquals("DecoderInputData is missing mandatory fields: 'sourceDeviceEui', 'manufacturer and/or model'", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () ->
                new LpwanDecoderInputData(null, null, null));

        assertEquals("DecoderInputData is missing mandatory fields: 'sourceDeviceId', 'sourceDeviceEui', 'inputData', 'manufacturer and/or model'", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () ->
                new LpwanDecoderInputData(null, null, null));

        HashMap<String, String> args = new HashMap<>();
        args.put(LpwanDecoderInputData.SOURCE_DEVICE_EUI_KEY, "EUI_ID");
        args.put(DeviceInfo.DEVICE_MANUFACTURER, "MANUFACTURER_1");
        args.put(DeviceInfo.DEVICE_MODEL, "MODEL_1");
        args.put(LpwanDecoderInputData.FPORT_KEY, "aaa");
        args.put(LpwanDecoderInputData.UPDATE_TIME_KEY, String.valueOf(currentTimeMillis));

        exception = assertThrows(IllegalArgumentException.class, () ->
                new LpwanDecoderInputData("HEX INPUT", GId.asGId("SOURCE_ID"), args));

        assertEquals("java.lang.NumberFormatException: For input string: \"aaa\"", exception.getMessage());

        args.put(LpwanDecoderInputData.FPORT_KEY, "999");
        args.put(LpwanDecoderInputData.UPDATE_TIME_KEY, "TIME");

        exception = assertThrows(IllegalArgumentException.class, () ->
                new LpwanDecoderInputData("HEX INPUT", GId.asGId("SOURCE_ID"), args));

        assertEquals("java.lang.NumberFormatException: For input string: \"TIME\"", exception.getMessage());
    }
}