package com.cumulocity.rest.representation;

import com.cumulocity.model.JSONBase;
import com.cumulocity.rest.representation.mongo.queryplanner.QueryPlanner;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.cumulocity.rest.representation.mongo.queryplanner.QueryPlanLoader.getSampleQueryPlanner;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("rawtypes")
class BaseResourceWithExplainRepresentationTest {

    private final Map sampleQueryPlan = JSONBase.fromJSON(getSampleQueryPlanner(), Map.class);
    private QueryPlanner queryPlanner;

    private BaseResourceWithExplainRepresentation representation;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        queryPlanner = QueryPlanner.setQueryPlanner(sampleQueryPlan);

        representation = new BaseResourceWithExplainRepresentation();
    }

    @Test
    void getQueryPlanner() {
        representation.setQueryPlanner(queryPlanner);
        val queryPlan = representation.getQueryPlanner();

        assertThat(queryPlan).isNotNull();
        assertThat(queryPlan).size().isNotZero();
        assertThat(queryPlan).containsKey("queryPlanner");
    }

    @Test
    void setQueryPlanner() {
        assertThat(representation.getQueryPlanner()).isNull();

        representation.setQueryPlanner(queryPlanner);
        assertThat(representation.getQueryPlanner()).isNotNull();
    }

    @Test
    void getQueryPlannerReturnsNull() {
        assertThat(representation.getQueryPlanner()).isNull();
        queryPlanner = QueryPlanner.setQueryPlanner(emptyMap());

        representation.setQueryPlanner(queryPlanner);
        assertThat(representation.getQueryPlanner()).isNull();
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void nullQueryPlannerObjectThrowsException() {
        assertThrows(NullPointerException.class, () -> representation.setQueryPlanner(null));
    }
}