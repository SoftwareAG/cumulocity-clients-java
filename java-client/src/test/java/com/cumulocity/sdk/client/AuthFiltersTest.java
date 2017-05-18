package com.cumulocity.sdk.client;

import org.fest.assertions.Assertions;
import org.junit.Test;

import static com.cumulocity.sdk.client.AuthFilters.BaseFilter.formatCredentials;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Created by dombiel on 17.05.17.
 */
public class AuthFiltersTest {

    /**
     * for some reason paypal authorization header has format:
     * "Paypal tenant:user:password"
     * while basic authorization header:
     * "Basic tenant/user:password"
     */
    @Test
    public void shouldFormatAuthHeaderCredentialsDependingOnAuthPrefix() {
        assertThat(formatCredentials("management/john", "secret", "Basic")).isEqualTo("management/john:secret");
        assertThat(formatCredentials("management/john", "secret", "BASIC")).isEqualTo("management/john:secret");
        assertThat(formatCredentials("management/john", "secret", "Paypal")).isEqualTo("management:john:secret");
        assertThat(formatCredentials("management/john", "secret", "PAYPAL")).isEqualTo("management:john:secret");
    }

}