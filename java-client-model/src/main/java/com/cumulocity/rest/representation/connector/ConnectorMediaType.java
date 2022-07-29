package com.cumulocity.rest.representation.connector;

import javax.ws.rs.core.MediaType;

import com.cumulocity.rest.representation.CumulocityMediaType;

/**
 * We follow here convention from {@link MediaType} class, where we have both {@link MediaType}
 * instances, and string representations (with '_TYPE' suffix in name). 
 */
public class ConnectorMediaType extends CumulocityMediaType {

    public static final ConnectorMediaType CONNECTOR = new ConnectorMediaType("connector");

    public static final String CONNECTOR_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "connector+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final ConnectorMediaType CONNECTOR_COLLECTION = new ConnectorMediaType("connectorCollection");

    public static final String CONNECTOR_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "connectorCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final ConnectorMediaType CONNECTOR_API = new ConnectorMediaType("connectorApi");

    public static final String CONNECTOR_API_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "connectorApi+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public ConnectorMediaType(String entity) {
        super("application", VND_COM_NSN_CUMULOCITY + entity + "+json;" + VND_COM_NSN_CUMULOCITY_PARAMS);
    }

    public static void main(String... args) {
        System.out.println(CONNECTOR_TYPE);
    }
}
