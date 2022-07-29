package com.cumulocity.rest.representation.user;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.ReferenceRepresentation;
import com.cumulocity.rest.representation.annotation.Command;
import com.cumulocity.rest.representation.annotation.NotNull;

public class RoleReferenceRepresentation extends AbstractExtensibleRepresentation implements ReferenceRepresentation {

    @NotNull(operation = {Command.CREATE, Command.UPDATE})
    private RoleRepresentation role;

    public void setRole(RoleRepresentation role) {
        this.role = role;
    }

    public RoleRepresentation getRole() {
        return role;
    }
}
