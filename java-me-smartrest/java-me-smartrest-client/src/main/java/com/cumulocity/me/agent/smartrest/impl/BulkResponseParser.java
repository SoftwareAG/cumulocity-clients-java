package com.cumulocity.me.agent.smartrest.impl;

import com.cumulocity.me.agent.smartrest.model.MessageId;
import com.cumulocity.me.agent.smartrest.model.SmartResponseList;
import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.smartrest.client.impl.SmartResponseImpl;
import com.cumulocity.me.smartrest.client.impl.SmartRow;
import java.util.Vector;

public class BulkResponseParser {

    private final SmartResponse original;
    private String xId;
    private int lineNumber = Integer.MIN_VALUE;
    private Vector rows = new Vector();
    private SmartResponseList smartResponseList = new SmartResponseList();

    public BulkResponseParser(SmartResponse original) {
        this.original = original;
    }

    public SmartResponseList parse() {
        SmartRow[] rows = original.getDataRows();
        for (int i = 0; i < rows.length; i++) {
            SmartRow row = rows[i];
            if (isSetXIdRow(row)) {
                handleSetXIdRow(row);
            } else {
                handleStandardRow(row);
            }
        }
        appendSingleRequestResponse();
        return smartResponseList;
    }

    public boolean isSetXIdRow(SmartRow row) {
        if (MessageId.SET_XID_RESPONSE.getValue() == row.getMessageId()) {
            return true;
        }
        return false;
    }

    public void handleSetXIdRow(SmartRow row) {
        appendSingleRequestResponse();
        xId = row.getData(0);
        lineNumber = Integer.MIN_VALUE;
    }

    public void handleStandardRow(SmartRow row) {
        if (lineNumber == row.getRowNumber()) {
            rows.addElement(row);
        } else {
            appendSingleRequestResponse();
            rows.addElement(row);
            lineNumber = row.getRowNumber();
        }
    }

    public void appendSingleRequestResponse() {
        if (!rows.isEmpty()) {
            SmartRow[] rowsArray = new SmartRow[rows.size()];
            rows.copyInto(rowsArray);
            rows = new Vector();
            smartResponseList.addElement(new SmartResponseImpl(original.getStatus(), original.getMessage(), rowsArray));
        }
    }
}
