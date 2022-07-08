package com.cumulocity.rest.representation.user;

import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class UserCollectionRepresentation extends BaseCollectionRepresentation<UserRepresentation> {
    
    private List<UserRepresentation> users;

    public List<UserRepresentation> getUsers() {
        return users;
    }

    @JSONTypeHint(UserRepresentation.class)
    public void setUsers(List<UserRepresentation> users) {
        this.users = users;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<UserRepresentation> iterator() {
        return users.iterator();
    }
}
