package com.cumulocity.rest.representation.mongo.queryplanner;

import com.cumulocity.model.JSONBase;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.regex.Pattern;

public final class QueryPlanner {

    private static final Pattern pattern = Pattern.compile("[(^{\":,\\s\\t}$)]");

    @Getter
    private final Map<String, Object> queryPlanner;

    private QueryPlanner(final Map<String, Object> queryPlanner) {
        this.queryPlanner = queryPlanner;
    }

    public static QueryPlanner setQueryPlanner(@NotNull final Map<String, Object> queryPlanner) {
        if (isQueryPlanner(queryPlanner)) {
            return new QueryPlanner(queryPlanner);
        }
        return new QueryPlanner(null);
    }

    @SuppressWarnings("unchecked")
    public static QueryPlanner setQueryPlanner(@NotNull final String queryPlanner) {
        return setQueryPlanner(JSONBase.fromJSON(queryPlanner, Map.class));
    }

    public static boolean isQueryPlanner(@NotNull final String json) {
        if (isJson(json)) {
            return QueryPlannerValidator.isValid(json);
        }
        return false;
    }

    public static boolean isQueryPlanner(@NotNull final Map<String, Object> jsonMap) {
        return QueryPlannerValidator.isValid(jsonMap);
    }

    private static boolean isJson(final String json) {
        return pattern.matcher(json).find();
    }
}
