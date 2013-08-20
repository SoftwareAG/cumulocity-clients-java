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

public class Battery {
	public static final String LEVEL_UNIT = "%";

	private MeasurementValue level;

	public MeasurementValue getLevel() {
		return level;
	}
	
	public void setLevel(MeasurementValue level) {
		this.level = level;
	}

	@JSONProperty(ignore = true)	
	public BigDecimal getLevelValue() {
		return level == null ? null : level.getValue();
	}
	
	public void setLevelValue(BigDecimal lev) {
		level = new MeasurementValue(LEVEL_UNIT);
		level.setValue(lev);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof AccelerationMeasurement))
			return false;

		Battery rhs = (Battery) obj;
		return level == null ? (rhs.level == null)
				: level.equals(rhs.level);
	}

	@Override
	public int hashCode() {
		return level != null ? level.hashCode() : 0;
	}
}
