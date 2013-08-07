package com.cumulocity.model.energy.measurement;

import org.svenson.JSONProperty;

import com.cumulocity.model.energy.sensor.SinglePhaseElectricitySensor;
import com.cumulocity.model.util.Alias;

/**
 * Provides a representation for a electricity measurement, as reported by {@link SinglePhaseElectricitySensor}.
 * See <a>https://code.telcoassetmarketplace.com/devcommunity/index.php/c8ydocumentation/114/320#Energy</a> for details.
 * @author ricardomarques
 *
 */
@Alias(value = "c8y_SinglePhaseEnergyMeasurement")
public class SinglePhaseEnergyMeasurement  {

    private EnergyValue A_plus, A_minus;

    private PowerValue P_plus, P_minus;

    public SinglePhaseEnergyMeasurement() {
    }

    public SinglePhaseEnergyMeasurement(EnergyValue a_plus, EnergyValue a_minus, PowerValue p_plus, PowerValue p_minus) {
        A_plus = a_plus;
        A_minus = a_minus;
        P_plus = p_plus;
        P_minus = p_minus;
    }

    /**
     * @return the a_plus
     */
    @JSONProperty(value = "A+", ignoreIfNull = true)
    public final EnergyValue getTotalActiveEnergyIn() {
        return A_plus;
    }

    /**
     * @param a_plus the a_plus to set
     */
    public final void setTotalActiveEnergyIn(EnergyValue a_plus) {
        A_plus = a_plus;
    }

    /**
     * @return the a_minus
     */
    @JSONProperty(value = "A-", ignoreIfNull = true)
    public final EnergyValue getTotalActiveEnergyOut() {
        return A_minus;
    }

    /**
     * @param a_minus the a_minus to set
     */
    public final void setTotalActiveEnergyOut(EnergyValue a_minus) {
        A_minus = a_minus;
    }

    /**
     * @return the p_plus
     */
    @JSONProperty(value = "P+", ignoreIfNull = true)
    public final PowerValue getTotalActivePowerIn() {
        return P_plus;
    }

    /**
     * @param p_plus the p_plus to set
     */
    public final void setTotalActivePowerIn(PowerValue p_plus) {
        P_plus = p_plus;
    }

    /**
     * @return the p_minus
     */
    @JSONProperty(value = "P-", ignoreIfNull = true)
    public final PowerValue getTotalActivePowerOut() {
        return P_minus;
    }

    /**
     * @param p_minus the p_minus to set
     */
    public final void setTotalActivePowerOut(PowerValue p_minus) {
        P_minus = p_minus;
    }

    @Override
    public int hashCode() {
        int result = A_plus != null ? A_plus.hashCode() : 0;
        result = 31 * result + (A_minus != null ? A_minus.hashCode() : 0);
        result = 31 * result + (P_plus != null ? P_plus.hashCode() : 0);
        result = 31 * result + (P_minus != null ? P_minus.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SinglePhaseEnergyMeasurement)) return false;

        SinglePhaseEnergyMeasurement that = (SinglePhaseEnergyMeasurement) o;

        if (A_minus != null ? !A_minus.equals(that.A_minus) : that.A_minus != null) return false;
        if (A_plus != null ? !A_plus.equals(that.A_plus) : that.A_plus != null) return false;
        if (P_minus != null ? !P_minus.equals(that.P_minus) : that.P_minus != null) return false;
        if (P_plus != null ? !P_plus.equals(that.P_plus) : that.P_plus != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "SinglePhaseEnergyMeasurement{" +
                "A_plus=" + A_plus +
                ", A_minus=" + A_minus +
                ", P_plus=" + P_plus +
                ", P_minus=" + P_minus +
                '}';
    }
}
