package c8y;

import java.math.BigDecimal;

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;

import com.cumulocity.model.measurement.MeasurementValue;

public class PowerMeasurement extends AbstractDynamicProperties {
	public static final String POWER_UNIT="W";
	private MeasurementValue power;
	
	@JSONProperty("power")
	public MeasurementValue getPower(){
		return power;
	}
	
	public void setPower(MeasurementValue power){
		this.power = power;
	}
	
	@JSONProperty(ignore=true)
	public BigDecimal getPowerValue(){
		return power==null?null:power.getValue();
	}
	
	public void setPowerValue(BigDecimal powerValue){
		power = new MeasurementValue(POWER_UNIT);
		power.setValue(powerValue);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof PowerMeasurement))
			return false;
		
		PowerMeasurement pm = (PowerMeasurement)obj;
		return power==null?pm.power==null:power.equals(pm.power);
	}
	
	@Override
	public int hashCode() {
		return power==null ? 0 : power.hashCode();
	}
}
