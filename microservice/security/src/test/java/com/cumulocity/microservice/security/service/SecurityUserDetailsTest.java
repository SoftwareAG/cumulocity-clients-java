package com.cumulocity.microservice.security.service;

import com.cumulocity.model.authentication.CumulocityOAuthCredentials;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class SecurityUserDetailsTest {

    @Test
    public void shouldCompareEqualBasicUserObjects() {
        SecurityUserDetails basicUserDetails = basicUserDetails();
        SecurityUserDetails anotherBasicUserDetails = basicUserDetails();

        assertEquals(basicUserDetails, anotherBasicUserDetails);
        assertEquals(basicUserDetails.hashCode(), anotherBasicUserDetails.hashCode());
        assertEquals(basicUserDetails.toString(), anotherBasicUserDetails.toString());
    }

    @Test
    public void shouldCompareEqualOauthUserObjects() {
        SecurityUserDetails oauthUserDetails = oauthUserDetails();
        SecurityUserDetails anotherOauthUserDetails = oauthUserDetails();

        assertEquals(oauthUserDetails, anotherOauthUserDetails);
        assertEquals(oauthUserDetails.hashCode(), anotherOauthUserDetails.hashCode());
        assertEquals(oauthUserDetails.toString(), anotherOauthUserDetails.toString());
    }

    @Test
    public void shouldNotFailWhenComparingTwoObjectsWithDifferentCredentialsImplementation() {
        SecurityUserDetails basicUserDetails = basicUserDetails();
        SecurityUserDetails oauthUserDetails = oauthUserDetails();

        assertNotEquals(basicUserDetails, oauthUserDetails);
        // It shouldn't fail.
        assertNotNull(basicUserDetails.hashCode());
        assertNotNull(oauthUserDetails.hashCode());
        assertNotNull(basicUserDetails.toString());
        assertNotNull(oauthUserDetails.toString());
    }

    private static SecurityUserDetails oauthUserDetails() {
        return SecurityUserDetails.activeUser(Collections.singletonList("role"))
                .credentials(CumulocityOAuthCredentials.builder()
                        .applicationKey("applicationKey")
                        .oAuthAccessToken("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOm51bGwsImlzcyI6ImN1bXVsb2NpdHkuZGVmYXVsdC5zdmMuY2x1c3Rlci5sb2NhbCIsImF1ZCI6ImN1bXVsb2NpdHkuZGVmYXVsdC5zdmMuY2x1c3Rlci5sb2NhbCIsInN1YiI6ImFkbWluIiwidGNpIjoiMzhhNWU3YjUtZGYwZS00Zjk2LTlkNTYtMzYxNzBhZDAzNjQyIiwiaWF0IjoxNTYzMjczMTk5LCJuYmYiOjE1NjMyNzMxOTksImV4cCI6MTU2NDQ4Mjc5OSwidGVuIjoibWFuYWdlbWVudCIsInhzcmZUb2tlbiI6ImFBeFFIWFV3TXh3SWtjUGtsWmlDIn0.LHKwRnmfb69HYNoVJD-3M_0raf3tG0tVXwsDAOZzEbuSup9eq3haIIC6_ZwUzrf6iKA52iOYS_UQ6t5B9cQPlw")
                        .requestOrigin("requestOrigin")
                        .xsrfToken("xsrf")
                        .build())
                .build();
    }

    private static SecurityUserDetails basicUserDetails() {
        return SecurityUserDetails.activeUser("tenant", "username", "password", "role");
    }
}
