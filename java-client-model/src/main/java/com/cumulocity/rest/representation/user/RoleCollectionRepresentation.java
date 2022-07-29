package com.cumulocity.rest.representation.user;

import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class RoleCollectionRepresentation extends BaseCollectionRepresentation<RoleRepresentation> {

	private List<RoleRepresentation> roles;

	public List<RoleRepresentation> getRoles() {
		return roles;
	}

	@JSONTypeHint(RoleRepresentation.class)
	public void setRoles(List<RoleRepresentation> roles) {
		this.roles = roles;
	}

    @Override
    @JSONProperty(ignore = true)
    public Iterator<RoleRepresentation> iterator() {
        return roles.iterator();
    }
}
