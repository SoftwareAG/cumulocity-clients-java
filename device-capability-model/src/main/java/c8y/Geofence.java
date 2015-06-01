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

public class Geofence extends AbstractDynamicProperties {
	private BigDecimal lat;
	private BigDecimal lng;
	private BigDecimal radius;
	private boolean active;

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

	public BigDecimal getRadius() {
		return radius;
	}

	public void setRadius(BigDecimal radius) {
		this.radius = radius;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof Geofence))
			return false;

		Geofence rhs = (Geofence) obj;
		boolean result = (lat == null) ? (rhs.lat == null) : lat.equals(rhs.lat);
		result = result && ((lng == null) ? (rhs.lng == null) : lng.equals(rhs.lng));
		result = result && ((radius == null) ? (rhs.radius == null) : radius.equals(rhs.radius));
		result = result && (active == rhs.active);
		return result;
	}
	
    @Override
    public int hashCode() {
        int result = lat == null ? 0 : lat.hashCode();
        result = 31 * result + (lng == null ? 0 : lng.hashCode());
        result = 31 * result + (radius == null ? 0 : radius.hashCode());
        result = 31 * result + (active ? 1 : 0);
        return result;
    }
}
