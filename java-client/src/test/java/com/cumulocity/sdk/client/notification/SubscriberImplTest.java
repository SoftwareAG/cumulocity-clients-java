package com.cumulocity.sdk.client.notification;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.cometd.bayeux.ChannelId;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSession;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.client.ClientSessionChannel.MessageListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cumulocity.sdk.client.SDKException;

@RunWith(MockitoJUnitRunner.class)
public class SubscriberImplTest {

    @Mock
    ClientSession client;

    @Mock
    ClientSessionChannel metaSubscribeChannel;

    @Mock
    SubscriptionNameResolver<Object> subscriptionNameResolver;

    @Mock
    BayeuxSessionProvider bayeuxSessionProvider;

    Subscriber<Object, Message> subscriber;

    @Mock
    SubscriptionListener<Object, Message> listener;

    @Before
    public void setup() throws SDKException {
        subscriber = new SubscriberImpl<Object>(subscriptionNameResolver, bayeuxSessionProvider);
        mockClientProvider();
        when(client.getChannel(ClientSessionChannel.META_SUBSCRIBE)).thenReturn(metaSubscribeChannel);
    }

    private void mockClientProvider() throws SDKException {
        Mockito.when(bayeuxSessionProvider.get()).thenReturn(client);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void shouldFailSubscribeWhenSubscriptionObjectIsNull() throws SDKException {
        //Given
        //When
        subscriber.subscribe(null, listener);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void shouldFailSubscribeWhenNotificationListenerIsNull() throws SDKException {
        //Given
        //When
        subscriber.subscribe(new Object(), null);
    }

    @Test(expected = IllegalStateException.class)
    public final void shouldFailSubscribeWhenNotConnected() throws SDKException {
        //Given
        //When
        subscriber.subscribe(new Object(), listener);
    }

    @Test
    public final void shouldSuccesfulySubscribeWhenConnected() throws SDKException {
        //Given
        final String channelId = "/channel";
        final ClientSessionChannel channel = mockChannel(channelId);
        //When
        subscriber.subscribe(channelId, listener);
        //Then
        verify(channel).subscribe(Mockito.any(MessageListenerAdapter.class));
    }

    
    public final void shouldFailDisconnectNotThrowExcpetionWhenNotConnected() {
        //Given
        //When
        subscriber.disconnect();
        //Then
    }

    @Test
    public final void shouldSuccesfulySubscribeAndMakeHandshake() throws SDKException {
        //Given
        final String channelId = "/channel";
        final ClientSessionChannel channel = mockChannel(channelId);
        //When
        subscriber.subscribe(channelId, listener);
        verifyConnected();
        verifySubscribed(channel);
    }

    @Test
    public final void shouldSuccesfulyUnSubscribeAfterSubscribe() throws SDKException {
        //Given
        final String channelId = "/channel";
        final ClientSessionChannel channel = mockChannel(channelId);
        //When
        final Subscription<Object> subscription = subscriber.subscribe(channelId, listener);
        subscription.unsubscribe();
        //Then
        verifyConnected();
        verifySubscribed(channel);
        verifyUnsubscribe(channel);
    }
    
    
    

    @Test
    public final void shouldSuccesfulyDisconnectOnStop() throws SDKException {
        //Given
        subscriberConnected();
        //When
        subscriber.disconnect();
        //Then
        verifyDisconnected();
    }
    
    
    @Test
    public final void shouldNotifySubscriberAboutSubscribeOperationFail() throws SDKException {
        //Given
        final String channelId = "/channel";
        final ClientSessionChannel channel = mockChannel(channelId);
        final ArgumentCaptor<MessageListener> listenerCaptor = ArgumentCaptor.forClass(MessageListener.class);
      
        //When
        final Subscription<Object> subscription = subscriber.subscribe(channelId, listener);
        verify(metaSubscribeChannel).addListener(listenerCaptor.capture());
        listenerCaptor.getValue().onMessage(metaSubscribeChannel, mockUnsuccessfulSubscribeMessge(channelId.toString()));
        //Then
        verifyConnected();
        verifySubscribed(channel);
        
        verify(listener).onError(Mockito.eq(subscription),Mockito.any(SDKException.class));
    }

    private Message mockUnsuccessfulSubscribeMessge(String channelID) {
        Message message = mock(Message.class);
        when(message.get(Message.SUBSCRIPTION_FIELD)).thenReturn(channelID);
        when(message.isSuccessful()).thenReturn(false);
        return message;
    }
    
    private void subscriberConnected() throws SDKException {
        final String channelId = "/channel";
        final ClientSessionChannel channel = mockChannel(channelId);
        final Subscription<Object> subscription = subscriber.subscribe(channelId, listener);
    }

    private void verifyUnsubscribe(final ClientSessionChannel channel) {
        verify(channel).unsubscribe(Mockito.any(MessageListenerAdapter.class));
    }

    private void verifySubscribed(final ClientSessionChannel channel) {
        verify(channel).subscribe(Mockito.any(MessageListenerAdapter.class));
    }

    private void verifyDisconnected() {
        verify(client).disconnect();
    }

    private void verifyConnected() throws SDKException {
        verify(bayeuxSessionProvider).get();
    }

    private ClientSessionChannel mockChannel(String channelId) {
        when(subscriptionNameResolver.apply(channelId)).thenReturn(channelId);
        ClientSessionChannel channel = Mockito.mock(ClientSessionChannel.class);
        when(client.getChannel(channelId)).thenReturn(channel);
        when(channel.getId()).thenReturn(channelId);
        return channel;
    }

}
