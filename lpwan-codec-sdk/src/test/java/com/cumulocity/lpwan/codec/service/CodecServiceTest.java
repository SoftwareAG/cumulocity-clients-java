/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.service;

import com.cumulocity.lpwan.codec.decoder.Decoder;
import com.cumulocity.lpwan.codec.decoder.exception.DecoderException;
import com.cumulocity.lpwan.codec.decoder.model.DecoderInput;
import com.cumulocity.lpwan.codec.decoder.model.DecoderOutput;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class CodecServiceTest {

    @InjectMocks
    private CodecService codecService;

    @Mock
    private Decoder decoder;

    @Mock
    private DecoderInput decoderInput;

    @Test
    public void doCallDecode_withValidDecoderInput_Success() throws Exception {
        doNothing().when(decoderInput).validate();
        when(decoder.decode(decoderInput)).thenReturn(new DecoderOutput());

        codecService.decode(decoderInput);

        verify(decoderInput).validate();
        verify(decoderInput).getDeviceMoId();
        verify(decoderInput).getPayload();
        verify(decoder).decode(decoderInput);
    }

    @Test
    public void doCallDecode_whenDecoderThrowsDecoderException_Fail() throws Exception {
        doNothing().when(decoderInput).validate();
        when(decoder.decode(decoderInput)).thenThrow(new DecoderException("Some error."));

        try {
            codecService.decode(decoderInput);
        } catch (IllegalArgumentException e) {
            Assertions.fail(e);
        } catch (DecoderException e) {
            // expected
        }

        verify(decoderInput).validate();
        verify(decoderInput).getDeviceMoId();
        verify(decoderInput).getPayload();
        verify(decoder).decode(decoderInput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void doCallDecode_withNullDecoderInput_Fail() throws DecoderException {
        codecService.decode(null);
    }

    @Test
    public void doCallDecode_withInvalidDecoderInput_Fail() throws Exception {
        doThrow(new IllegalArgumentException()).when(decoderInput).validate();
        when(decoder.decode(decoderInput)).thenReturn(new DecoderOutput());

        try {
            codecService.decode(decoderInput);
        } catch (IllegalArgumentException e) {
            // expected
        } catch (DecoderException e) {
            Assertions.fail(e);
        }

        verify(decoderInput).validate();
        verify(decoderInput, never()).getDeviceMoId();
        verify(decoderInput, never()).getPayload();
        verify(decoder, never()).decode(decoderInput);
    }
}