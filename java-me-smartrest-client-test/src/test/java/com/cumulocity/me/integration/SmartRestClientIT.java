package com.cumulocity.me.integration;

import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.cumulocity.me.sdk.SmartPlatform;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.SmartResponseEvaluator;
import com.cumulocity.me.smartrest.client.impl.SmartHttpConnection;
import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;
import com.cumulocity.me.smartrest.client.impl.SmartRow;

public class SmartRestClientIT {
    
    private static String templateString =  "10,100,GET,/inventory/managedObjects/&&,,,&&,,\n" +
                                              "10,101,GET,/inventory/managedObjects/,,,,,\n" +
                                              "11,200,,,\"$.name\"\n" +
                                              "11,201,managedObjects,,\"$.name\",\"$.id\"\n" +
                                              "11,202,,,\"$.name\",\"$.id\"";
    private SmartPlatform platform;
    
    
    
    @Before
    public void createPlatform() throws IOException {
        Properties cumulocityProps = new Properties();
        cumulocityProps.load(SmartRestClientIT.class.getClassLoader().getResourceAsStream("cumulocity-test.properties"));

        SystemPropertiesOverrider p = new SystemPropertiesOverrider(cumulocityProps);
        platform =  new SmartPlatform(new SmartHttpConnection(
                p.get("cumulocity.host"),
                p.get("cumulocity.tenant"),
                p.get("cumulocity.user"),
                p.get("cumulocity.password"),
                p.get("cumulocity.xid")));
        
        platform.registerTemplates(templateString);
    }
    
    @Test
    public void test() {
        String body = "101";
        SmartRequest request = new SmartRequestImpl(body);
        SmartResponse resp = platform.request(request);
        SmartRow[] rows = resp.getDataRows();
        for (SmartRow row : rows) {
            smartRowLogger(row);
        }
    }
    
    @Test
    public void testAsync() throws InterruptedException {
        SmartResponseEvaluator eval = new SmartResponseEvaluator() {
            public void evaluate(SmartResponse response) {
                SmartRow[] rows = response.getDataRows();
                for (SmartRow row : rows) {
                    smartRowLogger(row);
                }
            }
        };
        String body = "101";
        SmartRequest request = new SmartRequestImpl(body);
        platform.requestAsync(request, eval);
        Thread.sleep(2000);
    }
    
    @Test
    public void testDevicePush() {
        platform.listenToPushData("/cep/realtime/", new String[]{"/managedobjects/*"});
        platform.unsubscribePushChannels("/cep/realtime/", new String[]{"/managedobjects/*"});
        platform.stopListentoPushData("/cep/realtime/");
    }
    
    
    
    private void smartRowLogger(SmartRow row) {
        System.out.println("message id: " + row.getMessageId() + "\n" +
                "row number: " + row.getRowNumber() + "\n" +
                "data: " + joinArray(row.getData()));
    }
    
    private String joinArray(String[] array) {
        String result = "";
        for(String part : array) {
            result = result.concat(part);
            result = result.concat(",");
        }
        return result.substring(0, result.length()-1);
    }

}
