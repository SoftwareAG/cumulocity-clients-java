package com.cumulocity.rest.representation.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;
import com.cumulocity.rest.representation.ReferenceRepresentation;

public class UserReferenceCollectionRepresentation extends BaseCollectionRepresentation<UserReferenceRepresentation> implements ReferenceRepresentation {

    private List<UserReferenceRepresentation> references;

    public UserReferenceCollectionRepresentation() {
        this.references = new ArrayList<UserReferenceRepresentation>();
    }

    @JSONTypeHint(UserReferenceRepresentation.class)
    public List<UserReferenceRepresentation> getReferences() {
        return references;
    }

    public void setReferences(List<UserReferenceRepresentation> references) {
        this.references = references;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<UserReferenceRepresentation> iterator() {
        return references.iterator();
    }
}
