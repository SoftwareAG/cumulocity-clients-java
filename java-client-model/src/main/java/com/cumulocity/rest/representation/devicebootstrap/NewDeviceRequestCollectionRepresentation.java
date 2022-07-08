package com.cumulocity.rest.representation.devicebootstrap;

import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class NewDeviceRequestCollectionRepresentation extends BaseCollectionRepresentation<NewDeviceRequestRepresentation> {
    
    private List<NewDeviceRequestRepresentation> newDeviceRequests;
    
    @JSONTypeHint(NewDeviceRequestRepresentation.class)
    public List<NewDeviceRequestRepresentation> getNewDeviceRequests() {
        return newDeviceRequests;
    }

    public void setNewDeviceRequests(List<NewDeviceRequestRepresentation> newDeviceRequests) {
        this.newDeviceRequests = newDeviceRequests;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<NewDeviceRequestRepresentation> iterator() {
        return newDeviceRequests.iterator();
    }
}
