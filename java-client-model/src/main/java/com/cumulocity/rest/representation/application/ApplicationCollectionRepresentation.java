package com.cumulocity.rest.representation.application;

import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class ApplicationCollectionRepresentation extends BaseCollectionRepresentation<ApplicationRepresentation> {

    private List<ApplicationRepresentation> applications;

    public List<ApplicationRepresentation> getApplications() {
        return applications;
    }

    @JSONTypeHint(ApplicationRepresentation.class)
    public void setApplications(List<ApplicationRepresentation> applications) {
        this.applications = applications;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<ApplicationRepresentation> iterator() {
        return applications.iterator();
    }
}
