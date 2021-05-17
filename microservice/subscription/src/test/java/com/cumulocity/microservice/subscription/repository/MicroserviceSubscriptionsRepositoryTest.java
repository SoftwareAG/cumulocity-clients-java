package com.cumulocity.microservice.subscription.repository;

import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.rest.representation.application.ApplicationUserRepresentation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

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
        Mockito.when(platformProperties.getApplicationKey()).thenReturn("application-key");
        microserviceSubscriptionsRepository.updateCurrentSubscriptions(new ArrayList<>());
    }

    @Test
    public void shouldPreserveOrderWithoutManagement() {
        //given
        Mockito.when(repository.getSubscriptions("123")).thenReturn(Arrays.asList(
            new ApplicationUserRepresentation("t123","service_myapp","secret1"),
            new ApplicationUserRepresentation("t234","service_myapp","secret2"),
            new ApplicationUserRepresentation("t345","service_myapp","secret3")
        ));

        //when
        MicroserviceSubscriptionsRepository.Subscriptions subscriptions = microserviceSubscriptionsRepository.retrieveSubscriptions("123");

        //then
        Assertions.assertThat(subscriptions.getAll()).containsExactly(
                MicroserviceCredentials.builder().tenant("t123").username("service_myapp").password("secret1").appKey("application-key").build(),
                MicroserviceCredentials.builder().tenant("t234").username("service_myapp").password("secret2").appKey("application-key").build(),
                MicroserviceCredentials.builder().tenant("t345").username("service_myapp").password("secret3").appKey("application-key").build()
        );
    }

    @Test
    public void shouldMoveManagementToFront() {
        //given
        Mockito.when(repository.getSubscriptions("123")).thenReturn(Arrays.asList(
                new ApplicationUserRepresentation("t123","service_myapp","secret1"),
                new ApplicationUserRepresentation("t234","service_myapp","secret2"),
                new ApplicationUserRepresentation("management","service_myapp","secret3")
        ));

        //when
        MicroserviceSubscriptionsRepository.Subscriptions subscriptions = microserviceSubscriptionsRepository.retrieveSubscriptions("123");

        //then
        Assertions.assertThat(subscriptions.getAll()).containsExactly(
                MicroserviceCredentials.builder().tenant("management").username("service_myapp").password("secret3").appKey("application-key").build(),
                MicroserviceCredentials.builder().tenant("t123").username("service_myapp").password("secret1").appKey("application-key").build(),
                MicroserviceCredentials.builder().tenant("t234").username("service_myapp").password("secret2").appKey("application-key").build()
        );
    }

    @Test
    public void shouldNotTouchIfTooShortToOrder() {
        //given
        Mockito.when(repository.getSubscriptions("123")).thenReturn(Arrays.asList(
                new ApplicationUserRepresentation("t123","service_myapp","secret1")
        ));

        //when
        MicroserviceSubscriptionsRepository.Subscriptions subscriptions = microserviceSubscriptionsRepository.retrieveSubscriptions("123");

        //then
        Assertions.assertThat(subscriptions.getAll()).containsExactly(
                MicroserviceCredentials.builder().tenant("t123").username("service_myapp").password("secret1").appKey("application-key").build()

        );
    }
}
