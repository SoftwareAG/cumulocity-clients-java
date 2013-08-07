package com.cumulocity.model.environmental.measurement;

import org.svenson.JSONProperty;

import com.cumulocity.model.environmental.sensor.MotionSensor;
import com.cumulocity.model.util.Alias;

/**
 * Provides a representation for a motion measurement, as reported by {@link MotionSensor}.
 * See <a>https://code.telcoassetmarketplace.com/devcommunity/index.php/c8ydocumentation/114/320#Motion</a> for details.
 * @author ricardomarques
 */
@Alias(value = "c8y_SpeedMeasurement")
public class SpeedMeasurement {
    
    public static final String SPEED_UNITS = "m/s";

    public static final String BEARING_UNITS = "degrees";

    private MotionValue motion;

    private SpeedValue speed;

    private BearingValue bearing;

    public SpeedMeasurement() {
    }
    
    public SpeedMeasurement(MotionValue motion, SpeedValue speed, BearingValue bearing) {
        this.motion = motion;
        this.speed = speed;
        this.bearing = bearing;
    }

    /**
     * Motion will be true if motionDetected value is not 0
     * @return
     */
    public Boolean isMotionDetected() {
        return (motion.getValue().doubleValue() > 0);
    }

    /**
     * @return the motion, or null if no motion is set
     */
    @JSONProperty(value = "motionDetected", ignoreIfNull = true)
    public MotionValue getMotion() {
        return motion;
    }

    /**
     * @param motion the motion to set
     */
    public void setMotion(MotionValue motion) {
        this.motion = motion;
    }

    /**
     * @return the speed, or null if speed is not set
     */
    @JSONProperty(value = "speed", ignoreIfNull = true)
    public SpeedValue getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(SpeedValue speed) {
        this.speed = speed;
    }

    /**
     * @return the bearing, or null if the bearing is not set 
     */
    @JSONProperty(value = "bearing", ignoreIfNull = true)
    public BearingValue getBearing() {
        return bearing;
    }

    /**
     * @param bearing the bearing to set
     */
    public void setBearing(BearingValue bearing) {
        this.bearing = bearing;
    }

    @Override
    public int hashCode() {
        int result = motion != null ? motion.hashCode() : 0;
        result = 31 * result + (speed != null ? speed.hashCode() : 0);
        result = 31 * result + (bearing != null ? bearing.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpeedMeasurement)) return false;

        SpeedMeasurement that = (SpeedMeasurement) o;

        if (bearing != null ? !bearing.equals(that.bearing) : that.bearing != null) return false;
        if (motion != null ? !motion.equals(that.motion) : that.motion != null) return false;
        if (speed != null ? !speed.equals(that.speed) : that.speed != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "SpeedMeasurement{" +
                "motion=" + motion +
                ", speed=" + speed +
                ", bearing=" + bearing +
                '}';
    }
}
