package com.cumulocity.sdk.client;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import org.junit.Test;

import static com.cumulocity.sdk.client.SourceUtils.optimizeSource;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SourceUtilsTest {

    @Test
    public void shouldOptimizeSourceWhenContainsUnnecessaryFields() {
        // Given
        final String uselessFieldName = "UselessParam";
        EventRepresentation representation = new EventRepresentation();
        ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        source.setId(GId.asGId("123"));
        source.set("uselessValue", uselessFieldName);
        representation.setSource(source);
        // When
        optimizeSource(representation);
        // Then
        assertNull(representation.getSource().get(uselessFieldName));
        assertEquals("{\"id\":\"123\"}", representation.getSource().toJSON());
    }

    @Test
    public void shouldNotChangeRepresentationIfSourceContainsOnlyId() {
        // Given
        AlarmRepresentation toOptimizeRepresentation = new AlarmRepresentation();
        ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        source.setId(GId.asGId("321"));
        toOptimizeRepresentation.setSource(source);
        final String jsonBeforeOptimize = toOptimizeRepresentation.toJSON();
        // When
        optimizeSource(toOptimizeRepresentation);
        // Then
        assertEquals(jsonBeforeOptimize, toOptimizeRepresentation.toJSON());
    }

    @Test
    public void shouldNotChangeRepresentationWithoutSource() {
        // Given
        final MeasurementRepresentation representation = new MeasurementRepresentation();
        final String jsonBeforeOptimize = representation.toJSON();
        // When
        optimizeSource(representation);
        // Then
        assertEquals(jsonBeforeOptimize, representation.toJSON());
    }
}