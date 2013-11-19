package com.cumulocity.sdk.client;

public class CumulocityCredentials {

    private final String user;

    private final String password;

    private String tenantId;

    private String applicationKey;

    private CumulocityCredentials(String tenantId, String user, String password, String applicationKey) {
        this.tenantId = tenantId;
        this.user = user;
        this.password = password;
        this.applicationKey = applicationKey;
    }

    public static CumulocityCredentials create(String user, String password) {
        return new CumulocityCredentials(null, user, password, null);
    }

    public static CumulocityCredentials create(String tenantId, String user, String password, String applicationKey) {
        return new CumulocityCredentials(tenantId, user, password, applicationKey);
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

    public static final class Builder {

        private String user;

        private String password;

        private String applicationKey;

        private String tenantId;

        public Builder(String user, String password) {
            this.user = user;
            this.password = password;
        }

        public static Builder cumulocityCredentials(String user, String password) {
            return new Builder(user, password);
        }

        public Builder withApplicationKey(String applicationKey) {
            this.applicationKey = applicationKey;
            return this;
        }

        public Builder withTenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public CumulocityCredentials build() {
            return new CumulocityCredentials(tenantId, user, password, applicationKey);
        }
    }
}
