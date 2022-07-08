package com.cumulocity.rest.representation.mongo.queryplanner;

import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static com.cumulocity.model.JSONBase.fromJSON;
import static org.assertj.core.api.Assertions.assertThat;

class QueryPlannerValidatorTest {

    private final String sampleQueryPlanner = QueryPlanLoader.getSampleQueryPlanner();

    @BeforeAll
    static void beforeAll() {
        assertThat(QueryPlannerValidator.getFields()).isNotNull();
        assertThat(QueryPlannerValidator.getFields().size()).isEqualTo(4);
    }

    @SuppressWarnings("unchecked")
    @Test
    void isValid() {
        assertThat(QueryPlannerValidator.isValid(sampleQueryPlanner)).isTrue();
        assertThat(QueryPlannerValidator.isValid(fromJSON(sampleQueryPlanner, Map.class))).isTrue();
    }

    @Test
    void isInvalid() {
        val json = "{\"name\":\"ABC\", \"age\": 20}";

        assertThat(QueryPlannerValidator.isValid("")).isFalse();
        assertThat(QueryPlannerValidator.isValid(json)).isFalse();
    }

    @SuppressWarnings("unchecked")
    @Test
    void isInvalid_MissingRequiredFields() {
        val json1 = "{" +
                "    \"queryPlanner\": {" +
                "        \"winningPlan\": []," +
                "        \"rejectedPlans\": []" +
                "    }," +
                "    \"serverInfo\" : \"something\"" +
                "}";

        val json2 = "{" +
                "    \"executionStats\": {" +
                "        \"winningPlan\": []" +
                "    }" +
                "}";

        Arrays.asList(json1, json2).forEach(json -> {
            assertThat(QueryPlannerValidator.isValid(json)).isFalse();
            assertThat(QueryPlannerValidator.isValid(fromJSON(json, Map.class))).isFalse();
        });
    }
}