package com.cumulocity.me.smartrest.client.impl;

import com.cumulocity.me.util.StringUtils;

public class SmartRow {

    private final int messageId;
    
    private final String rowNumber;
    
    private final String[] data;

    public SmartRow(int messageId, String rowNumber, String[] data) {
        this.messageId = messageId;
        this.rowNumber = rowNumber;
        this.data = data;
    }
    
    public SmartRow(String row) {
        String[] values = StringUtils.split(row, ",");
        this.messageId = Integer.parseInt(values[0]);
        this.rowNumber = values[1];
        this.data = new String[values.length - 2];
        System.arraycopy(values, 2, this.data, 0, values.length - 2);
    }
    
    public int getMessageId() {
        return messageId;
    }

    public String getRowNumber() {
        return rowNumber;
    }

    public String[] getData() {
        return data;
    }

}
