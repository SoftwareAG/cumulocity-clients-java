package com.cumulocity.rest.representation.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;
import com.cumulocity.rest.representation.ReferenceRepresentation;

public class RoleReferenceCollectionRepresentation extends BaseCollectionRepresentation<RoleReferenceRepresentation> implements ReferenceRepresentation {

    private List<RoleReferenceRepresentation> references;

    public RoleReferenceCollectionRepresentation() {
        this.references = new ArrayList<RoleReferenceRepresentation>();
    }

    @JSONTypeHint(RoleReferenceRepresentation.class)
    public List<RoleReferenceRepresentation> getReferences() {
        return references;
    }

    public void setReferences(List<RoleReferenceRepresentation> references) {
        this.references = references;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<RoleReferenceRepresentation> iterator() {
        return references.iterator();
    }
}
