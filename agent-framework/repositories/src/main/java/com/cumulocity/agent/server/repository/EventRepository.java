package com.cumulocity.agent.server.repository;

import java.util.Date;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.sdk.client.Param;
import com.cumulocity.sdk.client.QueryParam;
import com.cumulocity.sdk.client.event.EventApi;
import com.cumulocity.sdk.client.event.EventFilter;
import com.cumulocity.sdk.client.event.PagedEventCollectionRepresentation;

@Component
public class EventRepository {

    private static final String REVERT_ORDER = "revert";
    
    private final EventApi api;

    @Autowired
    public EventRepository(EventApi api) {
        this.api = api;
    }

    public EventRepresentation save(EventRepresentation event) {
        if (event.getId() == null) {
            return api.create(event);
        } else {
            return api.update(event);
        }
    }
    
    public EventRepresentation findLastByTypeAndSource(String type, String source) {
        EventFilter filter = new EventFilter().byType(type).bySource(GId.asGId(source));
        filter.byCreationDate(new Date(0), new DateTime().plusDays(1).toDate());
        PagedEventCollectionRepresentation resultCollection = api.getEventsByFilter(filter).get(1, new QueryParam(new Param() {
            
            @Override
            public String getName() {
                return REVERT_ORDER;
            }
        }, Boolean.TRUE.toString()));
        return CollectionUtils.isEmpty(resultCollection.getEvents()) ? null : resultCollection.getEvents().get(0);
    }
}
