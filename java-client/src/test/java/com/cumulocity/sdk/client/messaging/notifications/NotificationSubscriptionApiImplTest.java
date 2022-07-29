package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.reliable.notification.NotificationApiRepresentation;
import com.cumulocity.rest.representation.reliable.notification.NotificationSubscriptionCollectionRepresentation;
import com.cumulocity.rest.representation.reliable.notification.NotificationSubscriptionRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.UrlProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.cumulocity.sdk.client.messaging.notifications.NotificationSubscriptionApiImpl.MEDIA_TYPE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class NotificationSubscriptionApiImplTest {
    
    private RestConnector restConnector;
    private PlatformParameters platformParameters;
    private UrlProcessor urlProcessor;
    private NotificationSubscriptionApi api;
    private final NotificationApiRepresentation notificationApiRepresentation = new NotificationApiRepresentation();
    private final NotificationSubscriptionCollectionRepresentation notificationSubscriptionRepresentations =
            new NotificationSubscriptionCollectionRepresentation();
    
    private static final int DEFAULT_PAGE_SIZE = 3;
    private static final String DEFAULT_HOST = "host/";
    private static final String DEFAULT_GID_VALUE = "value";
    private static final String NOTIFICATION_COLLECTION_URL = "path_to_notification";
    
    @BeforeEach
    public void initialize() {
        restConnector = mock(RestConnector.class);
        platformParameters = mock(PlatformParameters.class);
        when(platformParameters.getHost()).thenReturn(DEFAULT_HOST);
        when(restConnector.getPlatformParameters()).thenReturn(platformParameters);
        urlProcessor = mock(UrlProcessor.class);
        notificationApiRepresentation.setNotificationSubscriptions(notificationSubscriptionRepresentations);
        notificationSubscriptionRepresentations.setSelf(NOTIFICATION_COLLECTION_URL);
        api = new NotificationSubscriptionApiImpl(restConnector, urlProcessor, notificationApiRepresentation, DEFAULT_PAGE_SIZE);
    }
    
    @Test
    public void restConnectorCannotBeNullInConstructor() {
        assertThrows(NullPointerException.class,
                () -> new NotificationSubscriptionApiImpl(null, urlProcessor, notificationApiRepresentation, DEFAULT_PAGE_SIZE));
    }
    
    @Test
    public void urlProcessorCannotBeNullInConstructor() {
        assertThrows(NullPointerException.class,
                () -> new NotificationSubscriptionApiImpl(restConnector, null, notificationApiRepresentation, DEFAULT_PAGE_SIZE));
    }
    
    @Test
    public void representationCannotBeNullInDelete() {
        assertThrows(NullPointerException.class,
                () -> api.delete(null));
    }
    
    @Test
    public void representationCannotBeNullInSubscribe() {
        assertThrows(NullPointerException.class,
                () -> api.subscribe(null));
    }
    
    @Test
    public void testDelete() {
        NotificationSubscriptionRepresentation subscription = new NotificationSubscriptionRepresentation();
        subscription.setId(new GId(DEFAULT_GID_VALUE));
        api.delete(subscription);
        verify(restConnector).delete(NOTIFICATION_COLLECTION_URL + "/" + DEFAULT_GID_VALUE);
    }
    
    @Test
    public void testSubscribe() {
        NotificationSubscriptionRepresentation subscription = new NotificationSubscriptionRepresentation();
        api.subscribe(subscription);
        verify(restConnector).post(NOTIFICATION_COLLECTION_URL, MEDIA_TYPE, subscription);
    }
}
