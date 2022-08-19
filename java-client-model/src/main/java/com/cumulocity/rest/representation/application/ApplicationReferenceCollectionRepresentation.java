package com.cumulocity.rest.representation.application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class ApplicationReferenceCollectionRepresentation extends BaseCollectionRepresentation<ApplicationReferenceRepresentation> {

    private List<ApplicationReferenceRepresentation> references;

    public ApplicationReferenceCollectionRepresentation() {
        this.references = new ArrayList<ApplicationReferenceRepresentation>();
    }

    @JSONTypeHint(ApplicationReferenceRepresentation.class)
    public List<ApplicationReferenceRepresentation> getReferences() {
        return references;
    }

    public void setReferences(List<ApplicationReferenceRepresentation> references) {
        this.references = references;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<ApplicationReferenceRepresentation> iterator() {
        return references.iterator();
    }
}
