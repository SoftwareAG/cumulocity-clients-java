package com.cumulocity.sdk.client.devicecontrol;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.cumulocity.model.operation.OperationStatus;

public class OperationFilterTest {
    @Test
    public void shouldHaveStatusAndDeviceAndAgent() throws Exception {
        // Given
        OperationStatus status = OperationStatus.EXECUTING;
        String deviceId = "deviceId";
        String agentId = "agentId";

        //When
        OperationFilter filter = new OperationFilter().byStatus(status).byDevice(deviceId).byAgent(agentId);

        //Then
        assertThat(filter.getStatus(), is(status));
        assertThat(filter.getDevice(), is(deviceId));
        assertThat(filter.getAgent(), is(agentId));
    }

}
