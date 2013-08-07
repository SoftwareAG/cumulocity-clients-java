package com.cumulocity.model.energy.measurement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.svenson.JSON;
import org.svenson.JSONParser;

public class ThreePhaseEnergyMeasurementTest {

    private static final String K_V_AR = "kVAr";

    private static final String K_V_ARH = "kVArh";

    private static final String W = "W";

    private static final String K_WH = "kWh";

    private ThreePhaseEnergyMeasurement measurement;

    private EnergyValue A_plus, A_minus;

    private PowerValue P_plus, P_minus;

    private EnergyValue Ri_plus, Ri_minus, Rc_plus, Rc_minus;

    private PowerValue Qi_plus, Qi_minus, Qc_plus, Qc_minus;

    private EnergyValue A_plus_phase1, A_minus_phase1, A_plus_phase2, A_minus_phase2, A_plus_phase3, A_minus_phase3;

    private PowerValue P_plus_phase1, P_minus_phase1, P_plus_phase2, P_minus_phase2, P_plus_phase3, P_minus_phase3;

    @Before
    public void setUp() {
        measurement = new ThreePhaseEnergyMeasurement();

        A_plus = new EnergyValue();
        A_plus.setValue(new BigDecimal(10));
        A_plus.setUnit(K_WH);

        A_minus = new EnergyValue();
        A_minus.setValue(new BigDecimal(20));
        A_minus.setUnit(K_WH);

        A_plus_phase1 = new EnergyValue();
        A_plus_phase1.setValue(new BigDecimal(3));
        A_plus_phase1.setUnit(K_WH);

        A_minus_phase1 = new EnergyValue();
        A_minus_phase1.setValue(new BigDecimal(6));
        A_minus_phase1.setUnit(K_WH);

        A_plus_phase2 = new EnergyValue();
        A_plus_phase2.setValue(new BigDecimal(5));
        A_plus_phase2.setUnit(K_WH);

        A_minus_phase2 = new EnergyValue();
        A_minus_phase2.setValue(new BigDecimal(11));
        A_minus_phase2.setUnit(K_WH);

        A_plus_phase3 = new EnergyValue();
        A_plus_phase3.setValue(new BigDecimal(2));
        A_plus_phase3.setUnit(K_WH);

        A_minus_phase3 = new EnergyValue();
        A_minus_phase3.setValue(new BigDecimal(3));
        A_minus_phase3.setUnit(K_WH);

        P_plus = new PowerValue();
        P_plus.setValue(new BigDecimal(23));
        P_plus.setUnit(W);

        P_minus = new PowerValue();
        P_minus.setValue(new BigDecimal(0));
        P_minus.setUnit(W);

        P_plus_phase1 = new PowerValue();
        P_plus_phase1.setValue(new BigDecimal(32));
        P_plus_phase1.setUnit(W);

        P_minus_phase1 = new PowerValue();
        P_minus_phase1.setValue(new BigDecimal(12));
        P_minus_phase1.setUnit(W);

        P_plus_phase2 = new PowerValue();
        P_plus_phase2.setValue(new BigDecimal(321));
        P_plus_phase2.setUnit(W);

        P_minus_phase2 = new PowerValue();
        P_minus_phase2.setValue(new BigDecimal(121));
        P_minus_phase2.setUnit(W);

        P_plus_phase3 = new PowerValue();
        P_plus_phase3.setValue(new BigDecimal(3211));
        P_plus_phase3.setUnit(W);

        P_minus_phase3 = new PowerValue();
        P_minus_phase3.setValue(new BigDecimal(1));
        P_minus_phase3.setUnit(W);

        Ri_plus = new EnergyValue();
        Ri_plus.setValue(new BigDecimal(234));
        Ri_plus.setUnit(K_V_ARH);

        Ri_minus = new EnergyValue();
        Ri_minus.setValue(new BigDecimal(1234));
        Ri_minus.setUnit(K_V_ARH);

        Rc_plus = new EnergyValue();
        Rc_plus.setValue(new BigDecimal(42));
        Rc_plus.setUnit(K_V_ARH);

        Rc_minus = new EnergyValue();
        Rc_minus.setValue(new BigDecimal(12));
        Rc_minus.setUnit(K_V_ARH);

        Qi_plus = new PowerValue();
        Qi_plus.setValue(new BigDecimal(1212));
        Qi_plus.setUnit(K_V_AR);

        Qi_minus = new PowerValue();
        Qi_minus.setValue(new BigDecimal(12));
        Qi_minus.setUnit(K_V_AR);

        Qc_plus = new PowerValue();
        Qc_plus.setValue(new BigDecimal(33));
        Qc_plus.setUnit(K_V_AR);

        Qc_minus = new PowerValue();
        Qc_minus.setValue(new BigDecimal(31));
        Qc_minus.setUnit(K_V_AR);

        measurement.setTotalActiveEnergyIn(A_plus);
        measurement.setTotalActiveEnergyInPhase1(A_plus_phase1);
        measurement.setTotalActiveEnergyInPhase2(A_plus_phase2);
        measurement.setTotalActiveEnergyInPhase3(A_plus_phase3);
        measurement.setTotalActiveEnergyOut(A_minus);
        measurement.setTotalActiveEnergyOutPhase1(A_minus_phase1);
        measurement.setTotalActiveEnergyOutPhase2(A_minus_phase2);
        measurement.setTotalActiveEnergyOutPhase3(A_minus_phase3);
        measurement.setTotalActivePowerIn(P_plus);
        measurement.setTotalActivePowerInPhase1(P_plus_phase1);
        measurement.setTotalActivePowerInPhase2(P_plus_phase2);
        measurement.setTotalActivePowerInPhase3(P_plus_phase3);
        measurement.setTotalActivePowerOut(P_plus);
        measurement.setTotalActivePowerOutPhase1(P_minus_phase1);
        measurement.setTotalActivePowerOutPhase2(P_minus_phase2);
        measurement.setTotalActivePowerOutPhase3(P_minus_phase3);
        measurement.setTotalReactiveCapacitiveEnergyIn(Rc_plus);
        measurement.setTotalReactiveCapacitiveEnergyOut(Rc_minus);
        measurement.setTotalReactiveCapacitivePowerIn(Qc_plus);
        measurement.setTotalReactiveCapacitivePowerOut(Qc_minus);
        measurement.setTotalReactiveInductiveEnergyIn(Ri_plus);
        measurement.setTotalReactiveInductiveEnergyOut(Ri_minus);
        measurement.setTotalReactiveInductivePowerIn(Qi_plus);
        measurement.setTotalReactiveInductivePowerOut(Qi_minus);
    }

