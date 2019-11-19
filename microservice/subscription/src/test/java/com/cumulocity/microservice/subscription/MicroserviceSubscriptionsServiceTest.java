package com.cumulocity.microservice.subscription;

import com.cumulocity.microservice.context.ContextServiceImpl;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionAddedEvent;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.MicroserviceSubscriptionsRepository;
import com.cumulocity.microservice.subscription.repository.MicroserviceSubscriptionsRepository.Subscriptions;
import com.cumulocity.microservice.subscription.service.impl.MicroserviceSubscriptionsServiceImpl;
import com.cumulocity.microservice.subscription.service.impl.MicroserviceSubscriptionsServiceImpl.MicroserviceChangedListener;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;
import java.util.concurrent.Callable;

import static com.google.common.base.Optional.of;
import static com.google.common.collect.FluentIterable.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
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

    @InjectMocks
    private MicroserviceSubscriptionsServiceImpl subscriptionsService;

    @Before
    public void before() {
        when(contextService.callWithinContext(anyObject(), any(Callable.class))).thenAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return invocationOnMock.callRealMethod();
            }
        });
    }

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
    public void getCredentialsMethodShouldReturnAllSubscribingCredentialsInListener() {
        // given
        final ApplicationRepresentation application = new ApplicationRepresentation();
        final Subscriptions subscriptions = givenSubscriptions();
        final Set<MicroserviceCredentials> subscribingCredentials = new HashSet<>();

        given(repository.register(anyString(), any(MicroserviceMetadataRepresentation.class))).willReturn(of(application));
        given(repository.retrieveSubscriptions(anyString())).willReturn(subscriptions);

        // when
        subscriptionsService.listen(MicroserviceSubscriptionAddedEvent.class, new MicroserviceChangedListener<MicroserviceSubscriptionAddedEvent>() {
            public boolean apply(MicroserviceSubscriptionAddedEvent event) {
                subscribingCredentials.add(event.getCredentials());

                from(subscribingCredentials).filter(new Predicate<MicroserviceCredentials>() {
                    @Override
                    public boolean apply(MicroserviceCredentials credentialsToVerify) {
                        final Optional<MicroserviceCredentials> credentials = subscriptionsService.getCredentials(credentialsToVerify.getTenant());
                        assertThat(credentialsToVerify).isEqualTo(credentials.get());
                        return true;
                    }
                }).toList();

                return true;
            }
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
                .removed(Collections.<MicroserviceCredentials>emptyList())
                .all(Arrays.asList(credentials1, credentials2))
                .build();
    }
}
