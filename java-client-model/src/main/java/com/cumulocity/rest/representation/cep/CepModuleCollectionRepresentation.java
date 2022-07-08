package com.cumulocity.rest.representation.cep;

import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class CepModuleCollectionRepresentation extends BaseCollectionRepresentation<CepModuleRepresentation> {

    private List<CepModuleRepresentation> modules;

    @JSONTypeHint(CepModuleRepresentation.class)
    @JSONProperty(ignoreIfNull = true)
    public List<CepModuleRepresentation> getModules() {
        return modules;
    }

    public void setModules(List<CepModuleRepresentation> modules) {
        this.modules = modules;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<CepModuleRepresentation> iterator() {
        return modules.iterator();
    }
}
