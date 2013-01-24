package com.cumulocity.sdk.agent.action;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.agent.driver.DeviceDriver;
import com.cumulocity.sdk.agent.model.Device;
import com.cumulocity.sdk.agent.model.DevicesManagingAgent;

public class ObtainDeviceMeasurementsActionTest {

    @Mock
    private DevicesManagingAgent<Device> agent;

    private Queue<MeasurementRepresentation> queue = new LinkedList<MeasurementRepresentation>();

    @Mock
    private Device device1;

    @Mock
    private Device device2;

    @Mock
    private DeviceDriver<Device> deviceDriver;

    private ObtainDeviceMeasurementsAction<Device> action;

    private MeasurementRepresentation mr11 = measurementRepresentation("11");

    private MeasurementRepresentation mr12 = measurementRepresentation("12");

    private MeasurementRepresentation mr2 = measurementRepresentation("2");

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(deviceDriver.loadMeasuremntsFromDevice(device1)).thenReturn(asList(mr11, mr12));
        when(deviceDriver.loadMeasuremntsFromDevice(device2)).thenReturn(asList(mr2));

        action = new ObtainDeviceMeasurementsAction<Device>(agent);
        action.setDeviceDriver(deviceDriver);
    }

    @Test
    public void testPerformStartupAction() {
        //given
        when(agent.getDevices()).thenReturn(asList(device1, device2));
        when(agent.getMeasurementsQueue()).thenReturn(queue);

        //when
        action.run();

        //then
        Queue<MeasurementRepresentation> expected = new LinkedList<MeasurementRepresentation>(asList(mr11, mr12, mr2));
        assertThat(queue, is(expected));
    }

    private static MeasurementRepresentation measurementRepresentation(String id) {
        MeasurementRepresentation mr = new MeasurementRepresentation();
        mr.setId(new GId(id));
        return mr;
    }
}
