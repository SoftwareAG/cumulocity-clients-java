package com.cumulocity.model.environmental.measurement;

import java.io.Serializable;

import org.svenson.JSONProperty;

import com.cumulocity.model.environmental.sensor.TemperatureSensor;
import com.cumulocity.model.util.Alias;

/**
 * Provides a representation for a temperature measurement, as reported by {@link TemperatureSensor}.
 * See <a>https://code.telcoassetmarketplace.com/devcommunity/index.php/c8ydocumentation/114/320#Temperature</a> for details.
 * @author ricardomarques
 *
 */
@Alias(value = "c8y_TemperatureMeasurement")
public class TemperatureMeasurement implements Serializable{

    private static final long serialVersionUID = -1362873967251449504L;
    
    private TemperatureValue temperature;

    public TemperatureMeasurement() {
    }

    public TemperatureMeasurement(TemperatureValue temperature) {
        this.temperature = temperature;
    }

    /**
     * @return the temperature
     */
    @JSONProperty(value = "T", ignoreIfNull = true)
    public final TemperatureValue getTemperature() {
        return temperature;
    }

    /**
     * @param temperature the temperature to set
     */
    public final void setTemperature(TemperatureValue temperatureValue) {
        this.temperature = temperatureValue;
    }

    @Override
    public int hashCode() {
        return temperature != null ? temperature.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TemperatureMeasurement)) return false;

        TemperatureMeasurement that = (TemperatureMeasurement) o;

        if (temperature != null ? !temperature.equals(that.temperature) : that.temperature != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "TemperatureMeasurement{" +
                "temperature=" + temperature +
                '}';
    }
}
