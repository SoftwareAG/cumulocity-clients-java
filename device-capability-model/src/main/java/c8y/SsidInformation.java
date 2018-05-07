package c8y;

import org.svenson.AbstractDynamicProperties;

import java.util.Map;
import java.util.Objects;

public class SsidInformation extends AbstractDynamicProperties {
    private static final String MAC_ADDRESS_KEY = "macAddress";
    private static final String SSID_KEY = "ssid";
    private static final String SIGNAL_STRENGTH_KEY = "signalStrength";


    private String macAddress;
    private String ssid;
    private Integer signalStrength;

    public SsidInformation() {
    }

    public SsidInformation(String macAddress, String ssid, Integer signalStrength) {
        this.macAddress = macAddress;
        this.ssid = ssid;
        this.signalStrength = signalStrength;
    }

    public SsidInformation(Map<String, Object> map) {
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value == null) {
                continue;
            }
            switch (key) {
                case MAC_ADDRESS_KEY:
                    this.macAddress = value.toString();
                    continue;
                case SSID_KEY:
                    this.ssid = value.toString();
                    continue;
                case SIGNAL_STRENGTH_KEY:
                    if (value instanceof Number) {
                        this.signalStrength = ((Number) value).intValue();
                    }
                    continue;
                default:
                    setProperty(key, value);
            }
        }

        Object macAddress = map.get("macAddress");
        if (macAddress != null) {
            this.macAddress = macAddress.toString();
        }
        Object ssid = map.get("ssid");
        if (ssid != null) {
            this.ssid = ssid.toString();
        }
        Object signalStrength = map.get("signalStrength");
        if (signalStrength != null) {
            if (signalStrength instanceof Number) {
                this.signalStrength = ((Number) signalStrength).intValue();
            } else {
                this.signalStrength = Integer.valueOf(signalStrength.toString());
            }
        }
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public Integer getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(Integer signalStrength) {
        this.signalStrength = signalStrength;
    }

    @Override
    public String toString() {
        return "SsidInformation{" +
                "macAddress='" + macAddress + '\'' +
                ", ssid='" + ssid + '\'' +
                ", signalStrength=" + signalStrength +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SsidInformation that = (SsidInformation) o;
        return Objects.equals(macAddress, that.macAddress) &&
                Objects.equals(ssid, that.ssid) &&
                Objects.equals(signalStrength, that.signalStrength);
    }

    @Override
    public int hashCode() {

        return Objects.hash(macAddress, ssid, signalStrength);
    }
}
