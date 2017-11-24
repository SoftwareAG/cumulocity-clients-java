package com.cumulocity.microservice.agent.server.api.service;

import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.microservice.MicroserviceMetadataRepresentation;
import com.cumulocity.sdk.client.RestOperations;
import com.cumulocity.sdk.client.SDKException;
import com.google.common.base.Suppliers;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static com.cumulocity.rest.representation.application.ApplicationMediaType.APPLICATION;
import static com.cumulocity.rest.representation.application.ApplicationRepresentation.applicationRepresentation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MicroserviceRepositoryTest {

    private MicroserviceRepository repository;
    private RestOperations restOperations;

    @Before
    public void setup() {
        restOperations = Mockito.mock(RestOperations.class);
        repository = MicroserviceRepository.microserviceApi()
                .baseUrl(Suppliers.ofInstance("http://c8y.com"))
                .connector(Suppliers.ofInstance(restOperations))
                .register(false)
                .build();
    }

    @Test
    public void shouldFailWhenNoCurrentApplication() {


        Throwable exception = catchThrowable(new ThrowingCallable() {
            @Override
            public void call() throws Throwable {
                repository.register("cep", MicroserviceMetadataRepresentation.microserviceMetadataRepresentation().build());
            }
        });

        assertThat(exception)
                .isInstanceOf(SDKException.class)
                .hasMessageContaining("No microservice with name cep registered.");


    }

    @Test
    public void shouldNotUpdateApplicationWhenNotRequired() {

        ApplicationRepresentation existing = applicationRepresentation()
                .name("cep")
                .build();
        when(restOperations.get("http://c8y.com/application/currentApplication", APPLICATION, ApplicationRepresentation.class)).thenReturn(existing);


        ApplicationRepresentation cep = repository.register("cep", MicroserviceMetadataRepresentation.microserviceMetadataRepresentation().build());

        assertThat(cep).isSameAs(existing);
        verify(restOperations, never()).put(anyString(), any(CumulocityMediaType.class), any(ApplicationRepresentation.class));
    }
}
