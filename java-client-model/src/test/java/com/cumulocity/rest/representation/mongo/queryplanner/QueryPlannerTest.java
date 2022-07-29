package com.cumulocity.rest.representation.mongo.queryplanner;

import lombok.val;
import org.junit.jupiter.api.Test;

import static com.cumulocity.rest.representation.mongo.queryplanner.QueryPlanLoader.getSampleQueryPlanner;
import static com.cumulocity.rest.representation.mongo.queryplanner.QueryPlanner.*;
import static com.cumulocity.rest.representation.mongo.queryplanner.QueryPlanner.setQueryPlanner;
import static org.assertj.core.api.Assertions.assertThat;

class QueryPlannerTest {

    private final String sampleQueryPlan = getSampleQueryPlanner();
    private final String validJsonNotQueryPlan = "{\"name\":\"ABC\", \"age\": 20}";

    @Test
    void isValidQueryPlanner() {
        assertThat(isQueryPlanner(sampleQueryPlan)).isTrue();
    }

    @Test
    void isNotValidQueryPlanner() {
        assertThat(isQueryPlanner("")).isFalse();
        assertThat(isQueryPlanner("ABCabc123")).isFalse();
        assertThat(isQueryPlanner(validJsonNotQueryPlan)).isFalse();
    }

    @Test
    void setQueryPlannerNotNull() {
        val queryPlanner = setQueryPlanner(sampleQueryPlan);

        assertThat(queryPlanner).isNotNull();
        assertThat(queryPlanner.getQueryPlanner()).isNotNull();
        assertThat(queryPlanner.getQueryPlanner()).containsKey("queryPlanner");
    }

    @Test
    void setQueryPlannerGetNull() {
        assertThat(setQueryPlanner(validJsonNotQueryPlan).getQueryPlanner()).isNull();
    }
}
