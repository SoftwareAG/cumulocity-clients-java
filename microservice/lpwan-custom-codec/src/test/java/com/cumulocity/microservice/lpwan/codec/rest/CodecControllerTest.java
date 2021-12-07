package com.cumulocity.microservice.lpwan.codec.rest;


import com.cumulocity.microservice.customdecoders.api.exception.DecoderServiceException;
import com.cumulocity.microservice.customdecoders.api.exception.InvalidInputDataException;
import com.cumulocity.microservice.customdecoders.api.model.DecoderInputData;
import com.cumulocity.microservice.customdecoders.api.model.DecoderResult;
import com.cumulocity.microservice.customdecoders.api.service.DecoderService;
import com.cumulocity.microservice.lpwan.codec.decoder.model.LpwanDecoderInputData;
import com.cumulocity.model.idtype.GId;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.NestedServletException;

import java.util.HashMap;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodecController.class)
@AutoConfigureMockMvc(addFilters = false)
@EnableWebMvc
public class CodecControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DecoderService decoderService;

    @Captor
    private ArgumentCaptor<DecoderInputData> decoderInputCapture;

    private ObjectMapper jsonObjectMapper = new ObjectMapper();

    @Test
    public void doCallDecodeAPI_withValidJSONInput_Success() throws Exception {
        DecoderInputData decoderInputData = new DecoderInputData();
        decoderInputData.setValue("HEX VALUE");
        decoderInputData.setSourceDeviceId("SOURCE_DEVICE_ID");

        HashMap<String, String> args = new HashMap<>();
        args.put(LpwanDecoderInputData.SOURCE_DEVICE_EUI_KEY, "SOURCE_DEVICE_EUI_ID");
        args.put(LpwanDecoderInputData.DEVICE_MANUFACTURER_KEY, "MANUFACTURER_1");
        args.put(LpwanDecoderInputData.DEVICE_MODEL_KEY, "MODEL_1");
        decoderInputData.setArgs(args);

        DecoderResult decoderResult = new DecoderResult();
        decoderResult.setSuccess(true);

        when(decoderService.decode(eq("HEX VALUE"), eq(GId.asGId("SOURCE_DEVICE_ID")), eq(args))).thenReturn(decoderResult);

        this.mockMvc.perform(post("/decode")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonObjectMapper.writeValueAsString(decoderInputData)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonObjectMapper.writeValueAsString(decoderResult), true));

        verify(decoderService).decode(eq("HEX VALUE"), eq(GId.asGId("SOURCE_DEVICE_ID")), eq(args));
    }

    @Test
    public void doCallDecodeAPI_withDecoderServiceException_Fail() throws Exception {
        DecoderInputData decoderInputData = new DecoderInputData();
        decoderInputData.setValue("HEX VALUE");
        decoderInputData.setSourceDeviceId("SOURCE_DEVICE_ID");

        HashMap<String, String> args = new HashMap<>();
        args.put(LpwanDecoderInputData.SOURCE_DEVICE_EUI_KEY, "SOURCE_DEVICE_EUI_ID");
        args.put(LpwanDecoderInputData.DEVICE_MANUFACTURER_KEY, "MANUFACTURER_1");
        args.put(LpwanDecoderInputData.DEVICE_MODEL_KEY, "MODEL_1");
        decoderInputData.setArgs(args);

        DecoderResult decoderResult = new DecoderResult();
        decoderResult.setSuccess(true);

        when(decoderService.decode(eq("HEX VALUE"), eq(GId.asGId("SOURCE_DEVICE_ID")), eq(args))).thenThrow(new DecoderServiceException(null, "Decoding failed.", DecoderResult.empty()));

        try {
            this.mockMvc.perform(post("/decode")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonObjectMapper.writeValueAsString(decoderInputData)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(jsonObjectMapper.writeValueAsString(decoderResult), true));
        } catch (NestedServletException e) {
            Assert.assertNotNull(e.getCause());
            Assert.assertEquals(DecoderServiceException.class, e.getCause().getClass());
            Assert.assertEquals("Decoding failed.", e.getCause().getMessage());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        verify(decoderService).decode(eq("HEX VALUE"), eq(GId.asGId("SOURCE_DEVICE_ID")), eq(args));
    }

    @Test
    public void doCallDecodeAPI_withInvalidJSONInput_Fail() throws Exception {
        DecoderInputData decoderInputData = new DecoderInputData();

        DecoderResult decoderResult = new DecoderResult();
        decoderResult.setSuccess(true);

        try {
            this.mockMvc.perform(post("/decode")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonObjectMapper.writeValueAsString(decoderInputData)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().json(jsonObjectMapper.writeValueAsString(decoderResult), true));
        } catch (NestedServletException e) {
            Assert.assertNotNull(e.getCause());
            Assert.assertEquals(InvalidInputDataException.class, e.getCause().getClass());
            Assert.assertEquals("DecoderInputData is missing mandatory fields: 'sourceDeviceId', 'sourceDeviceEui', 'inputData', 'manufacturer and/or model'", e.getCause().getMessage());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}