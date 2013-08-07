package com.cumulocity.model.location.measurement;

import com.cumulocity.model.location.sensor.GPSSensor;
import com.cumulocity.model.util.Alias;

/**
 * Provides a representation for a location measurement, as reported by, for example, {@link GPSSensor}.
 * See <a>https://code.telcoassetmarketplace.com/devcommunity/index.php/c8ydocumentation/114/320#GPSSensor</a> for details.
 * @author ricardomarques
 */
@Alias(value = "c8y_LocationMeasurement")
public class LocationMeasurement  {

    private CoordinateValue latitude;

    private CoordinateValue longitude;

    private CoordinateValue altitude;

    public LocationMeasurement() {
    }
    
    public LocationMeasurement(CoordinateValue latitude, CoordinateValue longitude, CoordinateValue altitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    /**
     * @return the latitude
     */
    public CoordinateValue getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(CoordinateValue latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public CoordinateValue getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(CoordinateValue longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the altitude
     */
    public CoordinateValue getAltitude() {
        return altitude;
    }

    /**
     * @param altitude the altitude to set
     */
    public void setAltitude(CoordinateValue altitude) {
        this.altitude = altitude;
    }

    @Override
    public int hashCode() {
        int result = latitude != null ? latitude.hashCode() : 0;
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (altitude != null ? altitude.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationMeasurement)) return false;

        LocationMeasurement that = (LocationMeasurement) o;

        if (altitude != null ? !altitude.equals(that.altitude) : that.altitude != null) return false;
        if (latitude != null ? !latitude.equals(that.latitude) : that.latitude != null) return false;
        if (longitude != null ? !longitude.equals(that.longitude) : that.longitude != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "LocationMeasurement{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                '}';
    }
}
