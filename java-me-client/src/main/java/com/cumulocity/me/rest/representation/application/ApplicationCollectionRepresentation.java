package com.cumulocity.me.rest.representation.application;


import com.cumulocity.me.lang.List;
import com.cumulocity.me.rest.representation.BaseCollectionRepresentation;

public class ApplicationCollectionRepresentation extends BaseCollectionRepresentation {

    private List applications;

    public List getApplications() {
        return applications;
    }

//    @JSONTypeHint(ApplicationRepresentation.class)
    public void setApplications(List applications) {
        this.applications = applications;
    }
}
