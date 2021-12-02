package com.cumulocity.lpwan.payload.service;

import com.cumulocity.device.codec.decoder.model.DecoderOutput;
import com.cumulocity.lpwan.devicetype.model.UplinkConfiguration;
import com.cumulocity.lpwan.mapping.model.*;
import com.cumulocity.lpwan.payload.exception.PayloadDecodingFailedException;
import com.cumulocity.lpwan.payload.uplink.model.AlarmMapping;
import com.cumulocity.lpwan.payload.uplink.model.EventMapping;
import com.cumulocity.lpwan.payload.uplink.model.ManagedObjectMapping;
import com.cumulocity.lpwan.payload.uplink.model.MeasurementMapping;
import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementCollectionRepresentation;
import com.cumulocity.rest.representation.measurement.MeasurementRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.alarm.AlarmApi;
import com.cumulocity.sdk.client.alarm.AlarmCollection;
import com.cumulocity.sdk.client.alarm.AlarmFilter;
import com.cumulocity.sdk.client.event.EventApi;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.measurement.MeasurementApi;
import com.google.common.collect.FluentIterable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.cumulocity.model.event.CumulocityAlarmStatuses.ACTIVE;
import static com.cumulocity.model.event.CumulocityAlarmStatuses.CLEARED;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PayloadMappingService {

    @Autowired
    private MeasurementApi measurementApi;
    @Autowired
    private AlarmApi alarmApi;
    @Autowired
    private EventApi eventApi;
    @Autowired
    private InventoryApi inventoryApi;


    public void addMappingsToCollection(MappingCollections mappingCollections, DecodedObject decodedObject,
                                        UplinkConfiguration uplinkConfiguration) {
        if (uplinkConfiguration.containsMeasurementMapping()) {
            addMeasurementFragment(mappingCollections.getMeasurementFragments(), decodedObject, uplinkConfiguration.getMeasurementMapping());
        }
        if (uplinkConfiguration.containsAlarmMapping()) {
            addAlarmMapping(mappingCollections.getAlarmMappings(), decodedObject, uplinkConfiguration.getAlarmMapping());
        }
        if (uplinkConfiguration.containsEventMapping()) {
            EventMapping eventMapping = uplinkConfiguration.getEventMapping();

            if (StringUtils.isBlank(eventMapping.getFragmentType())) {
                addEventMapping(mappingCollections.getEventMappings(), eventMapping);
            } else {
                addEventFragmentObject(mappingCollections.getEventFragments(), decodedObject, uplinkConfiguration.getEventMapping());
            }
        }
        if (uplinkConfiguration.containsManagedObjectMapping()) {
            addManagedObjectFragment(mappingCollections.getManagedObjectFragments(), decodedObject,
                    uplinkConfiguration.getManagedObjectMapping());
        }

    }

    private void addManagedObjectFragment(ManagedObjectFragmentCollection managedObjectFragmentCollection, DecodedObject decodedObject,
                                          ManagedObjectMapping managedObjectConf) {
        String fragmentType = managedObjectConf.getFragmentType();
        ManagedObjectFragment managedObjectFragment = managedObjectFragmentCollection.get(fragmentType);
        if (managedObjectFragment == null) {
            managedObjectFragment = new ManagedObjectFragment();
            managedObjectFragment.setFragmentType(fragmentType);
        }

        if (StringUtils.isNotBlank(managedObjectConf.getInnerType())) {
            managedObjectFragment.putFragmentValue(managedObjectConf.getInnerType(), decodedObject);
        } else {
            managedObjectFragment.putFragmentValue(decodedObject);
        }

        managedObjectFragmentCollection.put(fragmentType, managedObjectFragment);
    }

    private void addEventFragmentObject(EventFragmentCollection eventFragmentCollection, DecodedObject decodedObject, EventMapping eventConf) {
        String fragmentType = eventConf.getFragmentType();
        EventFragment eventFragment = eventFragmentCollection.get(fragmentType);
        if (eventFragment == null) {
            eventFragment = new EventFragment();
            eventFragment.setText(eventConf.getText());
            eventFragment.setType(eventConf.getType());
            eventFragment.setFragmentType(eventConf.getFragmentType());

        }

        if (StringUtils.isNotBlank(eventConf.getInnerType())) {
            eventFragment.putFragmentValue(eventConf.getInnerType(), decodedObject);
        } else {
            eventFragment.putFragmentValue(decodedObject);
        }
        eventFragmentCollection.put(fragmentType, eventFragment);

    }

    private void addEventMapping(EventMappingCollection eventMappingCollection, EventMapping eventConf) {
        eventMappingCollection.add(eventConf);
    }

    private void addAlarmMapping(AlarmMappingCollection alarmMappingCollection, DecodedObject decodedObject, AlarmMapping alarmConf) {
        Double decodedValue = (Double) decodedObject.getValue();
        if (decodedValue.intValue() == 0) {
            alarmMappingCollection.addToClearAlarms(alarmConf);
        } else {
            alarmMappingCollection.addToActivateAlarms(alarmConf);
        }
    }

    private void addMeasurementFragment(MeasurementFragmentCollection measurementFragmentCollection, DecodedObject decodedObject,
                                        MeasurementMapping measurementConf) {
        String type = measurementConf.getType();
        MeasurementFragment measurementFragment = measurementFragmentCollection.get(type);
        if (measurementFragment == null) {
            measurementFragment = new MeasurementFragment();
            measurementFragment.setType(type);
        }
        measurementFragment.putFragmentValue(measurementConf.getSeries(), decodedObject);
        measurementFragmentCollection.put(type, measurementFragment);
    }

    public void executeMappings(MappingCollections mappingCollections, ManagedObjectRepresentation source, DateTime time) {
        createMeasurements(mappingCollections.getMeasurementFragments(), source, time);
        createEvents(mappingCollections.getEventMappings(), source, time);
        createEventsWithFragments(mappingCollections.getEventFragments(), source, time);
        executeAlarmMappings(mappingCollections.getAlarmMappings(), source, time);
        updateManagedObjects(mappingCollections.getManagedObjectFragments(), source);
    }

    private void createMeasurements(MeasurementFragmentCollection measurementFragmentCollection, ManagedObjectRepresentation source,
                                    DateTime time) {
        for (Map.Entry<String, MeasurementFragment> entry : measurementFragmentCollection.entrySet()) {
            createMeasurement(entry.getValue(), source, time);
        }

    }

    private void createEvents(EventMappingCollection eventMappingCollection, ManagedObjectRepresentation source, DateTime time) {
        for (EventMapping eventMapping : eventMappingCollection.getCollection()) {
            createEvent(eventMapping, source, time);
        }

    }

    private void createEventsWithFragments(EventFragmentCollection eventFragmentCollection, ManagedObjectRepresentation source, DateTime time) {
        for (Map.Entry<String, EventFragment> entry : eventFragmentCollection.entrySet()) {
            createEvent(entry.getValue(), source, time);
        }

    }

    private void executeAlarmMappings(AlarmMappingCollection alarmMappingCollection, ManagedObjectRepresentation source, DateTime time) {
        for (AlarmMapping alarmMapping : alarmMappingCollection.getActivateAlarms()) {
            createAlarm(alarmMapping, source, time);
        }
        for (AlarmMapping alarmMapping : alarmMappingCollection.getClearAlarms()) {
            clearAlarms(alarmMapping, source, time);
        }

    }

    private void updateManagedObjects(ManagedObjectFragmentCollection managedObjectFragmentCollection,
                                      ManagedObjectRepresentation source) {
        for (Map.Entry<String, ManagedObjectFragment> entry : managedObjectFragmentCollection.entrySet()) {
            updateManagedObject(entry.getValue(), source);
        }

    }

    private void createEvent(EventMapping eventMapping, ManagedObjectRepresentation source, DateTime time) {
        EventRepresentation event = new EventRepresentation();
        event.setText(eventMapping.getText());
        event.setType(eventMapping.getType());
        event.setSource(source);
        event.setDateTime(time);
        eventApi.create(event);
    }

    private void createEvent(EventFragment eventFragment, ManagedObjectRepresentation source, DateTime time) {
        EventRepresentation event = new EventRepresentation();
        event.setText(eventFragment.getText());
        event.setType(eventFragment.getType());

        if (eventFragment.getInnerField() != null) {
            event.setProperty(eventFragment.getFragmentType(), eventFragment.getInnerField());
        } else if (eventFragment.getInnerObject().size() > 0) {
            event.setProperty(eventFragment.getFragmentType(), eventFragment.getInnerObject());
        }
        event.setSource(source);
        event.setDateTime(time);
        eventApi.create(event);
    }

    private void createAlarm(AlarmMapping alarmMapping, ManagedObjectRepresentation source, DateTime time) {
        AlarmRepresentation alarm = new AlarmRepresentation();
        alarm.setText(alarmMapping.getText());
        alarm.setType(alarmMapping.getType());
        alarm.setStatus(CumulocityAlarmStatuses.ACTIVE.name());
        alarm.setSeverity(alarmMapping.getSeverity());
        alarm.setSource(source);
        alarm.setDateTime(time);
        alarmApi.create(alarm);
    }

    private void clearAlarms(AlarmMapping alarmMapping, ManagedObjectRepresentation source, DateTime time) {
        AlarmFilter filter = new AlarmFilter().bySource(source.getId()).byType(alarmMapping.getType()).byStatus(CumulocityAlarmStatuses.ACTIVE);
        Iterable<AlarmRepresentation> alarms = alarmApi.getAlarmsByFilter(filter).get(2000).allPages();

        for (AlarmRepresentation alarm : alarms) {
            AlarmRepresentation alarmRepr = new AlarmRepresentation();
            alarmRepr.setId(alarm.getId());
            alarmRepr.setStatus(CumulocityAlarmStatuses.CLEARED.name());
            alarmApi.update(alarmRepr);
        }
    }

    private void createMeasurement(MeasurementFragment measurementFragment, ManagedObjectRepresentation source, DateTime time) {

        MeasurementRepresentation measurement = new MeasurementRepresentation();
        measurement.setType(measurementFragment.getType());
        measurement.setSource(source);
        measurement.setDateTime(time);
        measurement.setProperty(measurementFragment.getType(), measurementFragment.getSeriesObject());

        measurementApi.create(measurement);
    }

    private void updateManagedObject(ManagedObjectFragment managedObjectFragment, ManagedObjectRepresentation source) {

        ManagedObjectRepresentation toUpdate = new ManagedObjectRepresentation();
        toUpdate.setId(source.getId());

        if (managedObjectFragment.getInnerField() != null) {
            toUpdate.setProperty(managedObjectFragment.getFragmentType(), managedObjectFragment.getInnerField());
        } else if (managedObjectFragment.getInnerObject().size() > 0) {
            toUpdate.setProperty(managedObjectFragment.getFragmentType(), managedObjectFragment.getInnerObject());
        }

        inventoryApi.update(toUpdate);
    }

    public void handleCodecServiceResponse(DecoderOutput decoderOutput, ManagedObjectRepresentation source, String deviceEui) throws PayloadDecodingFailedException {
        if (Objects.nonNull(decoderOutput)) {
            //Create Measurements
            List<MeasurementRepresentation> measurementsToCreate = decoderOutput.getMeasurementsToCreate();
            if (Objects.nonNull(measurementsToCreate) && !measurementsToCreate.isEmpty()) {
                MeasurementCollectionRepresentation measurementCollection = new MeasurementCollectionRepresentation();
                measurementCollection.setMeasurements(measurementsToCreate);
                try {
                    measurementApi.createBulkWithoutResponse(measurementCollection);
                } catch (SDKException e) {
                    throw new PayloadDecodingFailedException(String.format("Unable to create measurements for device EUI '%s'", deviceEui), e);
                }
            }

            //Create Events
            List<EventRepresentation> eventsToCreate = decoderOutput.getEventsToCreate();
            if (Objects.nonNull(eventsToCreate) && !eventsToCreate.isEmpty()) {
                for (EventRepresentation event : eventsToCreate) {
                    try {
                        eventApi.create(event);
                    } catch (SDKException e) {
                        throw new PayloadDecodingFailedException(String.format("Unable to create event for device EUI '%s'", deviceEui), e);
                    }
                }
            }

            //Clear Alarms
            Set<String> alarmTypesToClear = decoderOutput.getAlarmTypesToClear();
            if (Objects.nonNull(alarmTypesToClear) && !alarmTypesToClear.isEmpty()) {
                for (String alarmType : alarmTypesToClear) {
                    Iterable<AlarmRepresentation> alarmMaybe = find(source.getId(), ACTIVE, alarmType);
                    for (AlarmRepresentation alarm : alarmMaybe) {
                        alarm.setStatus(CLEARED.name());
                        try {
                            alarmApi.update(alarm);
                        } catch (SDKException e) {
                            throw new PayloadDecodingFailedException(String.format("Unable to clear alarm for device EUI '%s'", deviceEui), e);
                        }
                    }
                }
            }

            //Create Alarms
            List<AlarmRepresentation> alarmsToCreate = decoderOutput.getAlarmsToCreate();
            if (Objects.nonNull(alarmsToCreate) && !alarmsToCreate.isEmpty()) {
                for (AlarmRepresentation alarm : alarmsToCreate) {
                    try {
                        alarmApi.create(alarm);
                    } catch (SDKException e) {
                        throw new PayloadDecodingFailedException(String.format("Unable to create alarm for device EUI '%s'", deviceEui), e);
                    }
                }
            }

            //Update device managed object
            ManagedObjectRepresentation deviceManagedObjectToUpdate = decoderOutput.getDeviceManagedObjectToUpdate();
            if (Objects.nonNull(deviceManagedObjectToUpdate)) {
                try {
                    inventoryApi.update(deviceManagedObjectToUpdate);
                } catch (SDKException e) {
                    throw new PayloadDecodingFailedException(String.format("Unable to update the device with id '%s' and device EUI '%s'", deviceManagedObjectToUpdate.getId().getValue(), deviceEui), e);
                }
            }
        }
    }

    private Iterable<AlarmRepresentation> find(GId source, CumulocityAlarmStatuses alarmStatus, String alarmType) {
        try {
            AlarmFilter filter = new AlarmFilter().bySource(source).byType(alarmType);
            if (alarmStatus != null) {
                filter.byStatus(alarmStatus);
            }
            AlarmCollection alarms = alarmApi.getAlarmsByFilter(filter);
            return alarms.get().allPages();
        } catch (SDKException e) {
            // This exception is caught to only log and return an empty alarms collection.
            log.debug("Couldn't find any Alarm with type '{}' on source '{}'", alarmType, source.getValue());
        }

        return FluentIterable.of();
    }
}
