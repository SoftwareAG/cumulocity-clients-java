package com.cumulocity.microservice.customdecoders.api.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueTranslatorTest {

    @Test
    void translate() {
        byte[] givenArray1 = new byte[] {47, 74, 57, 55};
        byte[] givenArray2 = new byte[] {57, 55, 47, 74};
        assertNotEquals(2.783566136E8, ValueTranslator.translate(givenArray1, REGISTER_DATA_TYPE.Integer, true, false, BIT_BYTE_WORD_ORDER.Reverse_Bits, 4, 2, 1));

        assertEquals(2.783566136E8, ValueTranslator.translate(givenArray2, REGISTER_DATA_TYPE.Integer, true, false, BIT_BYTE_WORD_ORDER.Reverse_Bits, 4, 2, 1));
        assertEquals(2.4892166260000002E8, ValueTranslator.translate(givenArray2, REGISTER_DATA_TYPE.Integer, true, true, BIT_BYTE_WORD_ORDER.Reverse_Bits, 4, 2, 1));
        assertEquals(2.783566136E8, ValueTranslator.translate(givenArray2, REGISTER_DATA_TYPE.Integer, false, false, BIT_BYTE_WORD_ORDER.Reverse_Bits, 4, 2, 1));
        assertEquals(2.4892166260000002E8, ValueTranslator.translate(givenArray2, REGISTER_DATA_TYPE.Integer, false, true, BIT_BYTE_WORD_ORDER.Reverse_Bits, 4, 2, 1));

        assertEquals(1.051941666816E11, ValueTranslator.translate(givenArray2, REGISTER_DATA_TYPE.Float, true, false, BIT_BYTE_WORD_ORDER.Reverse_Bits, 4, 2, 1));
        assertEquals(574146.85, ValueTranslator.translate(givenArray2, REGISTER_DATA_TYPE.Float, true, true, BIT_BYTE_WORD_ORDER.Reverse_Bits, 4, 2, 1));
        assertEquals(1.051941666816E11, ValueTranslator.translate(givenArray2, REGISTER_DATA_TYPE.Float, false, false, BIT_BYTE_WORD_ORDER.Reverse_Bits, 4, 2, 1));
        assertEquals(574146.85, ValueTranslator.translate(givenArray2, REGISTER_DATA_TYPE.Float, false, true, BIT_BYTE_WORD_ORDER.Reverse_Bits, 4, 2, 1));
    }
}