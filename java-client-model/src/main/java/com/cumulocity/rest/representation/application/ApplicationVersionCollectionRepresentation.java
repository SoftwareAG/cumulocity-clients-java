package com.cumulocity.rest.representation.application;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import java.util.Iterator;
import java.util.List;

public class ApplicationVersionCollectionRepresentation extends BaseCollectionRepresentation<ApplicationVersionRepresentation> {

    private List<ApplicationVersionRepresentation> applicationVersions;

    public List<ApplicationVersionRepresentation> getApplicationVersions() {
        return applicationVersions;
    }

    @JSONTypeHint(ApplicationVersionRepresentation.class)
    public void setApplicationVersions(List<ApplicationVersionRepresentation> applicationVersions) {
        this.applicationVersions = applicationVersions;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<ApplicationVersionRepresentation> iterator() {
        return applicationVersions.iterator();
    }
}