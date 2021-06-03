package com.cumulocity.sdk.client.cep;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cumulocity.rest.representation.cep.CepMediaType;
import com.cumulocity.rest.representation.cep.CepModuleRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;

@ExtendWith(MockitoExtension.class)
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

    @BeforeEach
    public void setup() throws Exception {
        when(platformParameters.getHost()).thenReturn(BASE_URL);
        cepApi = new CepApiImpl(platformParameters, restConnector, DEAFAULT_PAGE_SIZE);
    }

    @Test
    public void shouldCreateCepModule() {
        // given
        CepModuleRepresentation created = new CepModuleRepresentation();
        when(restConnector.postStream(
                eq(CEP_MODULES_COLLECTION_URL),
                eq(CepMediaType.CEP_MODULE),
                any(InputStream.class),
                eq(CepModuleRepresentation.class)
                )
        )
        .thenReturn(created);

        // when
        CepModuleRepresentation result = cepApi.create(mock(InputStream.class));

        // then
        assertThat(result).isSameAs(created);
    }

    @Test
    public void shouldUpdateCepModule() {
        // given
        CepModuleRepresentation updated = new CepModuleRepresentation();
        when(restConnector.putStream(
                eq(CEP_MODULES_COLLECTION_URL+"/"+ID),
                eq(CepMediaType.CEP_MODULE),
                any(InputStream.class),
                eq(CepModuleRepresentation.class))
        ).thenReturn(updated);

        // when
        CepModuleRepresentation result = cepApi.update(ID, mock(InputStream.class));

        // then
        assertThat(result).isSameAs(updated);
    }

    @Test
    public void shouldDeleteCepModule() {
        // given
        CepModuleRepresentation deleted = new CepModuleRepresentation();
        deleted.setId(ID);
        // when
        cepApi.delete(deleted);
        // then
        verify(restConnector).delete(CEP_MODULES_COLLECTION_URL+"/"+ID);
    }
}
