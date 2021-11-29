/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.rest;

import com.cumulocity.lpwan.codec.model.DecoderInput;
import com.cumulocity.lpwan.codec.model.DecoderOutput;
import com.cumulocity.lpwan.codec.model.DeviceInfo;
import com.cumulocity.lpwan.codec.service.CodecService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodecRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@EnableWebMvc
public class CodecRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CodecService codecService;

    @Captor
    private ArgumentCaptor<DecoderInput> decoderInputCapture;

    private ObjectMapper jsonObjectMapper = new ObjectMapper();

    @Test
    public void doCallDecodeAPI_withValidJSONInput_Success() throws Exception {
        when(codecService.decode(any(DecoderInput.class))).thenReturn(new DecoderOutput());

        this.mockMvc.perform(post("/decode")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonObjectMapper.writeValueAsString(new DecoderInput())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonObjectMapper.writeValueAsString(new DecoderOutput()), true));

        verify(codecService).decode(decoderInputCapture.capture());
    }

    @Test
    public void doCallDecodeAPI_withValidJSONInput_1_Success() throws Exception {
        when(codecService.decode(any(DecoderInput.class))).thenReturn(new DecoderOutput());

        DecoderInput decoderInput = DecoderInput.builder()
                .deviceMoId("MO_ID")
                .deviceInfo(new DeviceInfo("Manufacturer_1", "Model_1"))
                .deviceEui("EUI ID")
                .fPort(123)
                .payload("PAYLOAD STRING")
                .updateTime(System.currentTimeMillis()).build();

        this.mockMvc.perform(post("/decode")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonObjectMapper.writeValueAsString(decoderInput)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonObjectMapper.writeValueAsString(new DecoderOutput()), true));

        verify(codecService).decode(decoderInputCapture.capture());
        DecoderInput decoderInputCaptureValue = decoderInputCapture.getValue();

        assertEquals(decoderInput.getDeviceMoId(), decoderInputCaptureValue.getDeviceMoId());
        assertEquals(decoderInput.getDeviceInfo(), decoderInputCaptureValue.getDeviceInfo());
        assertEquals(decoderInput.getDeviceEui(), decoderInputCaptureValue.getDeviceEui());
        assertEquals(decoderInput.getFPort(), decoderInputCaptureValue.getFPort());
        assertEquals(decoderInput.getPayload(), decoderInputCaptureValue.getPayload());
        assertEquals(decoderInput.getUpdateTime(), decoderInputCaptureValue.getUpdateTime());
    }

    @Test
    public void doCallDecodeAPI_withInvalidJSONInput_Fail() throws Exception {
        this.mockMvc.perform(post("/decode")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(codecService, never()).decode(decoderInputCapture.capture());
    }
}