package com.cumulocity.rest.representation.connector;

import java.util.Iterator;
import java.util.List;

import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import com.cumulocity.rest.representation.BaseCollectionRepresentation;

public class ConnectorCollectionRepresentation extends BaseCollectionRepresentation<ConnectorRepresentation> {

    private List<ConnectorRepresentation> connectors;

    public void setConnectors(List<ConnectorRepresentation> connectors) {
        this.connectors = connectors;
    }

    @JSONTypeHint(ConnectorRepresentation.class)
    public List<ConnectorRepresentation> getConnectors() {
        return connectors;
    }

    @Override
    @JSONProperty(ignore = true)
    public Iterator<ConnectorRepresentation> iterator() {
        return connectors.iterator();
    }
}
