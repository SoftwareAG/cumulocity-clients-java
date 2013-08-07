package c8y;

import org.svenson.JSONProperty;

import com.cumulocity.model.measurement.MeasurementValue;

/**
 * Provides a representation for a electricity measurement, as reported by {@link ThreePhaseElectricitySensor}.
 * See <a>https://code.telcoassetmarketplace.com/devcommunity/index.php/c8ydocumentation/114/320#Energy</a> for details.
 * @author ricardomarques
 *
 */
public class ThreePhaseEnergyMeasurement extends SinglePhaseEnergyMeasurement {

    private MeasurementValue Ri_plus, Ri_minus, Rc_plus, Rc_minus;

    private MeasurementValue Qi_plus, Qi_minus, Qc_plus, Qc_minus;

    private MeasurementValue A_plus_phase1, A_minus_phase1, A_plus_phase2, A_minus_phase2, A_plus_phase3, A_minus_phase3;

    private MeasurementValue P_plus_phase1, P_minus_phase1, P_plus_phase2, P_minus_phase2, P_plus_phase3, P_minus_phase3;

    public ThreePhaseEnergyMeasurement() {
    }

    public ThreePhaseEnergyMeasurement(MeasurementValue a_plus, MeasurementValue a_minus, MeasurementValue p_plus, MeasurementValue p_minus, MeasurementValue ri_plus,
    		MeasurementValue ri_minus, MeasurementValue rc_plus, MeasurementValue rc_minus, MeasurementValue qi_plus, MeasurementValue qi_minus, MeasurementValue qc_plus,
    		MeasurementValue qc_minus, MeasurementValue a_plus_phase1, MeasurementValue a_minus_phase1, MeasurementValue a_plus_phase2,
    		MeasurementValue a_minus_phase2, MeasurementValue a_plus_phase3, MeasurementValue a_minus_phase3, MeasurementValue p_plus_phase1,
    		MeasurementValue p_minus_phase1, MeasurementValue p_plus_phase2, MeasurementValue p_minus_phase2, MeasurementValue p_plus_phase3,
    		MeasurementValue p_minus_phase3) {
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
    public final MeasurementValue getTotalReactiveInductiveEnergyIn() {
        return Ri_plus;
    }

    /**
     * @param ri_plus the a_plus to set
     */
    public final void setTotalReactiveInductiveEnergyIn(MeasurementValue ri_plus) {
        Ri_plus = ri_plus;
    }

    /**
     * @return the Ri_minus
     */
    @JSONProperty(value = "Ri-", ignoreIfNull = true)
    public final MeasurementValue getTotalReactiveInductiveEnergyOut() {
        return Ri_minus;
    }

    /**
     * @param ri_minus the ri_minus to set
     */
    public final void setTotalReactiveInductiveEnergyOut(MeasurementValue ri_minus) {
        Ri_minus = ri_minus;
    }
    
    /**
     * @return the Rc_plus
     */
    @JSONProperty(value = "Rc+", ignoreIfNull = true)
    public final MeasurementValue getTotalReactiveCapacitiveEnergyIn() {
        return Rc_plus;
    }

    /**
     * @param Rc_plus the a_plus to set
     */
    public final void setTotalReactiveCapacitiveEnergyIn(MeasurementValue rc_plus) {
        Rc_plus = rc_plus;
    }

    /**
     * @return the Rc_minus
     */
    @JSONProperty(value = "Rc-", ignoreIfNull = true)
    public final MeasurementValue getTotalReactiveCapacitiveEnergyOut() {
        return Rc_minus;
    }

    /**
     * @param rc_minus the rc_minus to set
     */
    public final void setTotalReactiveCapacitiveEnergyOut(MeasurementValue rc_minus) {
        Rc_minus = rc_minus;
    }
    
    /**
     * @return the qi_plus
     */
    @JSONProperty(value = "Qi+", ignoreIfNull = true)
    public final MeasurementValue getTotalReactiveInductivePowerIn() {
        return Qi_plus;
    }

    /**
     * @param qi_plus the qi_plus to set
     */
    public final void setTotalReactiveInductivePowerIn(MeasurementValue qi_plus) {
        Qi_plus = qi_plus;
    }

    /**
     * @return the qi_minus
     */
    @JSONProperty(value = "Qi-", ignoreIfNull = true)
    public final MeasurementValue getTotalReactiveInductivePowerOut() {
        return Qi_minus;
    }

    /**
     * @param qi_minus the qi_minus to set
     */
    public final void setTotalReactiveInductivePowerOut(MeasurementValue qi_minus) {
        Qi_minus = qi_minus;
    }

    /**
     * @return the qc_plus
     */
    @JSONProperty(value = "Qc+", ignoreIfNull = true)
    public final MeasurementValue getTotalReactiveCapacitivePowerIn() {
        return Qc_plus;
    }

    /**
     * @param qc_plus the qc_plus to set
     */
    public final void setTotalReactiveCapacitivePowerIn(MeasurementValue qc_plus) {
        Qc_plus = qc_plus;
    }

    /**
     * @return the qc_minus
     */
    @JSONProperty(value = "Qc-", ignoreIfNull = true)
    public final MeasurementValue getTotalReactiveCapacitivePowerOut() {
        return Qc_minus;
    }

    /**
     * @param qc_minus the qc_minus to set
     */
    public final void setTotalReactiveCapacitivePowerOut(MeasurementValue qc_minus) {
        Qc_minus = qc_minus;
    }

    /**
     * @return the a_plus_phase1
     */
    @JSONProperty(value = "A+:1", ignoreIfNull = true)
    public final MeasurementValue getTotalActiveEnergyInPhase1() {
        return A_plus_phase1;
    }

    /**
     * @param a_plus_phase1 the a_plus_phase1 to set
     */
    public final void setTotalActiveEnergyInPhase1(MeasurementValue a_plus_phase1) {
        A_plus_phase1 = a_plus_phase1;
    }

    /**
     * @return the a_minus_phase1
     */
    @JSONProperty(value = "A-:1", ignoreIfNull = true)
    public final MeasurementValue getTotalActiveEnergyOutPhase1() {
        return A_minus_phase1;
    }

    /**
     * @param a_minus_phase1 the a_minus_phase1 to set
     */
    public final void setTotalActiveEnergyOutPhase1(MeasurementValue a_minus_phase1) {
        A_minus_phase1 = a_minus_phase1;
    }

    /**
     * @return the p_plus_phase1
     */
    @JSONProperty(value = "P+:1", ignoreIfNull = true)
    public final MeasurementValue getTotalActivePowerInPhase1() {
        return P_plus_phase1;
    }

    /**
     * @param p_plus_phase1 the p_plus_phase1 to set
     */
    public final void setTotalActivePowerInPhase1(MeasurementValue p_plus_phase1) {
        P_plus_phase1 = p_plus_phase1;
    }

    /**
     * @return the p_minus_phase1
     */
    @JSONProperty(value = "P-:1", ignoreIfNull = true)
    public final MeasurementValue getTotalActivePowerOutPhase1() {
        return P_minus_phase1;
    }

    /**
     * @param p_minus_phase1 the p_minus_phase1 to set
     */
    public final void setTotalActivePowerOutPhase1(MeasurementValue p_minus_phase1) {
        P_minus_phase1 = p_minus_phase1;
    }

    /**
     * @return the a_plus_phase1
     */
    @JSONProperty(value = "A+:2", ignoreIfNull = true)
    public final MeasurementValue getTotalActiveEnergyInPhase2() {
        return A_plus_phase2;
    }

    /**
     * @param a_plus_phase2 the a_plus_phase2 to set
     */
    public final void setTotalActiveEnergyInPhase2(MeasurementValue a_plus_phase2) {
        A_plus_phase2 = a_plus_phase2;
    }

    /**
     * @return the a_minus_phase2
     */
    @JSONProperty(value = "A-:2", ignoreIfNull = true)
    public final MeasurementValue getTotalActiveEnergyOutPhase2() {
        return A_minus_phase2;
    }

    /**
     * @param a_minus_phase2 the a_minus_phase2 to set
     */
    public final void setTotalActiveEnergyOutPhase2(MeasurementValue a_minus_phase2) {
        A_minus_phase2 = a_minus_phase2;
    }

    /**
     * @return the p_plus_phase2
     */
    @JSONProperty(value = "P+:2", ignoreIfNull = true)
    public final MeasurementValue getTotalActivePowerInPhase2() {
        return P_plus_phase2;
    }

    /**
     * @param p_plus_phase2 the p_plus_phase2 to set
     */
    public final void setTotalActivePowerInPhase2(MeasurementValue p_plus_phase2) {
        P_plus_phase2 = p_plus_phase2;
    }

    /**
     * @return the p_minus_phase2
     */
    @JSONProperty(value = "P-:2", ignoreIfNull = true)
    public final MeasurementValue getTotalActivePowerOutPhase2() {
        return P_minus_phase2;
    }

    /**
     * @param p_minus_phase2 the p_minus_phase2 to set
     */
    public final void setTotalActivePowerOutPhase2(MeasurementValue p_minus_phase2) {
        P_minus_phase2 = p_minus_phase2;
    }

    /**
     * @return the a_plus_phase3
     */
    @JSONProperty(value = "A+:3", ignoreIfNull = true)
    public final MeasurementValue getTotalActiveEnergyInPhase3() {
        return A_plus_phase3;
    }

    /**
     * @param a_plus_phase3 the a_plus_phase3 to set
     */
    public final void setTotalActiveEnergyInPhase3(MeasurementValue a_plus_phase3) {
        A_plus_phase3 = a_plus_phase3;
    }

    /**
     * @return the a_minus_phase3
     */
    @JSONProperty(value = "A-:3", ignoreIfNull = true)
    public final MeasurementValue getTotalActiveEnergyOutPhase3() {
        return A_minus_phase3;
    }

    /**
     * @param a_minus_phase3 the a_minus_phase3 to set
     */
    public final void setTotalActiveEnergyOutPhase3(MeasurementValue a_minus_phase3) {
        A_minus_phase3 = a_minus_phase3;
    }

    /**
     * @return the p_plus_phase3
     */
    @JSONProperty(value = "P+:3", ignoreIfNull = true)
    public final MeasurementValue getTotalActivePowerInPhase3() {
        return P_plus_phase3;
    }

    /**
     * @param p_plus_phase3 the p_plus_phase3 to set
     */
    public final void setTotalActivePowerInPhase3(MeasurementValue p_plus_phase3) {
        P_plus_phase3 = p_plus_phase3;
    }

    /**
     * @return the p_minus_phase3
     */
    @JSONProperty(value = "P-:3", ignoreIfNull = true)
    public final MeasurementValue getTotalActivePowerOutPhase3() {
        return P_minus_phase3;
    }

    /**
     * @param p_minus_phase3 the p_minus_phase3 to set
     */
    public final void setTotalActivePowerOutPhase3(MeasurementValue p_minus_phase3) {
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