    @Test
    public void shouldSerializeAndDeserializeCorrectly() {

        String serialized = JSON.defaultJSON().forValue(measurement);
        ThreePhaseEnergyMeasurement newMeasurement = JSONParser.defaultJSONParser().parse(ThreePhaseEnergyMeasurement.class, serialized);

        assertTrue(serialized.contains("\"A+\":"));     // According to wiki page
        assertTrue(serialized.contains("\"A+:1\":"));   // According to wiki page
        assertTrue(serialized.contains("\"A+:2\":"));   // According to wiki page
        assertTrue(serialized.contains("\"A+:3\":"));   // According to wiki page
        assertTrue(serialized.contains("\"A-\":"));     // According to wiki page
        assertTrue(serialized.contains("\"A-:1\":"));   // According to wiki page
        assertTrue(serialized.contains("\"A-:2\":"));   // According to wiki page
        assertTrue(serialized.contains("\"A-:3\":"));   // According to wiki page
        assertTrue(serialized.contains("\"P+\":"));     // According to wiki page
        assertTrue(serialized.contains("\"P+:1\":"));   // According to wiki page
        assertTrue(serialized.contains("\"P+:2\":"));   // According to wiki page
        assertTrue(serialized.contains("\"P+:3\":"));   // According to wiki page
        assertTrue(serialized.contains("\"Rc+\":"));    // According to wiki page
        assertTrue(serialized.contains("\"Rc-\":"));    // According to wiki page
        assertTrue(serialized.contains("\"Ri+\":"));    // According to wiki page
        assertTrue(serialized.contains("\"Ri-\":"));    // According to wiki page
        assertTrue(serialized.contains("\"Qc+\":"));    // According to wiki page
        assertTrue(serialized.contains("\"Qc-\":"));    // According to wiki page
        assertTrue(serialized.contains("\"Qi+\":"));    // According to wiki page
        assertTrue(serialized.contains("\"Qi-\":"));    // According to wiki page
        
        assertEquals(measurement.getTotalActiveEnergyIn(), newMeasurement.getTotalActiveEnergyIn());
        assertEquals(measurement.getTotalActiveEnergyInPhase1(), newMeasurement.getTotalActiveEnergyInPhase1());
        assertEquals(measurement.getTotalActiveEnergyInPhase2(), newMeasurement.getTotalActiveEnergyInPhase2());
        assertEquals(measurement.getTotalActiveEnergyInPhase3(), newMeasurement.getTotalActiveEnergyInPhase3());
        assertEquals(measurement.getTotalActiveEnergyOut(), newMeasurement.getTotalActiveEnergyOut());
        assertEquals(measurement.getTotalActiveEnergyOutPhase1(), newMeasurement.getTotalActiveEnergyOutPhase1());
        assertEquals(measurement.getTotalActiveEnergyOutPhase2(), newMeasurement.getTotalActiveEnergyOutPhase2());
        assertEquals(measurement.getTotalActiveEnergyOutPhase3(), newMeasurement.getTotalActiveEnergyOutPhase3());
        assertEquals(measurement.getTotalActivePowerIn(), newMeasurement.getTotalActivePowerIn());
        assertEquals(measurement.getTotalActivePowerInPhase1(), newMeasurement.getTotalActivePowerInPhase1());
        assertEquals(measurement.getTotalActivePowerInPhase2(), newMeasurement.getTotalActivePowerInPhase2());
        assertEquals(measurement.getTotalActivePowerInPhase3(), newMeasurement.getTotalActivePowerInPhase3());
        assertEquals(measurement.getTotalActivePowerOut(), newMeasurement.getTotalActivePowerOut());
        assertEquals(measurement.getTotalActivePowerOutPhase1(), newMeasurement.getTotalActivePowerOutPhase1());
        assertEquals(measurement.getTotalActivePowerOutPhase2(), newMeasurement.getTotalActivePowerOutPhase2());
        assertEquals(measurement.getTotalActivePowerOutPhase3(), newMeasurement.getTotalActivePowerOutPhase3());
        assertEquals(measurement.getTotalReactiveCapacitiveEnergyIn(), newMeasurement.getTotalReactiveCapacitiveEnergyIn());
        assertEquals(measurement.getTotalReactiveCapacitiveEnergyOut(), newMeasurement.getTotalReactiveCapacitiveEnergyOut());
        assertEquals(measurement.getTotalReactiveCapacitivePowerIn(), newMeasurement.getTotalReactiveCapacitivePowerIn());
        assertEquals(measurement.getTotalReactiveCapacitivePowerOut(), newMeasurement.getTotalReactiveCapacitivePowerOut());
        assertEquals(measurement.getTotalReactiveInductiveEnergyIn(), newMeasurement.getTotalReactiveInductiveEnergyIn());
        assertEquals(measurement.getTotalReactiveInductiveEnergyOut(), newMeasurement.getTotalReactiveInductiveEnergyOut());
        assertEquals(measurement.getTotalReactiveInductivePowerIn(), newMeasurement.getTotalReactiveInductivePowerIn());
        assertEquals(measurement.getTotalReactiveInductivePowerOut(), newMeasurement.getTotalReactiveInductivePowerOut());
    }
}
