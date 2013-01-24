package com.cumulocity.sdk.client.measurement.autopoll;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.cumulocity.sdk.client.devicecontrol.autopoll.OperationsQueue;

public class MeasurementRequestTaskTest {
    
    @Test
    public void emptyQueueShouldAcceptMoreThanOneMeasurementAgentRequest() throws InterruptedException {
        //given
        MeasurementRequestTask mrt = new MeasurementRequestTask();
        OperationsQueue queue = new OperationsQueue();
        
        //when
        queue.add(mrt.getNewMeasurementOperation());
        Thread.sleep(1); // unique measurement operations use system time in millis. To ensure unique op wait at least 1 milli
        queue.add(mrt.getNewMeasurementOperation());
        
        //then
        assertTrue(queue.size() == 2);
    }

}
