package com.cumulocity.rest.representation;

import org.svenson.JSONProperty;

import java.util.Map;

public class BaseResourceWithExplainRepresentation extends AbstractExtensibleRepresentation {

    private Map<String, Object> queryPlanner;

    @JSONProperty(value = "mongoDBQueryPlanner", ignoreIfNull = true)
    public final Map<String, Object> getQueryPlanner() {
        return queryPlanner;
    }

    public final void setQueryPlanner(final Map<String, Object> queryPlanner) {
        this.queryPlanner = queryPlanner;
    }
}
