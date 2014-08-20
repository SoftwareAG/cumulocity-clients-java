package com.cumulocity.me.smartrest.client.impl;

import com.cumulocity.me.util.StringUtils;

public class SmartRow {

    private final int messageId;
    
    private final int rowNumber;
    
    private final String[] data;

    public SmartRow(int messageId, int rowNumber, String[] data) {
        this.messageId = messageId;
        this.rowNumber = rowNumber;
        this.data = data;
    }
    
    public SmartRow(String row) {
        String[] values = StringUtils.split(row, ",");
        if (values.length == 1) {
            this.messageId = 0;
            this.rowNumber = 1;
            this.data = values;
        } else if (values.length == 2) {
            this.messageId = Integer.parseInt(values[0]);
            this.rowNumber = 1;
            this.data = new String[]{values[1]};
        } else {
            this.messageId = Integer.parseInt(values[0]);
            this.rowNumber = Integer.parseInt(values[1]);
            this.data = new String[values.length -2];
            System.arraycopy(values, 2, this.data, 0, values.length -2);
        }
    }
    
    public int getMessageId() {
        return messageId;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public String[] getData() {
        return data;
    }

}
