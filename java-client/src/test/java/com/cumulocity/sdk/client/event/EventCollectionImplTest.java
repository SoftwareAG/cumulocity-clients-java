package com.cumulocity.sdk.client.event;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.event.EventCollectionRepresentation;
import com.cumulocity.rest.representation.event.EventMediaType;

public class EventCollectionImplTest {

    EventCollectionImpl target;

    @Before
    public void setup() {
        target = new EventCollectionImpl(null, null, 0);
    }

    @Test
    public void shouldHaveEventCollectionMediaType() throws Exception {
        CumulocityMediaType mediaType = EventMediaType.EVENT_COLLECTION;
        assertThat(target.getMediaType(), equalTo(mediaType));
    }

    @Test
    public void shouldHaveEventCollectionRepresentationResponseClass() throws Exception {
        assertThat(target.getResponseClass(), equalTo(EventCollectionRepresentation.class));
    }
}
