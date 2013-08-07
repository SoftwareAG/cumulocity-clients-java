package com.cumulocity.model.environmental.sensor;

import com.cumulocity.model.util.Alias;


/**
 * Provides a representation for a motion sensor.
 * 
 * A motion sensor detects motion in a given area and can do so by looking for body heat (passive infrared sensors),
 * sending pulses of ultrasonic waves and measuring the reflection off a moving object (ultrasonic sensors),
 * sending microwave pulses and measuring the reflection off a moving object (microwave sensors),
 * sensing disturbances to radio waves as they travel through an area surrounded by mesh network nodes (tomographic detector), 
 * among others.
 * 
 * At this moment in time, this representation does not have any properties of its own.
 * @author ricardomarques
 *
 */
@Alias(value = "c8y_MotionSensor")
public class MotionSensor {
    // So far, this representation does not have any properties of its own.
}
