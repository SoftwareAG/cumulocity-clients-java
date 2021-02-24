package com.cumulocity.lpwan.payload.service;

import static org.junit.Assert.*;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.cumulocity.lpwan.payload.exception.PayloadDecodingFailedException;

public class DecoderUtilTest {

    @Test
    public void addNegativeOffset() {
        Double value = (double) -19999;
        Double offset = (double) -1;
        Double expectedResult = (double) -20000;
        Double actualResult = DecoderUtil.offset(value, offset);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void addNegativeMultiplier() {
        Double value = (double) -19999;
        Double multiplier = -1.1;
        Double expectedResult = 21998.9;
        Double actualResult = DecoderUtil.multiply(value, multiplier);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void bcdToNumber() {
        String hex = "45";
        Integer expectedResult = 45;

        try {
            Integer actualResult = DecoderUtil.extractBCDFromHex(hex, 0, 8);
            assertEquals(expectedResult, actualResult);
        } catch (PayloadDecodingFailedException e) {
            fail();
        }
    }

    @Test
    public void decimalFromHex() {
        String hex = "0103A1DE79C1";
        Integer expectedInt_from_8_to_32_bits = 60939897;

        try {
            Integer actualInt_from_8_to_32_bits = DecoderUtil.extractDecimalFromHex(hex, 8, 32);
            assertEquals(expectedInt_from_8_to_32_bits, actualInt_from_8_to_32_bits);
        } catch (PayloadDecodingFailedException e) {
            fail();
        }
    }

    @Test(expected = PayloadDecodingFailedException.class)
    public void shouldThrowExceptionIfPayloadLengthNotSufficientInExtractDecimalFromHex() throws PayloadDecodingFailedException {
        DecoderUtil.extractDecimalFromHex("0103A1DE79C1", 16, 40);
    }

    @Test(expected = PayloadDecodingFailedException.class)
    public void shouldThrowExceptionIfPayloadPartExceeds32bitsInExtractDecimalFromHex() throws PayloadDecodingFailedException {
        DecoderUtil.extractDecimalFromHex("0103A1DE79C1", 8, 40);
    }

    @Test(expected = PayloadDecodingFailedException.class)
    public void shouldThrowExceptionIfUndefinedPayloadInExtractDecimalFromHex() throws PayloadDecodingFailedException {
        DecoderUtil.extractDecimalFromHex(null, 16, 40);
    }

    @Test
    public void signedDecimalFromHex() {
        String hex = "F4";
        Integer expectedResult = -12;

        String hex1 = "C1BD89CA";
        Integer expectedResult1 = -1044543030;

        String hex2 = "1B";
        Integer expectedResult2 = 27;

        String hex3 = "1F1BD89C";
        Integer expectedResult3 = 521918620;

        String hex4 = "F1F1BD89C";
        Integer expectedSignedInt_from_4_to_32_bits = 521918620;

        String hex5 = "0103A1DE79C1";

        Integer expectedSignedInt_from_16_to_32_bits = -1579255359;
        Integer expectedSignedInt_from_8_to_32_bits = 60939897;

        try {

            Integer actualResult = DecoderUtil.extractSignedDecimalFromHex(hex, 0, 8);
            assertEquals(expectedResult, actualResult);

            Integer actualResult1 = DecoderUtil.extractSignedDecimalFromHex(hex1, 0, 32);
            assertEquals(expectedResult1, actualResult1);

            Integer actualResult2 = DecoderUtil.extractSignedDecimalFromHex(hex2, 0, 8);
            assertEquals(expectedResult2, actualResult2);

            Integer actualResult3 = DecoderUtil.extractSignedDecimalFromHex(hex3, 0, 32);
            assertEquals(expectedResult3, actualResult3);

            Integer actualSignedInt_from_4_to_32_bits = DecoderUtil.extractSignedDecimalFromHex(hex4, 4, 32);
            assertEquals(expectedSignedInt_from_4_to_32_bits, actualSignedInt_from_4_to_32_bits);

            Integer actualSignedInt_from_16_to_32_bits = DecoderUtil.extractSignedDecimalFromHex(hex5, 16, 32);
            assertEquals(expectedSignedInt_from_16_to_32_bits, actualSignedInt_from_16_to_32_bits);

            Integer actualSignedInt_from_8_to_32_bits = DecoderUtil.extractSignedDecimalFromHex(hex5, 8, 32);
            assertEquals(expectedSignedInt_from_8_to_32_bits, actualSignedInt_from_8_to_32_bits);

        } catch (PayloadDecodingFailedException e) {
            fail();
        }
    }

    @Test(expected = PayloadDecodingFailedException.class)
    public void shouldThrowExceptionIfPayloadLengthNotSufficientInExtractSignedDecimalFromHex() throws PayloadDecodingFailedException {
        DecoderUtil.extractSignedDecimalFromHex("0103A1DE79C1", 16, 40);
    }

    @Test(expected = PayloadDecodingFailedException.class)
    public void shouldThrowExceptionIfPayloadPartExceeds32bitsInExtractSignedDecimalFromHex() throws PayloadDecodingFailedException {
        DecoderUtil.extractSignedDecimalFromHex("0103A1DE79C1", 8, 40);
    }

    @Test(expected = PayloadDecodingFailedException.class)
    public void shouldThrowExceptionIfUndefinedPayloadInExtractSignedDecimalFromHex() throws PayloadDecodingFailedException {
        DecoderUtil.extractSignedDecimalFromHex(null, 16, 40);
    }
    
    @Test
    public void signedBcdToNumber() {        
        String hex = "00";
        Integer expectedResult = 0;
        
        String hex1 = "72";
        Integer expectedResult1 = -28;
        
        String hex2 = "499";
        Integer expectedResult2 = 499;
        
        String hex3 = "501";
        Integer expectedResult3 = -499;
        
        String hex4 = "500";
        Integer expectedResult4 = -500;
        
        String hex5 = "9999";
        Integer expectedResult5 = -1;
        
        try {
            Integer actualResult = DecoderUtil.extractSignedBCDFromHex(hex, 0, 8);
            assertEquals(expectedResult, actualResult);
            
            Integer actualResult1 = DecoderUtil.extractSignedBCDFromHex(hex1, 0, 8);
            assertEquals(expectedResult1, actualResult1);
            
            Integer actualResult2 = DecoderUtil.extractSignedBCDFromHex(hex2, 0, 12);
            assertEquals(expectedResult2, actualResult2);
            
            Integer actualResult3 = DecoderUtil.extractSignedBCDFromHex(hex3, 0, 12);
            assertEquals(expectedResult3, actualResult3);
            
            Integer actualResult4 = DecoderUtil.extractSignedBCDFromHex(hex4, 0, 12);
            assertEquals(expectedResult4, actualResult4);
            
            Integer actualResult5 = DecoderUtil.extractSignedBCDFromHex(hex5, 0, 16);
            assertEquals(expectedResult5, actualResult5);
        } catch (PayloadDecodingFailedException e) {
            fail();
        }
    }
    
    @Test
    public void convertingLittleEndianToBigEndian() {
        try {
            String actualResult = DecoderUtil.convertHexToBigEndianOrdering("5A109061", 0, 32);
            String expectedResult = "6190105A";
            assertEquals(expectedResult, actualResult);
            
            String actualResult2 = DecoderUtil.convertHexToBigEndianOrdering("5A109061", 8, 24);
            String expectedResult2 = "619010";
            assertEquals(expectedResult2, actualResult2);
            
        } catch (PayloadDecodingFailedException e) {
            fail();
        }
    }

}
