package com.cumulocity.sdk.client.measurement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;

public class MeasurementFilterTest {

    @Test
    public void shouldHoldTypeSourceDateAndFragmentType() throws Exception {
        ManagedObjectRepresentation mo = new ManagedObjectRepresentation();

        Date fromDate = new Date(0L);
        Date toDate = new Date(1L);
        MeasurementFilter filter = new MeasurementFilter().byType("type").bySource(mo).byFragmentType(NonRelevantFragmentType.class).byDate(
                fromDate, toDate);

        assertThat(filter.getType(), is("type"));
        assertThat(filter.getSource(), sameInstance(mo));
        assertThat(filter.getFragmentType(), Matchers.<Object>equalTo(NonRelevantFragmentType.class));
        assertThat(filter.getFromDate(), is(fromDate));
        assertThat(filter.getToDate(), is(toDate));
    }

    private static class NonRelevantFragmentType {
    }
}
