package com.cumulocity.microservice.customdecoders.api.util;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.Formatter;

public final class DecoderUtils {

    public static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static DateTime toDateTime(long timestamp) {
        Date date = new Date(timestamp * 1000);
        return new DateTime(date);
    }
}
