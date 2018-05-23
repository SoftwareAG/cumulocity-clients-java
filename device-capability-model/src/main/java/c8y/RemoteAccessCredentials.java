package c8y;


import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import sun.org.mozilla.javascript.internal.annotations.JSGetter;

/**
 * Password publicKey, privateKey & hostCertificate will be encoded and with {cipher} as prefix
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class RemoteAccessCredentials {

    private RemoteAccessCredentialsType type;
    private String username;
    private String password;
    private String publicKey;
    private String privateKey;
    private String hostCertificate;

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

