package com.cumulocity.model.control;

import org.junit.jupiter.api.Test;
import org.svenson.JSONParser;

import c8y.Relay;
import c8y.Relay.RelayState;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class RelayTest {

    private String relayRepresentationWithValidState = "{\"relayState\":\"OPEN\"}";

    private String relayRepresentationWithInvalidState = "{\"relayState\":\"MEH\"}";

    @Test
    final void testThatTheRepresentationOfTheEnumerationSerializesCorrectlyIfStateIsValid() {
        Relay relay = new Relay();
        relay.setRelayState(RelayState.OPEN);
        Relay newRelay = JSONParser.defaultJSONParser().parse(Relay.class, relayRepresentationWithValidState);
        assertThat(newRelay.getRelayState()).isEqualTo(relay.getRelayState());
    }

    @Test
    final void testThatTheRepresentationOfTheEnumerationDoesNotSerializeCorrectlyIfStateIsInvalid() {
        // when
        Throwable thrown = catchThrowable(() -> JSONParser.defaultJSONParser().parse(Relay.class, relayRepresentationWithInvalidState));

        // then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

}
