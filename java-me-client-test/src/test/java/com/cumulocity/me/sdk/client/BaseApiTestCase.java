package com.cumulocity.me.sdk.client;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;

import com.cumulocity.me.rest.representation.platform.PlatformApiRepresentation;
import com.cumulocity.me.rest.representation.platform.PlatformMediaType;
import com.cumulocity.me.sdk.client.TemplateUrlParser;
import com.cumulocity.me.sdk.client.http.RestConnector;

public class BaseApiTestCase {

    protected static final String PLATFORM_URL = "http://localhost";
    
    protected RestConnector restConnector = mock(RestConnector.class);
    protected TemplateUrlParser templateUrlParser = mock(TemplateUrlParser.class);
    protected PlatformApiRepresentation platformApiRepresentation;

    @Before
    public void setUp() throws Exception {
        platformApiRepresentation = new PlatformApiRepresentation();
        when(restConnector.get(PLATFORM_URL, PlatformMediaType.PLATFORM_API, PlatformApiRepresentation.class))
            .thenReturn(platformApiRepresentation);
    }

}
