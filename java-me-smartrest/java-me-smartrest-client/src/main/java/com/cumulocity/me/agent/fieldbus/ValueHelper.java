package com.cumulocity.me.agent.fieldbus;

import com.cumulocity.me.agent.fieldbus.model.Conversion;
import com.cumulocity.me.agent.fieldbus.model.RegisterDefinition;
import com.cumulocity.me.agent.util.ArrayUtils;
import com.cumulocity.me.util.StringUtils;

public class ValueHelper {
    public static String convert(byte[] value, RegisterDefinition definition){
        byte[] cutValue = cut(value, definition.getStartBit(), definition.getLength());
        long decimalValue = ArrayUtils.byteArrayToLong(cutValue);
        return convert(decimalValue, definition.getConversion());

    }

    public static byte[] cut(byte[] source, int startBit, int length){
        byte[] sourceBits = ArrayUtils.toBitArray(source);
        byte[] cutBits = new byte[length];
        System.arraycopy(sourceBits, startBit, cutBits, 0, length);
        return ArrayUtils.toByteArray(cutBits);
    }

    public static String convert(long value, Conversion conversion) {
        long convertedValue = value * conversion.getMultiplier();
        convertedValue = convertedValue / conversion.getDivisor();
        convertedValue = convertedValue + conversion.getOffset();

        String stringValue = Long.toString(convertedValue);
        if (conversion.getDecimalPlaces() > 0) {
            int splitIndex = (stringValue.length()) - conversion.getDecimalPlaces();
            while (splitIndex < 1) {
                stringValue = "0" + stringValue;
                splitIndex = (stringValue.length()) - conversion.getDecimalPlaces();
            }
            return stringValue.substring(0, splitIndex) + "."
                    + stringValue.substring(splitIndex);
        }
        return stringValue;
    }

    public static byte[] merge(String newValue, byte[] oldBytes, RegisterDefinition definition) {
        long convertedNewValue = convertBackwards(newValue, definition.getConversion());
        byte[] oldBits = ArrayUtils.toBitArray(oldBytes);
        byte[] newBits = ArrayUtils.toBitArray(convertedNewValue);
        byte[] mergedBits = new byte[oldBits.length];
        System.arraycopy(oldBits, 0, mergedBits, 0, oldBits.length);
        System.arraycopy(newBits, 64 - definition.getLength(), mergedBits, definition.getStartBit(), definition.getLength());
        return ArrayUtils.toByteArray(mergedBits);
    }

    public static long convertBackwards(String value, Conversion conversion) {
        int decimalPointIndex = value.indexOf('.');
        StringBuffer buffer = new StringBuffer();
        if (decimalPointIndex > -1) {
            String[] splitNewValue = StringUtils.split(value, ".");
            buffer.append(splitNewValue[0]);
            for (int i = 0; i < conversion.getDecimalPlaces() ; i++) {
                try {
                    char current = splitNewValue[1].charAt(i);
                    buffer.append(current);
                } catch (IndexOutOfBoundsException e){
                    buffer.append('0');
                }
            }
        } else {
            buffer.append(value);
            for (int i = 0; i < conversion.getDecimalPlaces(); i++) {
                buffer.append('0');
            }
        }
        long decimal = Long.parseLong(buffer.toString());
        decimal -= conversion.getOffset();
        decimal *= conversion.getDivisor();
        decimal /= conversion.getMultiplier();
        return decimal;
    }
}
