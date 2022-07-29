package com.cumulocity.rest.representation.identity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class ExternalIDCollectionRepresentation extends BaseCollectionRepresentation<ExternalIDRepresentation> {

    private List<ExternalIDRepresentation> externalIds = new ArrayList<ExternalIDRepresentation>();

    @JSONTypeHint(ExternalIDRepresentation.class)
    public List<ExternalIDRepresentation> getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(List<ExternalIDRepresentation> externalIds) {
        this.externalIds = externalIds;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<ExternalIDRepresentation> iterator() {
        return externalIds.iterator();
    }
}
