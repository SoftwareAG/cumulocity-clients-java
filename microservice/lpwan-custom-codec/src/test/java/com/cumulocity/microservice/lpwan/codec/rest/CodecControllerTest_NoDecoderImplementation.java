package com.cumulocity.microservice.lpwan.codec.rest;

import com.cumulocity.microservice.customdecoders.api.model.DecoderInputData;
import com.cumulocity.microservice.customdecoders.api.model.DecoderResult;
import com.cumulocity.microservice.customdecoders.api.service.DecoderService;
import com.cumulocity.microservice.customencoders.api.model.EncoderInputData;
import com.cumulocity.microservice.customencoders.api.model.EncoderResult;
import com.cumulocity.microservice.customencoders.api.service.EncoderService;
import com.cumulocity.microservice.lpwan.codec.decoder.model.LpwanDecoderInputData;
import com.cumulocity.microservice.lpwan.codec.encoder.model.LpwanEncoderInputData;
import com.cumulocity.microservice.lpwan.codec.handler.CodecExceptionsHandler;
import com.cumulocity.microservice.lpwan.codec.model.DeviceInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashMap;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodecController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(CodecExceptionsHandler.class)
@EnableWebMvc
// Application starts with only EncoderService implementation
public class CodecControllerTest_NoDecoderImplementation {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EncoderService encoderService;

    @Captor
    private ArgumentCaptor<DecoderInputData> decoderInputCapture;

    private ObjectMapper jsonObjectMapper = new ObjectMapper();

    // The goal of this test is to prove that the Decode API call fails with HTTP 405 error (UnsupportedOperationException) as there is no DecoderService implementation.
    @Test
    public void doCallDecodeAPI_withValidJSONInput_NoDecoderService_Failure() throws Exception {
        DecoderInputData decoderInputData = new DecoderInputData();
        decoderInputData.setValue("HEX VALUE");
        decoderInputData.setSourceDeviceId("SOURCE_DEVICE_ID");

        HashMap<String, String> args = new HashMap<>();
        args.put(LpwanDecoderInputData.SOURCE_DEVICE_EUI_KEY, "SOURCE_DEVICE_EUI_ID");
        args.put(DeviceInfo.DEVICE_MANUFACTURER, "MANUFACTURER_1");
        args.put(DeviceInfo.DEVICE_MODEL, "MODEL_1");
        decoderInputData.setArgs(args);

        DecoderResult decoderResult = new DecoderResult();
        decoderResult.setSuccess(true);

        this.mockMvc.perform(post("/decode")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonObjectMapper.writeValueAsString(decoderInputData)))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    // This test succeeds as there is a EncoderService implementation
    @Test
    public void doCallEncodeAPI_withValidJSONInput_NoDecoderService_Success() throws Exception {
        EncoderInputData encoderInputData = new EncoderInputData();
        encoderInputData.setSourceDeviceId("SOURCE_DEVICE_ID");
        encoderInputData.setCommandName("set config");
        encoderInputData.setCommandData("{\"set config\":{\"breakpoint\":\"false\",\"selfadapt\":\"true\",\"oneoff\":\"false\",\"alreport\":\"false\",\"pos\":\"0\",\"hb\":\"10\"}}");

        HashMap<String, String> args = new HashMap<>();
        args.put(LpwanEncoderInputData.SOURCE_DEVICE_EUI_KEY, "SOURCE_DEVICE_EUI_ID");
        args.put(DeviceInfo.DEVICE_MANUFACTURER, "MANUFACTURER_1");
        args.put(DeviceInfo.DEVICE_MODEL, "MODEL_1");
        encoderInputData.setArgs(args);

        EncoderResult encoderResult = new EncoderResult();
        encoderResult.setSuccess(true);

        when(encoderService.encode(encoderInputData)).thenReturn(encoderResult);

        this.mockMvc.perform(post("/encode")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonObjectMapper.writeValueAsString(encoderInputData)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonObjectMapper.writeValueAsString(encoderResult), true));

        verify(encoderService).encode(encoderInputData);
    }
}
