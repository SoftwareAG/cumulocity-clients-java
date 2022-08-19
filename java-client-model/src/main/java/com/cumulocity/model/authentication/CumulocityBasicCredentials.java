package com.cumulocity.model.authentication;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

@Getter
@EqualsAndHashCode
public class CumulocityBasicCredentials implements CumulocityCredentials {

    private static final String TENANT_USERNAME_SEPARATOR = "/";
    private static final String USERNAME_PASSWORD_SEPARATOR = ":";

    private String username;
    @Setter
    private String tenantId;
    @Setter
    private String password;
    @Setter
    private String applicationKey;
    @Setter
    private String requestOrigin;

    public static CumulocityBasicCredentials from(String loginString) {
        CumulocityBasicCredentialsBuilder builder = CumulocityBasicCredentials.builder();
        String[] tenantUsernamePasswordSplitted = splitTenantUsernamePassword(loginString);
        builder.tenantId(tenantUsernamePasswordSplitted[0]);
        builder.username(tenantUsernamePasswordSplitted[1]);
        builder.password(tenantUsernamePasswordSplitted[2]);
        return builder.build();
    }

    private static String[] splitTenantUsernamePassword(String loginString) {
        //index 0 - tenant, index 1 - username, index 2 - password
        String[] tenantUsernamePassword = new String[3];
        String[] tenantFromUsernameSplit = loginString.split(TENANT_USERNAME_SEPARATOR);
        String usernamePasswordString;

        if (hasTenant(tenantFromUsernameSplit)) {
            tenantUsernamePassword[0] = tenantFromUsernameSplit[0];
            //password can contain slashes so tenantLoginSplit can contain more than 2 elements
            usernamePasswordString = loginString.substring(loginString.indexOf(TENANT_USERNAME_SEPARATOR) + 1);
        } else {
            usernamePasswordString = loginString;
        }

        String[] usernamePasswordSplitted = splitUsernamePassword(usernamePasswordString);
        tenantUsernamePassword[1] = usernamePasswordSplitted[0];
        tenantUsernamePassword[2] = usernamePasswordSplitted[1];
        return tenantUsernamePassword;
    }

    private static boolean hasTenant(String[] tenantLoginSplit) {
        if (hasNotTenantAndNotSlashesInPassword(tenantLoginSplit)) {
            return false;
        }
        String tenantIfProvided = tenantLoginSplit[0];
        //tenant name shouldnt contain ":" inside, so if it is, then it means that no tenant was sent
        boolean tenantWasProvided = !tenantIfProvided.contains(USERNAME_PASSWORD_SEPARATOR);
        return tenantWasProvided;
    }

    private static String[] splitUsernamePassword(String loginPassword) {
        //index 0 - username, index 1 - password
        String[] usernamePasswordSplitted = new String[2];
        String[] loginPasswordSplitted = loginPassword.split(USERNAME_PASSWORD_SEPARATOR);
        usernamePasswordSplitted[0] = loginPasswordSplitted[0];
        //password can contain some colons inside, so loginPassword splitted by colons can contain more than 2 elements
        if (loginPasswordSplitted.length >= 2) {
            usernamePasswordSplitted[1] = loginPassword.substring(loginPassword.indexOf(USERNAME_PASSWORD_SEPARATOR) + 1);
        }
        return usernamePasswordSplitted;
    }

    private static boolean hasNotTenantAndNotSlashesInPassword(String[] tenantLoginSplit) {
        return tenantLoginSplit.length == 1;
    }

    @lombok.Builder
    public CumulocityBasicCredentials(String username, String tenantId, String password, String applicationKey, String requestOrigin) {
        this.username = username;
        this.tenantId = tenantId;
        this.password = password;
        this.applicationKey = applicationKey;
        this.requestOrigin = requestOrigin;
    }

    @Override
    public String getAuthenticationString() {
        return "Basic " + Base64.encodeBase64String((getLoginWithTenant() + USERNAME_PASSWORD_SEPARATOR + password).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public AuthenticationMethod getAuthenticationMethod() {
        return AuthenticationMethod.HEADER;
    }

    @Override
    public CumulocityCredentials copy() {
        return CumulocityBasicCredentials.builder()
                .tenantId(getTenantId())
                .username(getUsername())
                .password(getPassword())
                .requestOrigin(getRequestOrigin())
                .applicationKey(getApplicationKey())
                .build();
    }

    public String getLoginWithTenant() {
        return tenantId == null ? username : tenantId + TENANT_USERNAME_SEPARATOR + username;
    }

    @Override
    public <T> T accept(CumulocityCredentialsVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "CumulocityBasicCredentials{" +
                "username='" + username + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", password='" + hidePassword() + '\'' +
                ", applicationKey='" + applicationKey + '\'' +
                ", requestOrigin='" + requestOrigin + '\'' +
                '}';
    }

    private String hidePassword() {
        return password != null ? "******" : null;
    }
}
