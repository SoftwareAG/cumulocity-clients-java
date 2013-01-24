package com.cumulocity.me.rest.representation.application;

import com.cumulocity.me.rest.representation.BaseCumulocityMediaType;

/**
 * We follow here convention from {@link MediaType} class, where we have both {@link MediaType}
 * instances, and string representations (with '_TYPE' suffix in name). 
 */
public class ApplicationMediaType extends BaseCumulocityMediaType {
 
    public static final ApplicationMediaType APPLICATION = new ApplicationMediaType("application");
    
    public static final String APPLICATION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "application+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final ApplicationMediaType APPLICATION_COLLECTION = new ApplicationMediaType("applicationCollection");

    public static final String APPLICATION_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "applicationCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final ApplicationMediaType APPLICATION_REFERENCE = new ApplicationMediaType("applicationReference");

    public static final String APPLICATION_REFERENCE_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "applicationReference+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;
    
    public static final ApplicationMediaType APPLICATION_REFERENCE_COLLECTION = new ApplicationMediaType("applicationReferenceCollection");

    public static final String APPLICATION_REFERENCE_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "applicationReferenceCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;
  
    public static final ApplicationMediaType APPLICATION_API = new ApplicationMediaType("applicationApi");
 
    public static final String APPLICATION_API_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "applicationApi+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;
    
    public ApplicationMediaType(String entity) {
        super(entity);
    }
}
