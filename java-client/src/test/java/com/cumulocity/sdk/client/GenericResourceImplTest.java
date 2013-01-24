package com.cumulocity.sdk.client;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.rest.representation.BaseCumulocityResourceRepresentation;
import com.cumulocity.rest.representation.CumulocityMediaType;

public class GenericResourceImplTest {

    private static final Class<BaseCumulocityResourceRepresentation> RESPONSE_TYPE = BaseCumulocityResourceRepresentation.class;

    private static final String URL = "path_to_resource";

    private static final CumulocityMediaType MEDIA_TYPE = new CumulocityMediaType();

    @Mock
    private RestConnector restConnector;

    private GenericResourceImpl<BaseCumulocityResourceRepresentation> resource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        resource = new GenericResourceImpl<BaseCumulocityResourceRepresentation>(restConnector, URL) {

            @Override
            protected CumulocityMediaType getMediaType() {
                return MEDIA_TYPE;
            }

            @Override
            protected Class<BaseCumulocityResourceRepresentation> getResponseClass() {
                return RESPONSE_TYPE;
            }

        };
    }

    @Test
    public void shouldUseRestConnector() throws Exception {
        // given
        BaseCumulocityResourceRepresentation representation = new BaseCumulocityResourceRepresentation();
        when(restConnector.get(URL, MEDIA_TYPE, RESPONSE_TYPE)).thenReturn(representation);

        // when
        BaseCumulocityResourceRepresentation result = resource.get();

        // then
        assertThat(result, sameInstance(representation));

    }

}
