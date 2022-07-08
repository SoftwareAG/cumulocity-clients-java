package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.CumulocityMediaType;

public class OAuth2ConfigMediaType extends CumulocityMediaType {

    public static final OAuth2ConfigMediaType O_AUTH_2_CONFIG = new OAuth2ConfigMediaType("oAuth2Config");

    public static final String O_AUTH_2_CONFIG_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "oAuth2Config+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public OAuth2ConfigMediaType(String entity) {
        super(entity);
    }
}
