package com.cumulocity.model.control;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.svenson.JSONParser;

import com.cumulocity.model.control.Relay.RelayState;

public class RelayTest {

    private String relayRepresentationWithValidState = "{\"relayState\":\"OPEN\"}";

    private String relayRepresentationWithInvalidState = "{\"relayState\":\"MEH\"}";

    @Test
    public final void testThatTheRepresentationOfTheEnumerationSerializesCorrectlyIfStateIsValid() {
        Relay relay = new Relay();
        relay.setRelayState(RelayState.OPEN);
        Relay newRelay = JSONParser.defaultJSONParser().parse(Relay.class, relayRepresentationWithValidState);
        assertTrue(newRelay.getRelayState().equals(relay.getRelayState()));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testThatTheRepresentationOfTheEnumerationDoesNotSerializeCorrectlyIfStateIsInvalid() {
        JSONParser.defaultJSONParser().parse(Relay.class, relayRepresentationWithInvalidState);
        // IllegalArgumentException should be thrown.
    }

}
