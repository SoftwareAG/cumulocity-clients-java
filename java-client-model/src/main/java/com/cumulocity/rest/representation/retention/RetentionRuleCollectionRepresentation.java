package com.cumulocity.rest.representation.retention;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import java.util.Iterator;
import java.util.List;

public class RetentionRuleCollectionRepresentation extends BaseCollectionRepresentation<RetentionRuleRepresentation> {

    private List<RetentionRuleRepresentation> retentionRules;

    @JSONTypeHint(RetentionRuleRepresentation.class)
    public List<RetentionRuleRepresentation> getRetentionRules() {
        return retentionRules;
    }

    public void setRetentionRules(List<RetentionRuleRepresentation> retentionRules) {
        this.retentionRules = retentionRules;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<RetentionRuleRepresentation> iterator() {
        return retentionRules.iterator();
    }

}
