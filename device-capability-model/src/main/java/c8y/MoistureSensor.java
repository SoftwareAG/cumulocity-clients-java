package c8y;

import com.cumulocity.model.measurement.MeasurementValue;

public class MoistureSensor {
	
	private MeasurementValue moisture;

	public MeasurementValue getMoisture() {
		return moisture;
	}

	public void setMoisture(MeasurementValue moisture) {
		this.moisture = moisture;
	}
	
	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((moisture == null) ? 0 : moisture.hashCode());
	    return result;
    }

	@Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    MoistureSensor other = (MoistureSensor) obj;
	    if (moisture == null) {
		    if (other.moisture != null)
			    return false;
	    } else if (!moisture.equals(other.moisture))
		    return false;
	    return true;
    }

	@Override
    public String toString() {
	    return String.format("MoistureSensor [moisture=%s]", moisture);
    }
	
}
