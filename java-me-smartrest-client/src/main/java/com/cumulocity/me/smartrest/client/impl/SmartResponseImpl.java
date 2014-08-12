package com.cumulocity.me.smartrest.client.impl;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.microedition.io.HttpConnection;

import com.cumulocity.me.smartrest.client.SmartResponse;
import com.cumulocity.me.util.IOUtils;

public class SmartResponseImpl implements SmartResponse {

    private final int status;
    private final String message;
    private final SmartRow[] dataRows;

    public SmartResponseImpl(HttpConnection connection) throws IOException {
        this.status = connection.getResponseCode();
        this.message = connection.getResponseMessage();
        this.dataRows = readData(connection);
    }
    
    public boolean isSuccessful() {
        return status < 300;
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
    
    
    private SmartRow[] readData(HttpConnection connection) throws IOException {
        if (!isSuccessful()) {
            return null;
        }
        String[] responseLines = IOUtils.readData(new InputStreamReader(connection.openInputStream()));
        SmartRow[] parsedLines = new SmartRow[responseLines.length];
        for (int i = 0; i < responseLines.length; i++) {
            parsedLines[i] = new SmartRow(responseLines[i]);
        }
        return parsedLines;
    }
}
