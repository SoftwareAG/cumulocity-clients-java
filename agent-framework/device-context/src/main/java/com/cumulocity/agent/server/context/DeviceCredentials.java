package com.cumulocity.agent.server.context;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.devicebootstrap.DeviceCredentialsRepresentation;
import com.google.common.io.BaseEncoding;

public class DeviceCredentials {

    public static final int DEFAULT_PAGE_SIZE = 25;

    public static final String AUTH_ENCODING = "ASCII";

    public static final String AUTH_PREFIX = "BASIC ";

    public static final String AUTH_SEPARATOR = ":";

    public static final String LOGIN_SEPARATOR = "/";

    private final String tenant;

    private final String username;

    private final String password;

    private final String appKey;

    private final GId deviceId;

    private final int pageSize;

    private static final Base64 encoder = new Base64();

    public DeviceCredentials(String tenant, String username, String password, String appKey, GId deviceId) {
        this(tenant, username, password, appKey, deviceId, DEFAULT_PAGE_SIZE);
    }

    public DeviceCredentials(String tenant, String username, String password, String appKey, GId deviceId, int pageSize) {
        this.tenant = tenant;
        this.username = username;
        this.password = password;
        this.appKey = appKey;
        this.deviceId = deviceId;
        this.pageSize = pageSize;
    }

    public static DeviceCredentials from(DeviceCredentialsRepresentation credentials) {
        return new DeviceCredentials(credentials.getTenantId(), credentials.getUsername(), credentials.getPassword(), null, null);
    }

    public static DeviceCredentials from(String authorization, String appKey, int pageSize) {
        if (authorization == null || !authorization.toUpperCase().startsWith(AUTH_PREFIX)) {
            return new DeviceCredentials(null, "", "", appKey, null, pageSize);
        }

        String[] loginAndPass = decode(authorization);
        String tenant = null;
        String username = loginAndPass.length > 0 ? loginAndPass[0] : "";
        String password = loginAndPass.length > 1 ? loginAndPass[1] : "";

        String[] tenantAndUsername = splitUsername(username);
        if (tenantAndUsername.length > 1) {
            tenant = tenantAndUsername[0];
            username = tenantAndUsername[1];
        }

        return new DeviceCredentials(tenant, username, password, appKey, null, pageSize);
    }

    public static String[] splitUsername(String username) {
        return username.split(LOGIN_SEPARATOR);
    }

    public static String[] decode(String authorization) {
        try {

            return new String(encoder.decode(authorization.substring(AUTH_PREFIX.length()).getBytes()), AUTH_ENCODING).split(
                    AUTH_SEPARATOR, 2);
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

    public GId getDeviceId() {
        return deviceId;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public String toString() {
        return tenant + " " + username + " " + password;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((appKey == null) ? 0 : appKey.hashCode());
        result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
        result = prime * result + pageSize;
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((tenant == null) ? 0 : tenant.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DeviceCredentials other = (DeviceCredentials) obj;
        if (appKey == null) {
            if (other.appKey != null)
                return false;
        } else if (!appKey.equals(other.appKey))
            return false;
        if (deviceId == null) {
            if (other.deviceId != null)
                return false;
        } else if (!deviceId.equals(other.deviceId))
            return false;
        if (pageSize != other.pageSize)
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (tenant == null) {
            if (other.tenant != null)
                return false;
        } else if (!tenant.equals(other.tenant))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

    public String encode() {
        return BaseEncoding.base64().encode(((tenant(this) + getUsername() + ":" + this.getPassword())).getBytes());
    }

    private String tenant(DeviceCredentials credentials) {
        return isNullOrEmpty(credentials.getTenant()) ? "" : credentials.getTenant() + "/";
    }

}
