package c8y;

import org.svenson.AbstractDynamicProperties;
import org.svenson.JSONProperty;

import com.cumulocity.model.measurement.MeasurementValue;

/**
 * Provides a representation for a motion measurement, as reported by {@link MotionSensor}.
 * See <a>https://code.telcoassetmarketplace.com/devcommunity/index.php/c8ydocumentation/114/320#Motion</a> for details.
 * @author ricardomarques
 */
public class SpeedMeasurement extends AbstractDynamicProperties {

    public static final String SPEED_UNITS = "m/s";

    public static final String BEARING_UNITS = "degrees";

    private MeasurementValue motion;

    private MeasurementValue speed;

    private MeasurementValue bearing;

    public SpeedMeasurement() {
    }

    public SpeedMeasurement(MeasurementValue motion, MeasurementValue speed, MeasurementValue bearing) {
        this.motion = motion;
        this.speed = speed;
        this.bearing = bearing;
    }

    /**
     * @return the motion, or null if no motion is set
     */
    @JSONProperty(value = "motionDetected", ignoreIfNull = true)
    public MeasurementValue getMotion() {
        return motion;
    }

    /**
     * @param motion the motion to set
     */
    public void setMotion(MeasurementValue motion) {
        this.motion = motion;
    }

    /**
     * MotionDetected will be true if motion value is present and is not 0
     * @return
     */
    @JSONProperty(ignore = true)
    public Boolean motionDetected() {
        if (motion == null || motion.getValue() == null) {
            return false;
        }
        return (motion.getValue().doubleValue() > 0);
    }

    /**
     * @return the speed, or null if speed is not set
     */
    @JSONProperty(value = "speed", ignoreIfNull = true)
    public MeasurementValue getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(MeasurementValue speed) {
        this.speed = speed;
    }

    /**
     * @return the bearing, or null if the bearing is not set
     */
    @JSONProperty(value = "bearing", ignoreIfNull = true)
    public MeasurementValue getBearing() {
        return bearing;
    }

    /**
     * @param bearing the bearing to set
     */
    public void setBearing(MeasurementValue bearing) {
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpeedMeasurement)) {
            return false;
        }

        SpeedMeasurement that = (SpeedMeasurement) o;

        if (bearing != null ? !bearing.equals(that.bearing) : that.bearing != null) {
            return false;
        }
        if (motion != null ? !motion.equals(that.motion) : that.motion != null) {
            return false;
        }
        if (speed != null ? !speed.equals(that.speed) : that.speed != null) {
            return false;
        }

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
