package com.cumulocity.agent.server.context;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.UnsupportedEncodingException;


import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.devicebootstrap.DeviceCredentialsRepresentation;
import com.google.common.io.BaseEncoding;
import com.sun.jersey.core.util.Base64;

public class DeviceCredentials {

    public static final int DEFAULT_PAGE_SIZE = 25;

    public static final String AUTH_ENCODING = "ASCII";

    public static final String AUTH_PREFIX = "BASIC ";

    public static final String PAYPAL_AUTH_PREFIX = "Paypal ";

    public static final String AUTH_SEPARATOR = ":";

    public static final String LOGIN_SEPARATOR = "/";

    private final String tenant;

    private final String username;

    private final String password;

    private final String appKey;

    private final GId deviceId;

    private final int pageSize;

    private final String authPrefix;

    private String tfaToken;

    public DeviceCredentials(String tenant, String username, String password, String appKey, GId deviceId) {
        this(tenant, username, password, appKey, deviceId, DEFAULT_PAGE_SIZE);
    }

    public DeviceCredentials(String tenant, String username, String password, String appKey, GId deviceId, int pageSize) {
        this(tenant, username, password, appKey, deviceId, DEFAULT_PAGE_SIZE, AUTH_PREFIX);
    }

    public DeviceCredentials(String tenant, String username, String password, String appKey, GId deviceId, int pageSize, String authPrefix) {
        this.tenant = tenant;
        this.username = username;
        this.password = password;
        this.appKey = appKey;
        this.deviceId = deviceId;
        this.pageSize = pageSize;
        this.authPrefix = authPrefix;
    }

    public static DeviceCredentials from(DeviceCredentialsRepresentation credentials) {
        return new DeviceCredentials(credentials.getTenantId(), credentials.getUsername(), credentials.getPassword(), null, null);
    }

    public static DeviceCredentials from(String authorization, String appKey, String tfaToken, int pageSize) {
        DeviceCredentials deviceCredentials = from(authorization, appKey, pageSize);
        deviceCredentials.setTfaToken(tfaToken);
        return deviceCredentials;
    }

    public static DeviceCredentials from(String authorization, String appKey, int pageSize) {
        // expected format [tenant]/username:password
        if (acceptAuthorizationPrefix(authorization, AUTH_PREFIX)) {
            String[] loginAndPass = decode(authorization);
            String tenant = null;
            String username = loginAndPass.length > 0 ? loginAndPass[0] : "";
            String password = loginAndPass.length > 1 ? loginAndPass[1] : "";
            String[] tenantAndUsername = splitUsername(username);
            if (tenantAndUsername.length > 1) {
                tenant = tenantAndUsername[0];
                username = tenantAndUsername[1];
            }
            return new DeviceCredentials(tenant, username, password, appKey, null, pageSize, AUTH_PREFIX);
            // expected format [tenant]:locationId:tabId
        } else if (acceptAuthorizationPrefix(authorization, PAYPAL_AUTH_PREFIX)) {
            String[] parts = decodePaypal(authorization);
            String tenant = parts.length > 0 ? parts[0] : "";
            String locationId = parts.length > 1 ? parts[1] : "";
            String tabId = parts.length > 2 ? parts[2] : "";
            return new DeviceCredentials(tenant, locationId, tabId, appKey, null, pageSize, PAYPAL_AUTH_PREFIX);
        } else {
            return new DeviceCredentials(null, "", "", appKey, null, pageSize);
        }
    }

    private static boolean acceptAuthorizationPrefix(String authorization, String authPrefix) {
        return authorization != null && authorization.toUpperCase().startsWith(authPrefix.toUpperCase());
    }

    public static String[] splitUsername(String username) {
        return username.split(LOGIN_SEPARATOR);
    }

    public static String[] decode(String authorization) {
        return decode(authorization, AUTH_PREFIX, 2);
    }

    public static String[] decodePaypal(String authorization) {
        return decode(authorization, PAYPAL_AUTH_PREFIX, 3);
    }

    private static String[] decode(String authorization, String authPrefix, int limit) {
        try {
            return new String(Base64.decode(authorization.substring(authPrefix.length()).getBytes()), AUTH_ENCODING).split(
                    AUTH_SEPARATOR, limit);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public String getTenant() {
        return tenant;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getTfaToken() {
        return tfaToken;
    }

    public GId getDeviceId() {
        return deviceId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getAuthPrefix() {
        return authPrefix;
    }

    @Override
    public String toString() {
        return tenant + " " + username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceCredentials that = (DeviceCredentials) o;

        if (pageSize != that.pageSize) return false;
        if (tenant != null ? !tenant.equals(that.tenant) : that.tenant != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (appKey != null ? !appKey.equals(that.appKey) : that.appKey != null) return false;
        if (deviceId != null ? !deviceId.equals(that.deviceId) : that.deviceId != null) return false;
        if (authPrefix != null ? !authPrefix.equals(that.authPrefix) : that.authPrefix != null) return false;
        return tfaToken != null ? tfaToken.equals(that.tfaToken) : that.tfaToken == null;
    }

    @Override
    public int hashCode() {
        int result = tenant != null ? tenant.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (appKey != null ? appKey.hashCode() : 0);
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
        result = 31 * result + pageSize;
        result = 31 * result + (authPrefix != null ? authPrefix.hashCode() : 0);
        result = 31 * result + (tfaToken != null ? tfaToken.hashCode() : 0);
        return result;
    }

    public String encode() {
        return BaseEncoding.base64().encode(((tenant(this) + getUsername() + ":" + this.getPassword())).getBytes());
    }

    private String tenant(DeviceCredentials credentials) {
        return isNullOrEmpty(credentials.getTenant()) ? "" : credentials.getTenant() + "/";
    }

    private void setTfaToken(String tfaToken) {
        this.tfaToken = tfaToken;
    }
}
