package com.cumulocity.microservice.context.credentials;

public class MicroserviceCredentials implements Credentials {
    private String tenant;
    private String username;
    private String password;
    private String tfaToken;
    private String appKey;

    @java.beans.ConstructorProperties({"tenant", "username", "password", "tfaToken", "appKey"})
    public MicroserviceCredentials(String tenant, String username, String password, String tfaToken, String appKey) {
        this.tenant = tenant;
        this.username = username;
        this.password = password;
        this.tfaToken = tfaToken;
        this.appKey = appKey;
    }

    public MicroserviceCredentials() {
    }

    public static MicroserviceCredentialsBuilder builder() {
        return new MicroserviceCredentialsBuilder();
    }

    public static MicroserviceCredentialsBuilder copyOf(MicroserviceCredentials source) {
        return builder()
                .tenant(source.getTenant())
                .username(source.getUsername())
                .password(source.getPassword())
                .tfaToken(source.getTfaToken())
                .appKey(source.getAppKey());
    }

    public UserCredentials withIdentifier(String identifier) {
        return UserCredentials.builder()
                .tenant(tenant)
                .username(username)
                .password(password)
                .identifier(identifier)
                .tfaToken(tfaToken)
                .appKey(appKey)
                .build();
    }

    public String getTenant() {
        return this.tenant;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getTfaToken() {
        return this.tfaToken;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTfaToken(String tfaToken) {
        this.tfaToken = tfaToken;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MicroserviceCredentials)) return false;
        final MicroserviceCredentials other = (MicroserviceCredentials) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$tenant = this.getTenant();
        final Object other$tenant = other.getTenant();
        if (this$tenant == null ? other$tenant != null : !this$tenant.equals(other$tenant)) return false;
        final Object this$username = this.getUsername();
        final Object other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        final Object this$tfaToken = this.getTfaToken();
        final Object other$tfaToken = other.getTfaToken();
        if (this$tfaToken == null ? other$tfaToken != null : !this$tfaToken.equals(other$tfaToken)) return false;
        final Object this$appKey = this.getAppKey();
        final Object other$appKey = other.getAppKey();
        if (this$appKey == null ? other$appKey != null : !this$appKey.equals(other$appKey)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $tenant = this.getTenant();
        result = result * PRIME + ($tenant == null ? 43 : $tenant.hashCode());
        final Object $username = this.getUsername();
        result = result * PRIME + ($username == null ? 43 : $username.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        final Object $tfaToken = this.getTfaToken();
        result = result * PRIME + ($tfaToken == null ? 43 : $tfaToken.hashCode());
        final Object $appKey = this.getAppKey();
        result = result * PRIME + ($appKey == null ? 43 : $appKey.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof MicroserviceCredentials;
    }

    public MicroserviceCredentials withTenant(String tenant) {
        return this.tenant == tenant ? this : new MicroserviceCredentials(tenant, this.username, this.password, this.tfaToken, this.appKey);
    }

    public MicroserviceCredentials withUsername(String username) {
        return this.username == username ? this : new MicroserviceCredentials(this.tenant, username, this.password, this.tfaToken, this.appKey);
    }

    public MicroserviceCredentials withPassword(String password) {
        return this.password == password ? this : new MicroserviceCredentials(this.tenant, this.username, password, this.tfaToken, this.appKey);
    }

    public MicroserviceCredentials withTfaToken(String tfaToken) {
        return this.tfaToken == tfaToken ? this : new MicroserviceCredentials(this.tenant, this.username, this.password, tfaToken, this.appKey);
    }

    public MicroserviceCredentials withAppKey(String appKey) {
        return this.appKey == appKey ? this : new MicroserviceCredentials(this.tenant, this.username, this.password, this.tfaToken, appKey);
    }

    public String toString() {
        return "MicroserviceCredentials(tenant=" + this.getTenant() + ", username=" + this.getUsername() + ")";
    }

    public static class MicroserviceCredentialsBuilder {
        private String tenant;
        private String username;
        private String password;
        private String tfaToken;
        private String appKey;

        MicroserviceCredentialsBuilder() {
        }

        public MicroserviceCredentials.MicroserviceCredentialsBuilder tenant(String tenant) {
            this.tenant = tenant;
            return this;
        }

        public MicroserviceCredentials.MicroserviceCredentialsBuilder username(String username) {
            this.username = username;
            return this;
        }

        public MicroserviceCredentials.MicroserviceCredentialsBuilder password(String password) {
            this.password = password;
            return this;
        }

        public MicroserviceCredentials.MicroserviceCredentialsBuilder tfaToken(String tfaToken) {
            this.tfaToken = tfaToken;
            return this;
        }

        public MicroserviceCredentials.MicroserviceCredentialsBuilder appKey(String appKey) {
            this.appKey = appKey;
            return this;
        }

        public MicroserviceCredentials build() {
            return new MicroserviceCredentials(tenant, username, password, tfaToken, appKey);
        }

        public String toString() {
            return "MicroserviceCredentials.MicroserviceCredentialsBuilder(tenant=" + this.tenant + ", username=" + this.username + ", password=" + this.password + ", tfaToken=" + this.tfaToken + ", appKey=" + this.appKey + ")";
        }
    }
}
