package com.cumulocity.model.measurement;
/**
 * The type of a measurement value is used as a base for processing logic on top of the value. Examples:
 * <ul>
 *  <li>A counter is a value that is always increasing, such as energy consumption in electricity meters or packet counts in IP devices. A counter can be validated by checking that it is indeed increasing. A counter cannot be aggregated over time, since it is already an aggregate value. However, it can be summed up over devices.
 *  <li>A gauge is a sampled value such as "current temperature". Typically, statistics are computed over gauges, such as average, minimas and maximas over time and devices.
 *  <li>A rate represents a count over a certain time period. Such values are typically added up over time and averaged over devices.
 * </ul>
 * @author pitchfor
 *
 */
public enum ValueType {
    COUNTER, GAUGE, RATE, BOOLEAN;
}
