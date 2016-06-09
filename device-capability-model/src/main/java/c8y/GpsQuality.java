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

public class GpsQuality extends AbstractDynamicProperties {

    public static final String QUALITY_UNIT = "%";

    private MeasurementValue satellites;
    private MeasurementValue quality;

    @JSONProperty(ignoreIfNull = true)
    public MeasurementValue getSatellites() {
        return satellites;
    }

    public void setSatellites(MeasurementValue satellites) {
        this.satellites = satellites;
    }

    @JSONProperty(ignoreIfNull = true)
    public MeasurementValue getQuality() {
        return quality;
    }

    public void setQuality(MeasurementValue quality) {
        this.quality = quality;
    }

    @JSONProperty(ignore = true)
    public BigDecimal getSatellitesValue() {
        return satellites == null ? null : satellites.getValue();
    }

    public void setSatellitesValue(int satellitesValue) {
        satellites = new MeasurementValue();
        satellites.setValue(new BigDecimal(satellitesValue));
    }

    @JSONProperty(ignore = true)
    public BigDecimal getQualityValue() {
        return quality == null ? null : quality.getValue();
    }

    public void setQualityValue(BigDecimal qualityValue) {
        quality = new MeasurementValue(QUALITY_UNIT);
        quality.setValue(qualityValue);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((quality == null) ? 0 : quality.hashCode());
        result = prime * result + ((satellites == null) ? 0 : satellites.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GpsQuality other = (GpsQuality) obj;
        if (quality == null) {
            if (other.quality != null)
                return false;
        } else if (!quality.equals(other.quality))
            return false;
        if (satellites == null) {
            if (other.satellites != null)
                return false;
        } else if (!satellites.equals(other.satellites))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GpsQuality [satellites=" + satellites + ", quality=" + quality + "]";
    }
    
    

}
