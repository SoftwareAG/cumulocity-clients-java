/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package c8y;

import java.math.BigDecimal;

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;

import com.cumulocity.model.measurement.MeasurementValue;

public class SignalStrength extends AbstractDynamicProperties {
    public static final String RSSI_UNIT = "dBm";
    public static final String BER_UNIT = "%";

    public static final double[] BER_TABLE = { 0.14, 0.28, 0.57, 1.13, 2.26,
        4.53, 9.05, 18.10 };

    private MeasurementValue rssi;
    private MeasurementValue ber;

    public MeasurementValue getRssi() {
        return rssi;
    }

    public void setRssi(MeasurementValue rssi) {
        this.rssi = rssi;
    }

    @JSONProperty(ignoreIfNull = true)
    public MeasurementValue getBer() {
        return ber;
    }

    public void setBer(MeasurementValue ber) {
        this.ber = ber;
    }

    @JSONProperty(ignore = true)
    public BigDecimal getRssiValue() {
        return rssi == null ? null : rssi.getValue();
    }

    public void setRssiValue(BigDecimal rssiVal) {
        rssi = new MeasurementValue(RSSI_UNIT);
        rssi.setValue(rssiVal);
    }

    public void putRawRssi(int rawRssi) {
        if (rawRssi != 99) {
            int rssiVal = -53 - (30 - rawRssi) * 2;
            setRssiValue(new BigDecimal(rssiVal));
        }
    }

    @JSONProperty(ignore = true)
    public BigDecimal getBerValue() {
        return ber == null ? null : ber.getValue();
    }

    public void setBerValue(BigDecimal berValue) {
        ber = new MeasurementValue(BER_UNIT);
        ber.setValue(berValue);
    }

    public void putRawBer(int rawBer) {
        if (rawBer != 99) {
            setBerValue(new BigDecimal(BER_TABLE[rawBer]));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SignalStrength)) {
            return false;
        }

        SignalStrength rhs = (SignalStrength) obj;
        boolean result = (rssi == null) ? (rhs.rssi == null) : rssi.equals(rhs.rssi);
        result = result && ((ber == null) ? (rhs.ber == null) : ber.equals(rhs.ber));
        return result;
    }

    @Override
    public int hashCode() {
        int result = rssi == null ? 0 : rssi.hashCode();
        return result * 31 + (ber == null ? 0 : ber.hashCode());
    }
}
