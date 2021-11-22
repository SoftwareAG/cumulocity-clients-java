package com.cumulocity.lpwan.payload.service;

import com.cumulocity.lpwan.codec.model.*;
import com.cumulocity.lpwan.devicetype.model.DeviceType;
import com.cumulocity.lpwan.devicetype.model.UplinkConfiguration;
import com.cumulocity.lpwan.mapping.model.DecodedObject;
import com.cumulocity.lpwan.mapping.model.MappingCollections;
import com.cumulocity.lpwan.mapping.model.MessageTypeMapping;
import com.cumulocity.lpwan.payload.exception.PayloadDecodingFailedException;
import com.cumulocity.lpwan.payload.uplink.model.MessageIdConfiguration;
import com.cumulocity.lpwan.payload.uplink.model.MessageIdMapping;
import com.cumulocity.lpwan.payload.uplink.model.UplinkMessage;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.cumulocity.model.event.CumulocityAlarmStatuses;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.rest.representation.event.EventRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.cumulocity.model.event.CumulocityAlarmStatuses.*;

@Slf4j
@AllArgsConstructor
public class PayloadDecoderService<T extends UplinkMessage> {

    @Autowired
    MicroserviceSubscriptionsService subscriptionsService;

    @Autowired
    private MeasurementApi measurementApi;

    @Autowired
    private EventApi eventApi;

    @Autowired
    private AlarmApi alarmApi;

    @Autowired
    private InventoryApi inventoryApi;

    public interface MessageIdReader<T> {
        Integer read(T uplinkMessage, MessageIdConfiguration messageIdConfiguration);
    }

