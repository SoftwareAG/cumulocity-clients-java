package com.cumulocity.model.measurement;

/**
 * The state of a measurement.
 * Can be the original reading, a validated reading or an interpolated value.
 * 
 * @author pitchfor
 *
 */
public enum StateType {
    ORIGINAL, VALIDATED, INTERPOLATED;
}
