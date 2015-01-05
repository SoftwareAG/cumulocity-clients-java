package com.cumulocity.smartrest.client;


public interface SmartResponse {

    boolean isSuccessful();
    
    boolean isTimeout();
    
    int getStatus();
    
    String getMessage();
    
    SmartRow[] getDataRows();
    
    SmartRow getRow(int index);
}
