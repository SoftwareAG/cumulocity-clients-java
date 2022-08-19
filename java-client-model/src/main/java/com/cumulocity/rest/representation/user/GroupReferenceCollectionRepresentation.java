package com.cumulocity.rest.representation.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;
import com.cumulocity.rest.representation.ReferenceRepresentation;

public class GroupReferenceCollectionRepresentation extends BaseCollectionRepresentation<GroupReferenceRepresentation> implements ReferenceRepresentation {

    private List<GroupReferenceRepresentation> references;

    public GroupReferenceCollectionRepresentation() {
        this.references = new ArrayList<GroupReferenceRepresentation>();
    }

    @JSONTypeHint(GroupReferenceRepresentation.class)
    public List<GroupReferenceRepresentation> getReferences() {
        return references;
    }

    public void setReferences(List<GroupReferenceRepresentation> references) {
        this.references = references;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<GroupReferenceRepresentation> iterator() {
        return references.iterator();
    }
}
