package com.cumulocity.me.smartrest.client.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.cumulocity.me.smartrest.client.SmartConnection;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.util.Base64;

@PrepareForTest(Connector.class)
@RunWith(PowerMockRunner.class)
public class SmartHttpConnectionTest {
    
    private static final String TEST_AUTH = "testAuth";

    private static final String TEST_XID = "testXid";

    private static final String TEST_HOST = "http://my.cumulocity.com";
    
    SmartHttpConnection smartConnection;
    HttpConnection connection;
    OutputStream output;
    InputStream input;
    
    @Before
    public void mockConnection() throws IOException {   
        PowerMockito.mockStatic(Connector.class);
        connection = Mockito.mock(HttpConnection.class);
        Mockito.when(Connector.open(Matchers.anyString())).thenReturn(connection);
    }
    
    @Test
    public void executeSimpleRequest() throws IOException {
        // setup
        input = createMockResponse("200,1,3,4,5");
        output = createMockOutputStream();
        Mockito.when(connection.openOutputStream()).thenReturn(output);
        Mockito.when(connection.openInputStream()).thenReturn(input); 
        
        // when
        smartConnection = new SmartHttpConnection(TEST_HOST, TEST_XID, TEST_AUTH);
        SmartResponse response = smartConnection.executeRequest(new SmartRequestImpl(100,"test"));
        
        // then
        SmartRow[] rows = response.getDataRows();
        Assert.assertEquals(1, rows.length);
        Assert.assertEquals(200, rows[0].getMessageId());
        Assert.assertEquals(1, rows[0].getRowNumber());
        String[] data = rows[0].getData();
        Assert.assertEquals(3, data.length);
        Assert.assertEquals("3", data[0]);
        Assert.assertEquals("4", data[1]);
        Assert.assertEquals("5", data[2]);
    }
    
    @Test
    public void executeRequestWithBulkResponse() throws IOException {
        // setup
        input = createMockResponse("200,1,3,4,5\n201,1,2");
        output = createMockOutputStream();
        Mockito.when(connection.openOutputStream()).thenReturn(output);
        Mockito.when(connection.openInputStream()).thenReturn(input); 
        
        // when
        smartConnection = new SmartHttpConnection(TEST_HOST, TEST_XID, TEST_AUTH);
        SmartResponse response = smartConnection.executeRequest(new SmartRequestImpl(100,"test"));
        
        // then
        SmartRow[] rows = response.getDataRows();
        Assert.assertEquals(2, rows.length);
        
        Assert.assertEquals(200, rows[0].getMessageId());
        Assert.assertEquals(1, rows[0].getRowNumber());
        String[] data = rows[0].getData();
        Assert.assertEquals(3, data.length);
        Assert.assertEquals("3", data[0]);
        Assert.assertEquals("4", data[1]);
        Assert.assertEquals("5", data[2]);
        
        Assert.assertEquals(201, rows[1].getMessageId());
        Assert.assertEquals(1, rows[1].getRowNumber());
        String[] data1 = rows[1].getData();
        Assert.assertEquals(1, data1.length);
        Assert.assertEquals("2", data1[0]);
    }
    
    @Test
    public void executeRequestTestHTTPResponses() throws IOException {
        // setup
        input = createMockResponse("200,1,3,4,5\n201,1,2");
        output = createMockOutputStream();
        Mockito.when(connection.openOutputStream()).thenReturn(output);
        Mockito.when(connection.openInputStream()).thenReturn(input);
        Mockito.when(connection.getResponseCode()).thenReturn(200);
        Mockito.when(connection.getResponseMessage()).thenReturn("OK");
        
        // when
        smartConnection = new SmartHttpConnection(TEST_HOST, TEST_XID, TEST_AUTH);
        SmartResponse response = smartConnection.executeRequest(new SmartRequestImpl(100,"test"));
        
        // then
        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("OK", response.getMessage());
    }
    
    @Test
    public void executeRequestWithEmptyResponseLines() throws IOException {
     // setup
        input = createMockResponse("200,1,3,4,5\n\n201,1,2");
        output = createMockOutputStream();
        Mockito.when(connection.openOutputStream()).thenReturn(output);
        Mockito.when(connection.openInputStream()).thenReturn(input); 
        
        // when
        smartConnection = new SmartHttpConnection(TEST_HOST, TEST_XID, TEST_AUTH);
        SmartResponse response = smartConnection.executeRequest(new SmartRequestImpl(100,"test"));
        
        // then
        SmartRow[] rows = response.getDataRows();
        Assert.assertEquals(2, rows.length);
        
        Assert.assertEquals(200, rows[0].getMessageId());
        Assert.assertEquals(1, rows[0].getRowNumber());
        String[] data = rows[0].getData();
        Assert.assertEquals(3, data.length);
        Assert.assertEquals("3", data[0]);
        Assert.assertEquals("4", data[1]);
        Assert.assertEquals("5", data[2]);
        
        Assert.assertEquals(201, rows[1].getMessageId());
        Assert.assertEquals(1, rows[1].getRowNumber());
        String[] data1 = rows[1].getData();
        Assert.assertEquals(1, data1.length);
        Assert.assertEquals("2", data1[0]);
    }
    
