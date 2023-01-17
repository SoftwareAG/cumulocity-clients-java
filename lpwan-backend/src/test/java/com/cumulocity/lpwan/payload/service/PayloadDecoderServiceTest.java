package com.cumulocity.lpwan.payload.service;

import com.cumulocity.lpwan.codec.service.LpwanCodecService;
import com.cumulocity.lpwan.codec.service.WebClientFactory;
import com.cumulocity.lpwan.devicetype.model.DeviceType;
import com.cumulocity.lpwan.devicetype.model.UplinkConfiguration;
import com.cumulocity.lpwan.payload.exception.PayloadDecodingFailedException;
import com.cumulocity.lpwan.payload.uplink.model.UplinkMessage;
import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.customdecoders.api.model.DecoderResult;
import com.cumulocity.microservice.lpwan.codec.decoder.model.LpwanDecoderInputData;
import com.cumulocity.microservice.lpwan.codec.model.DeviceInfo;
import com.cumulocity.microservice.lpwan.codec.model.LpwanCodecDetails;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjects;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PayloadDecoderServiceTest {

    @Mock
    private MicroserviceSubscriptionsService subscriptionsService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    RestConnector restConnector;

    @Mock
    private InventoryApi inventoryApi;

    @Mock
    private ContextService<MicroserviceCredentials> contextService;

    @Mock
    WebClient webClient;

    @InjectMocks
    LpwanCodecService lpwanCodecService;

    @Mock
    Mono<DecoderResult> decoderResultMono;

    @Mock
    WebClient.RequestBodyUriSpec post;

    @Mock
    WebClient.RequestBodySpec uri;

    @Mock
    WebClient.ResponseSpec responseSpec;

    @Mock
    WebClient.RequestHeadersSpec requestHeadersSpec;

    @Captor
    ArgumentCaptor<Mono<LpwanDecoderInputData>> inputMonoCaptor;

    @Captor
    ArgumentCaptor<DecoderResult> decoderResultCaptor;

    PayloadMappingService payloadMappingService = spy(new PayloadMappingService());

    @Captor
    ArgumentCaptor<Runnable> taskCaptor;

    @InjectMocks
    PayloadDecoderService<UplinkMessage> payloadDecoderService = new PayloadDecoderService<>(payloadMappingService, null);

    @Before
    public void setup() {
        ReflectionTestUtils.setField(lpwanCodecService, "webClient", webClient);
        ReflectionTestUtils.setField(payloadDecoderService, "lpwanCodecService", lpwanCodecService);
    }

    @Test
    public void decodeLittleEndianHex() {
        try {
            UplinkConfiguration uplinkConfiguration = new UplinkConfiguration();
            uplinkConfiguration.setLittleEndian(true);
            uplinkConfiguration.setStartBit(0);
            uplinkConfiguration.setNoBits(32);
            uplinkConfiguration.setMultiplier(1.0);
            uplinkConfiguration.setOffset(0.0);

            Double actualVal = payloadDecoderService.decodeByConfiguration("5A109061", uplinkConfiguration);
            Double expectedVal = 1636831322.0;
            assertEquals(expectedVal, actualVal);

            uplinkConfiguration.setStartBit(8);
            uplinkConfiguration.setNoBits(16);
            actualVal = payloadDecoderService.decodeByConfiguration("5A109061", uplinkConfiguration);
            expectedVal = 36880.0;
            assertEquals(expectedVal, actualVal);

            uplinkConfiguration.setSigned(true);
            actualVal = payloadDecoderService.decodeByConfiguration("5A109061", uplinkConfiguration);
            expectedVal = -28656.0;
            assertEquals(expectedVal, actualVal);

            uplinkConfiguration.setBcd(true);
            actualVal = payloadDecoderService.decodeByConfiguration("5A109061", uplinkConfiguration);
            expectedVal = -990.0;
            assertEquals(expectedVal, actualVal);

        } catch (PayloadDecodingFailedException e) {
            fail();
        }
    }


    @Test
    public void decodeWithMultiplierAndOffset() {
        try {
            UplinkConfiguration uplinkConfiguration = new UplinkConfiguration();
            uplinkConfiguration.setStartBit(0);
            uplinkConfiguration.setNoBits(16);
            uplinkConfiguration.setMultiplier(1.0);
            uplinkConfiguration.setOffset(0.0);

            Double actualVal = payloadDecoderService.decodeByConfiguration("9062", uplinkConfiguration);
            Double expectedVal = 36962.0;
            assertEquals(expectedVal, actualVal);

            uplinkConfiguration.setMultiplier(0.5);
            actualVal = payloadDecoderService.decodeByConfiguration("9062", uplinkConfiguration);
            expectedVal = 18481.0;
            assertEquals(expectedVal, actualVal);

            uplinkConfiguration.setOffset(1.0);
            actualVal = payloadDecoderService.decodeByConfiguration("9062", uplinkConfiguration);
            expectedVal = 18482.0;
            assertEquals(expectedVal, actualVal);

            uplinkConfiguration.setBcd(true);
            actualVal = payloadDecoderService.decodeByConfiguration("9062", uplinkConfiguration);
            expectedVal = 4532.0;
            assertEquals(expectedVal, actualVal);

        } catch (PayloadDecodingFailedException e) {
            fail();
        }
    }

    @Test
    public void shouldTestPayloadDecodeAndMap_1() throws PayloadDecodingFailedException {
        DateTime timeNow = DateTime.now();
        UplinkMessage uplinkMessage = Mockito.mock(UplinkMessage.class, Mockito.CALLS_REAL_METHODS);
        when(uplinkMessage.getPayloadHex()).thenReturn("ABCDEF1234567");
        when(uplinkMessage.getExternalId()).thenReturn("EUI ID");
        when(uplinkMessage.getFport()).thenReturn(999);
        when(uplinkMessage.getDateTime()).thenReturn(timeNow);

        DeviceInfo supportedDevice = new DeviceInfo("Manufacturer_X", "Model_X");
        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails("lpwanContextPath", supportedDevice);
        DeviceType deviceType = new DeviceType();
        deviceType.setLpwanCodecDetails(lpwanCodecDetails);
        deviceType.setFieldbusType("lpwan");

        MicroserviceCredentials credentials = new MicroserviceCredentials("tenant", "username", "password", null, null, null, "appKey");
        when(contextService.getContext()).thenReturn(credentials);
        when(subscriptionsService.getTenant()).thenReturn("tenant");
        when(subscriptionsService.getCredentials(eq("tenant"))).thenReturn(Optional.of(credentials));

        when(webClient.post()).thenReturn(post);
        when(post.uri(eq("/service/" + lpwanCodecDetails.getCodecServiceContextPath() + "/decode"))).thenReturn(uri);
        when(uri.header(eq(HttpHeaders.AUTHORIZATION), eq(credentials.toCumulocityCredentials().getAuthenticationString()))).thenReturn(uri);
        when(uri.body(inputMonoCaptor.capture(), eq(LpwanDecoderInputData.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(DecoderResult.class)).thenReturn(decoderResultMono);

        DecoderResult decoderResult = new DecoderResult();
        when(decoderResultMono.block(eq(WebClientFactory.DEFAULT_TIMEOUT_IN_MILLIS))).thenReturn(decoderResult);

        ManagedObjectRepresentation source = ManagedObjects.asManagedObject(GId.asGId("12345"));

        payloadDecoderService.decodeAndMap(uplinkMessage, source, deviceType);
        verify(contextService).runWithinContext(eq(credentials), taskCaptor.capture());
        taskCaptor.getValue().run();

        // Verify if the invokeCodecMicroservice was invoked with the correct DecoderInputData
        LpwanDecoderInputData capturedInputData = inputMonoCaptor.getValue().block(WebClientFactory.DEFAULT_TIMEOUT_IN_MILLIS);
        assertEquals(source.getId().getValue(), capturedInputData.getSourceDeviceId());
        assertEquals(uplinkMessage.getExternalId(), capturedInputData.getSourceDeviceEui());
        assertEquals(lpwanCodecDetails.getSupportedDevice().getDeviceManufacturer(), capturedInputData.getSourceDeviceInfo().getDeviceManufacturer());
        assertEquals(lpwanCodecDetails.getSupportedDevice().getDeviceModel(), capturedInputData.getSourceDeviceInfo().getDeviceModel());
        assertEquals(uplinkMessage.getPayloadHex(), capturedInputData.getValue());
        assertEquals(uplinkMessage.getFport(), capturedInputData.getFport());
        assertEquals(uplinkMessage.getDateTime().getMillis(), capturedInputData.getUpdateTime());

        // Verify if the handleCodecServiceResponse is invoked with the correct data
        verify(payloadMappingService).handleCodecServiceResponse(decoderResult, source, uplinkMessage.getExternalId());
    }

    @Test
    public void shouldTestPayloadDecodeAndMap_2() throws PayloadDecodingFailedException {
        DateTime timeNow = DateTime.now();
        UplinkMessage uplinkMessage = Mockito.mock(UplinkMessage.class, Mockito.CALLS_REAL_METHODS);
        when(uplinkMessage.getExternalId()).thenReturn("EUI ID");

        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails();
        DeviceType deviceType = new DeviceType();
        deviceType.setLpwanCodecDetails(lpwanCodecDetails);
        deviceType.setFieldbusType("lpwan");

        MicroserviceCredentials credentials = new MicroserviceCredentials("tenant", "username", "password", null, null, null, "appKey");
        when(contextService.getContext()).thenReturn(credentials);
        when(subscriptionsService.getCredentials(eq("tenant"))).thenReturn(Optional.of(credentials));

        ManagedObjectRepresentation source = ManagedObjects.asManagedObject(GId.asGId("12345"));

        payloadDecoderService.decodeAndMap(uplinkMessage, source, deviceType);
        verify(contextService).runWithinContext(eq(credentials), taskCaptor.capture());
        taskCaptor.getValue().run();


        // Verify if the handleCodecServiceResponse is invoked with the correct data
        verify(payloadMappingService).handleCodecServiceResponse(decoderResultCaptor.capture(), any(ManagedObjectRepresentation.class), any(String.class));
        assertEquals(String.format("Error decoding payload for device EUI '%s'. Skipping the decoding of the payload part. \nCause: %s", uplinkMessage.getExternalId(), String.format("'c8y_LpwanCodecDetails' fragment in the device type associated with device EUI '%s' is invalid.", uplinkMessage.getExternalId())), decoderResultCaptor.getValue().getMessage());
        assertFalse(decoderResultCaptor.getValue().isSuccess());
    }

    @Test
    public void shouldTestPayloadDecodeAndMap_3() throws PayloadDecodingFailedException {
        DateTime timeNow = DateTime.now();
        UplinkMessage uplinkMessage = Mockito.mock(UplinkMessage.class, Mockito.CALLS_REAL_METHODS);
        when(uplinkMessage.getPayloadHex()).thenReturn("ABCDEF1234567");
        when(uplinkMessage.getExternalId()).thenReturn("EUI ID");
        when(uplinkMessage.getFport()).thenReturn(999);
        when(uplinkMessage.getDateTime()).thenReturn(timeNow);

        DeviceInfo supportedDevice = new DeviceInfo("Manufacturer_X", "Model_X");
        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails("lpwanContextPath", supportedDevice);
        DeviceType deviceType = new DeviceType();
        deviceType.setLpwanCodecDetails(lpwanCodecDetails);
        deviceType.setFieldbusType("lpwan");

        MicroserviceCredentials credentials = new MicroserviceCredentials("tenant", "username", "password", null, null, null, "appKey");
        when(contextService.getContext()).thenReturn(credentials);
        when(subscriptionsService.getTenant()).thenReturn("tenant");
        when(subscriptionsService.getCredentials(eq("tenant"))).thenReturn(Optional.of(credentials));

        when(webClient.post()).thenReturn(post);
        when(post.uri(eq("/service/" + lpwanCodecDetails.getCodecServiceContextPath() + "/decode"))).thenReturn(uri);
        when(uri.header(eq(HttpHeaders.AUTHORIZATION), eq(credentials.toCumulocityCredentials().getAuthenticationString()))).thenReturn(uri);
        when(uri.body(inputMonoCaptor.capture(), eq(LpwanDecoderInputData.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(DecoderResult.class)).thenReturn(decoderResultMono);

        DecoderResult decoderResult = new DecoderResult();
        when(decoderResultMono.block(eq(WebClientFactory.DEFAULT_TIMEOUT_IN_MILLIS))).thenThrow(new RuntimeException("Failed to invoke Decoder service."));

        ManagedObjectRepresentation source = ManagedObjects.asManagedObject(GId.asGId("12345"));

        payloadDecoderService.decodeAndMap(uplinkMessage, source, deviceType);
        verify(contextService).runWithinContext(eq(credentials), taskCaptor.capture());
        taskCaptor.getValue().run();

        // Verify if the invokeCodecMicroservice was invoked with the correct DecoderInputData
        LpwanDecoderInputData capturedInputData = inputMonoCaptor.getValue().block(WebClientFactory.DEFAULT_TIMEOUT_IN_MILLIS);
        assertEquals(source.getId().getValue(), capturedInputData.getSourceDeviceId());
        assertEquals(uplinkMessage.getExternalId(), capturedInputData.getSourceDeviceEui());
        assertEquals(lpwanCodecDetails.getSupportedDevice().getDeviceManufacturer(), capturedInputData.getSourceDeviceInfo().getDeviceManufacturer());
        assertEquals(lpwanCodecDetails.getSupportedDevice().getDeviceModel(), capturedInputData.getSourceDeviceInfo().getDeviceModel());
        assertEquals(uplinkMessage.getPayloadHex(), capturedInputData.getValue());
        assertEquals(uplinkMessage.getFport(), capturedInputData.getFport());
        assertEquals(uplinkMessage.getDateTime().getMillis(), capturedInputData.getUpdateTime());

        // Verify if the handleCodecServiceResponse is invoked with the correct data
        verify(payloadMappingService).handleCodecServiceResponse(decoderResultCaptor.capture(), any(ManagedObjectRepresentation.class), any(String.class));
        assertEquals(String.format("Error decoding payload for device EUI '%s'. Skipping the decoding of the payload part. \nCause: %s", uplinkMessage.getExternalId(), String.format("Error invoking the LPWAN /decode service with context path '%s'", lpwanCodecDetails.getCodecServiceContextPath())), decoderResultCaptor.getValue().getMessage());
        assertFalse(decoderResultCaptor.getValue().isSuccess());
    }
}
