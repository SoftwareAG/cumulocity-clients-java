package com.cumulocity.lpwan.lns.connection.event;

import org.springframework.context.ApplicationEvent;

public class LnsConnectionUpdateEvent extends ApplicationEvent {

    public LnsConnectionUpdateEvent(String lnsConnectionName) {
        super(lnsConnectionName);
    }
    
}
