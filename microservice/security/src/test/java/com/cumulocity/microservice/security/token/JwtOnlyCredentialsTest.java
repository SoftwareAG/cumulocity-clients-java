package com.cumulocity.microservice.security.token;

import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.rest.representation.user.CurrentUserRepresentation;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

public class JwtOnlyCredentialsTest extends JwtTokenTestsHelper {
    @Test
    public void shouldBuildUserCredentialsWithoutXsrfToken() {
        JwtOnlyCredentials jwtOnlyCredentials = new JwtOnlyCredentials(mockedJwtImpl());
        JwtTokenAuthentication jwtTokenAuthentication = new JwtTokenAuthentication(jwtOnlyCredentials);
        CurrentUserRepresentation currentUserRepresentation = new CurrentUserRepresentation();
        currentUserRepresentation.setUserName(SAMPLE_USERNAME);
        jwtTokenAuthentication.setCurrentUserRepresentation(currentUserRepresentation);

        UserCredentials userCredentials = jwtOnlyCredentials.buildUserCredentials(SAMPLE_TENANT_NAME, jwtTokenAuthentication);

        assertThat(userCredentials.getUsername()).isEqualTo(SAMPLE_USERNAME);
        assertThat(userCredentials.getOAuthAccessToken()).isEqualTo(SAMPLE_ENCODED_TOKEN);
        assertThat(userCredentials.getXsrfToken()).isEqualTo(null);
    }

    @Test
    public void tokensShouldBeEqual(){
        JwtOnlyCredentials token1 = new JwtOnlyCredentials(mockedJwtImpl());
        JwtOnlyCredentials token2 = new JwtOnlyCredentials(mockedJwtImpl());

        assertThat(token1.equals(token2)).isTrue();
        assertThat(token2.equals(token1)).isTrue();
        assertThat(token1.hashCode()).isEqualTo(token2.hashCode());
    }
}
