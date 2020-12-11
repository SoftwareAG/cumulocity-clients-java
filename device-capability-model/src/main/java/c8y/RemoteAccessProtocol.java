package c8y;

import com.google.common.collect.Lists;
import java.util.List;

public enum RemoteAccessProtocol {

    // Null has been introduced to fix backward compatibility for VNC
    VNC(null, RemoteAccessCredentialsType.NONE, RemoteAccessCredentialsType.PASS_ONLY),

    TELNET(RemoteAccessCredentialsType.NONE),

    SSH(RemoteAccessCredentialsType.USER_PASS, RemoteAccessCredentialsType.KEY_PAIR, RemoteAccessCredentialsType.CERTIFICATE),

    PASSTHROUGH(RemoteAccessCredentialsType.NONE);

    private final List<RemoteAccessCredentialsType> supportedCredentialTypes;

    RemoteAccessProtocol(RemoteAccessCredentialsType credentialsType, RemoteAccessCredentialsType... supportedCredentialTypes) {
        this.supportedCredentialTypes = Lists.asList(credentialsType, supportedCredentialTypes);
    }

    public boolean isCredentialTypeSupported(RemoteAccessCredentialsType credentialsTypeToCheck) {
        return supportedCredentialTypes.contains(credentialsTypeToCheck);
    }
}
