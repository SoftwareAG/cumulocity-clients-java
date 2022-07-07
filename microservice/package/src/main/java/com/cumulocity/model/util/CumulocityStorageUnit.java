package com.cumulocity.model.util;


import lombok.Getter;
import org.apache.commons.io.FileUtils;

/**
 * A lightweight enum allowing for the conversion of Cumulocity Storage units into bytes
 *
 */
public enum CumulocityStorageUnit {

    Bi(1),
    Ki(FileUtils.ONE_KB),
    Mi(FileUtils.ONE_MB),
    Gi(FileUtils.ONE_GB),
    Ti(FileUtils.ONE_TB),
    Pi(FileUtils.ONE_PB),
    Ei(FileUtils.ONE_EB),
    B(1), //bytes

    //below the SI units, see for example https://en.wikipedia.org/wiki/Metric_prefix
    K(pow10(3)), //Kilo
    M(pow10(6)), //Mega
    G(pow10(9)), //Giga
    T(pow10(12)), //Terra
    P(pow10(15)), //Peta
    E(pow10(18)); //Exa

    @Getter
    private long byteLen;

    CumulocityStorageUnit(long value) {
        this.byteLen = value;
    }

    public static long getBytes(long quantity, String unit) {
        CumulocityStorageUnit storageUnit = CumulocityStorageUnit.valueOf(unit);
        return quantity*storageUnit.byteLen;
    }

    public static long getBytes(String storageSizeString) {

        if (!Character.isDigit(storageSizeString.charAt(0))) {
            throw new IllegalArgumentException("Storage string does not start with a number. Valid examples: 1G or 512Mi");
        }

        if (storageSizeString.length()<2) {
            throw new IllegalArgumentException("Illegal format of storage size string. Must be at least 2 chars long");
        }

        if (Character.isDigit(storageSizeString.charAt(storageSizeString.length()-1))) {
            throw new IllegalArgumentException("No unit given in storage parameter: "+storageSizeString);
        }

        String suffix = storageSizeString.substring(storageSizeString.length()-2);

        String unit;
        int unitLength;

        if (Character.isAlphabetic(suffix.charAt(0))) {
            unit = suffix;
            unitLength=2;
        } else if (Character.isDigit(suffix.charAt(0))) {
            unit = suffix.substring(1);
            unitLength=1;
        } else {
            throw new IllegalArgumentException("Given unit string is invalid :" + suffix);
        }

        String numberString = storageSizeString.substring(0,storageSizeString.length()-unitLength);
        Long quantity = Long.parseLong(numberString);

        return getBytes(quantity, unit);

    }

    private static long pow10(int exponent) {
        return (long) Math.pow(10, exponent);
    }





}
