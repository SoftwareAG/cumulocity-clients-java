package com.cumulocity.model.authentication;


import com.google.common.base.Optional;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonsTest {

    private static final String JSON_FIELD_KEY = "month";
    private static final String JSON_FIELD_VALUE = "APRIL";
    private static final String JSON = "{\"" + JSON_FIELD_KEY + "\":\"" + JSON_FIELD_VALUE + "\"}";

    @Test
    public void shouldReadStringValueFromJson() {
        //when
        Optional<String> value = Jsons.readField(JSON_FIELD_KEY, JSON);

        //then
        assertThat(value.isPresent()).isTrue();
        assertThat(value.get()).isEqualTo(JSON_FIELD_VALUE);
    }

    @ParameterizedTest
    @MethodSource("emptyString")
    public void shouldReturnAbsentWhenValueIsEmpty(String emptyString) {
        // When
        final Optional<String> stringOptional = Jsons.readField(emptyString, JSON);
        // Then
        assertThat(stringOptional).isEqualTo(Optional.absent());
    }

    private static Stream<String> emptyString() {
        return Stream.of(
                "", null
        );
    }

    @Test
    public void shouldReturnEmptyOptionalWhenCannotFindValue() {
        //given
        String nonExistingField = "qwerty";

        //when
        Optional<String> value = Jsons.readField(nonExistingField, JSON);

        //then
        assertThat(value.isPresent()).isFalse();
    }

    @Test
    public void shouldConvertToStringWhenValueIsInt() throws JSONException {
        //given
        JSONObject jsonWithInt = new JSONObject();
        String key = "salary";
        int valueFromJson = 1000;
        jsonWithInt.put(key, valueFromJson);

        //when
        Optional<String> value = Jsons.readField(key, jsonWithInt.toString());

        //then
        assertThat(value.isPresent()).isTrue();
        assertThat(value.get()).isEqualTo(Integer.toString(valueFromJson));
    }
}
