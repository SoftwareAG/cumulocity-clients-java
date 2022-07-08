package com.cumulocity.model.authentication;

public interface CumulocityCredentials {

    String getAuthenticationString();
    String getUsername();
    String getTenantId();
    String getApplicationKey();
    void setApplicationKey(String applicationKey);
    String getRequestOrigin();
    void setRequestOrigin(String requestOrigin);
    AuthenticationMethod getAuthenticationMethod();

    CumulocityCredentials copy();

    <T> T accept(CumulocityCredentialsVisitor<T> visitor);

    interface CumulocityCredentialsVisitor<T>{
        T visit(CumulocityBasicCredentials credentials);
        T visit(CumulocityOAuthCredentials credentials);
    }

}
