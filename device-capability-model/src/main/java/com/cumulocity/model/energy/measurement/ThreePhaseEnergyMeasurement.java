package com.cumulocity.model.energy.measurement;

import org.svenson.JSONProperty;

import com.cumulocity.model.energy.sensor.ThreePhaseElectricitySensor;
import com.cumulocity.model.util.Alias;

/**
 * Provides a representation for a electricity measurement, as reported by {@link ThreePhaseElectricitySensor}.
 * See <a>https://code.telcoassetmarketplace.com/devcommunity/index.php/c8ydocumentation/114/320#Energy</a> for details.
 * @author ricardomarques
 *
 */
@Alias(value = "c8y_ThreePhaseEnergyMeasurement")
public class ThreePhaseEnergyMeasurement extends SinglePhaseEnergyMeasurement {

    private EnergyValue Ri_plus, Ri_minus, Rc_plus, Rc_minus;

    private PowerValue Qi_plus, Qi_minus, Qc_plus, Qc_minus;

    private EnergyValue A_plus_phase1, A_minus_phase1, A_plus_phase2, A_minus_phase2, A_plus_phase3, A_minus_phase3;

    private PowerValue P_plus_phase1, P_minus_phase1, P_plus_phase2, P_minus_phase2, P_plus_phase3, P_minus_phase3;

    public ThreePhaseEnergyMeasurement() {
    }

    public ThreePhaseEnergyMeasurement(EnergyValue a_plus, EnergyValue a_minus, PowerValue p_plus, PowerValue p_minus, EnergyValue ri_plus,
            EnergyValue ri_minus, EnergyValue rc_plus, EnergyValue rc_minus, PowerValue qi_plus, PowerValue qi_minus, PowerValue qc_plus,
            PowerValue qc_minus, EnergyValue a_plus_phase1, EnergyValue a_minus_phase1, EnergyValue a_plus_phase2,
            EnergyValue a_minus_phase2, EnergyValue a_plus_phase3, EnergyValue a_minus_phase3, PowerValue p_plus_phase1,
            PowerValue p_minus_phase1, PowerValue p_plus_phase2, PowerValue p_minus_phase2, PowerValue p_plus_phase3,
            PowerValue p_minus_phase3) {
        super(a_plus, a_minus, p_plus, p_minus);
        Ri_plus = ri_plus;
        Ri_minus = ri_minus;
        Rc_plus = rc_plus;
        Rc_minus = rc_minus;
        Qi_plus = qi_plus;
        Qi_minus = qi_minus;
        Qc_plus = qc_plus;
        Qc_minus = qc_minus;
        A_plus_phase1 = a_plus_phase1;
        A_minus_phase1 = a_minus_phase1;
        A_plus_phase2 = a_plus_phase2;
        A_minus_phase2 = a_minus_phase2;
        A_plus_phase3 = a_plus_phase3;
        A_minus_phase3 = a_minus_phase3;
        P_plus_phase1 = p_plus_phase1;
        P_minus_phase1 = p_minus_phase1;
        P_plus_phase2 = p_plus_phase2;
        P_minus_phase2 = p_minus_phase2;
        P_plus_phase3 = p_plus_phase3;
        P_minus_phase3 = p_minus_phase3;
    }

    /**
     * @return the Ri_plus
     */
    @JSONProperty(value = "Ri+", ignoreIfNull = true)
    public final EnergyValue getTotalReactiveInductiveEnergyIn() {
        return Ri_plus;
    }

    /**
     * @param ri_plus the a_plus to set
     */
    public final void setTotalReactiveInductiveEnergyIn(EnergyValue ri_plus) {
        Ri_plus = ri_plus;
    }

    /**
     * @return the Ri_minus
     */
    @JSONProperty(value = "Ri-", ignoreIfNull = true)
    public final EnergyValue getTotalReactiveInductiveEnergyOut() {
        return Ri_minus;
    }

    /**
     * @param ri_minus the ri_minus to set
     */
    public final void setTotalReactiveInductiveEnergyOut(EnergyValue ri_minus) {
        Ri_minus = ri_minus;
    }
    
    /**
     * @return the Rc_plus
     */
    @JSONProperty(value = "Rc+", ignoreIfNull = true)
    public final EnergyValue getTotalReactiveCapacitiveEnergyIn() {
        return Rc_plus;
    }

    /**
     * @param Rc_plus the a_plus to set
     */
    public final void setTotalReactiveCapacitiveEnergyIn(EnergyValue rc_plus) {
        Rc_plus = rc_plus;
    }

