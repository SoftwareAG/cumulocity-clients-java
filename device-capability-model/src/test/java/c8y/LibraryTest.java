package c8y;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.svenson.tokenize.InputStreamSource;

import com.cumulocity.model.ManagedObject;
import com.cumulocity.model.idtype.GId;

public class LibraryTest {

	private ManagedObject<GId> sensorLib;
	private ManagedObject<GId> deviceMgmtLib;
	
	@Before
	public void setup() {
		sensorLib = readFile("/sensor-library");
		deviceMgmtLib = readFile("/device-management-library");
	}

	@SuppressWarnings("unchecked")
	private ManagedObject<GId> readFile(String file) {
		InputStream is = getClass().getResourceAsStream(file);
		InputStreamSource source = new InputStreamSource(is, true);
		return ManagedObject.getJSONParser().parse(ManagedObject.class, source);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void trivialCases() {
		assertTrue( sensorLib.get(AccelerationSensor.class) instanceof AccelerationSensor);
		assertTrue( sensorLib.get(MotionSensor.class) instanceof MotionSensor);
		assertTrue( sensorLib.get(SinglePhaseElectricitySensor .class) instanceof SinglePhaseElectricitySensor);
		assertTrue( sensorLib.get(ThreePhaseElectricitySensor.class) instanceof ThreePhaseElectricitySensor);
		assertTrue( sensorLib.get(TemperatureSensor.class) instanceof TemperatureSensor);
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
}
