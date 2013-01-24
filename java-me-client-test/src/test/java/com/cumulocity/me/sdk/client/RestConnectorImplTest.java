package com.cumulocity.me.sdk.client;

import static com.cumulocity.me.rest.representation.BaseCumulocityMediaType.ERROR_MESSAGE;
import static com.cumulocity.me.test.ReturnsThis.doReturnsThis;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.microedition.io.HttpConnection;

import org.junit.Test;

import com.cumulocity.me.http.WebClient;
import com.cumulocity.me.http.WebRequestBuilder;
import com.cumulocity.me.rest.representation.CumulocityMediaType;
import com.cumulocity.me.rest.representation.CumulocityResourceRepresentation;
import com.cumulocity.me.rest.representation.ErrorMessageRepresentation;
import com.cumulocity.me.sdk.client.http.RestConnectorImpl;

public class RestConnectorImplTest {

    PlatformParameters platformParameters = new PlatformParameters("localhost", "test", "user", "pass", "appKey");
    WebClient httpClient = mock(WebClient.class);
    
    RestConnectorImpl restConnector = new RestConnectorImpl(platformParameters, httpClient);

    String path = "http://localhost/test/";
    CumulocityMediaType mediaType = ERROR_MESSAGE;
    Class<?> responseEntityType = ErrorMessageRepresentation.class;
    
    WebRequestBuilder requestBuilder = mock(WebRequestBuilder.class, doReturnsThis());
    
    @Test
    public void shouldGet() throws Exception {
        when(httpClient.request(path)).thenReturn(requestBuilder);
        when(requestBuilder.get(HttpConnection.HTTP_OK, responseEntityType)).thenReturn(new ErrorMessageRepresentation());
        
        CumulocityResourceRepresentation representation = restConnector.get(path, mediaType, responseEntityType);
        
        assertThat(representation).isInstanceOf(ErrorMessageRepresentation.class);
    }
}
