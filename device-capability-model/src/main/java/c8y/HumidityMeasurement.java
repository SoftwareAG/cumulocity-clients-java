package c8y;

import java.math.BigDecimal;

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;

import com.cumulocity.model.measurement.MeasurementValue;

public class HumidityMeasurement extends AbstractDynamicProperties {
	public static final String HUM_UNIT = "%RH";

	private MeasurementValue h;

	public MeasurementValue getH() {
		return h;
	}
	
	public void setH(MeasurementValue h) { 
		this.h = h;
	}

	@JSONProperty(ignore = true)
	public BigDecimal getHumidity() {
		return h == null ? null : h.getValue();
	}

	public void setHumidity(BigDecimal humidity) {
		h = new MeasurementValue(HUM_UNIT);
		h.setValue(humidity);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof HumidityMeasurement))
			return false;

		HumidityMeasurement rhs = (HumidityMeasurement) obj;
		return h == null ? (rhs.h == null) : h.equals(rhs.h);
	}

	@Override
	public int hashCode() {
		return h == null ? 0 : h.hashCode();
	}
}
