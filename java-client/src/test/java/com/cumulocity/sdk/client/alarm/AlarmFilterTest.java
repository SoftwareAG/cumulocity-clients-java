package com.cumulocity.sdk.client.alarm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;

public class AlarmFilterTest {

    private AlarmFilter alarmFilter = new AlarmFilter();

    @Test
    public void shouldHoldStatusAndSource() {
        // given
        ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        CumulocityAlarmStatuses status = CumulocityAlarmStatuses.ACKNOWLEDGED;

        // when
        AlarmFilter filter = alarmFilter.bySource(source).byStatus(status);

        // then
        assertThat(filter.getSource(), is(source));
        assertThat(filter.getStatus(), is(status));
    }

}
