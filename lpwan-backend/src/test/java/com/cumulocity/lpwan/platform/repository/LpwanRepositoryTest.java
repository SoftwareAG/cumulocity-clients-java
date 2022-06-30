package com.cumulocity.lpwan.platform.repository;

import com.cumulocity.model.ID;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjects;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.alarm.AlarmApi;
import com.cumulocity.sdk.client.alarm.AlarmCollection;
import com.cumulocity.sdk.client.alarm.AlarmFilter;
import com.cumulocity.sdk.client.alarm.PagedAlarmCollectionRepresentation;
import com.cumulocity.sdk.client.identity.IdentityApi;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.google.common.base.Optional;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static com.cumulocity.model.event.CumulocityAlarmStatuses.ACTIVE;
import static com.cumulocity.model.event.CumulocityAlarmStatuses.CLEARED;
import static com.cumulocity.model.idtype.GId.asGId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class LpwanRepositoryTest {

    @Mock
    private IdentityApi identity;

    @Mock
    private InventoryApi inventory;

    @Mock
    private AlarmApi alarmApi;

    @InjectMocks
    private LpwanRepository lpwanRepository;

    @Test
    public void shouldTestCreateExternalId(){
        lpwanRepository.createExternalId(GId.asGId("12345"),"DummyType", "DummyIdentifier");
        ArgumentCaptor<ExternalIDRepresentation> externalIDRepresentationArgumentCaptor = ArgumentCaptor.forClass(ExternalIDRepresentation.class);
        verify(identity).create(externalIDRepresentationArgumentCaptor.capture());
        ExternalIDRepresentation externalIDRepresentation = externalIDRepresentationArgumentCaptor.getValue();
        assertEquals(GId.asGId("12345"), externalIDRepresentation.getManagedObject().getId());
        assertEquals("DummyIdentifier", externalIDRepresentation.getExternalId());
        assertEquals("DummyType", externalIDRepresentation.getType());
    }

    @Test
    public void shouldTestFindGId(){
        ExternalIDRepresentation externalId = new ExternalIDRepresentation();
        externalId.setManagedObject(ManagedObjects.asManagedObject(GId.asGId("12345")));
        when(identity.getExternalId(any(ID.class))).thenReturn(externalId);
        Optional<GId> gIdOptional = lpwanRepository.findGId("DummyType", "DummyId");
        assertTrue(gIdOptional.isPresent());
        assertEquals(GId.asGId("12345"), gIdOptional.get());
    }

    @Test
    public void shouldThrowExceptionFindGId(){
        when(identity.getExternalId(any(ID.class))).thenThrow(new SDKException(HttpStatus.NOT_FOUND.value(), "NOT FOUND"));
        Optional<GId> gIdOptional = lpwanRepository.findGId("DummyType", "DummyId");
        assertFalse(gIdOptional.isPresent());
    }

    /*@Test
    public void shouldTestCreateManagedObject(){
        lpwanRepository.createManagedObject("DummyType", "DummyName");
        ArgumentCaptor<ManagedObjectRepresentation> managedObjectRepresentationArgumentCaptor = ArgumentCaptor.forClass(ManagedObjectRepresentation.class);
        verify(inventory).create(managedObjectRepresentationArgumentCaptor.capture());
        ManagedObjectRepresentation managedObjectRepresentation = managedObjectRepresentationArgumentCaptor.getValue();
        assertEquals("DummyName",managedObjectRepresentation.getName());
        assertEquals("DummyType",managedObjectRepresentation.getType());
    }*/

    @Test
    public void shouldCreate() {
        GId sourceId = GId.asGId(456);
        when(alarmApi.create(any(AlarmRepresentation.class))).thenReturn(mock(AlarmRepresentation.class));

        lpwanRepository.createAlarm(sourceId, "CRITICAL", "type", "text");

        ArgumentCaptor<AlarmRepresentation> alarmRepresentationCaptor = ArgumentCaptor.forClass(AlarmRepresentation.class);
        verify(alarmApi).create(alarmRepresentationCaptor.capture());
        AlarmRepresentation actualAlarm = alarmRepresentationCaptor.getValue();
        AlarmRepresentation expectedAlarm = representation(sourceId, "CRITICAL", "type", "text", ACTIVE.name());

        assertEquals(expectedAlarm.getSource().getId(), actualAlarm.getSource().getId());
    }

    private AlarmRepresentation representation(GId sourceId, String critical, String type, String text, String status) {
        final ManagedObjectRepresentation source = new ManagedObjectRepresentation();
        source.setId(sourceId);

        final AlarmRepresentation alarm = new AlarmRepresentation();
        alarm.setSource(source);
        alarm.setSeverity(critical);
        alarm.setType(type);
        alarm.setText(text);
        alarm.setDateTime(new DateTime(456546));
        alarm.setStatus(status);
        return alarm;
    }

    @Test
    public void shouldCLearAlarms() {
        GId sourceId = GId.asGId(456);
        String type = "c8y_TestType";

        PagedAlarmCollectionRepresentation alarmRepresentations = mock(PagedAlarmCollectionRepresentation.class);
        AlarmCollection alarmCollection = mock(AlarmCollection.class);

        when(alarmApi.getAlarmsByFilter(any(AlarmFilter.class))).thenReturn(alarmCollection);
        AlarmRepresentation alarmRepresentation = representation(sourceId, "CRITICAL", type, "text", CLEARED.name());
        Iterable<AlarmRepresentation> iterable = Arrays.asList(alarmRepresentation);
        when(alarmCollection.get()).thenReturn(alarmRepresentations);
        when(alarmRepresentations.allPages()).thenReturn(iterable);

        lpwanRepository.clearAlarm(sourceId, type);

        verify(alarmApi).getAlarmsByFilter(any(AlarmFilter.class));
        verify(alarmApi).update(alarmRepresentation);
    }
}