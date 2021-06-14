package com.cumulocity.sdk.client.messaging.notifications;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.reliable.notification.NotificationSubscriptionRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.UrlProcessor;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.cumulocity.sdk.client.messaging.notifications.NotificationSubscriptionApiImpl.MEDIA_TYPE;
import static com.cumulocity.sdk.client.messaging.notifications.NotificationSubscriptionApiImpl.REQUEST_URI;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotificationSubscriptionApiImplTest {
    
    private RestConnector restConnector;
    private PlatformParameters platformParameters;
    private UrlProcessor urlProcessor;
    private NotificationSubscriptionApi api;
    
    private static final int DEFAULT_PAGE_SIZE = 3;
    private static final String DEFAULT_HOST = "host/";
    private static final String DEFAULT_GID_VALUE = "value";
    
    @BeforeEach
    public void initialize() {
        restConnector = mock(RestConnector.class);
        platformParameters = mock(PlatformParameters.class);
        when(platformParameters.getHost()).thenReturn(DEFAULT_HOST);
        when(restConnector.getPlatformParameters()).thenReturn(platformParameters);
        urlProcessor = mock(UrlProcessor.class);
        api = new NotificationSubscriptionApiImpl(restConnector, urlProcessor, DEFAULT_PAGE_SIZE);
    }
    
    @Test
    public void restConnectorCannotBeNullInConstructor() {
        assertThrows(NullPointerException.class,
                () -> new NotificationSubscriptionApiImpl(null, urlProcessor, DEFAULT_PAGE_SIZE));
    }
    
    @Test
    public void urlProcessorCannotBeNullInConstructor() {
        assertThrows(NullPointerException.class,
                () -> new NotificationSubscriptionApiImpl(restConnector, null, DEFAULT_PAGE_SIZE));
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
        verify(restConnector).delete(DEFAULT_HOST + REQUEST_URI + "/" + DEFAULT_GID_VALUE);
    }
    
    @Test
    public void testSubscribe() {
        NotificationSubscriptionRepresentation subscription = new NotificationSubscriptionRepresentation();
        api.subscribe(subscription);
        verify(restConnector).post(DEFAULT_HOST + REQUEST_URI, MEDIA_TYPE, subscription);
    }
}
