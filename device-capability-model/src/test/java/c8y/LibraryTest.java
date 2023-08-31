package c8y;

import com.cumulocity.model.Document;
import com.cumulocity.model.idtype.GId;
import org.apache.commons.beanutils.ConversionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.svenson.JSONParseException;
import org.svenson.tokenize.InputStreamSource;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {

    private ManagedObject sensorLib;
    private ManagedObject deviceMgmtLib;
    private ManagedObject incorrectFragmentLib;
    private ManagedObject incorrectFragmentLib2;

    @BeforeEach
    public void setup() {
        sensorLib = readFile("/sensor-library");
        deviceMgmtLib = readFile("/device-management-library.json");
        incorrectFragmentLib = readFile("/incorrect-fragment-library.json");
        incorrectFragmentLib2 = readFile("/incorrect-fragment-library2.json");
    }

	@SuppressWarnings("unchecked")
	private ManagedObject readFile(String file) {
		InputStream is = getClass().getResourceAsStream(file);
		InputStreamSource source = new InputStreamSource(is, true);
		return ManagedObject.getJSONParser().parse(ManagedObject.class, source);
	}

	@Test
	public void trivialCases() {
		assertTrue( sensorLib.get(AccelerationSensor.class) instanceof AccelerationSensor);
		assertTrue( sensorLib.get(MotionSensor.class) instanceof MotionSensor);
		assertTrue( sensorLib.get(SinglePhaseElectricitySensor .class) instanceof SinglePhaseElectricitySensor);
		assertTrue( sensorLib.get(ThreePhaseElectricitySensor.class) instanceof ThreePhaseElectricitySensor);
		assertTrue( sensorLib.get(TemperatureSensor.class) instanceof TemperatureSensor);

		assertTrue( deviceMgmtLib.get(Availability.class) instanceof Availability);
		assertTrue( deviceMgmtLib.get(RequiredAvailability.class) instanceof RequiredAvailability);
	}

	@Test
	public void temperatureMeasurement() {
		TemperatureMeasurement m = sensorLib.get(TemperatureMeasurement.class);
		assertEquals(23, m.getTemperature().intValue());
	}

    @Test
    public void accelerationMeasurement() {
        AccelerationMeasurement m = sensorLib.get(AccelerationMeasurement.class);
        assertEquals(new BigDecimal("8.36"), m.getAccelerationValue());
    }

    @Test
    public void availability() {
        Availability a = deviceMgmtLib.get(Availability.class);
        assertEquals(ConnectionState.CONNECTED, a.getStatus());
    }

    @Test
    public void requiredAvailability() {
        RequiredAvailability ra = deviceMgmtLib.get(RequiredAvailability.class);
        assertEquals(60, ra.getResponseInterval());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReadArrayOnIncorrectPropertyFields() {
        // should be able to read as
        List<Object> positions = (List<Object>) incorrectFragmentLib.getFragment("c8y_Position");
        assertEquals("alt", positions.get(0));
        assertEquals(67L, positions.get(1));

        assertEquals(true, incorrectFragmentLib.getFragment("c8y_Relay"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReadObjectOnIncorrectPropertyFields() {
        Map<String, Object> position = (Map<String, Object>) incorrectFragmentLib.getFragment("c8y_Position");

        assertEquals(67L, position.get("lat"));
        assertEquals("123", position.get("lng"));
        assertEquals("xxxxstr", position.get("alt"));
    }

    @Test
    public void shouldNotReadAsClassOnIncorrectPropertyFields() {
        assertNull(incorrectFragmentLib.get(Position.class));
        assertNull(incorrectFragmentLib.get(Relay.class));
        assertNull(incorrectFragmentLib2.get(Position.class));
    }

    @Test
    public void shouldThrowExceptionOnExplicitParseOnIncorrectPropertyFields() {
        assertThatThrownBy(() -> incorrectFragmentLib.get("c8y_Position", Position.class))
                .isInstanceOf(JSONParseException.class);

        assertThatThrownBy(() -> incorrectFragmentLib.get("c8y_Relay", Relay.class))
                .isInstanceOf(JSONParseException.class);

        assertThatThrownBy(() -> incorrectFragmentLib2.get("c8y_Position", Position.class))
                .isInstanceOfAny(JSONParseException.class, ConversionException.class);
    }

    public static final class ManagedObject extends Document<GId> {
    }

}
