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
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;
import java.util.concurrent.Callable;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MicroserviceSubscriptionsServiceTest {

    @Mock
    private PlatformProperties properties;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private MicroserviceSubscriptionsRepository repository;

    @Mock
    private ContextServiceImpl<MicroserviceCredentials> contextService;

    @Mock
    private MicroserviceMetadataRepresentation microserviceMetadataRepresentation;

    @InjectMocks
    private MicroserviceSubscriptionsServiceImpl subscriptionsService;

    @BeforeEach
    public void before() {
        given(contextService.callWithinContext(any(), any(Callable.class))).willCallRealMethod();
        given(properties.getApplicationName()).willReturn("mockApp");
    }

    @Test
    public void shouldNotUpdateCurrentSubscriptionWhenEventProcessingFails() {
        //given
        given(repository.register(anyString(), any(MicroserviceMetadataRepresentation.class))).willReturn(of(application("mockId1")));
        given(repository.retrieveSubscriptions(anyString())).willReturn(givenSubscriptions());

        doThrow(new SDKException("mongo connection timeout"))
                .when(eventPublisher).publishEvent(any(ApplicationEvent.class));

        //when
        subscriptionsService.subscribe();

        //then
        verify(repository).updateCurrentSubscriptions(ImmutableList.of());
    }

    @Test
    public void shouldUpdateCurrentSubscriptionOnlyWhenEventsProcessedSuccessfully() {
        //given
        given(repository.register(anyString(), any(MicroserviceMetadataRepresentation.class))).willReturn(of(application("mockId2")));
        given(repository.retrieveSubscriptions(anyString())).willReturn(givenSubscriptions());

        //when
        subscriptionsService.subscribe();

        //then
        verify(repository).updateCurrentSubscriptions(anyCollection());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void shouldUpdateCurrentSubscriptionBasedOnPropertiesWhenPerTenantMicroservice() {
        //given
        given(repository.register(anyString(), any(MicroserviceMetadataRepresentation.class))).willReturn(of(application("mockId2")));
        MicroserviceCredentials microserviceUser = MicroserviceCredentials.builder()
                .username("app_service")
                .password("app_service_password")
                .appKey("app-key")
                .tenant("tenant")
                .build();
        given(properties.getIsolation()).willReturn(PlatformProperties.IsolationLevel.PER_TENANT);
        given(properties.getMicroserviceUser()).willReturn(microserviceUser);
        given(repository.diffWithCurrentSubscriptions(anyList())).willAnswer(i -> Subscriptions.builder()
                .added(new ArrayList<>((Collection<MicroserviceCredentials>) i.getArguments()[0]))
                .all(new ArrayList<>((Collection<MicroserviceCredentials>) i.getArguments()[0]))
                .removed(new ArrayList<>())
                .build());
        ArgumentCaptor<Collection<MicroserviceCredentials>> subscriptionsCaptor = ArgumentCaptor.forClass((Class) Collection.class);

        //when
        subscriptionsService.subscribe();

        //then
        verify(repository).updateCurrentSubscriptions(subscriptionsCaptor.capture());
        assertThat(subscriptionsCaptor.getValue().size()).isEqualTo(1);
        assertThat(subscriptionsCaptor.getValue().iterator().next()).isEqualTo(microserviceUser);
    }

    @Test
    public void getCredentialsMethodShouldReturnAllSubscribingCredentialsInListener() {
        // given
        final Subscriptions subscriptions = givenSubscriptions();
        final Set<MicroserviceCredentials> subscribingCredentials = new HashSet<>();

        given(repository.register(anyString(), any(MicroserviceMetadataRepresentation.class))).willReturn(of(application("mockId3")));
        given(repository.retrieveSubscriptions(any())).willReturn(subscriptions);

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
        final MicroserviceCredentials credentials1 = new MicroserviceCredentials().withUsername("name1").withPassword("pass1").withAppKey("app-key").withTenant("tenant1");
        final MicroserviceCredentials credentials2 = new MicroserviceCredentials().withUsername("name2").withPassword("pass2").withAppKey("app-key").withTenant("tenant2");
        return Subscriptions.builder()
                .added(Arrays.asList(credentials1, credentials2))
                .removed(Collections.emptyList())
                .all(Arrays.asList(credentials1, credentials2))
                .build();
    }

    private ApplicationRepresentation application(String id) {
        ApplicationRepresentation applicationRepresentation = new ApplicationRepresentation();
        applicationRepresentation.setId(id);
        applicationRepresentation.setKey("app-key");
        return applicationRepresentation;
    }
}
