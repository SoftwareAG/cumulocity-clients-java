package c8y;


import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Password publicKey, privateKey & hostCertificate will be encoded and with {cipher} as prefix
 */
public class RemoteAccessCredentials {

    private RemoteAccessCredentialsType type;
    private String username;
    private String password;
    private String publicKey;
    private String privateKey;
    private String hostCertificate;

    public RemoteAccessCredentials(RemoteAccessCredentialsType type, String username, String password, String privateKey, String publicKey, String hostCertificate) {
        this.type = type;
        this.username = username;
        this.password = password;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.hostCertificate = hostCertificate;
    }

    public RemoteAccessCredentialsType getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getHostCertificate() {
        return hostCertificate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setHostCertificate(String hostCertificate) {
        this.hostCertificate = hostCertificate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoteAccessCredentials that = (RemoteAccessCredentials) o;
        return type == that.type &&
                Objects.equal(username, that.username) &&
                Objects.equal(password, that.password) &&
                Objects.equal(publicKey, that.publicKey) &&
                Objects.equal(privateKey, that.privateKey) &&
                Objects.equal(hostCertificate, that.hostCertificate);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, username, password, publicKey, privateKey, hostCertificate);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("user", username)
                .add("password", getHiddenValuesIfPresent(password))
                .add("publicKey", getHiddenValuesIfPresent(publicKey))
                .add("privateKey", getHiddenValuesIfPresent(privateKey))
                .add("hostCertificate", getHiddenValuesIfPresent(hostCertificate))
                .toString();
    }

    private String getHiddenValuesIfPresent(String valueToCheck) {
        if (valueToCheck != null) {
            return "<hidden>";
        }
        return null;
    }

}

