package com.cumulocity.me.agent.smartrest.impl;

import com.cumulocity.me.agent.smartrest.model.BulkRequest;
import com.cumulocity.me.agent.smartrest.model.SmartResponseList;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.impl.SmartHttpConnection;
import com.cumulocity.me.smartrest.client.impl.SmartRow;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

import java.util.Enumeration;

public class SmartrestManager {
	private static final Logger LOG = LoggerFactory.getLogger(SmartrestManager.class);
	
    private final RequestBuffer buffer;
    private final SmartHttpConnection connection;

    public SmartrestManager(RequestBuffer buffer, SmartHttpConnection connection) {
        this.buffer = buffer;
        this.connection = connection;
    }

    public void send() {
        BulkRequest bulkRequest = buffer.extractBulkRequest();
        if (bulkRequest != null) {
            LOG.info("Sending bulk request");
            SmartRequest smartRequest = bulkRequest.buildSmartRequest();
            try {
                SmartResponse response = connection.executeRequest(smartRequest);
                LOG.debug("Bulk request sent successfully");
                buffer.acknowledgeExtracted();
                BulkResponseParser parser = new BulkResponseParser(response);
                SmartResponseList responseList = parser.parse();
                LOG.debug("bulk response parsed. length: " + responseList.size());
                Enumeration responseListEnumeration = responseList.elements();
                while (responseListEnumeration.hasMoreElements()) {
                    SmartResponse nextResponse = (SmartResponse) responseListEnumeration.nextElement();
                    bulkRequest.callEvaluator(nextResponse.getDataRows()[0].getRowNumber(), nextResponse);
                }
            } catch (SDKException e) {
                //skip & ignore
            	//resend is handled by not acknowledging extracted from buffer
            }
        }
    }
}
