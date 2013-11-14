package com.cumulocity.sdk.client;

public class CumulocityCredentials {

    private String user;
    private String password;
    private String applicationKey;
    private String tenantId;
    
    private CumulocityCredentials() {}
    
    public static CumulocityCredentials create(String user, String password) {
        CumulocityCredentials credentials = new CumulocityCredentials();
        credentials.user = user;
        credentials.password = password;
        return credentials;
    }
    
    public static CumulocityCredentials create(String tenantId, String user, String password, String applicationKey) {
        CumulocityCredentials credentials = new CumulocityCredentials();
        credentials.tenantId = tenantId;
        credentials.user = user;
        credentials.password = password;
        credentials.applicationKey = applicationKey;
        return credentials;
    }
    
    public CumulocityCredentials withTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
    
    public CumulocityCredentials withApplicationKey(String applicationKey) {
        this.applicationKey = applicationKey;
        return this;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getApplicationKey() {
        return applicationKey;
    }

    public String getTenantId() {
        return tenantId;
    }
}
