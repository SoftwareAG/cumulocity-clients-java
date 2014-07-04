package c8y;

import java.math.BigDecimal;

import org.svenson.JSONProperty;

import com.cumulocity.model.measurement.MeasurementValue;

public class MoistureMeasurement {
	public static final String MOISTURE_UNIT="%";
	private MeasurementValue m;
	
	@JSONProperty("M")
	public MeasurementValue getM(){
		return m;
	}
	
	public void setM(MeasurementValue m){
		this.m = m;
	}
	
	@JSONProperty(ignore = true)
	public BigDecimal getMoisutre(){
		return m==null?null:m.getValue();
	}
	
	public void setMoisture(BigDecimal moisture) {
		m = new MeasurementValue(MOISTURE_UNIT);
		m.setValue(moisture);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof TemperatureMeasurement))
			return false;
		
		MoistureMeasurement moist = (MoistureMeasurement)obj;
		return m==null?moist.m==null:m.equals(moist.m);
	}
	
	@Override
	public int hashCode() {
		return m==null ? 0 : m.hashCode();
	}
}
