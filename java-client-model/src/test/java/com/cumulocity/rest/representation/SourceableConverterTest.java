package com.cumulocity.rest.representation;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SourceableConverterTest {

    private SourceableConverter converter;

    @BeforeEach
    public void before() {
        converter = new SourceableConverter();
    }

    @Test
    public void shouldOptimizeSourceWhenContainsUnnecessaryFields() throws JSONException {
        // Given
        final String uselessFieldName = "uselessField";
        ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        source.setId(GId.asGId(1L));
        source.setName("someName");
        source.setSelf("http://c8y.com/inventory/managedObjects/1");
        source.set(uselessFieldName, "someValue");
        // When
        final ManagedObjectRepresentation result = (ManagedObjectRepresentation) converter.toJSON(source);
        // Then
        assertNull(result.get(uselessFieldName));
        JSONAssert.assertEquals("{\"name\":\"someName\",\"self\":\"http://c8y.com/inventory/managedObjects/1\",\"id\":\"1\"}",
                result.toJSON(),
                JSONCompareMode.STRICT);
    }

    @Test
    public void shouldNotChangeRepresentationIfSourceContainsOnlyId() throws JSONException {
        // Given
        ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        source.setId(GId.asGId("321"));
        final String jsonBeforeOptimize = source.toJSON();
        // When
        final ManagedObjectRepresentation result = (ManagedObjectRepresentation) converter.toJSON(source);
        // Then
        JSONAssert.assertEquals(jsonBeforeOptimize, result.toJSON(), JSONCompareMode.STRICT);
    }

    @Test
    public void shouldNotChangeObjectWhenIsNotInstanceOfManagedObjectRepresentation() {
        // Given
        AlarmRepresentation alarmRepresentation = new AlarmRepresentation();
        alarmRepresentation.setText("exampleText");
        // When
        final AlarmRepresentation result = (AlarmRepresentation) converter.toJSON(alarmRepresentation);
        // Then
        assertEquals(alarmRepresentation, result);
    }

}