package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation.microserviceMetadataRepresentation;
import static com.cumulocity.rest.representation.application.ApplicationRepresentation.applicationRepresentation;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MicroserviceSubscriptionsRepositoryTest {

    @Mock
    private MicroserviceRepository repository;

    @Mock
    private PlatformProperties platformProperties;

    @InjectMocks
    private MicroserviceSubscriptionsRepository microserviceSubscriptionsRepository;

    @BeforeEach
    public void beforeEach() {
        when(platformProperties.getApplicationKey()).thenReturn("application-key");
        microserviceSubscriptionsRepository.updateCurrentSubscriptions(new ArrayList<>());
    }

    @ParameterizedTest
    @CsvSource({
            ", 'application-key', 1",
            "'', 'application-key', 1",
            "'configured-key', 'application-key', 1",
            "'application-key', 'application-key', 0"
    })
    public void shouldUpdateConfiguredApplicationKeyIfDiffersWhenRegisteringApplication(String configuredAppKey, String appKey, int invocations) {
        // given
        when(platformProperties.getApplicationKey()).thenReturn(configuredAppKey);
        when(repository.register(any(MicroserviceMetadataRepresentation.class)))
                .thenReturn(applicationRepresentation().key(appKey).build());

        // when
        microserviceSubscriptionsRepository.register(microserviceMetadataRepresentation().build());

        // then
        verify(platformProperties, times(invocations)).setApplicationKey(eq(appKey));
    }

    @ParameterizedTest
    @CsvSource({
            ", 'application-key', 1",
            "'', 'application-key', 1",
            "'configured-key', 'application-key', 1",
            "'application-key', 'application-key', 0"
    })
    public void shouldUpdateConfiguredApplicationKeyIfDiffersWhenRegisteringApplicationByDeprecatedMethod(String configuredAppKey, String appKey, int invocations) {
        // given
        when(platformProperties.getApplicationKey()).thenReturn(configuredAppKey);
        when(repository.register(anyString(), any(MicroserviceMetadataRepresentation.class)))
                .thenReturn(applicationRepresentation().key(appKey).build());

        // when
        microserviceSubscriptionsRepository.register("application-name", microserviceMetadataRepresentation().build());

        // then
        verify(platformProperties, times(invocations)).setApplicationKey(eq(appKey));
    }
}
