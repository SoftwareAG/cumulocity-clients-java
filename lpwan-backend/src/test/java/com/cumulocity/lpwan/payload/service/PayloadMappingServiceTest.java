package com.cumulocity.lpwan.payload.service;

import com.cumulocity.lpwan.devicetype.model.UplinkConfiguration;
import com.cumulocity.lpwan.mapping.model.DecodedObject;
import com.cumulocity.lpwan.mapping.model.ManagedObjectFragment;
import com.cumulocity.lpwan.mapping.model.ManagedObjectFragmentCollection;
import com.cumulocity.lpwan.mapping.model.MappingCollections;
import com.cumulocity.lpwan.payload.uplink.model.AlarmMapping;
import com.cumulocity.lpwan.payload.uplink.model.EventMapping;
import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmCollectionRepresentation;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.alarm.AlarmApi;
import com.cumulocity.sdk.client.alarm.AlarmCollection;
import com.cumulocity.sdk.client.alarm.AlarmFilter;
import com.cumulocity.sdk.client.alarm.PagedAlarmCollectionRepresentation;
import com.cumulocity.sdk.client.event.EventApi;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.measurement.MeasurementApi;
import com.google.common.collect.ImmutableMap;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PayloadMappingServiceTest {

    @Mock
    private MeasurementApi measurementApi;

    @Mock
    private AlarmApi alarmApi;

    @Mock
    private AlarmCollection alarmCollection;

    @Mock
    private EventApi eventApi;

    @Mock
    private InventoryApi inventoryApi;

    @InjectMocks
    private PayloadMappingService payloadMappingService;

    private UplinkConfiguration uplinkConfiguration;

    private DecodedObject decodedObject;

    private ManagedObjectRepresentation source;

    @BeforeEach
    public void setup() {
        source = new ManagedObjectRepresentation();
        source.setId(new GId("18001"));
    }

    @Test
    public void shouldCreateCumulocityDataWithDecodedValue() {
        uplinkConfiguration = getUplinkConfigurationWithEvent("aType", "aFragment", null);
        decodedObject = new DecodedObject();
        decodedObject.putValue("dummyValue");
        MappingCollections mappingCollections = new MappingCollections();

        payloadMappingService.addMappingsToCollection(mappingCollections, decodedObject, uplinkConfiguration);
        payloadMappingService.executeMappings(mappingCollections, source, new DateTime());

        ArgumentCaptor<EventRepresentation> eventCaptor = ArgumentCaptor.forClass(EventRepresentation.class);
        verify(eventApi).create(eventCaptor.capture());

        EventRepresentation actualEvent = eventCaptor.getValue();
        assertNotNull(actualEvent.getProperty("aFragment"));
        assertEquals("dummyValue", (String) actualEvent.getProperty("aFragment"));
    }

    @Test
    public void shouldCreateCumulocityDataWithDecodedObject() {
        uplinkConfiguration = getUplinkConfigurationWithEvent("aType", "aFragment", null);
        decodedObject = new DecodedObject();
        decodedObject.putValue("dummyValue");
        decodedObject.putUnit("dummyUnit");
        MappingCollections mappingCollections = new MappingCollections();

        payloadMappingService.addMappingsToCollection(mappingCollections, decodedObject, uplinkConfiguration);
        payloadMappingService.executeMappings(mappingCollections, source, new DateTime());

        ArgumentCaptor<EventRepresentation> eventCaptor = ArgumentCaptor.forClass(EventRepresentation.class);
        verify(eventApi).create(eventCaptor.capture());

        EventRepresentation actualEvent = eventCaptor.getValue();
        assertNotNull(actualEvent.getProperty("aFragment"));
        Map<String, Object> actualFragment = (Map<String, Object>) actualEvent.getProperty("aFragment");
        assertEquals("dummyValue", actualFragment.get("value"));
        assertEquals("dummyUnit", actualFragment.get("unit"));
    }

    @Test
    public void shouldCreateCumulocityDataWithInnerFragment() {
        uplinkConfiguration = getUplinkConfigurationWithEvent("aType", "aFragment", "aProperty");
        decodedObject = new DecodedObject();
        decodedObject.putValue("dummyValue");
        MappingCollections mappingCollections = new MappingCollections();

        payloadMappingService.addMappingsToCollection(mappingCollections, decodedObject, uplinkConfiguration);
        payloadMappingService.executeMappings(mappingCollections, source, new DateTime());

        ArgumentCaptor<EventRepresentation> eventCaptor = ArgumentCaptor.forClass(EventRepresentation.class);
        verify(eventApi).create(eventCaptor.capture());

        EventRepresentation actualEvent = eventCaptor.getValue();
        Map<String, Object> fragment = (Map<String, Object>) actualEvent.getProperty("aFragment");
        assertNotNull(fragment.get("aProperty"));
    }

    @Test
    public void shouldCreateCumulocityDataWithMultInnerFragment() {
        uplinkConfiguration = getUplinkConfigurationWithEvent("aType", "aFragment", "aProperty");
        decodedObject = new DecodedObject();
        decodedObject.putValue("dummyValue");
        MappingCollections mappingCollections = new MappingCollections();
        UplinkConfiguration uplinkConfiguration2 = getUplinkConfigurationWithEvent("aType", "aFragment", "aProperty2");

        payloadMappingService.addMappingsToCollection(mappingCollections, decodedObject, uplinkConfiguration);
        payloadMappingService.addMappingsToCollection(mappingCollections, decodedObject, uplinkConfiguration2);
        payloadMappingService.executeMappings(mappingCollections, source, new DateTime());

        ArgumentCaptor<EventRepresentation> eventCaptor = ArgumentCaptor.forClass(EventRepresentation.class);
        verify(eventApi, times(1)).create(eventCaptor.capture());

        EventRepresentation actualEvent = eventCaptor.getValue();
        Map<String, Object> actualFragment = (Map<String, Object>) actualEvent.getProperty("aFragment");
        assertNotNull(actualFragment.get("aProperty"));
        assertNotNull(actualFragment.get("aProperty2"));
        assertEquals("dummyValue", (String) actualFragment.get("aProperty"));
    }

    @Test
    public void shouldCreateAlarmWhenValueIdNonZero() {
        UplinkConfiguration uplinkConfiguration = new UplinkConfiguration();
        uplinkConfiguration.setAlarmMapping(new AlarmMapping());

        decodedObject = new DecodedObject();
        decodedObject.putValue((double) 9);

        MappingCollections mappingCollections = new MappingCollections();
        payloadMappingService.addMappingsToCollection(mappingCollections, decodedObject, uplinkConfiguration);
        payloadMappingService.executeMappings(mappingCollections, source, new DateTime());

        ArgumentCaptor<AlarmRepresentation> alarmCaptor = ArgumentCaptor.forClass(AlarmRepresentation.class);
        verify(alarmApi).create(alarmCaptor.capture());
        AlarmRepresentation alarm = alarmCaptor.getValue();
        assertEquals(CumulocityAlarmStatuses.ACTIVE.name(), alarm.getStatus());
    }

    @Test
    public void shouldClearAlarmsWhenValueIsZero() {
        UplinkConfiguration uplinkConfiguration = new UplinkConfiguration();
        uplinkConfiguration.setAlarmMapping(new AlarmMapping());

        decodedObject = new DecodedObject();
        decodedObject.putValue((double) 0);

        AlarmCollectionRepresentation alarmCollectionRepr = new AlarmCollectionRepresentation();
        alarmCollectionRepr.setAlarms(Arrays.asList(new AlarmRepresentation()));
        PagedAlarmCollectionRepresentation pagedCollection = new PagedAlarmCollectionRepresentation(alarmCollectionRepr, null);
        when(alarmCollection.get(any(Integer.class))).thenReturn(pagedCollection);
        when(alarmApi.getAlarmsByFilter(any(AlarmFilter.class))).thenReturn(alarmCollection);

        MappingCollections mappingCollections = new MappingCollections();
        payloadMappingService.addMappingsToCollection(mappingCollections, decodedObject, uplinkConfiguration);
        payloadMappingService.executeMappings(mappingCollections, source, new DateTime());

        ArgumentCaptor<AlarmRepresentation> alarmCaptor = ArgumentCaptor.forClass(AlarmRepresentation.class);
        verify(alarmApi).update(alarmCaptor.capture());
        AlarmRepresentation alarm = alarmCaptor.getValue();
        assertEquals(CumulocityAlarmStatuses.CLEARED.name(), alarm.getStatus());
    }

    @Test
    public void shouldPartiallyUpdateManagedObject() {
        UplinkConfiguration uplinkConfiguration = new UplinkConfiguration();
        uplinkConfiguration.setAlarmMapping(new AlarmMapping());

        decodedObject = new DecodedObject();
        decodedObject.putValue((double) 0);

        AlarmCollectionRepresentation alarmCollectionRepr = new AlarmCollectionRepresentation();
        alarmCollectionRepr.setAlarms(Arrays.asList(new AlarmRepresentation()));
        PagedAlarmCollectionRepresentation pagedCollection = new PagedAlarmCollectionRepresentation(alarmCollectionRepr, null);
        when(alarmCollection.get(any(Integer.class))).thenReturn(pagedCollection);
        when(alarmApi.getAlarmsByFilter(any(AlarmFilter.class))).thenReturn(alarmCollection);

        MappingCollections mappingCollections = new MappingCollections();
        ManagedObjectFragmentCollection fragmentCollection = new ManagedObjectFragmentCollection();
        ManagedObjectFragment fragment = new ManagedObjectFragment();
        Object innerField = ImmutableMap.of("innerKey", "innerValue");
        fragment.setInnerField(innerField);
        fragment.setFragmentType("markerType");
        fragmentCollection.put("myKey", fragment);
        mappingCollections.setManagedObjectFragments(fragmentCollection);

        source.setLastUpdatedDateTime(DateTime.now());

        payloadMappingService.addMappingsToCollection(mappingCollections, decodedObject, uplinkConfiguration);
        payloadMappingService.executeMappings(mappingCollections, source, new DateTime());

        verify(inventoryApi).update(argThat(moThat(mo -> mo.getLastUpdatedDateTime() == null
                && innerField.equals(mo.get(fragment.getFragmentType())))));

        Map<String, Object> innerObject = ImmutableMap.of("innerObjectKey", ImmutableMap.of("innerObjectSubKey", "someValue"));
        fragment.setInnerObject(innerObject);
        fragment.setInnerField(null);
        reset(inventoryApi);

        payloadMappingService.addMappingsToCollection(mappingCollections, decodedObject, uplinkConfiguration);
        payloadMappingService.executeMappings(mappingCollections, source, new DateTime());

        verify(inventoryApi).update(argThat(moThat(mo -> mo.getLastUpdatedDateTime() == null
                && innerObject.equals(mo.get(fragment.getFragmentType())))));
    }

    private ArgumentMatcher<ManagedObjectRepresentation> moThat(Predicate<ManagedObjectRepresentation> moPredicate) {
        return mo -> {
            boolean matches = source.getId().equals(mo.getId());
            return matches && moPredicate.test(mo);
        };
    }

    private UplinkConfiguration getUplinkConfigurationWithEvent(String type, String fragment, String innerType) {
        UplinkConfiguration uplinkConfiguration = new UplinkConfiguration();
        EventMapping eventMapping = new EventMapping();
        eventMapping.setType(type);
        eventMapping.setFragmentType(fragment);
        if (innerType != null) {
            eventMapping.setInnerType(innerType);
        }
        uplinkConfiguration.setEventMapping(eventMapping);
        return uplinkConfiguration;
    }
}
