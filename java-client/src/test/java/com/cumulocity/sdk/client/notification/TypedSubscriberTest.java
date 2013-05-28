package com.cumulocity.sdk.client.notification;

import static org.mockito.Mockito.verify;

import org.cometd.bayeux.Message;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.cumulocity.sdk.client.SDKException;

@RunWith(MockitoJUnitRunner.class)
public class TypedSubscriberTest {

    @Mock
    Subscriber<Object, Message> subscriberMock;

    @Mock
    SubscriptionListener<Object, Object> handler;

    TypedSubscriber<Object, Object> subscriber;

    @Before
    public void setup() {
        subscriber = new TypedSubscriber<Object, Object>(subscriberMock, Object.class);
    }

    @Test
    public final void shouldDelegateSubscribe() throws SDKException {
        //Given
        final Object subscribeObject = new Object();
        //When
        subscriber.subscribe(subscribeObject, handler);
        //Then
        verify(subscriberMock).subscribe(Mockito.eq(subscribeObject), Mockito.any(SubscriptionListener.class));
    }

    @Test
    public final void shouldDelegateDisconnect() {
        //Given
        //When
        subscriber.disconnect();
        //Then
        verify(subscriberMock).disconnect();
    }
    
    @Test 
    public void shouldCastDataFieldFromNotificationMessage(){
        
    }
    
    
    
    

}
