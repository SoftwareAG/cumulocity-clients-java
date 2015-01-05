package com.cumulocity.smartrest.client;

import com.cumulocity.smartrest.util.StringUtils;

public class SmartRow {

    private final int messageId;
    
    private final int rowNumber;
    
    private final String[] data;
    
    public static SmartRow create(String row) {
        int messageId;
        int rowNumber;
        String[] data;
        String[] values = StringUtils.split(row, ",");
        if (values.length == 1) {
            if (values[0].equals("")) {
                return null;
            }
            messageId = 0;
            rowNumber = -1;
            data = values;
        } else if (values.length == 2) {
            messageId = Integer.parseInt(values[0]);
            rowNumber = -1;
            data = new String[]{values[1]};
        } else {
            messageId = Integer.parseInt(values[0]);
            if (!values[1].equals("")) {
                rowNumber = Integer.parseInt(values[1]);
            } else {
                rowNumber = -1;
            }
            data = new String[values.length - 2];
            System.arraycopy(values, 2, data, 0, values.length -2);
        }
        return new SmartRow(messageId, rowNumber, data);
    }

    public SmartRow(int messageId, int rowNumber, String[] data) {
        this.messageId = messageId;
        this.rowNumber = rowNumber;
        this.data = data;
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
    
    public String getData(int index) {
        return data[index];
    }

    public String toString() {
        String row = messageId + "," + rowNumber;
        if (data == null || data.length == 0) {
            return row;
        }
        row = row + "," + data[0];
        for (int i = 1; i < data.length; i++) {
            row = row + "," + data[i];
        }
        return row;
    }
}