    /**
     * Finds uplink message id from the uplink payload based on the given configuration.
     *
     * @param uplink the uplink message
     * @param messageIdConfiguration the message ID configuration
     *
     * @return decimal value of found message id
     */
    public static Integer messageIdFromPayload(UplinkMessage uplink, MessageIdConfiguration messageIdConfiguration) {
        String payload = uplink.getPayloadHex();
        MessageIdMapping messageIdMapping = messageIdConfiguration.getMessageIdMapping();
        try {
            int messageId = DecoderUtil.extractDecimalFromHex(payload, messageIdMapping.getStartBit(), messageIdMapping.getNoBits());
            return messageId;
        } catch (Exception e) {
            String errorMsg = "Error extracting message id from payload: " + e.getMessage();
            log.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
    }

    private PayloadMappingService payloadMappingService;

    private MessageIdReader<T> messageIdReader;

    /**
     * According to the configuration given for the device type,
     * decode the input uplink message and
     * persist the mapped data as measurement/alarm/event/managed object update to the source device.
     *
     * @param uplinkMessage the uplink message
     * @param source the source device managed object
     * @param deviceType the device type
     */
    public void decodeAndMap(T uplinkMessage, ManagedObjectRepresentation source, DeviceType deviceType) {

        if (deviceType.getLpwanCodecDetails() == null) {
            List<UplinkConfiguration> uplinkConfigurations = deviceType.getUplinkConfigurations();

            MessageIdConfiguration messageIdConfiguration = deviceType.getMessageIdConfiguration();

            try {
                Integer messageTypeId = messageIdReader.read(uplinkMessage, messageIdConfiguration);

                MessageTypeMapping messageTypeMappings = deviceType.getMessageTypes().getMappingIndexesByMessageType(Integer.toString(messageTypeId));

                if (messageTypeMappings == null) {
                    log.warn("Message type id {} not found for device type {}", messageTypeId, deviceType);
                    return;
                }

                MappingCollections mappingCollections = new MappingCollections();
                for (Integer registerIndex : messageTypeMappings.getRegisterIndexes()) {

                    try {
                        UplinkConfiguration uplinkConfiguration = uplinkConfigurations.get(registerIndex);
                        DecodedObject decodedObject = generateDecodedData(uplinkMessage, uplinkConfiguration);
                        payloadMappingService.addMappingsToCollection(mappingCollections, decodedObject, uplinkConfiguration);
                    } catch (PayloadDecodingFailedException e) {
                        log.error("Error decoding payload for device type {}: {} Skipping decoding payload part", deviceType, e.getMessage());
                    }
                }
                payloadMappingService.executeMappings(mappingCollections, source, uplinkMessage.getDateTime());

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else {
            LpwanCodecDetails lpwanCodecDetails = deviceType.getLpwanCodecDetails();
            DeviceInfo deviceInfo =  new DeviceInfo(lpwanCodecDetails.getDeviceManufacturer(), lpwanCodecDetails.getDeviceModel(), DeviceTypeEnum.valueOf(deviceType.getFieldbusType().toUpperCase()));
            DecodePayload decodePayload = DecodePayload.builder()
                            .deviceMoId(source.getId().getValue())
                            .deviceInfo(deviceInfo)
                            .deviceEui(uplinkMessage.getExternalId())
                            .fPort(uplinkMessage.getFport())
                            .payload(uplinkMessage.getPayloadHex())
                            .updateTime(uplinkMessage.getDateTime().getMillis())
                            .build();
            DecodeResponse decodeResponse = invokeCodecMicroservice(lpwanCodecDetails.getCodecServiceContextPath(), decodePayload);
            //CreateMeasurements
            if(!decodeResponse.getMeasurementsToCreate().isEmpty()){
                for(MeasurementRepresentation measuremnet : decodeResponse.getMeasurementsToCreate()) {
                    measurementApi.createWithoutResponse(measuremnet);
                }
            }

            //CreateEvents
            if(!decodeResponse.getEventsToCreate().isEmpty()){
                for(EventRepresentation event : decodeResponse.getEventsToCreate()) {
                    eventApi.create(event);
                }
            }

            //AlarmsToClear
            if(!decodeResponse.getAlarmTypesToClear().isEmpty()){
                for(String alarmTypeToClear : decodeResponse.getAlarmTypesToClear()) {
                    alarmApi.
                }
            }
            //createAlarms
            //UpdatedeviceMO
        }
    }

    private void clear(GId source, String... types) {
        final Iterable<AlarmRepresentation> alarmMaybe = find(source, ACTIVE, types);
        for (final AlarmRepresentation alarm : alarmMaybe) {
            alarm.setStatus(CLEARED.name());
            alarmApi.update(alarm);
        }
    }

    private Iterable<AlarmRepresentation> find(GId source, CumulocityAlarmStatuses alarmStatus, String... types) {
        try {
            final AlarmFilter filter = new AlarmFilter().bySource(source).byType(type);
            if (alarmStatus != null && alarmStatus.length > 0) {
                filter.byStatus(alarmStatus);
            }
            final AlarmCollection alarms = alarmApi.getAlarmsByFilter(filter);
            return alarms.get().allPages();
        } catch (final SDKException ex) {
            if (ex.getHttpStatus() != 404) {
                throw ex;
            }
        }
        return FluentIterable.of();
    }

    private DecodeResponse invokeCodecMicroservice(String codecServiceContextPath, DecodePayload decodePayload){
        /*String authentication = subscriptionsService.getCredentials(subscriptionsService.getTenant()).get()
                .toCumulocityCredentials().getAuthenticationString();

        WebClient webClient = WebClient.create(System.getenv("C8Y_BASEURL"));
        webClient.post()
                .uri("/service/"+codecServiceContextPath+"/decode")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, authentication)
                .body(Mono.just(decodePayload), DecodePayload.class)
                .retrieve()
                .bodyToMono(Void.class);*/
        return null;
    }

    private DecodedObject generateDecodedData(T uplinkMessage, UplinkConfiguration uplinkConfiguration)
            throws PayloadDecodingFailedException {
        DecodedObject decodedData = new DecodedObject();
        Double value = decodeByConfiguration(uplinkMessage.getPayloadHex(), uplinkConfiguration);
        String unit = uplinkConfiguration.getUnit();

        decodedData.putValue(value);
        if (StringUtils.isNotBlank(unit)) {
            decodedData.putUnit(unit);
        }

        return decodedData;
    }

    protected Double decodeByConfiguration(String payloadHex, UplinkConfiguration uplinkConfiguration)
            throws PayloadDecodingFailedException {
        Integer startBit = uplinkConfiguration.getStartBit();
        Integer numberOfBits = uplinkConfiguration.getNoBits();
        Double multiplication = uplinkConfiguration.getMultiplier();
        Double offset = uplinkConfiguration.getOffset();
        Double value;

        if (uplinkConfiguration.isLittleEndian()) {
            payloadHex = DecoderUtil.convertHexToBigEndianOrdering(payloadHex, startBit, numberOfBits);
            startBit = 0;
        }

        if (uplinkConfiguration.isBcd()) {
            if (uplinkConfiguration.isSigned()) {
                value = (double) DecoderUtil.extractSignedBCDFromHex(payloadHex, startBit, numberOfBits);
            } else {
                value = (double) DecoderUtil.extractBCDFromHex(payloadHex, startBit, numberOfBits);
            }
        } else if (uplinkConfiguration.isSigned()) {
            value = (double) DecoderUtil.extractSignedDecimalFromHex(payloadHex, startBit, numberOfBits);
        } else {
            value = (double) DecoderUtil.extractDecimalFromHex(payloadHex, startBit, numberOfBits);
        }

        value = DecoderUtil.multiply(value, multiplication);
        value = DecoderUtil.offset(value, offset);

        return value;

    }

}
