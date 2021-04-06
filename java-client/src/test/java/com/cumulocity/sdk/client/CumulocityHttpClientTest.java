package com.cumulocity.sdk.client;

import org.assertj.core.api.Assertions;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.ws.rs.client.Client;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)

public class CumulocityHttpClientTest {

    abstract public static class CumulocityHttpClientTestSetup {
        protected final String HOST = "http://management.cumulocity.com:8080";

        protected CumulocityHttpClient client;

        protected Client jerseyClient;

        protected ClientConfig clientConfig;

        protected int chunkedEncodingSize = 1024;

        protected  ResponseParser responseParser;

        protected RestConnector restConnector;

        @Before
        public void setUp() {
            PlatformParameters platformParameters = new PlatformParameters();
            platformParameters.setForceInitialHost(true);
            platformParameters.setHost(HOST);
            platformParameters.setChunkedEncodingSize(chunkedEncodingSize);
            restConnector = new RestConnector(platformParameters,responseParser);
            jerseyClient = restConnector.getClient();
            client = createClient(platformParameters);
        }

        protected void verifyResolvedPath(String expected, String initial) {
            String resolved = client.resolvePath(initial);
            assertThat(expected, is(resolved));
        }

        protected CumulocityHttpClient createClient(PlatformParameters platformParameters) {
            CumulocityHttpClient client = new CumulocityHttpClient(clientConfig);
            client.setPlatformParameters(platformParameters);
            return client;
        }
    }

    @Ignore
    public static class UnparameterizedCumulocityHttpClientTest extends CumulocityHttpClientTestSetup {
        @Test
        public void shouldChangeHostToPlatformHostIfThisIsForcedInParameters() {
            String queryParams = "?test=1&a=1";
            String pathParams = "/test/1/q=1&a=1";

            verifyResolvedPath(HOST, HOST);
            verifyResolvedPath(HOST, "http://127.0.0.1");
            verifyResolvedPath(HOST, "http://127.0.0.1:8181");
            verifyResolvedPath(HOST + queryParams, "http://127.0.0.1:8181" + queryParams);
            verifyResolvedPath(HOST + queryParams, "http://127.0.0.1" + queryParams);
            verifyResolvedPath(HOST + pathParams, "http://127.0.0.1" + pathParams);
        }

        @Test
        public void shouldEnableChunkedEncoding() {
            Integer chunkedProperty = (Integer) jerseyClient.getConfiguration().getProperty(ClientProperties.CHUNKED_ENCODING_SIZE);
            assertEquals(chunkedProperty, Integer.valueOf(chunkedEncodingSize));
        }
    }

    @RunWith(Parameterized.class)
    @Ignore
    public static class ParameterizedCumulocityHttpClientTest extends CumulocityHttpClientTestSetup {

        @Parameterized.Parameters
        public static Collection<Character> invalidUrlCharacters() {
            return Arrays.asList(
                    '{', '}', '<', '>', '[', ']', '^', '`', '|'
            );
        }

        private final Character character;

        public ParameterizedCumulocityHttpClientTest(Character character) {
            this.character = character;
        }

        @Test
        public void shouldThrowExceptionWithCode400WhenIllegalCharactersInPath() {
            String path = "http://example.com/" + character;
            Throwable thrown = catchThrowable(() -> client.target(path).request());

            Assertions.assertThat(thrown).isInstanceOf(SDKException.class);
            Assertions.assertThat(thrown).hasMessageContaining("Illegal characters used in URL.");
            Assertions.assertThat(((SDKException) thrown).getHttpStatus()).isEqualTo(400);
        }
    }
}
