package com.cumulocity.lpwan.payload.service;

import com.cumulocity.lpwan.devicetype.model.UplinkConfiguration;
import com.cumulocity.lpwan.mapping.model.DecodedObject;
import com.cumulocity.lpwan.mapping.model.ManagedObjectFragment;
import com.cumulocity.lpwan.mapping.model.ManagedObjectFragmentCollection;
import com.cumulocity.lpwan.mapping.model.MappingCollections;
import com.cumulocity.lpwan.payload.exception.PayloadDecodingFailedException;
import com.cumulocity.lpwan.payload.uplink.model.AlarmMapping;
import com.cumulocity.lpwan.payload.uplink.model.EventMapping;
import com.cumulocity.microservice.customdecoders.api.model.DataFragmentUpdate;
import com.cumulocity.microservice.customdecoders.api.model.DecoderResult;
import com.cumulocity.microservice.customdecoders.api.model.MeasurementDto;
import com.cumulocity.microservice.customdecoders.api.model.MeasurementValueDto;
import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.model.event.CumulocitySeverities;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.measurement.MeasurementValue;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
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

    private DecoderResult decoderResult = new DecoderResult();

    @BeforeEach
    public void setup() {
        source = new ManagedObjectRepresentation();
        source.setId(new GId("18001"));
    }

    @Test
    void doGenerateNestedMap() {
        Object[][] pathAndValueTuples = {
                {"F1/S1/unit_1", "UNIT_1"},
                {"F1/S1/value_1", Integer.valueOf(111)},
                {"F1/S1", "SERIES_1 VALUE"},
                {"/F2/S2", "SERIES_2 VALUE"},
                {"F2/S2/unit_2/", "UNIT_2"},
                {"/F2/S2/value_2/", Integer.valueOf(222)}
        };

        Map<String, Object> attributesMap = new HashMap<>();
        for (Object[] oneTuple : pathAndValueTuples) {
            payloadMappingService.generateNestedMap(oneTuple[0].toString(), oneTuple[1].toString(), attributesMap);
        }

        Map<String, Object> expectedAttributesMap = new HashMap<>();
        Map<String, Object> F1 = new HashMap<>();
        expectedAttributesMap.put("F1", F1);
        Map<String, Object> S1 = new HashMap<>();
        F1.put("S1", S1);
        S1.put("unit_1", "UNIT_1");
        S1.put("value_1", Integer.valueOf(111));
        S1.put("value", "SERIES_1 VALUE");
        Map<String, Object> F2 = new HashMap<>();
        expectedAttributesMap.put("F2", F2);
        Map<String, Object> S2 = new HashMap<>();
        F2.put("S2", S2);
        S2.put("unit_2", "UNIT_2");
        S2.put("value_2", Integer.valueOf(222));
        S2.put("value", "SERIES_2 VALUE");

        assertEquals(expectedAttributesMap.toString(), attributesMap.toString());
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
    public void shouldTestExecuteMEAsWIthClearAlarm() throws PayloadDecodingFailedException {
        setUpAlarmTypesToClearProperties(decoderResult);
        setupMEAProperties();

        payloadMappingService.handleCodecServiceResponse(decoderResult, ManagedObjects.asManagedObject(GId.asGId("12345")), anyString());

        verifyAlarmTypesToClear();
        verifyMEAs();
    }

    @Test
    public void shouldTestExecuteMEAsWIthAckAlarm() throws PayloadDecodingFailedException {
        setUpAlarmTypesToAcknowledgeProperties(decoderResult);
        setupMEAProperties();

        payloadMappingService.handleCodecServiceResponse(decoderResult, ManagedObjects.asManagedObject(GId.asGId("12345")), anyString());

        verifyAlarmTypesToAck();
        verifyMEAs();
    }

    @Test
    public void shouldTestMeasurementCreationThrowsException() {

        setUpMeasurementProperties(decoderResult);
        SDKException sdkException = new SDKException(500, "TEST ERROR MESSAGE");
        doThrow(sdkException).when(measurementApi).createBulkWithoutResponse(any(MeasurementCollectionRepresentation.class));

        String deviceEui = "ABCDEF1234567";
        try {
            payloadMappingService.handleCodecServiceResponse(decoderResult, ManagedObjects.asManagedObject(GId.asGId("12345")), deviceEui);
        } catch (PayloadDecodingFailedException e) {
            assertEquals(e.getMessage(), String.format("Unable to create measurements for device EUI '%s'", deviceEui));
            assertEquals(e.getCause().getMessage(), "TEST ERROR MESSAGE");
        }
    }

    @Test
    public void shouldTestEventCreationThrowsException() {

        setUpEventProperties(decoderResult);
        SDKException sdkException = new SDKException(500, "TEST ERROR MESSAGE");
        doThrow(sdkException).when(eventApi).create(any(EventRepresentation.class));

        String deviceEui = "ABCDEF1234567";
        try {
            payloadMappingService.handleCodecServiceResponse(decoderResult, ManagedObjects.asManagedObject(GId.asGId("12345")), deviceEui);
        } catch (PayloadDecodingFailedException e) {
            assertEquals(e.getMessage(), String.format("Unable to create event for device EUI '%s'", deviceEui));
            assertEquals(e.getCause().getMessage(), "TEST ERROR MESSAGE");
        }
    }

    @Test
    public void shouldTestAlarmTypeClearThrowsException() {

        setUpAlarmTypesToClearProperties(decoderResult);
        SDKException sdkException = new SDKException(500, "TEST ERROR MESSAGE");
        doThrow(sdkException).when(alarmApi).update(any(AlarmRepresentation.class));

        String deviceEui = "ABCDEF1234567";
        try {
            AlarmCollectionRepresentation alarmCollectionRepr = new AlarmCollectionRepresentation();
            alarmCollectionRepr.setAlarms(Arrays.asList(new AlarmRepresentation()));
            PagedAlarmCollectionRepresentation pagedCollection = new PagedAlarmCollectionRepresentation(alarmCollectionRepr, null);
            when(alarmCollection.get()).thenReturn(pagedCollection);
            when(alarmApi.getAlarmsByFilter(any(AlarmFilter.class))).thenReturn(alarmCollection);
            payloadMappingService.handleCodecServiceResponse(decoderResult, ManagedObjects.asManagedObject(GId.asGId("12345")), deviceEui);
        } catch (PayloadDecodingFailedException e) {
            assertEquals(e.getMessage(), String.format("Unable to update alarm with status '%s' for device EUI '%s'", "CLEARED", deviceEui));
            assertEquals(e.getCause().getMessage(), "TEST ERROR MESSAGE");
        }
    }

    @Test
    public void shouldTestGetAlarmFilterThrowsException() {

        setUpAlarmTypesToClearProperties(decoderResult);
        SDKException sdkException = new SDKException(500, "TEST ERROR MESSAGE");
        doThrow(sdkException).when(alarmApi).getAlarmsByFilter(any(AlarmFilter.class));

        String deviceEui = "ABCDEF1234567";
        try {
            payloadMappingService.handleCodecServiceResponse(decoderResult, ManagedObjects.asManagedObject(GId.asGId("12345")), deviceEui);
        } catch (PayloadDecodingFailedException e) {
            assertEquals(e.getCause().getMessage(), "TEST ERROR MESSAGE");
        }
    }

    @Test
    public void shouldTestAlarmCreationThrowsException() {

        setUpAlarmProperties(decoderResult);
        SDKException sdkException = new SDKException(500, "TEST ERROR MESSAGE");
        doThrow(sdkException).when(alarmApi).create(any(AlarmRepresentation.class));

        String deviceEui = "ABCDEF1234567";
        try {
            payloadMappingService.handleCodecServiceResponse(decoderResult, ManagedObjects.asManagedObject(GId.asGId("12345")), deviceEui);
        } catch (PayloadDecodingFailedException e) {
            assertEquals(e.getMessage(), String.format("Unable to create alarm for device EUI '%s'", deviceEui));
            assertEquals(e.getCause().getMessage(), "TEST ERROR MESSAGE");
        }
    }

    @Test
    public void shouldTestSetDeviceManagedObjectToUpdateThrowsException() {

        setUpDeviceManagedObjectToUpdate(decoderResult);
        SDKException sdkException = new SDKException(500, "TEST ERROR MESSAGE");
        doThrow(sdkException).when(inventoryApi).update(any(ManagedObjectRepresentation.class));

        String deviceEui = "ABCDEF1234567";
        try {
            payloadMappingService.handleCodecServiceResponse(decoderResult, ManagedObjects.asManagedObject(GId.asGId("12345")), deviceEui);
        } catch (PayloadDecodingFailedException e) {
            assertEquals(e.getMessage(), String.format("Unable to update the device with id '%s' and device EUI '%s'", "12345", deviceEui));
            assertEquals(e.getCause().getMessage(), "TEST ERROR MESSAGE");
        }
    }

    private void verifyAlarmTypesToClear() {
        verify(alarmApi).getAlarmsByFilter(alarmFilterCaptor.capture());
        AlarmFilter filter = alarmFilterCaptor.getValue();
        assertEquals(filter.getType(), "Type_1");
        assertEquals(filter.getSource(), "12345");
        assertEquals(filter.getStatus(), "ACTIVE");

        verify(alarmApi).update(alarmRepresentationCaptor.capture());
        AlarmRepresentation alarm = alarmRepresentationCaptor.getValue();
        assertEquals(alarm.getStatus(), "CLEARED");
    }

    private void verifyAlarmTypesToAck() {
        verify(alarmApi).getAlarmsByFilter(alarmFilterCaptor.capture());
        AlarmFilter filter = alarmFilterCaptor.getValue();
        assertEquals(filter.getType(), "Type_2,Type_3");
        assertEquals(filter.getSource(), "12345");
        assertEquals(filter.getStatus(), "ACTIVE");

        verify(alarmApi).update(alarmRepresentationCaptor.capture());
        AlarmRepresentation alarm = alarmRepresentationCaptor.getValue();
        assertEquals(alarm.getStatus(), "ACKNOWLEDGED");
    }

    private void verifyManagedObjectToUpdate() {
        verify(inventoryApi).update(managedObjectRepresentationCaptor.capture());
        ManagedObjectRepresentation managedObject = managedObjectRepresentationCaptor.getValue();
        Map<String, String> configMap = (HashMap<String, String>) managedObject.get("c8y_Configuration");
        assertEquals(configMap.get("config"), "dummyConfigString");
        Map<String, String> intervalMap = (HashMap<String, String>) managedObject.get("c8y_RequiredAvailability");
        assertEquals(intervalMap.get("responseInterval"), 5);
    }

    private void verifyAlarm() {
        verify(alarmApi).create(alarmRepresentationCaptor.capture());
        AlarmRepresentation alarm = alarmRepresentationCaptor.getValue();
        assertEquals(alarm.getSource().getId().getValue(), "123456");
        assertEquals(alarm.getType(), "Sample_1");
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
        ManagedObjectRepresentation seriesMap = (ManagedObjectRepresentation) measurementRepresentation.getAttrs().get("c8y_Temperature");
        MeasurementValue valuesMap = (MeasurementValue) seriesMap.get("value");
        assertEquals(valuesMap.getValue(), BigDecimal.valueOf(15));
        assertEquals(valuesMap.getUnit(), "C");
    }

    private void setUpMeasurementProperties(DecoderResult decoderResult) {
        MeasurementDto measurementToAdd = new MeasurementDto();
        measurementToAdd.setType("c8y_Temperature");
        measurementToAdd.setSeries("c8y_Temperature");
        ArrayList<MeasurementValueDto> measurementValueDtos = new ArrayList<>();
        MeasurementValueDto valueDto = new MeasurementValueDto();
        valueDto.setSeriesName("value");
        valueDto.setValue(BigDecimal.valueOf(15));
        valueDto.setUnit("C");
        measurementValueDtos.add(valueDto);
        measurementToAdd.setValues(measurementValueDtos);
        measurementToAdd.setTime(new DateTime("2020-11-28T10:11:12.123"));
        decoderResult.addMeasurement(measurementToAdd);
    }

    private void setUpAlarmProperties(DecoderResult decoderResult) {
        AlarmRepresentation alarm = new AlarmRepresentation();
        alarm.setSource(ManagedObjects.asManagedObject(GId.asGId("123456")));
        alarm.setType("Sample_1");
        alarm.setSeverity(CumulocitySeverities.WARNING.name());
        alarm.setStatus(CumulocityAlarmStatuses.ACTIVE.name());
        alarm.setText("Alarm_Text");
        alarm.setDateTime(new DateTime("2020-11-28T10:11:12.123"));
        decoderResult.addAlarm(alarm, true);
    }

    private void setUpAlarmTypesToClearProperties(DecoderResult decoderResult) {
        decoderResult.addAlarmTypesToClear("Type_1");
    }

    private void setUpAlarmTypesToAcknowledgeProperties(DecoderResult decoderResult) {
        decoderResult.addAlarmTypesToAcknowledge("Type_2", "Type_3");
    }

    private void setUpEventProperties(DecoderResult decoderResult) {
        EventRepresentation event = new EventRepresentation();
        event.setSource(ManagedObjects.asManagedObject(GId.asGId("12345")));
        event.setType("Event_Type");
        event.setText("Event_Text");
        event.setDateTime(new DateTime("2020-11-28T10:11:12.123"));
        decoderResult.addEvent(event, false);
    }

    private void setUpDeviceManagedObjectToUpdate(DecoderResult decoderResult) {
        decoderResult.addDataFragment(new DataFragmentUpdate("c8y_Configuration/config", "dummyConfigString"));
        decoderResult.addDataFragment(new DataFragmentUpdate("c8y_RequiredAvailability/responseInterval", 5));
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

    private void setupMEAProperties() {
        setUpMeasurementProperties(decoderResult);
        setUpEventProperties(decoderResult);

        AlarmCollectionRepresentation alarmCollectionRepr = new AlarmCollectionRepresentation();
        alarmCollectionRepr.setAlarms(Arrays.asList(new AlarmRepresentation()));
        PagedAlarmCollectionRepresentation pagedCollection = new PagedAlarmCollectionRepresentation(alarmCollectionRepr, null);
        when(alarmCollection.get()).thenReturn(pagedCollection);
        when(alarmApi.getAlarmsByFilter(any(AlarmFilter.class))).thenReturn(alarmCollection);

        setUpAlarmProperties(decoderResult);
        setUpDeviceManagedObjectToUpdate(decoderResult);
    }

    private void verifyMEAs() {
        verifyMeasurements();
        verifyEvents();
        verifyAlarm();
        verifyManagedObjectToUpdate();
    }
}
