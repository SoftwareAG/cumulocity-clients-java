package com.cumulocity.rest.representation.user;

import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class GroupCollectionRepresentation extends BaseCollectionRepresentation<GroupRepresentation> {
    
    private List<GroupRepresentation> groups;

    public List<GroupRepresentation> getGroups() {
        return groups;
    }

    @JSONTypeHint(GroupRepresentation.class)
    public void setGroups(List<GroupRepresentation> groups) {
        this.groups = groups;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<GroupRepresentation> iterator() {
        return groups.iterator();
    }
}
