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

    private static long pow10(int exponent) {
        return (long) Math.pow(10, exponent);
    }
}
