package com.cumulocity.me.agent.smartrest;

import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.impl.SmartHttpConnection;
import com.cumulocity.me.smartrest.client.impl.SmartRow;
import java.util.Enumeration;

public class SmartrestManager {

    private final RequestBuffer buffer;
    private final SmartHttpConnection connection;

    public SmartrestManager(RequestBuffer buffer, SmartHttpConnection connection) {
        this.buffer = buffer;
        this.connection = connection;
    }

    public void send() {
        System.out.println("Send call");
        BulkRequest bulkRequest = buffer.extractBulkRequest();
        System.out.println("tried to extract bulk request");
        if (bulkRequest != null) {
            System.out.println("bulk request not empty");
            SmartRequest smartRequest = bulkRequest.buildSmartRequest();
            System.out.println("smart request built from bulk request");
            System.out.println("------------");
            System.out.println(smartRequest.getData());
            System.out.println("------------");
            try {
                SmartResponse response = connection.executeRequest(smartRequest);
                System.out.println("request sent response received");
                buffer.acknowledgeExtracted();
                SmartRow[] rows = response.getDataRows();
                System.out.println("------------");
                for (int i = 0; i < rows.length; i++) {
                    SmartRow row = rows[i];
                    System.out.println(row.toString());
                }
                System.out.println("------------");
                BulkResponseParser parser = new BulkResponseParser(response);
                SmartResponseList responseList = parser.parse();
                System.out.println("bulk response parsed length: " + responseList.size());
                Enumeration responseListEnumeration = responseList.elements();
                while (responseListEnumeration.hasMoreElements()) {
                    SmartResponse nextResponse = (SmartResponse) responseListEnumeration.nextElement();
                    SmartRow[] innerRows = nextResponse.getDataRows();
                    System.out.println("------------");
                    for (int i = 0; i < innerRows.length; i++) {
                        SmartRow row = innerRows[i];
                        System.out.println(row.toString());
                    }
                    System.out.println("------------");
                    bulkRequest.callEvaluator(nextResponse.getDataRows()[0].getRowNumber(), nextResponse);
                }
            } catch (SDKException e) {
                //skip & ignore
            }
        }
    }
}
