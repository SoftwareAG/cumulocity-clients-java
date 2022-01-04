package com.cumulocity.microservice.lpwan.codec.encoder.model;

import com.cumulocity.microservice.lpwan.codec.decoder.model.LpwanDecoderInputData;
import com.cumulocity.microservice.lpwan.codec.model.DeviceInfo;
import com.cumulocity.model.idtype.GId;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LpwanEncoderInputDataTest {
    @Test
    void doCreateLpwanEncoderInputData_FromRawData_Success() {
        DeviceInfo sourceDeviceInfo = new DeviceInfo("MANUFACTURER_1", "MODEL_1");
        String commandName = "set config";
        String commandData = "{\"set config\":{\"breakpoint\":\"false\",\"selfadapt\":\"true\",\"oneoff\":\"false\",\"alreport\":\"false\",\"pos\":\"0\",\"hb\":\"10\"}}";

        LpwanEncoderInputData inputData = new LpwanEncoderInputData("SOURCE_ID",
                "EUI_ID",
                sourceDeviceInfo,
                commandName,
                commandData);

        assertEquals("SOURCE_ID", inputData.getSourceDeviceId());
        assertEquals("EUI_ID", inputData.getSourceDeviceEui());
        assertEquals(sourceDeviceInfo, inputData.getSourceDeviceInfo());
        assertEquals(commandName, inputData.getCommandName());
        assertEquals(commandData, inputData.getCommandData());
    }

    @Test
    void doCreateLpwanEncoderInputData_FromRawData_MissingParameters_Fail() {
        DeviceInfo sourceDeviceInfo = new DeviceInfo("MANUFACTURER_1", null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new LpwanEncoderInputData("SOURCE_ID",
                "EUI_ID",
                sourceDeviceInfo,
                null,
                null));
        assertEquals("EncoderInputData is missing mandatory fields: 'manufacturer, model and/or supportedCommands', 'commandName'", exception.getMessage());
    }

    @Test
    void doCreateLpwanEncoderInputData_FromRawData_MissingParameters_Fail_2() {
        DeviceInfo sourceDeviceInfo = null;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new LpwanEncoderInputData(null,
                        null,
                        sourceDeviceInfo,
                        "set config",
                        null));
        assertEquals("EncoderInputData is missing mandatory fields: 'sourceDeviceId', 'sourceDeviceEui', 'manufacturer, model and/or supportedCommands'", exception.getMessage());
    }

    @Test
    void doCreateLpwanEncoderInputData_FromMap_Success() {
        HashMap<String, String> args = new HashMap<>();
        args.put(LpwanDecoderInputData.SOURCE_DEVICE_EUI_KEY, "EUI_ID");
        args.put(DeviceInfo.DEVICE_MANUFACTURER, "MANUFACTURER_1");
        args.put(DeviceInfo.DEVICE_MODEL, "MODEL_1");

        String commandName = "set config";
        String commandData = "{\"set config\":{\"breakpoint\":\"false\",\"selfadapt\":\"true\",\"oneoff\":\"false\",\"alreport\":\"false\",\"pos\":\"0\",\"hb\":\"10\"}}";

        LpwanEncoderInputData inputData = new LpwanEncoderInputData(GId.asGId("SOURCE_ID"), commandName, commandData, args);

        assertEquals("SOURCE_ID", inputData.getSourceDeviceId());
        assertEquals("EUI_ID", inputData.getSourceDeviceEui());
        assertEquals("MANUFACTURER_1", inputData.getSourceDeviceInfo().getDeviceManufacturer());
        assertEquals("MODEL_1", inputData.getSourceDeviceInfo().getDeviceModel());
        assertEquals(commandName, inputData.getCommandName());
        assertEquals(commandData, inputData.getCommandData());
        assertEquals(args, inputData.getArgs());
    }

    @Test
    void doCreateLpwanEncoderInputData_FromMap_MissingParameters_Fail() {
        String commandName = "set config";
        String commandData = "{\"set config\":{\"breakpoint\":\"false\",\"selfadapt\":\"true\",\"oneoff\":\"false\",\"alreport\":\"false\",\"pos\":\"0\",\"hb\":\"10\"}}";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new LpwanEncoderInputData(GId.asGId("SOURCE_ID"), commandName, commandData, Collections.EMPTY_MAP));

        assertEquals("EncoderInputData is missing mandatory fields: 'sourceDeviceEui', 'manufacturer, model and/or supportedCommands'", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () ->
                new LpwanEncoderInputData(GId.asGId("SOURCE_ID"), commandName, commandData, null));

        assertEquals("EncoderInputData is missing mandatory fields: 'sourceDeviceEui', 'manufacturer, model and/or supportedCommands'", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () ->
                new LpwanEncoderInputData(null, null, null, null));

        assertEquals("EncoderInputData is missing mandatory fields: 'sourceDeviceId', 'sourceDeviceEui', 'manufacturer, model and/or supportedCommands', 'commandName'", exception.getMessage());
    }
}