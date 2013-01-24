package com.cumulocity.me.rest.representation.identity;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.rest.representation.BaseCollectionRepresentation;

public class ExternalIDCollectionRepresentation extends BaseCollectionRepresentation {

    private List externalIds = new ArrayList();

    public List getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(List externalIds) {
        this.externalIds = externalIds;
    }

}
