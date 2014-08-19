package c8y;

import java.math.BigDecimal;

import org.svenson.JSONProperty;

import com.cumulocity.model.measurement.MeasurementValue;

public class CurrentMeasurement {
	public static final String CURRENT_UNIT = "A";
	
	private MeasurementValue current = new MeasurementValue(CURRENT_UNIT);
	
	@JSONProperty("current")
	public MeasurementValue getCurrent() {
		return current;
	}
	
	public void setCurrent(MeasurementValue current) {
		this.current = current;
	}
	
	@JSONProperty(ignore = true)
	public BigDecimal getCurrentVallue() {
		return current == null ? null : current.getValue();
	}
	
	public void setCurrentValue(BigDecimal currentValue) {
		current = new MeasurementValue(CURRENT_UNIT);
		current.setValue(currentValue);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof CurrentMeasurement))
			return false;
		
		return current == null ? 
			((CurrentMeasurement)obj).current == null : 
				current.equals(((CurrentMeasurement)obj).current);
	}
	
	@Override
	public int hashCode() {
		return current==null? 0 : current.hashCode();
	}
	
}
