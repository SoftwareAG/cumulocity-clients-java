package c8y;

import com.google.common.base.MoreObjects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * Password publicKey, privateKey & hostKey will be encoded and with {cipher} as prefix
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class RemoteAccessCredentials {

    private String username;
    private String password;
    private String publicKey;
    private String privateKey;
    /**Can be either hostKey or hostCertificate*/
    private String hostKey;

    public RemoteAccessCredentials copy() {
        return builder()
                .username(username)
                .password(password)
                .publicKey(publicKey)
                .privateKey(privateKey)
                .hostKey(hostKey)
                .build();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("user", username)
                .add("password", getHiddenValuesIfPresent(password))
                .add("publicKey", getHiddenValuesIfPresent(publicKey))
                .add("privateKey", getHiddenValuesIfPresent(privateKey))
                .add("hostKey", getHiddenValuesIfPresent(hostKey))
                .toString();
    }

    private String getHiddenValuesIfPresent(String valueToCheck) {
        if (valueToCheck != null) {
            return "<hidden>";
        }
        return null;
    }

    public boolean isUserPasswordCredentials() {
        return !StringUtils.isEmpty(username) && !StringUtils.isEmpty(password);
    }

    public boolean isKeyPairCredentials() {
        return !StringUtils.isEmpty(username) && !StringUtils.isEmpty(privateKey);
    }

}

