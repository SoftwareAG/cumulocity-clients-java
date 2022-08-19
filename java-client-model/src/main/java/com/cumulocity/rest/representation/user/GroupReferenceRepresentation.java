package com.cumulocity.rest.representation.user;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.ReferenceRepresentation;

public class GroupReferenceRepresentation extends AbstractExtensibleRepresentation implements ReferenceRepresentation {

    private GroupRepresentation group;

    public GroupRepresentation getGroup() {
        return group;
    }

    public void setGroup(GroupRepresentation group) {
        this.group = group;
    }

}
