package com.cumulocity.me.rest.representation.inventory;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.rest.representation.BaseCollectionRepresentation;

public class ManagedObjectReferenceCollectionRepresentation extends BaseCollectionRepresentation {
	
	private List references;
	
	public ManagedObjectReferenceCollectionRepresentation() {
		this.references = new ArrayList();
	}

    public List getReferences() {
        return references;
    }

    public void setReferences(List references) {
        this.references = references;
    }
}
