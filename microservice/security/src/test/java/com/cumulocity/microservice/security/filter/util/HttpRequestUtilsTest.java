package com.cumulocity.microservice.security.filter.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HttpRequestUtilsTest {

    //Base 64 credentials coded: "management/rtanaka@hks-power.co.jp:(Def_Leppard)1"
    private final static String asciiCharactersCredentials = "Basic bWFuYWdlbWVudC9ydGFuYWthQGhrcy1wb3dlci5jby5qcDooRGVmX0xlcHBhcmQpMQ==";

    //Base 64 credentials coded: "management/田中隆太:(Def_Leppard)1"
    private final static String notAsciiCharactersCredentials = "Basic bWFuYWdlbWVudC/nlLDkuK3pmoblpKo6KERlZl9MZXBwYXJkKTE=";

    @Test
    void shouldDecodeAsciiCharactersCredentials() {
        // Given
        final String[] expectedArguments = {"management/rtanaka@hks-power.co.jp", "(Def_Leppard)1"};
        // When
        final String[] decoded = HttpRequestUtils.decode(asciiCharactersCredentials);
        // Then
        Assertions.assertArrayEquals(expectedArguments, decoded);
    }

    @Test
    void shouldDecodeNoAsciiCharactersCredentials() {
        // Given
        final String[] expectedArguments = {"management/田中隆太", "(Def_Leppard)1"};
        // When
        final String[] decoded = HttpRequestUtils.decode(notAsciiCharactersCredentials);
        // Then
        Assertions.assertArrayEquals(expectedArguments, decoded);

    }
}