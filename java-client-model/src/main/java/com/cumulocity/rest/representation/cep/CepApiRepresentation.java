package com.cumulocity.rest.representation.cep;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;

public class CepApiRepresentation extends AbstractExtensibleRepresentation {

    private String modules;

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }
}
