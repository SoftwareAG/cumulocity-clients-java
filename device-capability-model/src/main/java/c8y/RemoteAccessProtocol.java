package c8y;

import java.util.Arrays;
import java.util.List;

import static c8y.RemoteAccessCredentialsType.*;

public enum RemoteAccessProtocol {

    VNC(RemoteAccessCredentialsType.PASS_ONLY, USER_PASS), TELNET(NONE), SSH(USER_PASS, KEY_PAIR, KEY_PAIR_HOST);

    public static final String PROTOCOL_VNC = "VNC";
    public static final String PROTOCOL_SSH = "SSH";
    public static final String PROTOCOL_TELNET = "TELNET";

    public static final String PROTOCOL_PATTERN = "(" + PROTOCOL_VNC + "$|" + PROTOCOL_TELNET + "$|" + PROTOCOL_SSH + "$)";

    private final List<RemoteAccessCredentialsType> supportedCredentialTypes;

    RemoteAccessProtocol(RemoteAccessCredentialsType... supportedCredentialTypes) {
        this.supportedCredentialTypes = Arrays.asList(supportedCredentialTypes);
    }

    public boolean isCredentialTypeSupported(RemoteAccessCredentialsType credentialsTypeToCheck) {
        return supportedCredentialTypes.contains(credentialsTypeToCheck);
    }
}
