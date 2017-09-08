package com.cumulocity.agent.server.context;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.devicebootstrap.DeviceCredentialsRepresentation;
import com.google.common.io.BaseEncoding;
import com.sun.jersey.core.util.Base64;
import lombok.EqualsAndHashCode;

import java.io.UnsupportedEncodingException;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Be careful with equals and hashcode.{@link DeviceScopeContainerRegistry}
 */
@EqualsAndHashCode(of = {"tenant", "username", "password", "appKey", "deviceId", "pageSize"})
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

    private String tfaToken;

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
    
    public static DeviceCredentials from(String authorization, String appKey, String tfaToken, int pageSize) {
        DeviceCredentials deviceCredentials = from(authorization, appKey, pageSize);
        deviceCredentials.setTfaToken(tfaToken);
        return deviceCredentials;
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

            return new String(Base64.decode(authorization.substring(AUTH_PREFIX.length()).getBytes()), AUTH_ENCODING).split(
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
    
    public String getTfaToken() {
        return tfaToken;
    }
    
    public GId getDeviceId() {
        return deviceId;
    }

    public int getPageSize() {
        return pageSize;
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

    @Override
    public String toString() {
        return tenant + " " + username;
    }
}
