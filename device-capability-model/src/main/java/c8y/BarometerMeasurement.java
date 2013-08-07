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

import org.svenson.JSONProperty;

import com.cumulocity.model.measurement.MeasurementValue;

public class BarometerMeasurement {
	public static final String PRESS_UNIT = "mbar";
	public static final String ALT_UNIT = "m";
	
	private MeasurementValue p;
	private MeasurementValue alt;
	
	public MeasurementValue getP() {
		return p;
	}
	
	public void setP(MeasurementValue p) {
		this.p = p;
	}

	public MeasurementValue getAlt() {
		return alt;
	}
	
	public void setAlt(MeasurementValue alt) {
		this.alt = alt;
	}

	@JSONProperty(ignore = true)
	public BigDecimal getPressure() {
		return p == null ? null : p.getValue();
	}

	public void setPressure(BigDecimal pressure) {
		p = new MeasurementValue(PRESS_UNIT);
		p.setValue(pressure);
	}

	@JSONProperty(ignore = true)
	public BigDecimal getAltitude() {
		return alt == null ? null : alt.getValue();
	}
	
	public void setAltitude(BigDecimal altitude) {
		alt = new MeasurementValue(ALT_UNIT);
		alt.setValue(altitude);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof BarometerMeasurement))
			return false;

		BarometerMeasurement rhs = (BarometerMeasurement) obj;
		boolean result = (p == null) ? (rhs.p == null) : p.equals(rhs.p);
		result = result && ((alt == null) ? (rhs.alt == null) : alt.equals(rhs.alt));
		return result;
	}
	
	@Override
	public int hashCode() {
		int result = p == null ? 0 : p.hashCode();
		return result * 31 + (alt == null ? 0 : alt.hashCode());
	}
}
