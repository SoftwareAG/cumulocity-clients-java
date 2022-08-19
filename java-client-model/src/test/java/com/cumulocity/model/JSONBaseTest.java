package com.cumulocity.model;

import com.cumulocity.model.utils.StringReader;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class JSONBaseTest {

    @ParameterizedTest
    @ValueSource(strings = {"/json/unknownSimpleField.json", "/json/unknownComplexField.json"})
    void shouldIgnoreUnknownFields(String resource) throws Exception {
        //given
        String json = StringReader.readResource(resource);
        //when
        final TestModel testModel = JSONBase.fromJSON(json, TestModel.class);

        //then
        assertThat(testModel).isNotNull();
        assertThat(testModel.getMeasurement()).isNotNull();
        assertThat(testModel.getType()).isNotBlank();
    }

    @Test
    void shouldIgnoreUnknownFieldsInNestedStructure() throws Exception {
        //given
        String json = StringReader.readResource("/json/unknownComplexFieldInNestedStructure.json");
        //when
        final TestModel testModel = JSONBase.fromJSON(json, TestModel.class);

        //then
        assertThat(testModel).isNotNull();
        assertThat(testModel.getMeasurement()).isNotNull();
        assertThat(testModel.getType()).isNotBlank();
        assertThat(testModel.getDetails().getDescription()).isNotBlank();
    }

    @Data
    public static class TestModel {
        private Integer measurement;
        private String type;
        private Details details;
    }

    @Data
    public static class Details {
        private String description;
    }
}