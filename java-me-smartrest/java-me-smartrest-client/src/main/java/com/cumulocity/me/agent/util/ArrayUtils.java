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
}
