package c8y;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.svenson.AbstractDynamicProperties;

import java.util.Map;

public class RemoteAccess extends AbstractDynamicProperties {

    private static final long serialVersionUID = 6652959747455810127L;

    private String id;

    private String name;

    private String hostname;

    private Integer port;

    private RemoteAccessProtocol protocol;

    private RemoteAccessCredentials credentials;

    public RemoteAccess() {
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public RemoteAccessProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(RemoteAccessProtocol protocol) {
        this.protocol = protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = RemoteAccessProtocol.valueOf(protocol);
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RemoteAccessCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(RemoteAccessCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, hostname, port, protocol, credentials);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoteAccess that = (RemoteAccess) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(name, that.name) &&
                Objects.equal(hostname, that.hostname) &&
                Objects.equal(port, that.port) &&
                protocol == that.protocol &&
                Objects.equal(credentials, that.credentials);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("hostname", hostname)
                .add("port", port)
                .add("protocol", protocol)
                .add("credentials", credentials)
                .toString();
    }

}