    /**
     * @return the Rc_minus
     */
    @JSONProperty(value = "Rc-", ignoreIfNull = true)
    public final EnergyValue getTotalReactiveCapacitiveEnergyOut() {
        return Rc_minus;
    }

    /**
     * @param rc_minus the rc_minus to set
     */
    public final void setTotalReactiveCapacitiveEnergyOut(EnergyValue rc_minus) {
        Rc_minus = rc_minus;
    }
    
    /**
     * @return the qi_plus
     */
    @JSONProperty(value = "Qi+", ignoreIfNull = true)
    public final PowerValue getTotalReactiveInductivePowerIn() {
        return Qi_plus;
    }

    /**
     * @param qi_plus the qi_plus to set
     */
    public final void setTotalReactiveInductivePowerIn(PowerValue qi_plus) {
        Qi_plus = qi_plus;
    }

    /**
     * @return the qi_minus
     */
    @JSONProperty(value = "Qi-", ignoreIfNull = true)
    public final PowerValue getTotalReactiveInductivePowerOut() {
        return Qi_minus;
    }

    /**
     * @param qi_minus the qi_minus to set
     */
    public final void setTotalReactiveInductivePowerOut(PowerValue qi_minus) {
        Qi_minus = qi_minus;
    }

    /**
     * @return the qc_plus
     */
    @JSONProperty(value = "Qc+", ignoreIfNull = true)
    public final PowerValue getTotalReactiveCapacitivePowerIn() {
        return Qc_plus;
    }

    /**
     * @param qc_plus the qc_plus to set
     */
    public final void setTotalReactiveCapacitivePowerIn(PowerValue qc_plus) {
        Qc_plus = qc_plus;
    }

    /**
     * @return the qc_minus
     */
    @JSONProperty(value = "Qc-", ignoreIfNull = true)
    public final PowerValue getTotalReactiveCapacitivePowerOut() {
        return Qc_minus;
    }

    /**
     * @param qc_minus the qc_minus to set
     */
    public final void setTotalReactiveCapacitivePowerOut(PowerValue qc_minus) {
        Qc_minus = qc_minus;
    }

    /**
     * @return the a_plus_phase1
     */
    @JSONProperty(value = "A+:1", ignoreIfNull = true)
    public final EnergyValue getTotalActiveEnergyInPhase1() {
        return A_plus_phase1;
    }

    /**
     * @param a_plus_phase1 the a_plus_phase1 to set
     */
    public final void setTotalActiveEnergyInPhase1(EnergyValue a_plus_phase1) {
        A_plus_phase1 = a_plus_phase1;
    }

    /**
     * @return the a_minus_phase1
     */
    @JSONProperty(value = "A-:1", ignoreIfNull = true)
    public final EnergyValue getTotalActiveEnergyOutPhase1() {
        return A_minus_phase1;
    }

    /**
     * @param a_minus_phase1 the a_minus_phase1 to set
     */
    public final void setTotalActiveEnergyOutPhase1(EnergyValue a_minus_phase1) {
        A_minus_phase1 = a_minus_phase1;
    }

    /**
     * @return the p_plus_phase1
     */
    @JSONProperty(value = "P+:1", ignoreIfNull = true)
    public final PowerValue getTotalActivePowerInPhase1() {
        return P_plus_phase1;
    }

    /**
     * @param p_plus_phase1 the p_plus_phase1 to set
     */
    public final void setTotalActivePowerInPhase1(PowerValue p_plus_phase1) {
        P_plus_phase1 = p_plus_phase1;
    }

    /**
     * @return the p_minus_phase1
     */
    @JSONProperty(value = "P-:1", ignoreIfNull = true)
    public final PowerValue getTotalActivePowerOutPhase1() {
        return P_minus_phase1;
    }

    /**
     * @param p_minus_phase1 the p_minus_phase1 to set
     */
    public final void setTotalActivePowerOutPhase1(PowerValue p_minus_phase1) {
        P_minus_phase1 = p_minus_phase1;
    }

    /**
     * @return the a_plus_phase1
     */
    @JSONProperty(value = "A+:2", ignoreIfNull = true)
    public final EnergyValue getTotalActiveEnergyInPhase2() {
        return A_plus_phase2;
    }

    /**
     * @param a_plus_phase2 the a_plus_phase2 to set
     */
    public final void setTotalActiveEnergyInPhase2(EnergyValue a_plus_phase2) {
        A_plus_phase2 = a_plus_phase2;
    }

    /**
     * @return the a_minus_phase2
     */
    @JSONProperty(value = "A-:2", ignoreIfNull = true)
    public final EnergyValue getTotalActiveEnergyOutPhase2() {
        return A_minus_phase2;
    }

