package com.cumulocity.me.rest.representation;


import com.cumulocity.me.lang.Map;
import com.cumulocity.me.model.CumulocityCharset;
import com.cumulocity.me.model.CumulocityVersionParameter;

public class BaseCumulocityMediaType extends BaseMediaType implements CumulocityMediaType {

    protected static final String VND_COM_NSN_CUMULOCITY = "vnd.com.nsn.cumulocity.";

    protected static final String APPLICATION_VND_COM_NSN_CUMULOCITY = "application/" + VND_COM_NSN_CUMULOCITY;

    public static final CumulocityMediaType ERROR_MESSAGE = new BaseCumulocityMediaType("error");
    
    public static final String VND_COM_NSN_CUMULOCITY_CHARSET = "charset=" + CumulocityCharset.CHARSET;
    
    public static final String VND_COM_NSN_CUMULOCITY_VERSION = "ver=" + CumulocityVersionParameter.VERSION;
    
    public static final String VND_COM_NSN_CUMULOCITY_PARAMS = VND_COM_NSN_CUMULOCITY_CHARSET + ";" + VND_COM_NSN_CUMULOCITY_VERSION;

    public static final String ERROR_MESSAGE_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "error+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public BaseCumulocityMediaType() {
        super();
    }

    public BaseCumulocityMediaType(String type, String subtype) {
        super(type, subtype);
    }

    public BaseCumulocityMediaType(String entity) {
        super("application", VND_COM_NSN_CUMULOCITY + entity + "+json;" + VND_COM_NSN_CUMULOCITY_PARAMS);
    }

    public BaseCumulocityMediaType(String type, String subtype, Map parameters) {
        super(type, subtype, parameters);
    }

    public String getTypeString() {
        return getType() + "/" + getSubtype();
    }
}
