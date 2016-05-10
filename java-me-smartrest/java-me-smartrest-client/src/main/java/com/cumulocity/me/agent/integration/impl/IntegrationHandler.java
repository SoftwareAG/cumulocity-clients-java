package com.cumulocity.me.agent.integration.impl;

import com.cumulocity.me.agent.AgentTemplates;
import com.cumulocity.me.agent.config.ConfigurationService;
import com.cumulocity.me.agent.config.model.ConfigurationKey;
import com.cumulocity.me.agent.provider.ExternalIdProvider;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.impl.SmartHttpConnection;
import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;
import com.cumulocity.me.smartrest.client.impl.SmartRow;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

public class IntegrationHandler {

	private static final Logger LOG = LoggerFactory.getLogger(IntegrationHandler.class);
	
    private final ExternalIdProvider idProvider;
    private final int requiredInterval;
    private final SmartHttpConnection connection;


    public IntegrationHandler(ExternalIdProvider idProvider, SmartHttpConnection connection, ConfigurationService configService) {
        this.idProvider = idProvider;
        this.requiredInterval = configService.getInt(ConfigurationKey.AGENT_MONITORING_REQUIRED_INTERVAL).intValue();
        this.connection = connection;
    }

    public String integrate(){
        // get external ID reference
        String externalId = idProvider.getExternalId();
        String externalIdType = idProvider.getExternalIdType();
        LOG.info("getting external id reference");
        SmartRequestImpl r = new SmartRequestImpl(AgentTemplates.GET_EXTERNAL_ID_REQUEST_MESSAGE_ID, externalIdType + "," + externalId);
        SmartResponse resp = connection.executeRequest(r);
        SmartRow[] rows = resp.getDataRows();
        String deviceId = null;
        if (rows.length > 0 && rows[0].getMessageId() == AgentTemplates.EXTERNAL_ID_RESPONSE_MESSAGE_ID) {
            // managed object already exists
            deviceId = rows[0].getData(0);
            LOG.info("reference found using device id " + deviceId);
        } else {
            // create managed object
        	LOG.info("no reference found, creating device");
            resp = connection.executeRequest(new SmartRequestImpl(AgentTemplates.CREATE_MANAGED_OBJECT_REQUEST_MESSAGE_ID, "J2ME_device_" + externalId + "," + requiredInterval));
            rows = resp.getDataRows();
            if (rows.length > 0 && rows[0].getMessageId() == AgentTemplates.MANAGED_OBJECT_ID_RESPONSE_MESSAGE_ID) {
                deviceId = rows[0].getData(0);
                LOG.info("device created: " + deviceId + " creating external id reference");
                // create external id for managed object
                connection.executeRequest(new SmartRequestImpl(AgentTemplates.CREATE_EXTERNAL_ID_REQUEST_MESSAGE_ID, deviceId + "," + externalId + "," + externalIdType));
                LOG.debug("external id reference created");
            }
        }
        return deviceId;
    }
}
