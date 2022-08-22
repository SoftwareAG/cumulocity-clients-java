package com.cumulocity.rest.representation.user;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.ReferenceRepresentation;

public class RoleReferenceRepresentation extends AbstractExtensibleRepresentation implements ReferenceRepresentation {

    private RoleRepresentation role;

    public void setRole(RoleRepresentation role) {
        this.role = role;
    }

    public RoleRepresentation getRole() {
        return role;
    }
}
