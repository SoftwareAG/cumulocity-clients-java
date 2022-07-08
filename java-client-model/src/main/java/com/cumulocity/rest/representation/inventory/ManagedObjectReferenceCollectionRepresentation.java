package com.cumulocity.rest.representation.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

/*
 * Can be either mapped from  a collection (getReferences), or from an ID. In such Case its the parent Collection of a
 * ManagedObject and references should not be present.
 */
public class ManagedObjectReferenceCollectionRepresentation extends BaseCollectionRepresentation<ManagedObjectReferenceRepresentation> {

    private List<ManagedObjectReferenceRepresentation> references;
    private Integer count;

    public ManagedObjectReferenceCollectionRepresentation() {
        this.references = new ArrayList<>();
    }

    @JSONTypeHint(ManagedObjectReferenceRepresentation.class)
    public List<ManagedObjectReferenceRepresentation> getReferences() {
        return references;
    }

    public void setReferences(List<ManagedObjectReferenceRepresentation> references) {
        this.references = references;
    }

    @JSONProperty(ignoreIfNull = true)
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<ManagedObjectReferenceRepresentation> iterator() {
        return references.iterator();
    }
}
