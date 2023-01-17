package com.cumulocity.lpwan.codec.service;

import c8y.Command;
import com.cumulocity.lpwan.codec.exception.LpwanCodecServiceException;
import com.cumulocity.lpwan.devicetype.model.DeviceType;
import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.lpwan.codec.encoder.model.LpwanEncoderInputData;
import com.cumulocity.microservice.lpwan.codec.encoder.model.LpwanEncoderResult;
import com.cumulocity.microservice.lpwan.codec.model.DeviceCommand;
import com.cumulocity.microservice.lpwan.codec.model.DeviceInfo;
import com.cumulocity.microservice.lpwan.codec.model.LpwanCodecDetails;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjects;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LpwanCodecServiceTest {
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
    private LpwanCodecService lpwanCodecService;

    @Mock
    Mono<LpwanEncoderResult> lpwanEncoderResultMono;

    @Mock
    WebClient.RequestBodyUriSpec post;

    @Mock
    WebClient.RequestBodySpec uri;

    @Mock
    WebClient.ResponseSpec responseSpec;

    @Mock
    WebClient.RequestHeadersSpec requestHeadersSpec;

    @Captor
    ArgumentCaptor<Mono<LpwanEncoderInputData>> inputMonoCaptor;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(lpwanCodecService, "webClient", webClient);
    }

    @Test
    public void commandShouldBeCodecGenerated() {
        DeviceType deviceType = new DeviceType();

        DeviceCommand deviceCommand1 = new DeviceCommand("set config", "TestCategory", "{\"set config\":{\"breakpoint\":\"false\",\"selfadapt\":\"true\",\"oneoff\":\"false\",\"alreport\":\"false\",\"pos\":\"0\",\"hb\":\"10\"}}");
        DeviceCommand deviceCommand2 = new DeviceCommand("position request", "TestCategory", "{\"request pos\":{\"latitude\":\"10.35\",\"selfadapt\":\"-1.36\"");
        Set<DeviceCommand> supportedCommands = new HashSet<>();
        supportedCommands.addAll(Arrays.asList(deviceCommand1, deviceCommand2));

        DeviceInfo supportedDevice = new DeviceInfo("Manufacturer_X", "Model_X", supportedCommands);
        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails("lpwanContextPath", supportedDevice);
        deviceType.setLpwanCodecDetails(lpwanCodecDetails);

        OperationRepresentation operation = new OperationRepresentation();
        operation.setId(GId.asGId("SOURCE_DEVICE_ID"));
        operation.setDeviceId(GId.asGId(5461));
        operation.setDeviceName("Sample Device");
        operation.setProperty("c8y_Command", new Command(deviceCommand2.getCommand()));
        operation.setProperty("description", "Execute shell command:position request");

        assertTrue(lpwanCodecService.isCodecGeneratedCommand(operation, deviceType));
    }

    @Test
    public void commandShouldNotBeCodecGeneratedAsItIsNotSupported() {
        DeviceType deviceType = new DeviceType();

        DeviceCommand deviceCommand1 = new DeviceCommand("set config", "TestCategory", "{\"set config\":{\"breakpoint\":\"false\",\"selfadapt\":\"true\",\"oneoff\":\"false\",\"alreport\":\"false\",\"pos\":\"0\",\"hb\":\"10\"}}");
        DeviceCommand deviceCommand2 = new DeviceCommand("position request", "TestCategory", "{\"request pos\":{\"latitude\":\"10.35\",\"selfadapt\":\"-1.36\"");
        Set<DeviceCommand> supportedCommands = new HashSet<>();
        supportedCommands.addAll(Arrays.asList(deviceCommand1, deviceCommand2));

        DeviceInfo supportedDevice = new DeviceInfo("Manufacturer_X", "Model_X", supportedCommands);
        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails("lpwanContextPath", supportedDevice);
        deviceType.setLpwanCodecDetails(lpwanCodecDetails);

        OperationRepresentation operation = new OperationRepresentation();
        operation.setId(GId.asGId("SOURCE_DEVICE_ID"));
        operation.setDeviceId(GId.asGId(5461));
        operation.setDeviceName("Sample Device");
        operation.setProperty("c8y_Command", new Command("location request -place blr"));
        operation.setProperty("description", "Execute shell command:location request");

        assertFalse(lpwanCodecService.isCodecGeneratedCommand(operation, deviceType));
    }

    @Test
    public void commandShouldNotBeCodecGeneratedAsThereAreNoLpwanDetailsOrEmpty() {
        DeviceType deviceType = new DeviceType();
        DeviceCommand deviceCommand1 = new DeviceCommand("set config", "TestCategory", "{\"set config\":{\"breakpoint\":\"false\",\"selfadapt\":\"true\",\"oneoff\":\"false\",\"alreport\":\"false\",\"pos\":\"0\",\"hb\":\"10\"}}");
        DeviceCommand deviceCommand2 = new DeviceCommand("position request", "TestCategory", "{\"request pos\":{\"latitude\":\"10.35\",\"selfadapt\":\"-1.36\"");
        Set<DeviceCommand> supportedCommands = new HashSet<>();
        supportedCommands.addAll(Arrays.asList(deviceCommand1, deviceCommand2));

        DeviceInfo supportedDevice = new DeviceInfo("Manufacturer_X", "Model_X", supportedCommands);
        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails("lpwanContextPath", supportedDevice);

        OperationRepresentation operation = new OperationRepresentation();
        operation.setId(GId.asGId("SOURCE_DEVICE_ID"));
        operation.setDeviceId(GId.asGId(5461));
        operation.setDeviceName("Sample Device");
        operation.setProperty("c8y_Command", new Command(deviceCommand2.getCommand()));
        operation.setProperty("description", "Execute shell command:position request");

        assertFalse(lpwanCodecService.isCodecGeneratedCommand(operation, deviceType));

        deviceType.setLpwanCodecDetails(lpwanCodecDetails);
        operation.setProperty("description", "Execute shell command:");
        assertFalse(lpwanCodecService.isCodecGeneratedCommand(operation, deviceType));
    }

    @Test
    public void shouldTestEncode_1() throws LpwanCodecServiceException {
        DeviceCommand deviceCommand1 = new DeviceCommand("set config", "TestCategory", "{\"set config\":{\"breakpoint\":\"false\",\"selfadapt\":\"true\",\"oneoff\":\"false\",\"alreport\":\"false\",\"pos\":\"0\",\"hb\":\"10\"}}");
        DeviceCommand deviceCommand2 = new DeviceCommand("position request", "TestCategory", "{\"request pos\":{\"latitude\":\"10.35\",\"selfadapt\":\"-1.36\"");
        Set<DeviceCommand> supportedCommands = new HashSet<>();
        supportedCommands.addAll(Arrays.asList(deviceCommand1, deviceCommand2));

        DeviceInfo supportedDevice = new DeviceInfo("Manufacturer_X", "Model_X", supportedCommands);
        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails("lpwanContextPath", supportedDevice);
        DeviceType deviceType = new DeviceType();
        deviceType.setLpwanCodecDetails(lpwanCodecDetails);
        deviceType.setFieldbusType("lpwan");

        ManagedObjectRepresentation source = ManagedObjects.asManagedObject(GId.asGId("12345"));
        String devEui = "AAABBCC01";

        OperationRepresentation operation = new OperationRepresentation();
        operation.setId(GId.asGId("SOURCE_DEVICE_ID"));
        operation.setDeviceId(GId.asGId(5461));
        operation.setDeviceName("Sample Device");
        operation.setProperty("c8y_Command", new Command(deviceCommand2.getCommand()));
        operation.setProperty("description", "Execute shell command:position request");

        MicroserviceCredentials credentials = new MicroserviceCredentials("tenant", "username", "password", null, null, null, "appKey");
        when(subscriptionsService.getTenant()).thenReturn("tenant");
        when(subscriptionsService.getCredentials(eq("tenant"))).thenReturn(Optional.of(credentials));

        when(webClient.post()).thenReturn(post);
        when(post.uri(eq("/service/" + lpwanCodecDetails.getCodecServiceContextPath() + "/encode"))).thenReturn(uri);
        when(uri.header(eq(HttpHeaders.AUTHORIZATION), eq(credentials.toCumulocityCredentials().getAuthenticationString()))).thenReturn(uri);
        when(uri.body(inputMonoCaptor.capture(), eq(LpwanEncoderInputData.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LpwanEncoderResult.class)).thenReturn(lpwanEncoderResultMono);

        LpwanEncoderResult lpwanEncoderResult = new LpwanEncoderResult("6FSBC01", 41);
        when(lpwanEncoderResultMono.block(eq(WebClientFactory.DEFAULT_TIMEOUT_IN_MILLIS))).thenReturn(lpwanEncoderResult);

        lpwanCodecService.encode(deviceType, source, devEui, operation);

        LpwanEncoderInputData capturedInputData = inputMonoCaptor.getValue().block(WebClientFactory.DEFAULT_TIMEOUT_IN_MILLIS);
        assertEquals(source.getId().getValue(), capturedInputData.getSourceDeviceId());
        assertEquals(devEui, capturedInputData.getSourceDeviceEui());
        assertEquals(lpwanCodecDetails.getSupportedDevice().getDeviceManufacturer(), capturedInputData.getSourceDeviceInfo().getDeviceManufacturer());
        assertEquals(lpwanCodecDetails.getSupportedDevice().getDeviceModel(), capturedInputData.getSourceDeviceInfo().getDeviceModel());
        assertEquals(deviceCommand2.getName(), capturedInputData.getCommandName());
        assertEquals(deviceCommand2.getCommand(), capturedInputData.getCommandData());
    }

    @Test
    public void shouldTestEncode_2() {
        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails();
        DeviceType deviceType = new DeviceType();
        deviceType.setLpwanCodecDetails(lpwanCodecDetails);
        deviceType.setFieldbusType("lpwan");

        ManagedObjectRepresentation source = ManagedObjects.asManagedObject(GId.asGId("12345"));
        String devEui = "AAABBCC01";

        OperationRepresentation operation = new OperationRepresentation();
        operation.setId(GId.asGId("SOURCE_DEVICE_ID"));
        operation.setDeviceId(GId.asGId(5461));
        operation.setDeviceName("Sample Device");
        operation.setProperty("c8y_Command", new Command());
        operation.setProperty("description", "Execute shell command:position request");

        LpwanCodecServiceException exception = assertThrows(LpwanCodecServiceException.class,
                () ->  { lpwanCodecService.encode(deviceType, source, devEui, operation); });
        assertEquals(String.format("'c8y_LpwanCodecDetails' fragment in the device type associated with device EUI '%s' is invalid.", devEui), exception.getMessage());
    }

    @Test
    public void shouldTestEncode_3() throws LpwanCodecServiceException {
        DeviceCommand deviceCommand1 = new DeviceCommand("set config", "TestCategory", "{\"set config\":{\"breakpoint\":\"false\",\"selfadapt\":\"true\",\"oneoff\":\"false\",\"alreport\":\"false\",\"pos\":\"0\",\"hb\":\"10\"}}");
        DeviceCommand deviceCommand2 = new DeviceCommand("position request", "TestCategory", "{\"request pos\":{\"latitude\":\"10.35\",\"selfadapt\":\"-1.36\"");
        Set<DeviceCommand> supportedCommands = new HashSet<>();
        supportedCommands.addAll(Arrays.asList(deviceCommand1, deviceCommand2));

        DeviceInfo supportedDevice = new DeviceInfo("Manufacturer_X", "Model_X", supportedCommands);
        LpwanCodecDetails lpwanCodecDetails = new LpwanCodecDetails("lpwanContextPath", supportedDevice);
        DeviceType deviceType = new DeviceType();
        deviceType.setLpwanCodecDetails(lpwanCodecDetails);
        deviceType.setFieldbusType("lpwan");

        ManagedObjectRepresentation source = ManagedObjects.asManagedObject(GId.asGId("12345"));
        String devEui = "AAABBCC01";

        OperationRepresentation operation = new OperationRepresentation();
        operation.setId(GId.asGId("SOURCE_DEVICE_ID"));
        operation.setDeviceId(GId.asGId(5461));
        operation.setDeviceName("Sample Device");
        operation.setProperty("c8y_Command", new Command(deviceCommand2.getCommand()));
        operation.setProperty("description", "Execute shell command:position request");

        MicroserviceCredentials credentials = new MicroserviceCredentials("tenant", "username", "password", null, null, null, "appKey");
        when(subscriptionsService.getTenant()).thenReturn("tenant");
        when(subscriptionsService.getCredentials(eq("tenant"))).thenReturn(Optional.of(credentials));

        when(webClient.post()).thenReturn(post);
        when(post.uri(eq("/service/" + lpwanCodecDetails.getCodecServiceContextPath() + "/encode"))).thenReturn(uri);
        when(uri.header(eq(HttpHeaders.AUTHORIZATION), eq(credentials.toCumulocityCredentials().getAuthenticationString()))).thenReturn(uri);
        when(uri.body(inputMonoCaptor.capture(), eq(LpwanEncoderInputData.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(LpwanEncoderResult.class)).thenReturn(lpwanEncoderResultMono);

        when(lpwanEncoderResultMono.block(eq(WebClientFactory.DEFAULT_TIMEOUT_IN_MILLIS))).thenThrow(new RuntimeException("Failed to invoke Encoder service."));

        LpwanCodecServiceException exception = assertThrows(LpwanCodecServiceException.class,
                () -> { lpwanCodecService.encode(deviceType, source, devEui, operation); } );

        LpwanEncoderInputData capturedInputData = inputMonoCaptor.getValue().block(WebClientFactory.DEFAULT_TIMEOUT_IN_MILLIS);
        assertEquals(source.getId().getValue(), capturedInputData.getSourceDeviceId());
        assertEquals(devEui, capturedInputData.getSourceDeviceEui());
        assertEquals(lpwanCodecDetails.getSupportedDevice().getDeviceManufacturer(), capturedInputData.getSourceDeviceInfo().getDeviceManufacturer());
        assertEquals(lpwanCodecDetails.getSupportedDevice().getDeviceModel(), capturedInputData.getSourceDeviceInfo().getDeviceModel());
        assertEquals(deviceCommand2.getName(), capturedInputData.getCommandName());
        assertEquals(deviceCommand2.getCommand(), capturedInputData.getCommandData());

        assertEquals(String.format("Error invoking the LPWAN /encode service with context path '%s', for encoding the command '%s'", lpwanCodecDetails.getCodecServiceContextPath(), deviceCommand2.getName()), exception.getMessage());
    }
}