package com.cumulocity.model.energy.sensor;

import com.cumulocity.model.util.Alias;


/**
 * Provides a representation for a single phase electricity sensor.
 * 
 * An electricity sensor typically measures energy consumption (kWh), but more complex sensors can also measure
 * instantaneous power (W), reactive power, harmonic distortion, etc.
 * 
 * A single phase meter can perform these measurements for a single phase (typical home usage).
 * 
 * At this moment in time, this representation does not have any properties of its own.
 * @author ricardomarques
 *
 */
@Alias("c8y_SinglePhaseElectricitySensor")
public class SinglePhaseElectricitySensor  {
    // So far, this representation does not have any properties of its own.
}
