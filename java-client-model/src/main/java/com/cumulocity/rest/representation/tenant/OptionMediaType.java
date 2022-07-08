package com.cumulocity.rest.representation.tenant;

import com.cumulocity.rest.representation.CumulocityMediaType;

public class OptionMediaType extends CumulocityMediaType {
    
    public static final OptionMediaType OPTION = new OptionMediaType("option");

    public static final String OPTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "option+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final OptionMediaType OPTION_COLLECTION = new OptionMediaType("optionCollection");

    public static final String OPTION_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "optionCollection+json;"
            + VND_COM_NSN_CUMULOCITY_PARAMS;

    public OptionMediaType(String entity) {
        super(entity);
    }
}
