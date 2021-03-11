package com.cumulocity.lpwan.payload.service;

import com.cumulocity.lpwan.devicetype.model.UplinkConfiguration;
import com.cumulocity.lpwan.payload.exception.PayloadDecodingFailedException;
import com.cumulocity.lpwan.payload.uplink.model.MessageIdConfiguration;
import com.cumulocity.lpwan.payload.uplink.model.UplinkMessage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PayloadDecoderServiceTest {

    private PayloadDecoderService<UplinkMessage> payloadDecoderService = new PayloadDecoderServiceImpl(null);

    private class PayloadDecoderServiceImpl extends PayloadDecoderService<UplinkMessage> {

        public PayloadDecoderServiceImpl(PayloadMappingService payloadMappingService) {
            super(payloadMappingService, new MessageIdReader<UplinkMessage>() {
                @Override
                public Integer read(UplinkMessage uplinkMessage, MessageIdConfiguration messageIdConfiguration) {
                    return null;
                }
            });
        }
    }

    @Test
    public void decodeLittleEndianHex() {
        try {
            UplinkConfiguration uplinkConfiguration = new UplinkConfiguration();
            uplinkConfiguration.setLittleEndian(true);
            uplinkConfiguration.setStartBit(0);
            uplinkConfiguration.setNoBits(32);
            uplinkConfiguration.setMultiplier(1.0);
            uplinkConfiguration.setOffset(0.0);

            Double actualVal = payloadDecoderService.decodeByConfiguration("5A109061", uplinkConfiguration);
            Double expectedVal = 1636831322.0;
            assertEquals(expectedVal, actualVal);

            uplinkConfiguration.setStartBit(8);
            uplinkConfiguration.setNoBits(16);
            actualVal = payloadDecoderService.decodeByConfiguration("5A109061", uplinkConfiguration);
            expectedVal = 36880.0;
            assertEquals(expectedVal, actualVal);

            uplinkConfiguration.setSigned(true);
            actualVal = payloadDecoderService.decodeByConfiguration("5A109061", uplinkConfiguration);
            expectedVal = -28656.0;
            assertEquals(expectedVal, actualVal);

            uplinkConfiguration.setBcd(true);
            actualVal = payloadDecoderService.decodeByConfiguration("5A109061", uplinkConfiguration);
            expectedVal = -990.0;
            assertEquals(expectedVal, actualVal);

        } catch (PayloadDecodingFailedException e) {
            fail();
        }
    }


    @Test
    public void decodeWithMultiplierAndOffset() {
        try {
            UplinkConfiguration uplinkConfiguration = new UplinkConfiguration();
            uplinkConfiguration.setStartBit(0);
            uplinkConfiguration.setNoBits(16);
            uplinkConfiguration.setMultiplier(1.0);
            uplinkConfiguration.setOffset(0.0);

            Double actualVal = payloadDecoderService.decodeByConfiguration("9062", uplinkConfiguration);
            Double expectedVal = 36962.0;
            assertEquals(expectedVal, actualVal);

            uplinkConfiguration.setMultiplier(0.5);
            actualVal = payloadDecoderService.decodeByConfiguration("9062", uplinkConfiguration);
            expectedVal = 18481.0;
            assertEquals(expectedVal, actualVal);

            uplinkConfiguration.setOffset(1.0);
            actualVal = payloadDecoderService.decodeByConfiguration("9062", uplinkConfiguration);
            expectedVal = 18482.0;
            assertEquals(expectedVal, actualVal);

            uplinkConfiguration.setBcd(true);
            actualVal = payloadDecoderService.decodeByConfiguration("9062", uplinkConfiguration);
            expectedVal = 4532.0;
            assertEquals(expectedVal, actualVal);

        } catch (PayloadDecodingFailedException e) {
            fail();
        }
    }
}
