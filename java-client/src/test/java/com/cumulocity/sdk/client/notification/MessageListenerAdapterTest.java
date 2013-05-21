package com.cumulocity.sdk.client.notification;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageListenerAdapterTest {

    @Mock
    NotificationListener listenerMock;
    @Mock
    ClientSessionChannel channel;
    @Mock
    Message message;
    
    
    MessageListenerAdapter adapter;

    @Before
    public void setUp() {
        adapter = new MessageListenerAdapter(listenerMock);
    }

    @Test
    public final void onMessageShouldPassMessageDataAsMap() {
        //Given
        //When
        adapter.onMessage(channel, message);
        //Then
        verify(listenerMock).onNotification(Mockito.anyMap());
        verify(message).getDataAsMap();
    }

}
