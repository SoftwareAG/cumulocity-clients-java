package com.cumulocity.sdk.agent.action;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.agent.model.DevicesManagingAgent;
import com.cumulocity.sdk.client.Platform;

public class UploadDeviceMeasurementsActionTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Platform platform;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private DevicesManagingAgent<?> agent;

    private UploadDeviceMeasurementsAction testObj;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testObj = new UploadDeviceMeasurementsAction(platform, agent);
    }

    @Test
    public void testPerformStartupAction() throws Exception {
        //given
        Queue<MeasurementRepresentation> queue = new ConcurrentLinkedQueue<MeasurementRepresentation>();
        MeasurementRepresentation mr = new MeasurementRepresentation();
        queue.add(mr);
        when(agent.getMeasurementsQueue()).thenReturn(queue);

        //when
        testObj.run();

        //then
        verify(platform.getMeasurementApi(), times(1)).create(mr);
    }
}
