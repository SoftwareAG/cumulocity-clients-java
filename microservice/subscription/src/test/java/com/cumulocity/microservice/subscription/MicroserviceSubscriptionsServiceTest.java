package com.cumulocity.microservice.subscription;

import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionAddedEvent;
import com.cumulocity.microservice.subscription.model.core.HasCredentials;
import com.cumulocity.microservice.subscription.model.core.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.MicroserviceSubscriptionsRepository;
import com.cumulocity.microservice.subscription.repository.MicroserviceSubscriptionsRepository.Subscriptions;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService.MicroserviceChangedListener;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.rest.representation.microservice.MicroserviceMetadataRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MicroserviceSubscriptionsServiceTest {

    @Mock
    private PlatformProperties properties;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private MicroserviceSubscriptionsRepository repository;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();
    
    @InjectMocks
    private MicroserviceSubscriptionsService subscriptionsService;

    @Test
    public void shouldNotUpdateCurrentSubscriptionWhenEventProcessingFails() {
        //given
        final ApplicationRepresentation application = new ApplicationRepresentation();
        final Subscriptions subscriptions = givenSubscriptions();

        given(repository.register(anyString(), any(MicroserviceMetadataRepresentation.class))).willReturn(of(application));
        given(repository.retrieveSubscriptions(anyString())).willReturn(subscriptions);

        doThrow(new SDKException("mongo connection timeout")).when(eventPublisher).publishEvent(any(ApplicationEvent.class));

        //when
        subscriptionsService.subscribe();

        //then
        verify(repository).updateCurrentSubscriptions(ImmutableList.<MicroserviceCredentials>of());
    }

    @Test
    public void shouldUpdateCurrentSubscriptionOnlyWhenEventsProcessedSuccessfully() {
        //given
        final ApplicationRepresentation application = new ApplicationRepresentation();
        final Subscriptions subscriptions = givenSubscriptions();

        given(repository.register(anyString(), any(MicroserviceMetadataRepresentation.class))).willReturn(of(application));
        given(repository.retrieveSubscriptions(anyString())).willReturn(subscriptions);

        //when
        subscriptionsService.subscribe();

        //then
        verify(repository).updateCurrentSubscriptions(Matchers.anyCollection());
    }

    @Test
    public void getCredentialsMethodShouldReturnCurrentlyProcessedCredentialInListener() {
        final AtomicBoolean invoked = new AtomicBoolean(false);
        final ApplicationRepresentation application = new ApplicationRepresentation();
        final Subscriptions subscriptions = givenSubscriptions();

        given(repository.register(anyString(), any(MicroserviceMetadataRepresentation.class))).willReturn(of(application));
        given(repository.retrieveSubscriptions(anyString())).willReturn(subscriptions);
        subscriptionsService.listen(MicroserviceSubscriptionAddedEvent.class, new MicroserviceChangedListener<MicroserviceSubscriptionAddedEvent>() {
            public boolean apply(MicroserviceSubscriptionAddedEvent event) {
                final Optional<MicroserviceCredentials> credentials = subscriptionsService.getCredentials(event.getCredentials().getTenant());
                assertThat(credentials.get()).isEqualTo(event.getCredentials());
                invoked.set(true);
                return true;
            }
        });
        subscriptionsService.subscribe();

        assertThat(invoked.get()).isTrue();
    }

    private Subscriptions givenSubscriptions() {
        final MicroserviceCredentials credentials = new MicroserviceCredentials().withName("name").withPassword("pass").withTenant("tenant");
        return Subscriptions.builder()
                .added(Arrays.asList(credentials))
                .removed(Arrays.<MicroserviceCredentials>asList())
                .all(Arrays.asList(credentials))
                .build();
    }
}