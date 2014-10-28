package com.cumulocity.agent.server.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.sdk.client.alarm.AlarmApi;

@Component
public class AlarmRepository {

    private final AlarmApi api;

    @Autowired
    public AlarmRepository(AlarmApi api) {
        this.api = api;
    }

    public AlarmRepresentation save(AlarmRepresentation alarm) {
        if (alarm.getId() == null) {
            return api.create(alarm);
        } else {
            return api.update(alarm);
        }
    }
}
