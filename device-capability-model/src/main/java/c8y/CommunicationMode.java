package c8y;

import org.svenson.AbstractDynamicProperties;

public class CommunicationMode extends AbstractDynamicProperties {
    
    private String mode;

    public CommunicationMode() {
    }

    public CommunicationMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.mode != null ? this.mode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CommunicationMode other = (CommunicationMode) obj;
        return !((this.mode == null) ? (other.mode != null) : !this.mode.equals(other.mode));
    }

}
