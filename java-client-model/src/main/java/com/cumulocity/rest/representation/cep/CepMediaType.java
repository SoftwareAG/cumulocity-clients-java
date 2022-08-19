package com.cumulocity.rest.representation.cep;

import javax.ws.rs.core.MediaType;

import com.cumulocity.rest.representation.CumulocityMediaType;

/**
 * We follow here convention from {@link MediaType} class, where we have both {@link MediaType}
 * instances, and string representations (with '_TYPE' suffix in name). 
 */
public class CepMediaType extends CumulocityMediaType {

    public static final CepMediaType CEP_MODULE = new CepMediaType("cepModule");

    public static final String CEP_MODULE_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "cepModule+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final CepMediaType CEP_MODULE_COLLECTION = new CepMediaType("cepModuleCollection");

    public static final String CEP_MODULE_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "cepModuleCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;
    
    public static final CepMediaType CEP_STATEMENT = new CepMediaType("cepStatement");
    
    public static final String CEP_STATEMENT_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "cepStatement+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;
    
    public static final CepMediaType CEP_STATEMENT_COLLECTION = new CepMediaType("cepStatementCollection");
    
    public static final String CEP_STATEMENT_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "cepStatementCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final CepMediaType CEP_API = new CepMediaType("cepApi");

    public static final String CEP_API_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "cepApi+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public CepMediaType(String entity) {
        super("application", VND_COM_NSN_CUMULOCITY + entity + "+json;" + VND_COM_NSN_CUMULOCITY_PARAMS);
    }
}
