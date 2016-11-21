package c8y;

import org.svenson.AbstractDynamicProperties;

public class RemoteAccess extends AbstractDynamicProperties {

    private static final long serialVersionUID = 6652959747455810127L;

    public static final String PROTOCOL_VNC = "VNC";

    private String hostname;

    private Integer port;

    private String protocol;

    private String encryptedPassword;

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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((encryptedPassword == null) ? 0 : encryptedPassword.hashCode());
        result = prime * result + ((hostname == null) ? 0 : hostname.hashCode());
        result = prime * result + ((port == null) ? 0 : port.hashCode());
        result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
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
        RemoteAccess other = (RemoteAccess) obj;
        if (encryptedPassword == null) {
            if (other.encryptedPassword != null)
                return false;
        } else if (!encryptedPassword.equals(other.encryptedPassword))
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
        if (protocol == null) {
            if (other.protocol != null)
                return false;
        } else if (!protocol.equals(other.protocol))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RemoteAccess [hostname=" + hostname + ", port=" + port + ", protocol=" + protocol + "]";
    }

}
