package c8y;

import java.math.BigDecimal;

/*
 * Provides representation for the c8y_Value operation.
 * c8y_Value is a generic operation for passing a single value to a device.
 * It can be used to model control knobs, sliders, etc.
 */
public class Value {
	
	private BigDecimal value;
	
	public BigDecimal getValue() {
		return value;
	}
	
	public void setValue(BigDecimal actualValue) {
		this.value = actualValue;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(this == obj) return true;
		if(!(obj instanceof Value)) return false;
		
		Value that = (Value)obj;
		
		if(value!=null ? !value.equals(that.value) : that.value!=null) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return value!=null?value.hashCode():0;
	}
}
