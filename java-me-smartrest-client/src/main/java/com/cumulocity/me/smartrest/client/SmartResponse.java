package com.cumulocity.me.smartrest.client;

import com.cumulocity.me.smartrest.client.impl.SmartRow;

public interface SmartResponse {

    boolean isSuccessful();
    
    boolean isTimeout();
    
    int getStatus();
    
    String getMessage();
    
    SmartRow[] getDataRows();
    
    SmartRow getRow(int index);
}
