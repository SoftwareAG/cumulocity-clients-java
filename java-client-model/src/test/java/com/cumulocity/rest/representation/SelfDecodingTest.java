package com.cumulocity.rest.representation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.cumulocity.rest.representation.user.UserRepresentation;

public class SelfDecodingTest {

    @Test
    public void shouldDecodeSelf() throws Exception {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setSelf("http://test/user/asdf%C2%A7$%25&()=asdfsd");
        
        assertThat(userRepresentation.getSelfDecoded()).isEqualTo("http://test/user/asdf§$%&()=asdfsd");

        userRepresentation.setSelf("http://test/user/te%23%24%25%3F%2Fdad");

        assertThat(userRepresentation.getSelfDecoded()).isEqualTo("http://test/user/te#$%?/dad");
    }
}
