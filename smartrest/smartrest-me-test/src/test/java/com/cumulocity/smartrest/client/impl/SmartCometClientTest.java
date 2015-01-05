package com.cumulocity.smartrest.client.impl;

import java.io.IOException;
import java.io.OutputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.cumulocity.smartrest.client.SmartExecutorService;
import com.cumulocity.smartrest.client.SmartRequest;
import com.cumulocity.smartrest.client.SmartResponse;
import com.cumulocity.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.smartrest.client.impl.SmartCometClient;
import com.cumulocity.smartrest.client.impl.SmartHttpConnection;
import com.cumulocity.smartrest.client.impl.SmartResponseImpl;

public class SmartCometClientTest {
    
    SmartHttpConnection smartConnection;
    SmartCometClient cometClient;
    SmartExecutorService executorService;
    Evaluator evaluator;
    OutputStream output;
    SmartResponse resp;
    
    @Before
    public void mockConnection() throws IOException {   
        smartConnection = Mockito.mock(SmartHttpConnection.class);
        evaluator = new Evaluator();
        executorService = Mockito.mock(SmartExecutorService.class);
        cometClient = new SmartCometClient(smartConnection, evaluator, executorService);
    }
    
    @Test
    public void testHandshakeOnly() throws IOException {
        // setup
        resp = createMockResponse("12345");
        Mockito.when(smartConnection.executeRequest(Matchers.any(SmartRequest.class))).thenReturn(resp);
        // when
        String response = cometClient.handshake();
        // then
        Assert.assertEquals("12345", response);
    }
    
    @Test
    public void testSubscribeInclHandshake() throws IOException {
        // setup
        resp = createMockResponse("12345");
        Mockito.when(smartConnection.executeRequest(Matchers.any(SmartRequest.class))).thenReturn(resp);
        // when
        cometClient.startListenTo("mypath", new String[]{"mychannel"});
        // then
        Mockito.verify(smartConnection, Mockito.times(2)).executeRequest(Matchers.any(SmartRequest.class));
        
    }
    
    @Test
    public void testListenToAChannel() throws IOException {
        // setup
        resp = createMockResponse("12345");
        Mockito.when(smartConnection.executeRequest(Matchers.any(SmartRequest.class))).thenReturn(resp);
        cometClient = new SmartCometClient(smartConnection, evaluator);
        // when
        cometClient.startListenTo("mypath", new String[]{"mychannel"});
        // then
        Mockito.verify(smartConnection, Mockito.times(2)).executeRequest(Matchers.any(SmartRequest.class));
    }
    
    @Test
    public void testReceivingResponseWithAdvice() throws IOException {
        // setup
        resp = createMockResponse("12345");
        SmartResponse resp1 = createMockResponse("86,1,,10000,retry");
        Mockito.when(smartConnection.executeRequest(Matchers.any(SmartRequest.class))).thenReturn(resp).thenReturn(resp1);
        cometClient = new SmartCometClient(smartConnection, evaluator);
        // when
        cometClient.startListenTo("mypath", new String[]{"mychannel"});
        // then
        Mockito.verify(smartConnection, Mockito.times(2)).executeRequest(Matchers.any(SmartRequest.class));
        Assert.assertEquals(10000, cometClient.getInterval());
    }
    
    @Test
    public void testDisconnect() throws IOException {
        // setup
        resp = createMockResponse("12345");
        SmartResponse emptyResp = createMockResponse("");
        Mockito.when(smartConnection.executeRequest(Matchers.any(SmartRequest.class))).thenReturn(resp).thenReturn(emptyResp).thenReturn(emptyResp);
        // when
        cometClient.startListenTo("mypath", new String[]{"mychannel"});
        cometClient.stopListenTo();
        // then
        Mockito.verify(smartConnection, Mockito.times(3)).executeRequest(Matchers.any(SmartRequest.class));
    }
    
    private static SmartResponse createMockResponse(String body) throws IOException {
        return new SmartResponseImpl(200,"OK",body);
    }
    
    private class Evaluator implements SmartResponseEvaluator {
        
        @Override
        public void evaluate(SmartResponse response) {
        }
        
    }
}
