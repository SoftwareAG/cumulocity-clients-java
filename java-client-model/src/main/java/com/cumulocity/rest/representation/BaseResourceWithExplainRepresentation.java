package com.cumulocity.rest.representation;

import com.cumulocity.rest.representation.mongo.queryplanner.QueryPlanner;
import org.svenson.JSONProperty;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class BaseResourceWithExplainRepresentation extends AbstractExtensibleRepresentation {

    private Map<String, Object> queryPlanner;

    @JSONProperty(value = "mongoDBQueryPlanner", ignoreIfNull = true)
    public final Map<String, Object> getQueryPlanner() {
        return queryPlanner;
    }

    public final void setQueryPlanner(@NotNull final QueryPlanner queryPlanner) {
        this.queryPlanner = queryPlanner.getQueryPlanner();
    }
}
