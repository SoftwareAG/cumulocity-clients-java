package com.cumulocity.sdk.client.cep;

import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.Reader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cumulocity.rest.representation.cep.CepModuleRepresentation;
import com.cumulocity.rest.representation.operation.DeviceControlMediaType;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.UrlProcessor;

@RunWith(MockitoJUnitRunner.class)
public class CepApiImplTest {

    private static final String TEMPLATE_URL = "template_url";

    private static final String BASE_URL = "http://abcd.com";

    private static final String CEP_MODULES_COLLECTION_URL = BASE_URL + "/cep/modules";

    private static final String CEP_MODULE_ID = "123ABC";

    private static final int DEAFAULT_PAGE_SIZE = 7;

    private CepApi cepApi;

    @Mock
    private RestConnector restConnector;

    @Mock
    private PlatformParameters platformParameters;

    @Mock
    private UrlProcessor urlProcessor;

    @Before
    public void setup() throws Exception {
        cepApi = new CepApiImpl(platformParameters, restConnector, urlProcessor, DEAFAULT_PAGE_SIZE);
    }
    
    @Test
    public void shouldCreateCepModule() throws Exception {
        //Given
        CepModuleRepresentation created = new CepModuleRepresentation();

        when((CepModuleRepresentation)restConnector.post(Mockito.eq(CEP_MODULES_COLLECTION_URL), Mockito.eq(DeviceControlMediaType.MULTIPART_FORM_DATA_TYPE), Mockito.any(Reader.class))).thenReturn(created);

        //when
        CepModuleRepresentation result = cepApi.create(Mockito.mock(Reader.class));

        // then 
        assertThat(result, sameInstance(created));
    }
}
