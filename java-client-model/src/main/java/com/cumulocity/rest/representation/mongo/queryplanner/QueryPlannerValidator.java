package com.cumulocity.rest.representation.mongo.queryplanner;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@AllArgsConstructor
@Getter
enum QueryPlannerValidator {

    QUERY_PLANNER("queryPlanner"),
    EXECUTION_STATS("executionStats"),
    WINNING_PLAN("winningPlan"),
    REJECTED_PLANS("rejectedPlans");

    @Getter
    private static final Collection<String> fields = cacheFields();

    private final String field;

    private static Collection<String> cacheFields() {
        return stream(values())
                .map(QueryPlannerValidator::getField)
                .collect(Collectors.toList());
    }

    public static boolean isValid(final String json) {
        return fields.stream().allMatch(json::contains);
    }

    public static boolean isValid(final Map<String, Object> jsonMap) {
        return isValid(jsonMap.toString());
    }
}
