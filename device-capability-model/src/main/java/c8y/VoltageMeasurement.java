package c8y;

import java.math.BigDecimal;

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;

import com.cumulocity.model.measurement.MeasurementValue;

public class VoltageMeasurement extends AbstractDynamicProperties {
    public static final String VOLTAGE_UNIT="V";
    private MeasurementValue voltage;

    @JSONProperty("voltage")
    public MeasurementValue getVoltage(){
        return voltage;
    }

    public void setVoltage(MeasurementValue voltage){
        this.voltage = voltage;
    }

    @JSONProperty(ignore=true)
    public BigDecimal getVoltageValue(){
        return voltage==null?null:voltage.getValue();
    }

    public void setVoltageValue(BigDecimal voltageValue){
        voltage = new MeasurementValue(VOLTAGE_UNIT);
        voltage.setValue(voltageValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof VoltageMeasurement)) {
            return false;
        }

        VoltageMeasurement vm = (VoltageMeasurement)obj;
        return voltage==null?vm.voltage==null:voltage.equals(vm.voltage);
    }

    @Override
    public int hashCode() {
        return voltage==null? 0 : voltage.hashCode();
    }
}
