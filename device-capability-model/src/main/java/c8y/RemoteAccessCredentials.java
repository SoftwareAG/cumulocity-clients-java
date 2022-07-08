package c8y;

import com.google.common.base.MoreObjects;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Password publicKey, privateKey, certificate & hostKey will be encoded and with {cipher} as prefix
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RemoteAccessCredentials {

    private RemoteAccessCredentialsType type;
    private String username;
    private String password;
    private String publicKey;
    private String certificate;
    private String privateKey;
    /**Can be either hostKey or hostCertificate*/
    private String hostKey;

    public RemoteAccessCredentials(RemoteAccessCredentialsType type, String username, String password, String publicKey, String privateKey, String hostKey){
        this.type = type;
        this.username = username;
        this.password = password;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.hostKey = hostKey;
    }

    /** Creates and returns deep copy of current object */
    public RemoteAccessCredentials copy() {
        RemoteAccessCredentials copy = new RemoteAccessCredentials();
        copy.setType(this.type);
        copy.setUsername(this.username);
        copy.setPassword(this.password);
        copy.setPublicKey(this.publicKey);
        copy.setCertificate(this.certificate);
        copy.setPrivateKey(this.privateKey);
        copy.setHostKey(this.hostKey);
        return copy;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("user", username)
                .add("password", getHiddenValuesIfPresent(password))
                .add("publicKey", publicKey)
                .add("certificate", certificate)
                .add("privateKey", getHiddenValuesIfPresent(privateKey))
                .add("hostKey", hostKey)
                .toString();
    }

    private String getHiddenValuesIfPresent(String valueToCheck) {
        if (valueToCheck != null) {
            return "<hidden>";
        }
        return null;
    }

}

