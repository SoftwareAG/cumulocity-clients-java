package com.cumulocity.sdk.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.client.apache.ApacheHttpClientHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
            CumulocityHttpClient client = new CumulocityHttpClient(createDefaultClientHander(), null);
            client.setPlatformParameters(platformParameters);
            return client;
        }

        protected ApacheHttpClientHandler createDefaultClientHander() {
            final HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
            return new ApacheHttpClientHandler(client, mock(ClientConfig.class));
        }
    }

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
        public void shouldEnableChunckedEncoding() {
            Integer chunkedProperty = (Integer) jerseyClient.getProperties().get(clientConfig.PROPERTY_CHUNKED_ENCODING_SIZE);
            assertEquals(chunkedProperty, Integer.valueOf(chunkedEncodingSize));
        }
    }

    @RunWith(Parameterized.class)
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
            Throwable thrown = catchThrowable(() -> client.resource(path));

            Assertions.assertThat(thrown).isInstanceOf(SDKException.class);
            Assertions.assertThat(thrown).hasMessageContaining("Illegal characters used in URL.");
            Assertions.assertThat(((SDKException) thrown).getHttpStatus()).isEqualTo(400);
        }
    }
}
