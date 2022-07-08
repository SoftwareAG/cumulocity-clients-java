package com.cumulocity.rest.representation;

import com.cumulocity.model.idtype.GId;

public interface ResourceRepresentationWithId extends ResourceRepresentation {

    void setId(GId gId);

}