    /**
     * @param a_minus_phase2 the a_minus_phase2 to set
     */
    public final void setTotalActiveEnergyOutPhase2(EnergyValue a_minus_phase2) {
        A_minus_phase2 = a_minus_phase2;
    }

    /**
     * @return the p_plus_phase2
     */
    @JSONProperty(value = "P+:2", ignoreIfNull = true)
    public final PowerValue getTotalActivePowerInPhase2() {
        return P_plus_phase2;
    }

    /**
     * @param p_plus_phase2 the p_plus_phase2 to set
     */
    public final void setTotalActivePowerInPhase2(PowerValue p_plus_phase2) {
        P_plus_phase2 = p_plus_phase2;
    }

    /**
     * @return the p_minus_phase2
     */
    @JSONProperty(value = "P-:2", ignoreIfNull = true)
    public final PowerValue getTotalActivePowerOutPhase2() {
        return P_minus_phase2;
    }

    /**
     * @param p_minus_phase2 the p_minus_phase2 to set
     */
    public final void setTotalActivePowerOutPhase2(PowerValue p_minus_phase2) {
        P_minus_phase2 = p_minus_phase2;
    }

    /**
     * @return the a_plus_phase3
     */
    @JSONProperty(value = "A+:3", ignoreIfNull = true)
    public final EnergyValue getTotalActiveEnergyInPhase3() {
        return A_plus_phase3;
    }

    /**
     * @param a_plus_phase3 the a_plus_phase3 to set
     */
    public final void setTotalActiveEnergyInPhase3(EnergyValue a_plus_phase3) {
        A_plus_phase3 = a_plus_phase3;
    }

    /**
     * @return the a_minus_phase3
     */
    @JSONProperty(value = "A-:3", ignoreIfNull = true)
    public final EnergyValue getTotalActiveEnergyOutPhase3() {
        return A_minus_phase3;
    }

    /**
     * @param a_minus_phase3 the a_minus_phase3 to set
     */
    public final void setTotalActiveEnergyOutPhase3(EnergyValue a_minus_phase3) {
        A_minus_phase3 = a_minus_phase3;
    }

    /**
     * @return the p_plus_phase3
     */
    @JSONProperty(value = "P+:3", ignoreIfNull = true)
    public final PowerValue getTotalActivePowerInPhase3() {
        return P_plus_phase3;
    }

    /**
     * @param p_plus_phase3 the p_plus_phase3 to set
     */
    public final void setTotalActivePowerInPhase3(PowerValue p_plus_phase3) {
        P_plus_phase3 = p_plus_phase3;
    }

    /**
     * @return the p_minus_phase3
     */
    @JSONProperty(value = "P-:3", ignoreIfNull = true)
    public final PowerValue getTotalActivePowerOutPhase3() {
        return P_minus_phase3;
    }

    /**
     * @param p_minus_phase3 the p_minus_phase3 to set
     */
    public final void setTotalActivePowerOutPhase3(PowerValue p_minus_phase3) {
        P_minus_phase3 = p_minus_phase3;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (Ri_plus != null ? Ri_plus.hashCode() : 0);
        result = 31 * result + (Ri_minus != null ? Ri_minus.hashCode() : 0);
        result = 31 * result + (Rc_plus != null ? Rc_plus.hashCode() : 0);
        result = 31 * result + (Rc_minus != null ? Rc_minus.hashCode() : 0);
        result = 31 * result + (Qi_plus != null ? Qi_plus.hashCode() : 0);
        result = 31 * result + (Qi_minus != null ? Qi_minus.hashCode() : 0);
        result = 31 * result + (Qc_plus != null ? Qc_plus.hashCode() : 0);
        result = 31 * result + (Qc_minus != null ? Qc_minus.hashCode() : 0);
        result = 31 * result + (A_plus_phase1 != null ? A_plus_phase1.hashCode() : 0);
        result = 31 * result + (A_minus_phase1 != null ? A_minus_phase1.hashCode() : 0);
        result = 31 * result + (A_plus_phase2 != null ? A_plus_phase2.hashCode() : 0);
        result = 31 * result + (A_minus_phase2 != null ? A_minus_phase2.hashCode() : 0);
        result = 31 * result + (A_plus_phase3 != null ? A_plus_phase3.hashCode() : 0);
        result = 31 * result + (A_minus_phase3 != null ? A_minus_phase3.hashCode() : 0);
        result = 31 * result + (P_plus_phase1 != null ? P_plus_phase1.hashCode() : 0);
        result = 31 * result + (P_minus_phase1 != null ? P_minus_phase1.hashCode() : 0);
        result = 31 * result + (P_plus_phase2 != null ? P_plus_phase2.hashCode() : 0);
        result = 31 * result + (P_minus_phase2 != null ? P_minus_phase2.hashCode() : 0);
        result = 31 * result + (P_plus_phase3 != null ? P_plus_phase3.hashCode() : 0);
        result = 31 * result + (P_minus_phase3 != null ? P_minus_phase3.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ThreePhaseEnergyMeasurement)) return false;
        if (!super.equals(o)) return false;

