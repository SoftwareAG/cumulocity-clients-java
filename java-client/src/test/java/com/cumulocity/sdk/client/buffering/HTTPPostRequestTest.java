package com.cumulocity.sdk.client.buffering;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmMediaType;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.builder.AlarmRepresentationBuilder;
import com.cumulocity.rest.representation.builder.EventRepresentationBuilder;
import com.cumulocity.rest.representation.event.EventMediaType;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;

public class HTTPPostRequestTest {

    @Test
    public void shouldParseEventRepresentationRequest() {
        String csvString = new HTTPPostRequest("http://test.cumulocity.com/event/events", EventMediaType.EVENT, eventRepresentation())
                .toCsvString();
        
        assertThat(csvString).isEqualTo("http://test.cumulocity.com/event/events##E##{\"source\":{\"id\":\"1\"},\"text\":\"text#\"}##E");
    }
    
    @Test
    public void shouldParseAlarmRepresentationRequest() {
        String csvString = new HTTPPostRequest("http://test.cumulocity.com/alarm/alarms", AlarmMediaType.ALARM, alarmRepresentation())
                .toCsvString();
        
        assertThat(csvString).isEqualTo("http://test.cumulocity.com/alarm/alarms##A##{\"source\":{\"id\":\"1\"},\"text\":\"text#\"}##A");
    }
    
    @Test
    public void shouldParseCsvStringToAlarmRepresentation() {
        String csvString = "http://test.cumulocity.com/alarm/alarms##A##{\"source\":{\"id\":\"1\"},\"text\":\"text#\"}##A";
        
        HTTPPostRequest request = HTTPPostRequest.parseCsvString(csvString);
        
        assertThat(request.getPath()).isEqualTo("http://test.cumulocity.com/alarm/alarms");
        assertThat(request.getMediaType()).isEqualTo(AlarmMediaType.ALARM);
        assertThat(request.getRepresentation().getClass()).isEqualTo(AlarmRepresentation.class);
        assertThat(((AlarmRepresentation)request.getRepresentation()).getText()).isEqualTo("text#");
    }
    
    @Test
    public void shouldParseCsvStringToEventRepresentation() {
        String csvString = "http://test.cumulocity.com/event/events##E##{\"source\":{\"id\":\"1\"},\"text\":\"text#\"}##E";
        
        HTTPPostRequest request = HTTPPostRequest.parseCsvString(csvString);
        
        assertThat(request.getPath()).isEqualTo("http://test.cumulocity.com/event/events");
        assertThat(request.getMediaType()).isEqualTo(EventMediaType.EVENT);
        assertThat(request.getRepresentation().getClass()).isEqualTo(EventRepresentation.class);
        assertThat(((EventRepresentation)request.getRepresentation()).getText()).isEqualTo("text#");
    }

    private EventRepresentation eventRepresentation() {
        return new EventRepresentationBuilder()
            .withText("text#")
            .withSource(source())
            .build();
    }
    
    private AlarmRepresentation alarmRepresentation() {
        return new AlarmRepresentationBuilder()
            .withText("text#")
            .withSource(source())
            .build();
    }

    private ManagedObjectRepresentation source() {
        ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        source.setId(new GId("1"));
        return source;
    }
}
