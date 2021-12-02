package com.cumulocity.lpwan.payload.service;

import com.cumulocity.lpwan.codec.decoder.model.DecoderOutput;
import com.cumulocity.lpwan.devicetype.model.UplinkConfiguration;
import com.cumulocity.lpwan.mapping.model.DecodedObject;
import com.cumulocity.lpwan.mapping.model.ManagedObjectFragment;
import com.cumulocity.lpwan.mapping.model.ManagedObjectFragmentCollection;
import com.cumulocity.lpwan.mapping.model.MappingCollections;
import com.cumulocity.lpwan.payload.exception.PayloadDecodingFailedException;
import com.cumulocity.lpwan.payload.uplink.model.AlarmMapping;
import com.cumulocity.lpwan.payload.uplink.model.EventMapping;
import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmCollectionRepresentation;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjects;
import com.cumulocity.rest.representation.measurement.MeasurementCollectionRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.client.SDKException;
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
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;
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

    @Captor
    private ArgumentCaptor<ManagedObjectRepresentation> managedObjectRepresentationCaptor;

    @Captor
    private ArgumentCaptor<MeasurementCollectionRepresentation> measurementCollectionRepresentationCaptor;

    @Captor
    private ArgumentCaptor<EventRepresentation> eventRepresentationCaptor;

    @Captor
    private ArgumentCaptor<AlarmRepresentation> alarmRepresentationCaptor;

    @Captor
    private ArgumentCaptor<AlarmFilter> alarmFilterCaptor;

    @InjectMocks
    private PayloadMappingService payloadMappingService;

    private UplinkConfiguration uplinkConfiguration;

    private DecodedObject decodedObject;

    private ManagedObjectRepresentation source;

    private DecoderOutput decoderOutput = new DecoderOutput();

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

    @Test
    public void shouldTestExecuteMEAs() throws PayloadDecodingFailedException {

        setUpMeasurementProperties(decoderOutput);
        setUpEventProperties(decoderOutput);
        setUpAlarmProperties(decoderOutput);

        AlarmCollectionRepresentation alarmCollectionRepr = new AlarmCollectionRepresentation();
        alarmCollectionRepr.setAlarms(Arrays.asList(new AlarmRepresentation()));
        PagedAlarmCollectionRepresentation pagedCollection = new PagedAlarmCollectionRepresentation(alarmCollectionRepr, null);
        when(alarmCollection.get()).thenReturn(pagedCollection);
        when(alarmApi.getAlarmsByFilter(any(AlarmFilter.class))).thenReturn(alarmCollection);

        setUpAlarmTypesToClearProperties(decoderOutput);
        setUpDeviceManagedObjectToUpdate(decoderOutput);

        payloadMappingService.handleCodecServiceResponse(decoderOutput, ManagedObjects.asManagedObject(GId.asGId("12345")), anyString());

        verifyMeasurements();
        verifyEvents();
        verifyAlarm();
        verifyManagedObjectToUpdate();
        verifyAlamrTypesToClear();
    }

    @Test
    public void shouldTestMeasurementCreationThrowsException() {

        setUpMeasurementProperties(decoderOutput);
        SDKException sdkException = new SDKException(500, "TEST ERROR MESSAGE");
        doThrow(sdkException).when(measurementApi).createBulkWithoutResponse(any(MeasurementCollectionRepresentation.class));

        String deviceEui = "ABCDEF1234567";
        try {
            payloadMappingService.handleCodecServiceResponse(decoderOutput, ManagedObjects.asManagedObject(GId.asGId("12345")), deviceEui);
        } catch (PayloadDecodingFailedException e) {
            assertEquals(e.getMessage(), String.format("Unable to create measurements for device EUI '%s'", deviceEui));
            assertEquals(e.getCause().getMessage(), "TEST ERROR MESSAGE");
        }
    }

    @Test
    public void shouldTestEventCreationThrowsException() {

        setUpEventProperties(decoderOutput);
        SDKException sdkException = new SDKException(500, "TEST ERROR MESSAGE");
        doThrow(sdkException).when(eventApi).create(any(EventRepresentation.class));

        String deviceEui = "ABCDEF1234567";
        try {
            payloadMappingService.handleCodecServiceResponse(decoderOutput, ManagedObjects.asManagedObject(GId.asGId("12345")), deviceEui);
        } catch (PayloadDecodingFailedException e) {
            assertEquals(e.getMessage(), String.format("Unable to create event for device EUI '%s'", deviceEui));
            assertEquals(e.getCause().getMessage(), "TEST ERROR MESSAGE");
        }
    }

    @Test
    public void shouldTestAlarmTypeClearThrowsException() {

        setUpAlarmTypesToClearProperties(decoderOutput);
        SDKException sdkException = new SDKException(500, "TEST ERROR MESSAGE");
        doThrow(sdkException).when(alarmApi).update(any(AlarmRepresentation.class));

        String deviceEui = "ABCDEF1234567";
        try {
            AlarmCollectionRepresentation alarmCollectionRepr = new AlarmCollectionRepresentation();
            alarmCollectionRepr.setAlarms(Arrays.asList(new AlarmRepresentation()));
            PagedAlarmCollectionRepresentation pagedCollection = new PagedAlarmCollectionRepresentation(alarmCollectionRepr, null);
            when(alarmCollection.get()).thenReturn(pagedCollection);
            when(alarmApi.getAlarmsByFilter(any(AlarmFilter.class))).thenReturn(alarmCollection);
            payloadMappingService.handleCodecServiceResponse(decoderOutput, ManagedObjects.asManagedObject(GId.asGId("12345")), deviceEui);
        } catch (PayloadDecodingFailedException e) {
            assertEquals(e.getMessage(), String.format("Unable to clear alarm for device EUI '%s'", deviceEui));
            assertEquals(e.getCause().getMessage(), "TEST ERROR MESSAGE");
        }
    }

    @Test
    public void shouldTestGetAlarmFilterThrowsException() {

        setUpAlarmTypesToClearProperties(decoderOutput);
        SDKException sdkException = new SDKException(500, "TEST ERROR MESSAGE");
        doThrow(sdkException).when(alarmApi).getAlarmsByFilter(any(AlarmFilter.class));

        String deviceEui = "ABCDEF1234567";
        try {
            payloadMappingService.handleCodecServiceResponse(decoderOutput, ManagedObjects.asManagedObject(GId.asGId("12345")), deviceEui);
        } catch (PayloadDecodingFailedException e) {
            assertEquals(e.getCause().getMessage(), "TEST ERROR MESSAGE");
        }
    }

    @Test
    public void shouldTestAlarmCreationThrowsException() {

        setUpAlarmProperties(decoderOutput);
        SDKException sdkException = new SDKException(500, "TEST ERROR MESSAGE");
        doThrow(sdkException).when(alarmApi).create(any(AlarmRepresentation.class));

        String deviceEui = "ABCDEF1234567";
        try {
            payloadMappingService.handleCodecServiceResponse(decoderOutput, ManagedObjects.asManagedObject(GId.asGId("12345")), deviceEui);
        } catch (PayloadDecodingFailedException e) {
            assertEquals(e.getMessage(), String.format("Unable to create alarm for device EUI '%s'", deviceEui));
            assertEquals(e.getCause().getMessage(), "TEST ERROR MESSAGE");
        }
    }

    @Test
    public void shouldTestSetDeviceManagedObjectToUpdateThrowsException() {

        setUpDeviceManagedObjectToUpdate(decoderOutput);
        SDKException sdkException = new SDKException(500, "TEST ERROR MESSAGE");
        doThrow(sdkException).when(inventoryApi).update(any(ManagedObjectRepresentation.class));

        String deviceEui = "ABCDEF1234567";
        try {
            payloadMappingService.handleCodecServiceResponse(decoderOutput, ManagedObjects.asManagedObject(GId.asGId("12345")), deviceEui);
        } catch (PayloadDecodingFailedException e) {
            assertEquals(e.getMessage(), String.format("Unable to update the device with id '%s' and device EUI '%s'", "12345", deviceEui));
            assertEquals(e.getCause().getMessage(), "TEST ERROR MESSAGE");
        }
    }

    private void verifyAlamrTypesToClear() {
        verify(alarmApi).getAlarmsByFilter(alarmFilterCaptor.capture());
        AlarmFilter filter = alarmFilterCaptor.getValue();
        assertEquals(filter.getType(), "Type_1");
        assertEquals(filter.getSource(), "12345");
        assertEquals(filter.getStatus(), "ACTIVE");

        verify(alarmApi).update(alarmRepresentationCaptor.capture());
        AlarmRepresentation alarm = alarmRepresentationCaptor.getValue();
        assertEquals(alarm.getStatus(), "CLEARED");
    }

    private void verifyManagedObjectToUpdate() {
        verify(inventoryApi).update(managedObjectRepresentationCaptor.capture());
        ManagedObjectRepresentation managedObject = managedObjectRepresentationCaptor.getValue();
        assertEquals(managedObject.get("deviceSample"), "#sampleValue");
        assertEquals(managedObject.get("newDeviceSample"), 11);
    }

    private void verifyAlarm() {
        verify(alarmApi).create(alarmRepresentationCaptor.capture());
        AlarmRepresentation alarm = alarmRepresentationCaptor.getValue();
        assertEquals(alarm.getSource().getId().getValue(), "12345");
        assertEquals(alarm.getType(), "Type_1");
        assertEquals(alarm.getText(), "Alarm_Text");
        assertEquals(alarm.getSeverity(), "WARNING");
        assertEquals(alarm.getStatus(), CumulocityAlarmStatuses.ACTIVE.name());
        assertEquals(alarm.getDateTime(), new DateTime("2020-11-28T10:11:12.123"));
    }

    private void verifyEvents() {
        verify(eventApi).create(eventRepresentationCaptor.capture());
        EventRepresentation event = eventRepresentationCaptor.getValue();
        assertEquals(event.getSource().getId().getValue(), "12345");
        assertEquals(event.getDateTime(), new DateTime("2020-11-28T10:11:12.123"));
        assertEquals(event.getText(), "Event_Text");
        assertEquals(event.getType(), "Event_Type");
    }

    private void verifyMeasurements() {
        verify(measurementApi).createBulkWithoutResponse(measurementCollectionRepresentationCaptor.capture());
        MeasurementCollectionRepresentation measurementCollection = measurementCollectionRepresentationCaptor.getValue();
        assertEquals(measurementCollection.getMeasurements().size(), 1);
        MeasurementRepresentation measurementRepresentation = measurementCollection.getMeasurements().get(0);
        assertEquals(measurementRepresentation.getSource().getId().getValue(), "12345");
        assertEquals(measurementRepresentation.getDateTime(), new DateTime("2020-11-28T10:11:12.123"));
        assertTrue(measurementRepresentation.hasProperty("c8y_Temperature"));
        assertEquals(measurementRepresentation.getType(), "c8y_Temperature");
        Map<String, Object> seriesMap = (Map<String, Object>) measurementRepresentation.getAttrs().get("c8y_Temperature");
        Map<String, Object> valuesMap = (Map<String, Object>) seriesMap.get("T");
        assertEquals(valuesMap.get("value"), 15);
        assertEquals(valuesMap.get("unit"), "C");
    }

    private void setUpMeasurementProperties(DecoderOutput decoderOutput) {
        Map<String, Object> valuesMap = new HashMap<>();
        valuesMap.put("value", 15);
        valuesMap.put("unit", "C");

        Map<String, Object> seriesMap = new HashMap<>();
        seriesMap.put("T", valuesMap);

        MeasurementRepresentation measurement = new MeasurementRepresentation();
        measurement.setSource(ManagedObjects.asManagedObject(GId.asGId("12345")));
        measurement.setType("c8y_Temperature");
        measurement.setDateTime(new DateTime("2020-11-28T10:11:12.123"));
        measurement.setProperty("c8y_Temperature", seriesMap);
        decoderOutput.addMeasurementToCreate(measurement);
    }

    private void setUpAlarmProperties(DecoderOutput decoderOutput) {
        AlarmRepresentation alarm = new AlarmRepresentation();
        alarm.setSource(ManagedObjects.asManagedObject(GId.asGId("12345")));
        alarm.setType("Type_1");
        alarm.setText("Alarm_Text");
        alarm.setSeverity("WARNING");
        alarm.setStatus(CumulocityAlarmStatuses.ACTIVE.name());
        alarm.setDateTime(new DateTime("2020-11-28T10:11:12.123"));
        decoderOutput.addAlarmToCreate(alarm);
    }

    private void setUpAlarmTypesToClearProperties(DecoderOutput decoderOutput) {
        decoderOutput.addAlarmTypeToClear("Type_1");
    }

    private void setUpEventProperties(DecoderOutput decoderOutput) {
        EventRepresentation event = new EventRepresentation();
        event.setSource(ManagedObjects.asManagedObject(GId.asGId("12345")));
        event.setText("Event_Text");
        event.setType("Event_Type");
        event.setDateTime(new DateTime("2020-11-28T10:11:12.123"));
        decoderOutput.addEventToCreate(event);
    }

    private void setUpDeviceManagedObjectToUpdate(DecoderOutput decoderOutput) {
        ManagedObjectRepresentation managedObject = ManagedObjects.asManagedObject(GId.asGId("12345"));
        managedObject.setProperty("deviceSample", "#sampleValue");
        managedObject.setProperty("newDeviceSample", 11);

        decoderOutput.setDeviceManagedObjectToUpdate(managedObject);
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