    @Test
    public void executeAsyncRequest() throws IOException {
        // setup
        input = createMockResponse("200,1,3,4,5");
        output = createMockOutputStream();
        Mockito.when(connection.openOutputStream()).thenReturn(output);
        Mockito.when(connection.openInputStream()).thenReturn(input); 
        
        // when
        smartConnection = new SmartHttpConnection(TEST_HOST, TEST_XID, TEST_AUTH);
        smartConnection.executeRequestAsync(new SmartRequestImpl(100,"test"), new SmartResponseEvaluator() {
            @Override
            public void evaluate(SmartResponse response) {
                // then
                SmartRow[] rows = response.getDataRows();
                Assert.assertEquals(1, rows.length);
                Assert.assertEquals(200, rows[0].getMessageId());
                Assert.assertEquals(1, rows[0].getRowNumber());
                String[] data = rows[0].getData();
                Assert.assertEquals(3, data.length);
                Assert.assertEquals("3", data[0]);
                Assert.assertEquals("4", data[1]);
                Assert.assertEquals("5", data[2]);
            }
        });
    }
    
    @Test
    public void executeLongPollingRequest() throws IOException {
        // setup
        input = createMockResponse("200,1,3,4,5");
        output = createMockOutputStream();
        Mockito.when(connection.openOutputStream()).thenReturn(output);
        Mockito.when(connection.openInputStream()).thenReturn(input); 
        
        // when
        smartConnection = new SmartHttpConnection(TEST_HOST, TEST_XID, TEST_AUTH);
        SmartResponse response = smartConnection.executeLongPollingRequest(new SmartRequestImpl(100,"test"));
        
        // then
        SmartRow[] rows = response.getDataRows();
        Assert.assertEquals(1, rows.length);
        Assert.assertEquals(200, rows[0].getMessageId());
        Assert.assertEquals(1, rows[0].getRowNumber());
        String[] data = rows[0].getData();
        Assert.assertEquals(3, data.length);
        Assert.assertEquals("3", data[0]);
        Assert.assertEquals("4", data[1]);
        Assert.assertEquals("5", data[2]);
    }
    
    @Test
    public void executeBootstrap() throws IOException {
        // setup
        String tenant = "mytenant";
        String username = "myusername";
        String password = "mypassword";
        input = createMockResponse(SmartConnection.BOOTSTRAP_RESPONSE_CODE + ",1,1234," + tenant + "," + username + "," + password);
        output = createMockOutputStream();
        Mockito.when(connection.openOutputStream()).thenReturn(output);
        Mockito.when(connection.openInputStream()).thenReturn(input); 
        
        // when
        smartConnection = new SmartHttpConnection(TEST_HOST, TEST_XID, TEST_AUTH);
        String response = smartConnection.bootstrap("123");
        
        // then
        Assert.assertEquals("Basic " + Base64.encode(tenant + "/" + username + ":" + password), response);
    }
    
    @Test
    public void getXIDbyRegisterNewTemplates() throws IOException {
        // setup
        String xid = "12345";
        input = createMockResponse("40,1,error");
        InputStream input2 = createMockResponse("20,1," + xid);
        output = createMockOutputStream();
        Mockito.when(connection.openOutputStream()).thenReturn(output);
        Mockito.when(connection.openInputStream()).thenReturn(input).thenReturn(input2);
        
        // when
        smartConnection = new SmartHttpConnection(TEST_HOST, TEST_XID, TEST_AUTH);
        String response = smartConnection.templateRegistration("myTempates");
        
        // then
        Assert.assertEquals(xid, response);
    }
    
    @Test
    public void getXIDfromRegisteredTemplate() throws IOException {
        // setup
        String xid = "12345";
        input = createMockResponse("20,1," + xid);
        output = createMockOutputStream();
        Mockito.when(connection.openOutputStream()).thenReturn(output);
        Mockito.when(connection.openInputStream()).thenReturn(input);
        
        // when
        smartConnection = new SmartHttpConnection(TEST_HOST, TEST_XID, TEST_AUTH);
        String response = smartConnection.templateRegistration("myTempates");
        
        // then
        Assert.assertEquals(xid, response);
    }
    
    private static ByteArrayInputStream createMockResponse(String body) {
        return new ByteArrayInputStream(body.getBytes());
    }
    
    private static OutputStream createMockOutputStream() {
        return new OutputStream() {
            
            @Override
            public void write(int b) throws IOException {
            }
        };
    }
    
}
