package c8y;

import java.math.BigDecimal;

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;

import com.cumulocity.model.measurement.MeasurementValue;

public class LightMeasurement extends AbstractDynamicProperties {
	public static final String LIGHT_UNIT = "lux";

	private MeasurementValue e;
	
	public MeasurementValue getE() {
		return e;
	}
	
	public void setE(MeasurementValue e) {
		this.e = e;
	}
	
	@JSONProperty(ignore = true)
	public BigDecimal getIlluminance() {
		return e == null ? null : e.getValue();
	}

	public void setIlluminance(BigDecimal illuminance) {
		e = new MeasurementValue(LIGHT_UNIT);
		e.setValue(illuminance);
	}
	
	@Override
	public int hashCode() {
		return e != null ? e.hashCode() : 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof LightMeasurement))
			return false;

		LightMeasurement rhs = (LightMeasurement) obj;
		return e == null ? (rhs.e == null)
				: e.equals(rhs.e);
	}
}
