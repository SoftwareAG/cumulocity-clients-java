package com.cumulocity.me.rest.representation;

import com.cumulocity.me.model.idtype.GId;

public interface BaseCumulocityResourceRepresentationWithId extends CumulocityResourceRepresentation {
    
	void setId(GId gId);
}
