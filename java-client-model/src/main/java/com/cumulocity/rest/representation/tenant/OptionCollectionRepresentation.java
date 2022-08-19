package com.cumulocity.rest.representation.tenant;

import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class OptionCollectionRepresentation extends BaseCollectionRepresentation<OptionRepresentation> {

    private List<OptionRepresentation> options;

    public List<OptionRepresentation> getOptions() {
        return options;
    }

    @JSONTypeHint(OptionRepresentation.class)
    public void setOptions(List<OptionRepresentation> tenants) {
        this.options = tenants;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<OptionRepresentation> iterator() {
        return options.iterator();
    }
}
