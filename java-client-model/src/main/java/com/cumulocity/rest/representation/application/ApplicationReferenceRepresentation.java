package com.cumulocity.rest.representation.application;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;

public class ApplicationReferenceRepresentation extends AbstractExtensibleRepresentation {

    private ApplicationRepresentation application;

    /**
     * @return the application
     */
    public ApplicationRepresentation getApplication() {
        return application;
    }

    /**
     * @param application the application to set
     */
    public void setApplication(ApplicationRepresentation application) {
        this.application = application;
    }
}
