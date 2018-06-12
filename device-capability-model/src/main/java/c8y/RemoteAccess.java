package c8y;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.svenson.AbstractDynamicProperties;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RemoteAccess extends AbstractDynamicProperties {

    @Getter
    private static final long serialVersionUID = 6652959747455810127L;

    private String id;

    private String name;

    private String hostname;

    private Integer port;

    private RemoteAccessProtocol protocol;

    private RemoteAccessCredentials credentials;

    public RemoteAccess(Map<String, Object> map) {
        this.setId(getValueAsString(map, "id"));
        this.name = getValueAsString(map, "name");
        this.hostname = getValueAsString(map, "hostname");
        this.port = ((Long) map.get("port")).intValue();
        this.protocol = RemoteAccessProtocol.valueOf(getValueAsString(map, "protocol"));
        this.credentials = credentialsFromMap(getValueAsMap(map, "credentials"), this.protocol);
    }

    private RemoteAccessCredentials credentialsFromMap(Map<String, Object> map, RemoteAccessProtocol protocol) {
        return new RemoteAccessCredentialsBuilder(RemoteAccessCredentialsType.valueOf(getValueAsString(map, "type")), protocol).
                user(getValueAsString(map, "username")).
                password(getValueAsString(map, "password")).
                privateKey(getValueAsString(map,"privateKey")).
                publicKey(getValueAsString(map,"publicKey")).
                hostCertificate(getValueAsString(map,"hostCertificate")).
                build();
    }

    private String getValueAsString(Map<String, Object> map, String key) {
        return getValueAs(map, key, String.class);
    }

    private Map<String, Object> getValueAsMap(Map<String, Object> map, String key) {
        return (Map<String, Object>) map.get(key);
    }

    private <T> T getValueAs(Map<String, Object> map, String key, Class<T> type) {
        return (T) map.get(key);
    }

    public void setProtocol(String protocol) {
        this.protocol = RemoteAccessProtocol.valueOf(protocol);
    }

    public void setProtocol(RemoteAccessProtocol protocol) {
        this.protocol = protocol;
    }

}
