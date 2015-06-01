package c8y;

import java.math.BigDecimal;

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;

import com.cumulocity.model.measurement.MeasurementValue;

public class MoistureMeasurement extends AbstractDynamicProperties {
	public static final String MOISTURE_UNIT="%";
	private MeasurementValue moisture;
	
	@JSONProperty("moisture")
	public MeasurementValue getMoisture(){
		return moisture;
	}
	
	public void setMoisture(MeasurementValue m){
		this.moisture = m;
	}
	
	@JSONProperty(ignore = true)
	public BigDecimal getMoistureValue(){
		return moisture==null?null:moisture.getValue();
	}
	
	public void setMoistureValue(BigDecimal moistureValue) {
		moisture = new MeasurementValue(MOISTURE_UNIT);
		moisture.setValue(moistureValue);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof TemperatureMeasurement))
			return false;
		
		MoistureMeasurement mm = (MoistureMeasurement)obj;
		return moisture==null?mm.moisture==null:moisture.equals(mm.moisture);
	}
	
	@Override
	public int hashCode() {
		return moisture==null ? 0 : moisture.hashCode();
	}
}
