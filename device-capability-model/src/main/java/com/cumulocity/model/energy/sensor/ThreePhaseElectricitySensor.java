package com.cumulocity.model.energy.sensor;

import com.cumulocity.model.util.Alias;


/**
 * Provides a representation for a single phase electricity sensor.
 * 
 * An electricity sensor typically measures energy consumption (usually measured in kWh), but more complex sensors can also measure
 * instantaneous power (usually measured in W), reactive power, harmonic distortion, etc.
 * 
 * A three phase meter can perform these measurements for three phases at the same time (typical industrial usage).
 * 
 * At this moment in time, this representation does not have any properties of its own.
 * @author ricardomarques
 *
 */
@Alias("c8y_ThreePhaseElectricitySensor")
public class ThreePhaseElectricitySensor {
    // So far, this representation does not have any properties of its own.
}
