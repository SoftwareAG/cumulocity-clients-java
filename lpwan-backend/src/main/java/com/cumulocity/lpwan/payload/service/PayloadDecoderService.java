package com.cumulocity.lpwan.payload.service;

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
import com.cumulocity.microservice.customdecoders.api.model.DecoderResult;
import com.cumulocity.microservice.lpwan.codec.decoder.model.LpwanDecoderInputData;
import com.cumulocity.microservice.lpwan.codec.model.DeviceInfo;
import com.cumulocity.microservice.lpwan.codec.model.LpwanCodecDetails;
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
public class PayloadDecoderService<T extends UplinkMessage> {

    private static final Duration WEBCLIENT_DEFAULT_TIMEOUT_IN_MILLIS = Duration.ofMillis(5000);

    @Autowired
    private MicroserviceSubscriptionsService subscriptionsService;

    @Autowired
    private ContextService<MicroserviceCredentials> contextService;

    private Duration webClientTimeout;

    private WebClient webClient;

    private final PayloadMappingService payloadMappingService;

    private final MessageIdReader<T> messageIdReader;

    public PayloadDecoderService(PayloadMappingService payloadMappingService, MessageIdReader<T> messageIdReader){
        this(payloadMappingService, messageIdReader, WEBCLIENT_DEFAULT_TIMEOUT_IN_MILLIS);
    }

    public PayloadDecoderService(PayloadMappingService payloadMappingService, MessageIdReader<T> messageIdReader, Duration webClientTimeout){
        this.payloadMappingService =  payloadMappingService;
        this.messageIdReader =  messageIdReader;
        this.webClientTimeout = webClientTimeout;
        this.webClient = WebClientFactory.builder()
                .timeout(webClientTimeout)
                .baseUrl(System.getenv("C8Y_BASEURL"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .build();
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
            return DecoderUtil.extractDecimalFromHex(payload, messageIdMapping.getStartBit(), messageIdMapping.getNoBits());
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
                LpwanCodecDetails lpwanCodecDetails = deviceType.getLpwanCodecDetails();
                DecoderResult decoderResult;
                try {
                    try {
                        lpwanCodecDetails.validate();
                    } catch (IllegalArgumentException e) {
                        throw new PayloadDecodingFailedException(String.format("'c8y_LpwanCodecDetails' fragment in the device type associated with device EUI '%s' is invalid.", uplinkMessage.getExternalId()), e);
                    }

                    DeviceInfo deviceInfo = new DeviceInfo(lpwanCodecDetails.getDeviceManufacturer(), lpwanCodecDetails.getDeviceModel());
                    LpwanDecoderInputData decoderInputData = new LpwanDecoderInputData(
                            source.getId().getValue(),
                            uplinkMessage.getExternalId(),
                            deviceInfo,
                            uplinkMessage.getPayloadHex(),
                            uplinkMessage.getFport(),
                            uplinkMessage.getDateTime().getMillis()
                            );

                    decoderResult = invokeCodecMicroservice(lpwanCodecDetails.getCodecServiceContextPath(), decoderInputData);

                } catch (PayloadDecodingFailedException e) {
                    decoderResult = DecoderResult.empty();
                    decoderResult.setAsFailed(String.format("Error decoding payload for device EUI '%s'. Skipping the decoding of the payload part. \nCause: %s", uplinkMessage.getExternalId(), e.getMessage()));
                }

                try {
                    payloadMappingService.handleCodecServiceResponse(decoderResult, source, uplinkMessage.getExternalId());
                } catch (PayloadDecodingFailedException e) {
                    log.error("Error handling the decoder response for the device with EUI '{}'.", uplinkMessage.getExternalId(), e);
                }
            }
        });
    }

    private DecoderResult invokeCodecMicroservice(String codecServiceContextPath, LpwanDecoderInputData decoderInput) throws PayloadDecodingFailedException {
        String authentication = subscriptionsService.getCredentials(subscriptionsService.getTenant()).get()
                .toCumulocityCredentials().getAuthenticationString();
        try {
            Mono<DecoderResult> decoderOutput = webClient.post()
                    .uri("/service/" + codecServiceContextPath + "/decode")
                    .header(HttpHeaders.AUTHORIZATION, authentication)
                    .body(Mono.just(decoderInput), LpwanDecoderInputData.class)
                    .retrieve()
                    .bodyToMono(DecoderResult.class);
            return decoderOutput.block(webClientTimeout);
        } catch (Exception e) {
            String errorMessage = String.format("Error invoking the LPWAN Codec microservice with context path '%s'", codecServiceContextPath);
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

class WebClientFactory {

    private Duration timeout;
    private String baseUrl;
    private String contentType;
    private String accept;

    public static WebClientFactory builder() {
        return new WebClientFactory();
    }

    public WebClientFactory timeout(Duration duration) {
        this.timeout = duration;
        return this;
    }

    public WebClientFactory baseUrl(String url) {
        this.baseUrl = url;
        return this;
    }

    public WebClientFactory contentType(String mediaType) {
        this.contentType = mediaType;
        return this;
    }

    public WebClientFactory accept(String mediaType) {
        this.accept = mediaType;
        return this;
    }

    public WebClient build() {
        HttpClient httpClient = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) timeout.toMillis())
                .responseTimeout(timeout)
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(timeout.toMillis(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(timeout.toMillis(), TimeUnit.MILLISECONDS)));
        return WebClient.builder().baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, contentType)
                .defaultHeader(HttpHeaders.ACCEPT, accept)
                .clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    }
}
