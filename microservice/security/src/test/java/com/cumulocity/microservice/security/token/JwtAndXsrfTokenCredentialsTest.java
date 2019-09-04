package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.rest.representation.user.CurrentUserRepresentation;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class JwtAndXsrfTokenCredentialsTest extends JwtTokenTestsHelper{
    @Test
    public void shouldBuildUserCredentialsWithXsrfToken() {
        JwtAndXsrfTokenCredentials jwtAndXsrfTokenCredentials = new JwtAndXsrfTokenCredentials(mockedJwtImpl(), SAMPLE_XSRF_TOKEN);
        JwtTokenAuthentication jwtTokenAuthentication = new JwtTokenAuthentication(jwtAndXsrfTokenCredentials);
        CurrentUserRepresentation currentUserRepresentation = new CurrentUserRepresentation();
        currentUserRepresentation.setUserName(SAMPLE_USERNAME);
        jwtTokenAuthentication.setCurrentUserRepresentation(currentUserRepresentation);

        UserCredentials userCredentials = jwtAndXsrfTokenCredentials.toUserCredentials(SAMPLE_TENANT_NAME, jwtTokenAuthentication);

        assertThat(userCredentials.getUsername()).isEqualTo(SAMPLE_USERNAME);
        assertThat(userCredentials.getOAuthAccessToken()).isEqualTo(SAMPLE_ENCODED_TOKEN);
        assertThat(userCredentials.getXsrfToken()).isEqualTo(SAMPLE_XSRF_TOKEN);
    }

    @Test
    public void tokensShouldBeEqual(){
        JwtAndXsrfTokenCredentials token1 = new JwtAndXsrfTokenCredentials(mockedJwtImpl(), SAMPLE_XSRF_TOKEN);
        JwtAndXsrfTokenCredentials token2 = new JwtAndXsrfTokenCredentials(mockedJwtImpl(), SAMPLE_XSRF_TOKEN);

        assertThat(token1.equals(token2)).isTrue();
        assertThat(token2.equals(token1)).isTrue();
        assertThat(token1.hashCode()).isEqualTo(token2.hashCode());
    }
}
