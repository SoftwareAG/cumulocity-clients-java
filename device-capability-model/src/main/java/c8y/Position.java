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

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;

import java.math.BigDecimal;

public class Position extends AbstractDynamicProperties {
    private static final long serialVersionUID = -8365376637780307348L;

    private BigDecimal lat;
    private BigDecimal lng;
    private BigDecimal alt;
    private Long accuracy;

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal latitude) {
        this.lat = latitude;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal longitude) {
        this.lng = longitude;
    }

    public BigDecimal getAlt() {
        return alt;
    }

    public void setAlt(BigDecimal altitude) {
        this.alt = altitude;
    }

    @JSONProperty(ignoreIfNull = true)
    public Long getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(long accuracy) {
        this.accuracy = new Long(accuracy);
    }

    public void setAccuracy(Long accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof Position))
            return false;

        Position rhs = (Position) obj;
        return checkedCompare(lat, rhs.lat)
                && checkedCompare(lng, rhs.lng)
                && checkedCompare(alt, rhs.alt)
                && checkedCompare(accuracy, rhs.accuracy);
    }

    private boolean checkedCompare(Comparable left, Comparable right){
        if (left == null && right == null) {
        } else if (left != null && right != null) {
            if (left.compareTo(right) != 0) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = lat == null ? 0 : lat.hashCode();
        result = 31 * result + (lng == null ? 0 : lng.hashCode());
        result = 31 * result + (alt == null ? 0 : alt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return String.format("Position [lat=%s, lng=%s, alt=%s, accuracy=%s]", lat, lng, alt, accuracy);
    }
}
