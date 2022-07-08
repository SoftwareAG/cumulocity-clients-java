package com.cumulocity.rest.representation;

import org.junit.jupiter.api.Test;
import org.svenson.JSON;
import org.svenson.JSONParser;

import static org.assertj.core.api.Assertions.assertThat;

class BaseResourceRepresentationSerializationTest {

    private static final String REPRESENTATION_JSON = "{\"self\":\"selfExample\"}";
    private static final String REPRESENTATION_JSON_EXTRA_UNKNOWN_FIELD = "{\"self\":\"selfExample\",\"my_property\":\"my_value\"}";
    private static final String REPRESENTATION_JSON_WITH_SELF_DECODED = "{\"self\":\"selfExample\",\"selfDecoded\":null}";

    private final JSONParser jsonParser = new JSONParser();
    private final JSON jsonGenerator = new JSON();


    @Test
    public final void shouldDeserialize() {
        //when
        final BaseResourceRepresentation representation = jsonParser.parse(BaseResourceRepresentation.class, REPRESENTATION_JSON);

        //then
        assertThat(representation).isNotNull();
        assertThat(representation.getSelf()).isEqualTo("selfExample");
        assertThat(representation.getSelfDecoded()).isEqualTo("selfExample");
    }

    @Test
    public final void shouldDeserializeWithSelfDecodedField() {
        //when
        final BaseResourceRepresentation representation = jsonParser.parse(BaseResourceRepresentation.class, REPRESENTATION_JSON_WITH_SELF_DECODED);

        //then
        assertThat(representation).isNotNull();
        assertThat(representation.getSelf()).isEqualTo("selfExample");
        assertThat(representation.getSelfDecoded()).isEqualTo("selfExample");
    }

    @Test
    public final void shouldDeserializeWithUnknownExtraField() {
        //when
        final BaseResourceRepresentation representation = jsonParser.parse(BaseResourceRepresentation.class, REPRESENTATION_JSON_EXTRA_UNKNOWN_FIELD);

        //then
        assertThat(representation).isNotNull();
        assertThat(representation.getSelf()).isEqualTo("selfExample");
        assertThat(representation.getSelfDecoded()).isEqualTo("selfExample");
    }

    @Test
    public final void shouldSerialize() {
        //given
        final BaseResourceRepresentation representation = new BaseResourceRepresentation();
        representation.setSelf("selfExample");

        //when
        final String json = jsonGenerator.forValue(representation);

        //then
        assertThat(json).isEqualTo(REPRESENTATION_JSON);
    }
}
