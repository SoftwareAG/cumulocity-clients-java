package c8y;

public class RemoteAccessCredentialsBuilder {

    private RemoteAccessCredentialsType type;
    private String user;
    private String password;
    private String publicKey;
    private String privateKey;
    private String hostCertificate;

    public RemoteAccessCredentialsBuilder(String credentialType, String protocol) {
        this(RemoteAccessCredentialsType.valueOf(credentialType), RemoteAccessProtocol.valueOf(protocol));
    }

    public RemoteAccessCredentialsBuilder(String credentialType, RemoteAccessProtocol protocol) {
        this(RemoteAccessCredentialsType.valueOf(credentialType), protocol);
    }

    public RemoteAccessCredentialsBuilder(RemoteAccessCredentialsType credentialsType, RemoteAccessProtocol protocol) {
        this.type = credentialsType;
        if (!protocol.isCredentialTypeSupported(type)) {
            throw new RemoteAccessConfigurationException("Credentials type %s is not supported for protocol %s", type, protocol);
        }
    }

    public RemoteAccessCredentialsBuilder user(String user) {
        this.user = user;
        return this;
    }

    public RemoteAccessCredentialsBuilder password(String password) {
        this.password = password;
        return this;
    }

    public RemoteAccessCredentialsBuilder publicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public RemoteAccessCredentialsBuilder privateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public RemoteAccessCredentialsBuilder hostCertificate(String hostCertificate) {
        this.hostCertificate = hostCertificate;
        return this;
    }

    public RemoteAccessCredentials build() {
        RemoteAccessCredentials credentials = new RemoteAccessCredentials(type, user, password, privateKey, publicKey, hostCertificate);
        type.validateCredentials(credentials);
        return credentials;
    }
}
