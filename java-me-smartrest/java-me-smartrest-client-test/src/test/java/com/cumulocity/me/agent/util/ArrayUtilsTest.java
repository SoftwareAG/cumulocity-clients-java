package com.cumulocity.me.agent.util;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.omg.PortableInterceptor.Interceptor;

import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

public class ArrayUtilsTest {
    @Test
    public void shouldConvertFFBytetoBitsCorrectly() throws Exception {
        byte[] bits = ArrayUtils.toBitArray((byte) 0xFF);
        assertThat(bits.length).isEqualTo(8);
        for (byte bit : bits) {
            assertThat(bit).isEqualTo((byte) 1);
        }
    }

    @Test
    public void shouldConvert00BytetoBitsCorrectly() throws Exception {
        byte[] bits = ArrayUtils.toBitArray((byte) 0x00);
        assertThat(bits.length).isEqualTo(8);
        for (byte bit : bits) {
            assertThat(bit).isEqualTo((byte) 0);
        }
    }

    @Test
    public void shouldConvertBytetoBitsCorrectly() throws Exception {
        byte[] bits = ArrayUtils.toBitArray((byte) 0xAA);
        assertThat(bits.length).isEqualTo(8);
        for (int i = 0; i < bits.length; i+=2) {
            byte bit = bits[i];
            assertThat(bit).isEqualTo((byte) 1);
            byte next = bits[i + 1];
            assertThat(next).isEqualTo((byte) 0);
        }
    }

    @Test
    public void shouldConvertByteArraytoBitsCorrectly() throws Exception {
        byte[] bits = ArrayUtils.toBitArray(new byte[]{(byte) 0xFF, 0x00, (byte) 0xAA});
        assertThat(bits.length).isEqualTo(24);
        for (int i = 0; i < 8; i++) {
            byte bit = bits[i];
            assertThat(bit).isEqualTo((byte) 1);
        }
        for (int i = 8; i < 16; i++) {
            byte bit = bits[i];
            assertThat(bit).isEqualTo((byte) 0);
        }
        for (int i = 16; i < 24; i+=2) {
            byte bit = bits[i];
            assertThat(bit).isEqualTo((byte) 1);
            byte next = bits[i + 1];
            assertThat(next).isEqualTo((byte) 0);
        }
    }

    @Test
    public void shouldConvertBitstoByteCorrectly() throws Exception {
        byte result = ArrayUtils.toByte(new byte[]{1, 0, 1, 0, 1, 0, 1, 0});
        assertThat(result).isEqualTo((byte) 0xAA);
    }

    @Test
    public void shouldConvertEvenBitstoByteArrayCorrectly() throws Exception {
        byte[] result = ArrayUtils.toByteArray(new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0});
        assertThat(result.length).isEqualTo(3);
        assertThat(result[0]).isEqualTo((byte) 0xFF);
        assertThat(result[1]).isEqualTo((byte) 0x00);
        assertThat(result[2]).isEqualTo((byte) 0xAA);
    }

    @Test
    public void shouldConvertUnevenBitstoByteArrayCorrectly() throws Exception {
        byte[] result = ArrayUtils.toByteArray(new byte[]{1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0});
        assertThat(result.length).isEqualTo(3);
        assertThat(result[0]).isEqualTo((byte) 0x0F);
        assertThat(result[1]).isEqualTo((byte) 0x00);
        assertThat(result[2]).isEqualTo((byte) 0xAA);
    }

    @Test
    public void shouldConvertLongToBitsCorrectly() throws Exception {
        byte[] result = ArrayUtils.toBitArray(0xFF000000000000FFL);
        for (int i = 0; i < result.length; i++) {
            byte current = result[i];
            if (i < 8 || i > 55) {
                assertThat(current).isEqualTo((byte) 1);
            } else {
                assertThat(current).isEqualTo((byte) 0);
            }
        }
    }
}