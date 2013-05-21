package com.cumulocity.sdk.client.notification;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cumulocity.sdk.client.notification.Subscriber.Subscription;

@RunWith(MockitoJUnitRunner.class)
public class SubscriberImplTest {

    private static final class NullNotificationListener implements NotificationListener {
        @Override
        public void onNotification(Map<String, Object> notification) {

        }
    }

    @Mock
    BayeuxClient client;

    @Mock
    ChannelNameResolver<Object> channelNameResolver;

    @Mock
    BayeuxClientProvider bayeuxClientProvider;

    Subscriber<Object> subscriber;

    @Before
    public void setup() {
        subscriber = new SubscriberImpl("", channelNameResolver, bayeuxClientProvider);
    }

    private void mockClientProvider() {
        Mockito.when(bayeuxClientProvider.get(Mockito.anyString())).thenReturn(client);
    }

    @Test
    public final void shouldConnectSuccessfuly() {
        //Given
        mockClientProvider();
        subsciberConnected();
        //Then
        verify(client).handshake();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void shouldFailSubscribeWhenSubscriptionObjectIsNull() {
        //Given
        mockClientProvider();
        //when
        subscriber.subscribe(null, new NullNotificationListener());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public final void shouldFailSubscribeWhenNotificationListenerIsNull() {
        //Given
        mockClientProvider();
        //when
        subscriber.subscribe(new Object(), null);
    }

    @Test(expected = IllegalStateException.class)
    public final void shouldFailSubscribeWhenNotConnected() {
        //Given
        mockClientProvider();
        //when
        subscriber.subscribe(new Object(), new NullNotificationListener());
    }
    
    @Test
    public final void shouldSuccesfulySubscribeWhenConnected() {
        //Given
        final Object objectToSubscribe = new Object();
        mockClientProvider();
        subsciberConnected();
        final ClientSessionChannel channel = mockChannel(objectToSubscribe);
        //when
        subscriber.subscribe(objectToSubscribe, new NullNotificationListener());
        //Then
        verify(channel).subscribe(Mockito.any(MessageListenerAdapter.class));
    }
    
    @Test
    public final void shouldSuccesfulyUnSubscribeAfterSubscribe() {
        //Given
        final Object objectToSubscribe = new Object();
        mockClientProvider();
        subsciberConnected();
        final ClientSessionChannel channel = mockChannel(objectToSubscribe);
        //when
        final Subscription subscription = subscriber.subscribe(objectToSubscribe, new NullNotificationListener());
        subscription.unsubscribe();
        //Then
        verify(channel).subscribe(Mockito.any(MessageListenerAdapter.class));
        verify(channel).unsubscribe(Mockito.any(MessageListenerAdapter.class));
    }
    
    
    @Test(expected=IllegalStateException.class)
    public final void shouldFailDisconnectWithIllegalStateExceptionWhenNotConnected() {
        //Given
        mockClientProvider();
        //when
        subscriber.disconnect();
    }

    private ClientSessionChannel mockChannel(Object objectToSubscribe) {
        final String id = objectToSubscribe.toString();
        when(channelNameResolver.apply(objectToSubscribe)).thenReturn(id);
        ClientSessionChannel channel = Mockito.mock(ClientSessionChannel.class);
        when(client.getChannel(id)).thenReturn(channel); 
        return channel;
    }

    private void subsciberConnected() {
        subscriber.connect();
    }


   

}


