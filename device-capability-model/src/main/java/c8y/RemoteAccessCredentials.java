package c8y;

import com.google.common.base.MoreObjects;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Password publicKey, privateKey & hostKey will be encoded and with {cipher} as prefix
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
    private String privateKey;
    /**Can be either hostKey or hostCertificate*/
    private String hostKey;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("user", username)
                .add("password", getHiddenValuesIfPresent(password))
                .add("publicKey", publicKey)
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

