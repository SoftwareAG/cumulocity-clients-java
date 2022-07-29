package com.cumulocity.rest.representation.user;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.ReferenceRepresentation;

public class UserReferenceRepresentation extends AbstractExtensibleRepresentation implements ReferenceRepresentation {

	private UserRepresentation user;

	public UserRepresentation getUser() {
		return user;
	}

	public void setUser(UserRepresentation user) {
		this.user = user;
	}

}
