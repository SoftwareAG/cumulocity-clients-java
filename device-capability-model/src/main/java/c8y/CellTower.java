package c8y;

public class CellTower {

    private String radioType;
    private int mobileCountryCode;
    private int mobileNetworkCode;
    private int locationAreaCode;
    private int cellId;
    private int timingAdvance;
    private int signalStrength;
    private int primaryScramblingCode;
    private int serving;
    
    public String getRadioType() {
        return radioType;
    }
    public void setRadioType(String radioType) {
        this.radioType = radioType;
    }
    public int getMobileCountryCode() {
        return mobileCountryCode;
    }
    public void setMobileCountryCode(int mobileCountryCode) {
        this.mobileCountryCode = mobileCountryCode;
    }
    public int getMobileNetworkCode() {
        return mobileNetworkCode;
    }
    public void setMobileNetworkCode(int mobileNetworkCode) {
        this.mobileNetworkCode = mobileNetworkCode;
    }
    public int getLocationAreaCode() {
        return locationAreaCode;
    }
    public void setLocationAreaCode(int locationAreaCode) {
        this.locationAreaCode = locationAreaCode;
    }
    public int getCellId() {
        return cellId;
    }
    public void setCellId(int cellId) {
        this.cellId = cellId;
    }
    public int getTimingAdvance() {
        return timingAdvance;
    }
    public void setTimingAdvance(int timingAdvance) {
        this.timingAdvance = timingAdvance;
    }
    public int getSignalStrength() {
        return signalStrength;
    }
    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
    }
    public int getPrimaryScramblingCode() {
        return primaryScramblingCode;
    }
    public void setPrimaryScramblingCode(int primaryScramblingCode) {
        this.primaryScramblingCode = primaryScramblingCode;
    }
    public int getServing() {
        return serving;
    }
    public void setServing(int serving) {
        this.serving = serving;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + cellId;
        result = prime * result + locationAreaCode;
        result = prime * result + mobileCountryCode;
        result = prime * result + mobileNetworkCode;
        result = prime * result + primaryScramblingCode;
        result = prime * result + ((radioType == null) ? 0 : radioType.hashCode());
        result = prime * result + serving;
        result = prime * result + signalStrength;
        result = prime * result + timingAdvance;
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
        CellTower other = (CellTower) obj;
        if (cellId != other.cellId)
            return false;
        if (locationAreaCode != other.locationAreaCode)
            return false;
        if (mobileCountryCode != other.mobileCountryCode)
            return false;
        if (mobileNetworkCode != other.mobileNetworkCode)
            return false;
        if (primaryScramblingCode != other.primaryScramblingCode)
            return false;
        if (radioType == null) {
            if (other.radioType != null)
                return false;
        } else if (!radioType.equals(other.radioType))
            return false;
        if (serving != other.serving)
            return false;
        if (signalStrength != other.signalStrength)
            return false;
        if (timingAdvance != other.timingAdvance)
            return false;
        return true;
    }
}
