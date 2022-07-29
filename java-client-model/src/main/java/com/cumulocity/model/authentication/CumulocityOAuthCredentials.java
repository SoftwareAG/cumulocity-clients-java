package com.cumulocity.model.authentication;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.codec.binary.Base64;

@EqualsAndHashCode
@ToString(of = {"oAuthAccessToken", "xsrfToken", "applicationKey", "requestOrigin"})
public class CumulocityOAuthCredentials implements CumulocityCredentials {

    public static final String CUMULOCITY_USER_ID_CLAIM = "sub";
    public static final String TENANT_ID_CLAIM = "ten";

    private String oAuthAccessToken;

    @Getter
    private String xsrfToken;
    @Setter
    @Getter
    private String applicationKey;
    @Setter
    @Getter
    private String requestOrigin;
    @Setter
    @Getter
    private AuthenticationMethod authenticationMethod;

    @lombok.Builder
    public CumulocityOAuthCredentials(String oAuthAccessToken, String xsrfToken, String applicationKey, String requestOrigin, AuthenticationMethod authenticationMethod) {
        this.oAuthAccessToken = oAuthAccessToken;
        this.xsrfToken = xsrfToken;
        this.applicationKey = applicationKey;
        this.requestOrigin = requestOrigin;
        this.authenticationMethod = authenticationMethod != null ? authenticationMethod : AuthenticationMethod.COOKIE;
    }

    @Override
    public String getAuthenticationString() {
        switch (authenticationMethod) {
            case COOKIE:
                return oAuthAccessToken;
            case HEADER:
                return "Bearer " + oAuthAccessToken;
        }
        return null;
    }

    @Override
    public String getUsername() {
        String claims = new String(Base64.decodeBase64(oAuthAccessToken.split("\\.")[1]));
        return Jsons.readField(CUMULOCITY_USER_ID_CLAIM, claims).orNull();
    }

    @Override
    public String getTenantId() {
        String[] token = oAuthAccessToken.split("\\.");
        String header = new String(Base64.decodeBase64(token[0]));
        String claims = new String(Base64.decodeBase64(token[1]));
        return Jsons.readField(TENANT_ID_CLAIM, claims).or(Jsons.readField(TENANT_ID_CLAIM, header)).orNull();
    }

    @Override
    public CumulocityCredentials copy() {
        return CumulocityOAuthCredentials.builder()
                .oAuthAccessToken(getAuthenticationString())
                .xsrfToken(getXsrfToken())
                .requestOrigin(getRequestOrigin())
                .applicationKey(getApplicationKey())
                .authenticationMethod(getAuthenticationMethod())
                .build();
    }

    @Override
    public <T> T accept(CumulocityCredentialsVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String getAccessToken() {
        return oAuthAccessToken;
    }
}
