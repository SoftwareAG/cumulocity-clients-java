package c8y;

import com.cumulocity.model.measurement.MeasurementValue;

import com.jsoniter.annotation.JsonProperty;

/**
 * Provides a representation for a motion measurement, as reported by {@link MotionSensor}.
 * See <a>https://code.telcoassetmarketplace.com/devcommunity/index.php/c8ydocumentation/114/320#Motion</a> for details.
 * @author ricardomarques
 */
public class SpeedMeasurement {

    public static final String SPEED_UNITS = "m/s";

    public static final String BEARING_UNITS = "degrees";

    private MeasurementValue motionDetected;

    private MeasurementValue speed;

    private MeasurementValue bearing;

    public SpeedMeasurement() {}

    public SpeedMeasurement(MeasurementValue motionDetected, MeasurementValue speed,  MeasurementValue bearing) {
        this.motionDetected = motionDetected;
        this.speed = speed;
        this.bearing = bearing;
    }

    /**
     * Motion will be true if motionDetected value is not 0
     * @return
     */
    public Boolean isMotionDetected() {
        if (motionDetected == null || motionDetected.getValue() == null) {
            return false;
        }
        return (motionDetected.getValue().doubleValue() > 0);
    }

    /**
     * @return the motion, or null if no motion is set
     */
    public MeasurementValue getMotion() {
        return motionDetected;
    }

    /**
     * @param motionDetected the motion to set
     */
    @JsonProperty("motionDetected")
    public void setMotion(MeasurementValue motionDetected) {
        this.motionDetected = motionDetected;
    }

    /**
     * @return the speed, or null if speed is not set
     */
    public MeasurementValue getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    @JsonProperty("speed")
    public void setSpeed(MeasurementValue speed) {
        this.speed = speed;
    }

    /**
     * @return the bearing, or null if the bearing is not set
     */
    public MeasurementValue getBearing() {
        return bearing;
    }

    /**
     * @param bearing the bearing to set
     */
    @JsonProperty("bearing")
    public void setBearing(MeasurementValue bearing) {
        this.bearing = bearing;
    }

    @Override
    public int hashCode() {
        int result = motionDetected != null ? motionDetected.hashCode() : 0;
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
        if (motionDetected != null ? !motionDetected.equals(that.motionDetected) : that.motionDetected != null) {
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
                "motionDetected=" + motionDetected +
                ", speed=" + speed +
                ", bearing=" + bearing +
                '}';
    }
}
