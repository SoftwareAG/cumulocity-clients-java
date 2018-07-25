package c8y;

import com.google.common.collect.Lists;

import java.util.List;

import static c8y.RemoteAccessCredentialsType.*;

public enum RemoteAccessProtocol {

    // Null has been introduced to fix backward compatibility for VNC
    VNC(null, RemoteAccessCredentialsType.NONE, PASS_ONLY), TELNET(NONE), SSH(USER_PASS, KEY_PAIR);

    private final List<RemoteAccessCredentialsType> supportedCredentialTypes;

    RemoteAccessProtocol(RemoteAccessCredentialsType credentialsType, RemoteAccessCredentialsType... supportedCredentialTypes) {
        this.supportedCredentialTypes = Lists.asList(credentialsType, supportedCredentialTypes);
    }

    public boolean isCredentialTypeSupported(RemoteAccessCredentialsType credentialsTypeToCheck) {
        return supportedCredentialTypes.contains(credentialsTypeToCheck);
    }
}
