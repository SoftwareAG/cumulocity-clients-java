package com.cumulocity.me.agent.util;

public class ArrayUtils {

    public static long byteArrayToLong(byte[] bytes) {
        long converted = bytes[0] & 0xFF;
        for (int i = 1; i < bytes.length; i++) {
            converted = converted << 8;
            converted += bytes[i] & 0xFF;
        }
        return converted;
    }

    public static int byteArrayToInt(byte[] bytes) {
        int converted = bytes[0] & 0xFF;
        for (int i = 1; i < bytes.length; i++) {
            converted = converted << 8;
            converted += bytes[i] & 0xFF;
        }
        return converted;
    }

    public static byte[] toBitArray(byte[] bytes) {
        byte[] bits = new byte[bytes.length * 8];
        for (int i = 0; i < bytes.length; i++) {
            byte currentByte = bytes[i];
            byte[] currentBits = toBitArray(currentByte);
            System.arraycopy(currentBits, 0, bits, i * 8, 8);
        }
        return bits;
    }

    public static byte[] toBitArray(byte source){
        byte[] bits = new byte[8];
        bits[0] = (byte) ((source & 0x80) >> 7);
        bits[1] = (byte) ((source & 0x40) >> 6);
        bits[2] = (byte) ((source & 0x20) >> 5);
        bits[3] = (byte) ((source & 0x10) >> 4);
        bits[4] = (byte) ((source & 0x08) >> 3);
        bits[5] = (byte) ((source & 0x04) >> 2);
        bits[6] = (byte) ((source & 0x02) >> 1);
        bits[7] = (byte) (source & 0x01);
        return bits;
    }

    public static byte[] toByteArray(byte[] bits) {
        int firstByteLength = bits.length % 8;
        byte[] firstByteBits = new byte[8];
        byte[] result;
        if (firstByteLength > 0){
            result = new byte[(bits.length / 8) + 1];
            System.arraycopy(bits, 0, firstByteBits, 8 - firstByteLength, firstByteLength);
            result[0] = toByte(firstByteBits);
            byte[] evenBits = new byte[bits.length - firstByteLength];
            System.arraycopy(bits, firstByteLength, evenBits, 0, evenBits.length);
            byte[] evenResult = toByteArray(evenBits);
            System.arraycopy(evenResult, 0, result, 1, evenResult.length);
        } else {
            result = new byte[bits.length / 8];
            for (int i = 0; i < result.length; i++) {
                byte[] currentByteBits = new byte[8];
                System.arraycopy(bits, i * 8, currentByteBits, 0, 8);
                result[i] = toByte(currentByteBits);
            }
        }
        return result;
    }

    public static byte toByte(byte[] bits) {
        byte result = 0;
        result += bits[0] << 7;
        result += bits[1] << 6;
        result += bits[2] << 5;
        result += bits[3] << 4;
        result += bits[4] << 3;
        result += bits[5] << 2;
        result += bits[6] << 1;
        result += bits[7];
        return result;
    }

    public static byte[] toBitArray(long source) {
        byte[] result = new byte[64];
        for (int i = 0; i < result.length; i++) {
            int shift = 63 - i;
            result[i] = (byte) ((source >>> shift) & 0x01);
        }
        return result;
    }

    public static byte[] toBitArray(int source) {
        byte[] result = new byte[32];
        for (int i = 0; i < result.length; i++) {
            int shift = 31 - i;
            result[i] = (byte) ((source >>> shift) & 0x01);
        }
        return result;
    }

    public byte[] toByteArray(int source) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((source & 0xFF000000) >> 24);
        bytes[1] = (byte) ((source & 0xFF0000) >> 16);
        bytes[2] = (byte) ((source & 0xFF00) >> 8);
        bytes[3] = (byte) (source & 0xFF);
        return bytes;
    }
}
