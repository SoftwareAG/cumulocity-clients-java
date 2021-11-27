package com.cumulocity.lpwan.payload.service;

import com.cumulocity.lpwan.codec.model.DecoderInput;
import com.cumulocity.lpwan.codec.model.DecoderOutput;
import com.cumulocity.lpwan.codec.model.DeviceInfo;
import com.cumulocity.lpwan.codec.model.LpwanCodecDetails;
import com.cumulocity.lpwan.devicetype.model.DeviceType;
import com.cumulocity.lpwan.devicetype.model.UplinkConfiguration;
import com.cumulocity.lpwan.mapping.model.DecodedObject;
import com.cumulocity.lpwan.mapping.model.MappingCollections;
import com.cumulocity.lpwan.mapping.model.MessageTypeMapping;
import com.cumulocity.lpwan.payload.exception.PayloadDecodingFailedException;
import com.cumulocity.lpwan.payload.uplink.model.MessageIdConfiguration;
import com.cumulocity.lpwan.payload.uplink.model.MessageIdMapping;
import com.cumulocity.lpwan.payload.uplink.model.UplinkMessage;
import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
//@AllArgsConstructor
public class PayloadDecoderService<T extends UplinkMessage> {

    @Autowired
    private MicroserviceSubscriptionsService subscriptionsService;

    @Autowired
    private ContextService<MicroserviceCredentials> contextService;

    private final WebClient webClient;

    private final PayloadMappingService payloadMappingService;

    private final MessageIdReader<T> messageIdReader;

    public PayloadDecoderService(PayloadMappingService payloadMappingService, MessageIdReader<T> messageIdReader){
        this.payloadMappingService =  payloadMappingService;
        this.messageIdReader =  messageIdReader;
        HttpClient httpClient = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50000)
                .responseTimeout(Duration.ofMillis(50000))
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(50000, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(50000, TimeUnit.MILLISECONDS)));

        this.webClient = WebClient.builder().baseUrl(System.getenv("C8Y_BASEURL"))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }

    public interface MessageIdReader<T> {
        Integer read(T uplinkMessage, MessageIdConfiguration messageIdConfiguration);
    }

    /**
     * Finds uplink message id from the uplink payload based on the given configuration.
     *
     * @param uplink                 the uplink message
     * @param messageIdConfiguration the message ID configuration
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

    /**
     * According to the configuration given for the device type,
     * decode the input uplink message and
     * persist the mapped data as measurement/alarm/event/managed object update to the source device.
     *
     * @param uplinkMessage the uplink message
     * @param source        the source device managed object
     * @param deviceType    the device type
     */
    public void decodeAndMap(T uplinkMessage, ManagedObjectRepresentation source, DeviceType deviceType) {
        final String tenantId = contextService.getContext().getTenant();
        Optional<MicroserviceCredentials> serviceUser = subscriptionsService.getCredentials(tenantId);
        contextService.runWithinContext(serviceUser.get(), () -> {
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
                try {
                    LpwanCodecDetails lpwanCodecDetails = deviceType.getLpwanCodecDetails();
                    try {
                        lpwanCodecDetails.validate();
                    } catch (IllegalArgumentException e) {
                        throw new PayloadDecodingFailedException(String.format("'c8y_LpwanCodecDetails' fragment in the device type associated with device EUI '%s' is invalid.", uplinkMessage.getExternalId()), e);
                    }

                    DeviceInfo deviceInfo = new DeviceInfo(lpwanCodecDetails.getDeviceManufacturer(), lpwanCodecDetails.getDeviceModel());
                    DecoderInput decoderInput = DecoderInput.builder()
                            .deviceMoId(source.getId().getValue())
                            .deviceInfo(deviceInfo)
                            .deviceEui(uplinkMessage.getExternalId())
                            .fPort(uplinkMessage.getFport())
                            .payload(uplinkMessage.getPayloadHex())
                            .updateTime(uplinkMessage.getDateTime().getMillis())
                            .build();

                    DecoderOutput decoderOutput = invokeCodecMicroservice(lpwanCodecDetails.getCodecServiceContextPath(), decoderInput);

                    payloadMappingService.handleCodecServiceResponse(decoderOutput, source, uplinkMessage.getExternalId());
                } catch (PayloadDecodingFailedException e) {
                    log.error("Error decoding payload for device EUI '{}'. Skipping the decoding of the payload part.", uplinkMessage.getExternalId(), e);
                }
            }
        });
    }

    private DecoderOutput invokeCodecMicroservice(String codecServiceContextPath, DecoderInput decoderInput) throws PayloadDecodingFailedException {
        String authentication = subscriptionsService.getCredentials(subscriptionsService.getTenant()).get()
                .toCumulocityCredentials().getAuthenticationString();
        try {
            Mono<DecoderOutput> decoderOutput = webClient.post()
                    .uri("/service/" + codecServiceContextPath + "/decode")
                    .header(HttpHeaders.AUTHORIZATION, authentication)
                    .body(Mono.just(decoderInput), DecoderInput.class)
                    .retrieve()
                    .bodyToMono(DecoderOutput.class);
            return decoderOutput.block(Duration.ofMillis(50000));
        } catch (Exception e) {
            String errorMessage = String.format("Error invoking the Codec microservice with context path '%s'", codecServiceContextPath);
            log.error(errorMessage, e);
            throw new PayloadDecodingFailedException(errorMessage, e);
        }
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
