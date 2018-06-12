package c8y;

import org.svenson.AbstractDynamicProperties;

import java.io.Serializable;

public class RemoteAccessConnect extends AbstractDynamicProperties implements Serializable {

    private static final long serialVersionUID = -6443811928706492241L;

    private String connectionKey;

    private String hostname;

    private Integer port;

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

    public String getConnectionKey() {
        return connectionKey;
    }

    public void setConnectionKey(String connectionKey) {
        this.connectionKey = connectionKey;
    }

    @Override
    public String toString() {
        return "RemoteAccessConnect [hostname=" + hostname + ", port=" + port + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((connectionKey == null) ? 0 : connectionKey.hashCode());
        result = prime * result + ((hostname == null) ? 0 : hostname.hashCode());
        result = prime * result + ((port == null) ? 0 : port.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RemoteAccessConnect other = (RemoteAccessConnect) obj;
        if (connectionKey == null) {
            if (other.connectionKey != null)
                return false;
        } else if (!connectionKey.equals(other.connectionKey))
            return false;
        if (hostname == null) {
            if (other.hostname != null)
                return false;
        } else if (!hostname.equals(other.hostname))
            return false;
        if (port == null) {
            if (other.port != null)
                return false;
        } else if (!port.equals(other.port))
            return false;
        return true;
    }
}
