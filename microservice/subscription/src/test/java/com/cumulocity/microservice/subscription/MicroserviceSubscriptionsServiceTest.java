package com.cumulocity.microservice.subscription;

import com.cumulocity.microservice.context.ContextServiceImpl;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionAddedEvent;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.MicroserviceSubscriptionsRepository;
import com.cumulocity.microservice.subscription.repository.MicroserviceSubscriptionsRepository.Subscriptions;
import com.cumulocity.microservice.subscription.service.impl.MicroserviceSubscriptionsServiceImpl;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.sdk.client.SDKException;
import java.util.Optional;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;
import java.util.concurrent.Callable;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MicroserviceSubscriptionsServiceTest {

    @Mock
    private PlatformProperties properties;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private MicroserviceSubscriptionsRepository repository;

    @Mock
    private ContextServiceImpl contextService;

    @Mock
    private MicroserviceMetadataRepresentation microserviceMetadataRepresentation;

    @InjectMocks
    private MicroserviceSubscriptionsServiceImpl subscriptionsService;

    @Before
    public void before() {
        when(contextService.callWithinContext(any(), any(Callable.class))).thenAnswer(InvocationOnMock::callRealMethod);
        given(properties.getApplicationName()).willReturn("mockApp");
    }

    @Test
    public void shouldNotUpdateCurrentSubscriptionWhenEventProcessingFails() {
        //given
        final Subscriptions subscriptions = givenSubscriptions();

        given(repository.register(anyString(), any(MicroserviceMetadataRepresentation.class))).willReturn(of(application("mockId1")));
        given(repository.retrieveSubscriptions(anyString())).willReturn(subscriptions);

        doThrow(new SDKException("mongo connection timeout")).when(eventPublisher).publishEvent(any(ApplicationEvent.class));

        //when
        subscriptionsService.subscribe();

        //then
        verify(repository).updateCurrentSubscriptions(ImmutableList.of());
    }

    @Test
    public void shouldUpdateCurrentSubscriptionOnlyWhenEventsProcessedSuccessfully() {
        //given
        final Subscriptions subscriptions = givenSubscriptions();

        given(repository.register(anyString(), any(MicroserviceMetadataRepresentation.class))).willReturn(of(application("mockId2")));
        given(repository.retrieveSubscriptions(anyString())).willReturn(subscriptions);

        //when
        subscriptionsService.subscribe();

        //then
        verify(repository).updateCurrentSubscriptions(Matchers.anyCollection());
    }

    @Test
    public void getCredentialsMethodShouldReturnAllSubscribingCredentialsInListener() {
        // given
        final Subscriptions subscriptions = givenSubscriptions();
        final Set<MicroserviceCredentials> subscribingCredentials = new HashSet<>();

        given(repository.register(anyString(), any(MicroserviceMetadataRepresentation.class))).willReturn(of(application("mockId3")));
        given(repository.retrieveSubscriptions(anyString())).willReturn(subscriptions);

        // when
        subscriptionsService.listen(MicroserviceSubscriptionAddedEvent.class, event -> {
            subscribingCredentials.add(event.getCredentials());

            for (Credentials credentialsToVerify : subscribingCredentials) {
                final Optional<MicroserviceCredentials> retrievedCredentials = subscriptionsService.getCredentials(credentialsToVerify.getTenant());
                assertThat(credentialsToVerify).isEqualTo(retrievedCredentials.get());
            }

            return true;
        });
        subscriptionsService.subscribe();

        // then
        assertThat(subscribingCredentials).containsAll(subscriptions.getAdded());
    }

    private Subscriptions givenSubscriptions() {
        final MicroserviceCredentials credentials1 = new MicroserviceCredentials().withUsername("name1").withPassword("pass1").withTenant("tenant1");
        final MicroserviceCredentials credentials2 = new MicroserviceCredentials().withUsername("name2").withPassword("pass2").withTenant("tenant2");
        return Subscriptions.builder()
                .added(Arrays.asList(credentials1, credentials2))
                .removed(Collections.emptyList())
                .all(Arrays.asList(credentials1, credentials2))
                .build();
    }

    private ApplicationRepresentation application(String id) {
        ApplicationRepresentation applicationRepresentation = new ApplicationRepresentation();
        applicationRepresentation.setId(id);
        return applicationRepresentation;
    }
}
