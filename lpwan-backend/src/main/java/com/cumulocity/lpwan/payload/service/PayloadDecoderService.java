package com.cumulocity.lpwan.payload.service;

import com.cumulocity.lpwan.codec.model.Decode;
import com.cumulocity.lpwan.devicetype.model.DecodedDataMapping;
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
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class PayloadDecoderService<T extends UplinkMessage> {

    @Autowired
    MicroserviceSubscriptionsService subscriptionsService;

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

    @NonNull
    private PayloadMappingService payloadMappingService;
    @NonNull
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
    public void decodeAndMap(T uplinkMessage, ManagedObjectRepresentation source, DeviceType deviceType) throws IOException, InterruptedException {
        List<UplinkConfiguration> uplinkConfigurations = deviceType.getUplinkConfigurations();

        MessageIdConfiguration messageIdConfiguration = deviceType.getMessageIdConfiguration();

        try {
            Integer messageTypeId = messageIdReader.read(uplinkMessage, messageIdConfiguration);

           /* //for codec microservice
            if(messageTypeId == Integer.MIN_VALUE){
                MappingCollections mappingCollections = new MappingCollections();
                DecodedDataMapping decodedDataMapping = makeServiceCallToCodec(messageIdConfiguration.getSource());
                payloadMappingService.addMappingsToCollection(mappingCollections, decodedDataMapping.getDecodedObject(), decodedDataMapping.getUplinkConfigurationMapping());
                payloadMappingService.executeMappings(mappingCollections, source, uplinkMessage.getDateTime());
                return;
            }*/

            MessageTypeMapping messageTypeMappings = deviceType.getMessageTypes().getMappingIndexesByMessageType(Integer.toString(messageTypeId));

            if (messageTypeMappings == null) {
                log.warn("Message type id {} not found for device type {}", messageTypeId, deviceType);
                return;
            }

            MappingCollections mappingCollections = new MappingCollections();
            for (Integer registerIndex : messageTypeMappings.getRegisterIndexes()) {

                try {
                    UplinkConfiguration uplinkConfiguration = uplinkConfigurations.get(registerIndex);
                    if(Objects.nonNull(uplinkConfiguration.getCodec())) { //for codec microservice;
                        DecodedDataMapping decodedDataMapping = codecServiceCall(uplinkConfiguration.getCodec().getName(),
                                uplinkMessage.getPayloadHex(),uplinkConfiguration);
                        payloadMappingService.addMappingsToCollection(mappingCollections, decodedDataMapping.getDecodedObject(), decodedDataMapping.getUplinkConfigurationMapping());
                    } else {
                        DecodedObject decodedObject = generateDecodedData(uplinkMessage, uplinkConfiguration);
                        payloadMappingService.addMappingsToCollection(mappingCollections, decodedObject, uplinkConfiguration);
                    }
                } catch (PayloadDecodingFailedException e) {
                    log.error("Error decoding payload for device type {}: {} Skipping decoding payload part", deviceType, e.getMessage());
                }
            }
            payloadMappingService.executeMappings(mappingCollections, source, uplinkMessage.getDateTime());

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private DecodedDataMapping codecServiceCall(String codecServiceName, String payloadHex, UplinkConfiguration uplinkConfiguration) {

        Decode decode =  new Decode(payloadHex, uplinkConfiguration);
        String authentication = subscriptionsService.getCredentials(subscriptionsService.getTenant()).get()
                .toCumulocityCredentials().getAuthenticationString();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authentication);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        ResponseEntity<DecodedDataMapping> response = restTemplate.exchange(
                System.getenv("C8Y_BASEURL")+"/service/"+codecServiceName+"/decode", HttpMethod.POST,
                new HttpEntity<Decode>(decode, headers), DecodedDataMapping.class);

        return response.getBody();
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
