package com.cumulocity.sdk.client.cep;

import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.io.Reader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cumulocity.rest.representation.cep.CepMediaType;
import com.cumulocity.rest.representation.cep.CepModuleRepresentation;
import com.cumulocity.rest.representation.operation.DeviceControlMediaType;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.UrlProcessor;

@RunWith(MockitoJUnitRunner.class)
public class CepApiImplTest {

    private static final String ID = "5";

    private static final String TEMPLATE_URL = "template_url";

    private static final String BASE_URL = "http://abcd.com/";

    private static final String CEP_MODULES_COLLECTION_URL = BASE_URL + "cep/modules";

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
        when(platformParameters.getHost()).thenReturn(BASE_URL);
        cepApi = new CepApiImpl(platformParameters, restConnector, urlProcessor, DEAFAULT_PAGE_SIZE);
    }
    
    @Test
    public void shouldCreateCepModule() throws Exception {
        //Given
        CepModuleRepresentation created = new CepModuleRepresentation();
        when(restConnector.postStream(
                eq(CEP_MODULES_COLLECTION_URL), 
                eq(CepMediaType.CEP_MODULE),
                any(InputStream.class),
                eq(CepModuleRepresentation.class)
                )
        )
        .thenReturn(created);

        //when
        CepModuleRepresentation result = cepApi.create(mock(InputStream.class));

        // then 
        assertThat(result, sameInstance(created));
    }
    
    @Test
    public void shouldUpdateCepModule() throws Exception {
        //Given
        CepModuleRepresentation updated = new CepModuleRepresentation();
        when(restConnector.putStream(
                eq(CEP_MODULES_COLLECTION_URL+"/"+ID), 
                eq(CepMediaType.CEP_MODULE),
                any(InputStream.class),
                eq(CepModuleRepresentation.class))
        ).thenReturn(updated);

        //when
        CepModuleRepresentation result = cepApi.update(ID, mock(InputStream.class));

        // then 
        assertThat(result, sameInstance(updated));
    }
    
    @Test
    public void shouldDeleteCepModule() throws Exception {
        //Given
        CepModuleRepresentation deleted = new CepModuleRepresentation();
        deleted.setId(ID);
        //when
        cepApi.delete(deleted);
        // then 
        verify(restConnector).delete(CEP_MODULES_COLLECTION_URL+"/"+ID);
    }
}
