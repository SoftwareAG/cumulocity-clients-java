package com.cumulocity.me.sdk;

import com.cumulocity.me.smartrest.client.SmartConnection;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.impl.SmartCometDClient;
import com.cumulocity.me.smartrest.client.impl.SmartRequestAsyncRunner;
import com.cumulocity.me.smartrest.client.impl.SmartHttpConnection;
import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;
import com.cumulocity.me.util.Base64;

public class SmartPlatform {
    
    private SmartConnection connection;
    
    private SmartCometDClient cometDClient;

    public SmartPlatform(SmartConnection connection) {
        this.connection = connection;
        this.cometDClient = new SmartCometDClient(connection);
    }
    
    public SmartResponse request(final SmartRequest request) {
        SmartResponse response = connection.executeRequest(request);
        connection.closeConnection();
        return response;
    }
    
    public void requestAsync(final SmartRequest request, SmartResponseEvaluator evaluator) {
        SmartRequestAsyncRunner runner = new SmartRequestAsyncRunner(connection, request, evaluator);
        runner.start();
    }
    
    public void bootstrap() {
        // TODO
    }
    
    public String registerTemplates(String templates) {
        return connection.templateRegistration(templates);
    }
    
    public void listenToPushData(String endpoint, String[] channels) {
        this.cometDClient.listenTo(endpoint, channels);
    }
}
