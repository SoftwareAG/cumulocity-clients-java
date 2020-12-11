package c8y;

public class RemoteAccessCredentialsBuilder {

    private RemoteAccessCredentialsType type;
    private String user;
    private String password;
    private String publicKey;
    private String certificate;
    private String privateKey;
    private String hostKey;

    public RemoteAccessCredentialsBuilder(RemoteAccessCredentialsType credentialsType, RemoteAccessProtocol protocol) {
        this(credentialsType);
        if (!protocol.isCredentialTypeSupported(type)) {
            throw new RemoteAccessConfigurationException("Credentials type %s is not supported for protocol %s", type, protocol);
        }
    }

    public RemoteAccessCredentialsBuilder(RemoteAccessCredentials credentials) {
        this(credentials.getType());
        this.user(credentials.getUsername())
                .password(credentials.getPassword())
                .privateKey(credentials.getPrivateKey())
                .certificate(credentials.getCertificate())
                .publicKey(credentials.getPublicKey())
                .hostKey(credentials.getHostKey());
    }

    public RemoteAccessCredentialsBuilder(RemoteAccessCredentialsType credentialsType) {
        this.type = credentialsType;
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

    public RemoteAccessCredentialsBuilder certificate(String certificate) {
        this.certificate = certificate;
        return this;
    }

    public RemoteAccessCredentialsBuilder privateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public RemoteAccessCredentialsBuilder hostKey(String hostKey) {
        this.hostKey = hostKey;
        return this;
    }

    public RemoteAccessCredentials build() {
        RemoteAccessCredentials credentials = new RemoteAccessCredentials(type, user, password, publicKey, certificate, privateKey, hostKey);
        if (type != null) {
            type.validateCredentials(credentials);
        }
        return credentials;
    }
}
