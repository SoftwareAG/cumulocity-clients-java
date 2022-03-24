package com.cumulocity.microservice.lpwan.codec.sample;

import com.cumulocity.microservice.customdecoders.api.service.DecoderService;
import com.cumulocity.microservice.customencoders.api.service.EncoderService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class TestBeanConfiguration {
    @MockBean
    DecoderService decoderService;

    @MockBean
    EncoderService encoderService;
}
