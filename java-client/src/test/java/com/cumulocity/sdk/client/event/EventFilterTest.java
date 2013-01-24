package com.cumulocity.sdk.client.event;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Date;

import org.junit.Test;

import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;

public class EventFilterTest {

    @Test
    public void shouldHoldTypeAndSource() throws Exception {
        ManagedObjectRepresentation mo = new ManagedObjectRepresentation();
        EventFilter filter = new EventFilter().byType("type").bySource(mo);
        assertThat(filter.getType(), is("type"));
        assertThat(filter.getSource(), sameInstance(mo));
    }
    
    @Test
    public void shouldHoldFragmentTypeAndDate() throws Exception {
        Date fromDate = new Date(System.currentTimeMillis());
        Date toDate = new Date(System.currentTimeMillis());
        EventFilter filter = new EventFilter().byFragmentType(Object.class).byDate(fromDate, toDate);
        assertThat(filter.getFragmentType(), is(Object.class));
        assertThat(filter.getFromDate(), sameInstance(fromDate));
        assertThat(filter.getToDate(), sameInstance(toDate));
    }
}
