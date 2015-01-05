package com.cumulocity.smartrest.client.impl;

import java.io.IOException;
import java.util.Vector;

import com.cumulocity.smartrest.client.SmartResponse;
import com.cumulocity.smartrest.client.SmartRow;
import com.cumulocity.smartrest.util.StringUtils;

public class SmartResponseImpl implements SmartResponse {

    private final int status;
    private final String message;
    private final SmartRow[] dataRows;

    public SmartResponseImpl(int status, String message, String input) throws IOException {
        this.status = status;
        this.message = message;
        if (isSuccessful()) {
            this.dataRows = readData(StringUtils.split(input, "\n"));
        } else {
            this.dataRows = null;
        }
    }
    
    public SmartResponseImpl(int status, String message, SmartRow[] dataRows) {
        this.status = status;
        this.message = message;
        this.dataRows = dataRows;
    }
    
    public SmartRow getRow(int index) {
        if (dataRows == null) {
            return null;
        }
        return dataRows[index];
    }
    
    public boolean isSuccessful() {
        return status < 300;
    }
    
    public boolean isTimeout() {
        return status == 408;
    }
    
    public int getStatus() {
        return status;
    }
    
    public String getMessage() {
        return message;
    }
            
    public SmartRow[] getDataRows() {
        return dataRows;
    }
    
    
    private SmartRow[] readData(String[] input) throws IOException {
        if (!isSuccessful()) {
            return null;
        }
        SmartRow row;
        Vector parsedLines = new Vector(input.length);
        for (int i = 0; i < input.length; i++) {
            row = SmartRow.create(input[i]);
            if (row != null) {
                parsedLines.addElement(row);
            }
        }
        SmartRow[] rows = new SmartRow[parsedLines.size()];
        for (int j = 0; j < parsedLines.size(); j++) {
            rows[j] = (SmartRow) parsedLines.elementAt(j);
        }
        return rows;
    }
}
