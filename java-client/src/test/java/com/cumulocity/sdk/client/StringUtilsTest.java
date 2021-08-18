package com.cumulocity.sdk.client;

import org.junit.jupiter.api.Test;

import static com.cumulocity.sdk.client.util.StringUtils.isBlank;
import static com.cumulocity.sdk.client.util.StringUtils.isNotBlank;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringUtilsTest {
    @Test
    public void shouldEvaluateIfIsBlank() {
        assertTrue(isBlank(null));
        assertTrue(isBlank(""));
        assertTrue(isBlank(" "));
        assertFalse(isBlank("bob"));
        assertFalse(isBlank("  bob  "));
    }

    @Test
    public void shouldEvaluateIfIsNotBlank() {
        assertFalse(isNotBlank(null));
        assertFalse(isNotBlank(""));
        assertFalse(isNotBlank(" "));
        assertTrue(isNotBlank("bob"));
        assertTrue(isNotBlank("  bob  "));
    }
}