        ThreePhaseEnergyMeasurement that = (ThreePhaseEnergyMeasurement) o;

        if (A_minus_phase1 != null ? !A_minus_phase1.equals(that.A_minus_phase1) : that.A_minus_phase1 != null)
            return false;
        if (A_minus_phase2 != null ? !A_minus_phase2.equals(that.A_minus_phase2) : that.A_minus_phase2 != null)
            return false;
        if (A_minus_phase3 != null ? !A_minus_phase3.equals(that.A_minus_phase3) : that.A_minus_phase3 != null)
            return false;
        if (A_plus_phase1 != null ? !A_plus_phase1.equals(that.A_plus_phase1) : that.A_plus_phase1 != null)
            return false;
        if (A_plus_phase2 != null ? !A_plus_phase2.equals(that.A_plus_phase2) : that.A_plus_phase2 != null)
            return false;
        if (A_plus_phase3 != null ? !A_plus_phase3.equals(that.A_plus_phase3) : that.A_plus_phase3 != null)
            return false;
        if (P_minus_phase1 != null ? !P_minus_phase1.equals(that.P_minus_phase1) : that.P_minus_phase1 != null)
            return false;
        if (P_minus_phase2 != null ? !P_minus_phase2.equals(that.P_minus_phase2) : that.P_minus_phase2 != null)
            return false;
        if (P_minus_phase3 != null ? !P_minus_phase3.equals(that.P_minus_phase3) : that.P_minus_phase3 != null)
            return false;
        if (P_plus_phase1 != null ? !P_plus_phase1.equals(that.P_plus_phase1) : that.P_plus_phase1 != null)
            return false;
        if (P_plus_phase2 != null ? !P_plus_phase2.equals(that.P_plus_phase2) : that.P_plus_phase2 != null)
            return false;
        if (P_plus_phase3 != null ? !P_plus_phase3.equals(that.P_plus_phase3) : that.P_plus_phase3 != null)
            return false;
        if (Qc_minus != null ? !Qc_minus.equals(that.Qc_minus) : that.Qc_minus != null) return false;
        if (Qc_plus != null ? !Qc_plus.equals(that.Qc_plus) : that.Qc_plus != null) return false;
        if (Qi_minus != null ? !Qi_minus.equals(that.Qi_minus) : that.Qi_minus != null) return false;
        if (Qi_plus != null ? !Qi_plus.equals(that.Qi_plus) : that.Qi_plus != null) return false;
        if (Rc_minus != null ? !Rc_minus.equals(that.Rc_minus) : that.Rc_minus != null) return false;
        if (Rc_plus != null ? !Rc_plus.equals(that.Rc_plus) : that.Rc_plus != null) return false;
        if (Ri_minus != null ? !Ri_minus.equals(that.Ri_minus) : that.Ri_minus != null) return false;
        if (Ri_plus != null ? !Ri_plus.equals(that.Ri_plus) : that.Ri_plus != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "ThreePhaseEnergyMeasurement{" +
                "Ri_plus=" + Ri_plus +
                ", Ri_minus=" + Ri_minus +
                ", Rc_plus=" + Rc_plus +
                ", Rc_minus=" + Rc_minus +
                ", Qi_plus=" + Qi_plus +
                ", Qi_minus=" + Qi_minus +
                ", Qc_plus=" + Qc_plus +
                ", Qc_minus=" + Qc_minus +
                ", A_plus_phase1=" + A_plus_phase1 +
                ", A_minus_phase1=" + A_minus_phase1 +
                ", A_plus_phase2=" + A_plus_phase2 +
                ", A_minus_phase2=" + A_minus_phase2 +
                ", A_plus_phase3=" + A_plus_phase3 +
                ", A_minus_phase3=" + A_minus_phase3 +
                ", P_plus_phase1=" + P_plus_phase1 +
                ", P_minus_phase1=" + P_minus_phase1 +
                ", P_plus_phase2=" + P_plus_phase2 +
                ", P_minus_phase2=" + P_minus_phase2 +
                ", P_plus_phase3=" + P_plus_phase3 +
                ", P_minus_phase3=" + P_minus_phase3 +
                '}';
    }
}
