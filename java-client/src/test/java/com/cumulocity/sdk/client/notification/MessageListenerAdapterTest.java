package com.cumulocity.sdk.client.notification;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageListenerAdapterTest {

    @Mock
    SubscriptionListener<Object, Message> listenerMock;
    @Mock
    ClientSessionChannel channel;
    @Mock
    Message message;

    @Test
    public final void afterCreateMessageListenerShouldCreateCorrectSubscripsction() {
        //Given
        Object subscribedOn = new Object();
        //When
        MessageListenerAdapter<Object> adapter = new MessageListenerAdapter<Object>(listenerMock, channel, subscribedOn );
        //Then
        final Subscription<Object> subscription = adapter.getSubscription();
        assertThat(subscription).isNotNull();
        assertThat(subscription.getObject()).isNotNull().isEqualTo(subscribedOn);
    }
    
    @Test
    public final void onMessageShouldPassMessageData() {
        //Given
        Object subscribedOn = new Object();
        MessageListenerAdapter<Object> adapter = new MessageListenerAdapter<Object>(listenerMock, channel, subscribedOn );
        //When
        adapter.onMessage(channel, message);
        //Then
        verify(listenerMock).onNotification( Mockito.any(Subscription.class) ,Mockito.any(Message.class));
    }

}
