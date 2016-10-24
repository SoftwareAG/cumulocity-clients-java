package com.cumulocity.sdk.client.notification;

interface ConnectionListener {
    
    void onDisconnection(final int httpCode);
    
}
