package c8y;

import org.svenson.AbstractDynamicProperties;



/**
 * Provides a representation for an electrical relay.
 * 
 * A relay is an electrical operated switch that can be in one of two states: OPEN or CLOSED.
 * If the relay is OPEN, it breaks the circuit and electrical current does not flow. 
 * @author ricardomarques
 *
 */
public class Relay extends AbstractDynamicProperties {

    public enum RelayState {
        OPEN, CLOSED;
    }

    private RelayState relayState;

    /**
     * @return the relayState
     */
    public final RelayState getRelayState() {
        return relayState;
    }

    /**
     * @param relayState the relayState to set
     */
    public final void setRelayState(RelayState relayState) {
        this.relayState = relayState;
    }

    @Override
    public int hashCode() {
        return relayState != null ? relayState.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Relay)) return false;

        Relay relay = (Relay) o;

        if (relayState != relay.relayState) return false;

        return true;
    }

    @Override
    public String toString() {
        return "Relay{" +
                "relayState=" + relayState +
                '}';
    }
}
