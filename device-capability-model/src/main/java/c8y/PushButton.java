package c8y;

import org.svenson.AbstractDynamicProperties;

public class PushButton extends AbstractDynamicProperties { 

    private int duration;
    
    private String button;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((button == null) ? 0 : button.hashCode());
        result = prime * result + duration;
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
        PushButton other = (PushButton) obj;
        if (button == null) {
            if (other.button != null)
                return false;
        } else if (!button.equals(other.button))
            return false;
        if (duration != other.duration)
            return false;
        return true;
    }
    
    
}
