/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
