package com.cumulocity.microservice.subscription;

import com.cumulocity.microservice.context.ContextServiceImpl;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.model.MicroserviceMetadataRepresentation;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionAddedEvent;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.MicroserviceSubscriptionsRepository;
import com.cumulocity.microservice.subscription.repository.MicroserviceSubscriptionsRepository.Subscriptions;
import com.cumulocity.microservice.subscription.service.impl.MicroserviceSubscriptionsServiceImpl.MicroserviceChangedListener;
import com.cumulocity.microservice.subscription.service.impl.MicroserviceSubscriptionsServiceImpl;
import com.cumulocity.rest.representation.application.ApplicationRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

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
    public void getCredentialsMethodShouldReturnCurrentlyProcessedCredentialInListener() {
        final AtomicBoolean invoked = new AtomicBoolean(false);
        final ApplicationRepresentation application = new ApplicationRepresentation();
        final Subscriptions subscriptions = givenSubscriptions();

        given(repository.register(anyString(), any(MicroserviceMetadataRepresentation.class))).willReturn(of(application));
        given(repository.retrieveSubscriptions(anyString())).willReturn(subscriptions);
        subscriptionsService.listen(MicroserviceSubscriptionAddedEvent.class, new MicroserviceChangedListener<MicroserviceSubscriptionAddedEvent>() {
            public boolean apply(MicroserviceSubscriptionAddedEvent event) {
                assertCredentialsExists(event.getCredentials());
                invoked.set(true);
                return true;
            }
        });
        subscriptionsService.subscribe();

        assertThat(invoked.get()).isTrue();
    }

    @Test
    public void getCredentialsMethodShouldReturnPreviouslyProcessedCredentialInListener() {
        final ApplicationRepresentation application = new ApplicationRepresentation();
        final AtomicBoolean credentials1Invoked = new AtomicBoolean(false);
        final AtomicBoolean credentials2Invoked = new AtomicBoolean(false);
        final MicroserviceCredentials credentials1 = new MicroserviceCredentials().withUsername("name1").withPassword("pass1").withTenant("tenant1");
        final MicroserviceCredentials credentials2 = new MicroserviceCredentials().withUsername("name2").withPassword("pass2").withTenant("tenant2");
        final Subscriptions subscriptions = givenSubscriptions(Arrays.asList(credentials1, credentials2));

        given(repository.register(anyString(), any(MicroserviceMetadataRepresentation.class))).willReturn(of(application));
        given(repository.retrieveSubscriptions(anyString())).willReturn(subscriptions);
        subscriptionsService.listen(MicroserviceSubscriptionAddedEvent.class, new MicroserviceChangedListener<MicroserviceSubscriptionAddedEvent>() {
            public boolean apply(MicroserviceSubscriptionAddedEvent event) {
                if (credentials1Invoked.get()) {
                    assertCredentialsExists(credentials1);
                }
                if (credentials2Invoked.get()) {
                    assertCredentialsExists(credentials2);
                }

                if (credentials1.getTenant().equals(event.getCredentials().getTenant())) {
                    credentials1Invoked.set(true);
                }
                if (credentials2.getTenant().equals(event.getCredentials().getTenant())) {
                    credentials2Invoked.set(true);
                }
                return true;
            }
        });
        subscriptionsService.subscribe();

        assertThat(credentials1Invoked.get()).isTrue();
        assertThat(credentials2Invoked.get()).isTrue();
    }

    private void assertCredentialsExists(Credentials credentials) {
        final Optional<MicroserviceCredentials> retrievedCredentials = subscriptionsService.getCredentials(credentials.getTenant());
        assertThat(retrievedCredentials.get()).isEqualTo(credentials);
    }

    private Subscriptions givenSubscriptions() {
        final MicroserviceCredentials credentials = new MicroserviceCredentials().withUsername("name").withPassword("pass").withTenant("tenant");
        return givenSubscriptions(Collections.singletonList(credentials));
    }

    private Subscriptions givenSubscriptions(Collection<MicroserviceCredentials> credentials) {
        return Subscriptions.builder()
                .added(credentials)
                .removed(Collections.<MicroserviceCredentials>emptyList())
                .all(credentials)
                .build();
    }
}
