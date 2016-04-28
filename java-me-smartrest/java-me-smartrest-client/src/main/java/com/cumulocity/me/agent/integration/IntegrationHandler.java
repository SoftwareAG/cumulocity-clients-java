package com.cumulocity.me.agent.integration;

import com.cumulocity.me.agent.AgentTemplates;
import com.cumulocity.me.agent.bootstrap.ExternalIdProvider;
import com.cumulocity.me.agent.config.ConfigurationKey;
import com.cumulocity.me.agent.config.ConfigurationService;
import com.cumulocity.me.agent.feature.FeatureInitializationException;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.impl.SmartHttpConnection;
import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;
import com.cumulocity.me.smartrest.client.impl.SmartRow;

public class IntegrationHandler {

    private final ExternalIdProvider idProvider;
    private final int requiredInterval;
    private final SmartHttpConnection connection;


    public IntegrationHandler(ExternalIdProvider idProvider, SmartHttpConnection connection, ConfigurationService configService) {
        this.idProvider = idProvider;
        this.requiredInterval = configService.getInt(ConfigurationKey.AGENT_MONITORING_REQUIRED_INTERVAL);
        this.connection = connection;
    }

    public String integrate() throws FeatureInitializationException{
        // get external ID reference
        String externalId = idProvider.getExternalId();
        String externalIdType = idProvider.getExternalIdType();
        System.out.println("getting external id reference");
        SmartRequestImpl r = new SmartRequestImpl(AgentTemplates.GET_EXTERNAL_ID_REQUEST_MESSAGE_ID, externalIdType + "," + externalId);
        SmartResponse resp = connection.executeRequest(r);
        SmartRow[] rows = resp.getDataRows();
        String deviceId = null;
        if (rows.length > 0 && rows[0].getMessageId() == AgentTemplates.EXTERNAL_ID_RESPONSE_MESSAGE_ID) {
            // managed object already exists
            deviceId = rows[0].getData(0);
            System.out.println("reference found using device id " + deviceId);
        } else {
            // create managed object
            System.out.println("no reference found, creating device");
            resp = connection.executeRequest(new SmartRequestImpl(AgentTemplates.CREATE_MANAGED_OBJECT_REQUEST_MESSAGE_ID, "J2ME_device_" + externalId + "," + requiredInterval));
            System.out.println("tried to create device");
            rows = resp.getDataRows();
            if (rows.length > 0 && rows[0].getMessageId() == AgentTemplates.MANAGED_OBJECT_ID_RESPONSE_MESSAGE_ID) {
                System.out.println("rows[0]: " + rows[0]);
                deviceId = rows[0].getData(0);
                System.out.println("device created: " + deviceId + " creating external id reference");
                // create external id for managed object
                connection.executeRequest(new SmartRequestImpl(AgentTemplates.CREATE_EXTERNAL_ID_REQUEST_MESSAGE_ID, deviceId + "," + externalId + "," + externalIdType));
                System.out.println("external id reference created");
            }
        }
        return deviceId;
    }
}
