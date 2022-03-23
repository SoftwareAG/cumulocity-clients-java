package com.cumulocity.microservice.lpwan.codec.rest;

import com.cumulocity.microservice.customdecoders.api.exception.DecoderServiceException;
import com.cumulocity.microservice.customdecoders.api.model.DecoderInputData;
import com.cumulocity.microservice.customdecoders.api.model.DecoderResult;
import com.cumulocity.microservice.customdecoders.api.service.DecoderService;
import com.cumulocity.microservice.customencoders.api.exception.EncoderServiceException;
import com.cumulocity.microservice.customencoders.api.model.EncoderInputData;
import com.cumulocity.microservice.customencoders.api.model.EncoderResult;
import com.cumulocity.microservice.customencoders.api.service.EncoderService;
import com.cumulocity.microservice.lpwan.codec.decoder.model.LpwanDecoderInputData;
import com.cumulocity.microservice.lpwan.codec.encoder.model.LpwanEncoderInputData;
import com.cumulocity.microservice.lpwan.codec.model.DeviceInfo;
import com.cumulocity.model.idtype.GId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, TestBeanConfiguration.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "server.port=9192")
public class CodecControllerContractTest {
    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    DecoderService decoderService;

    @Autowired
    EncoderService encoderService;

    private MockMvc mockMvc;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String applicationUrl = "http://localhost:9192";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldThrowExceptionIfDifferentInputTypeIsPassed() throws URISyntaxException {
        final String baseUrl = applicationUrl + "/decode";
        URI uri = new URI(baseUrl);

        String decoderInputData = "{\"test\": null}";

        HttpClientErrorException httpClientErrorException = assertThrows(HttpClientErrorException.UnsupportedMediaType.class,
                () -> {
                    ResponseEntity<DecoderResult> result = restTemplate.postForEntity(uri, decoderInputData, DecoderResult.class);
                });
        assertNotNull(httpClientErrorException, "Contract has changed as the string input type does not match the DecoderInputData type required. " + httpClientErrorException.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfDifferentInputTypeIsPassedEncode() throws URISyntaxException {
        final String baseUrl = applicationUrl + "/encode";
        URI uri = new URI(baseUrl);

        String encoderInputData = "{\"test\": null}";

        HttpClientErrorException httpClientErrorException = assertThrows(HttpClientErrorException.UnsupportedMediaType.class,
                () -> {
                    ResponseEntity<EncoderResult> result = restTemplate.postForEntity(uri, encoderInputData, EncoderResult.class);
                });
        assertNotNull(httpClientErrorException, "Contract has changed as the string input type does not match the EncoderInputData type required. " + httpClientErrorException.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfNullInputIsPassed() throws URISyntaxException {
        final String baseUrl = applicationUrl + "/decode";
        URI uri = new URI(baseUrl);

        DecoderInputData decoderInputData = null;
        HttpClientErrorException httpClientErrorException = assertThrows(HttpClientErrorException.UnsupportedMediaType.class,
                () -> {
                    ResponseEntity<DecoderResult> result = restTemplate.postForEntity(uri, decoderInputData, DecoderResult.class);
                });
        assertNotNull(httpClientErrorException, "Contract has changed as null input data is passed. " + httpClientErrorException.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfNullInputIsPassedEncode() throws URISyntaxException {
        final String baseUrl = applicationUrl + "/encode";
        URI uri = new URI(baseUrl);

        EncoderInputData encoderInputData = null;
        HttpClientErrorException httpClientErrorException = assertThrows(HttpClientErrorException.UnsupportedMediaType.class,
                () -> {
                    ResponseEntity<EncoderResult> result = restTemplate.postForEntity(uri, encoderInputData, EncoderResult.class);
                });
        assertNotNull(httpClientErrorException, "Contract has changed as null input data is passed. " + httpClientErrorException.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfUnsupportedContentTypeIsUsed() throws URISyntaxException {
        final String baseUrl = applicationUrl + "/decode";
        URI uri = new URI(baseUrl);

        DecoderInputData decoderInputData = new DecoderInputData();
        decoderInputData.setValue("HEX VALUE");
        decoderInputData.setSourceDeviceId("SOURCE_DEVICE_ID");

        HashMap<String, String> args = new HashMap<>();
        args.put(LpwanDecoderInputData.SOURCE_DEVICE_EUI_KEY, "SOURCE_DEVICE_EUI_ID");
        args.put(DeviceInfo.DEVICE_MANUFACTURER, "MANUFACTURER_1");
        args.put(DeviceInfo.DEVICE_MODEL, "MODEL_1");
        decoderInputData.setArgs(args);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<DecoderInputData> entity = new HttpEntity<>(decoderInputData, headers);

        RestClientException restClientException = assertThrows(RestClientException.class, () -> {
            ResponseEntity<DecoderResult> result = restTemplate.postForEntity(uri, entity, DecoderResult.class);
        });
        assertNotNull(restClientException, "Contract does not support non-JSON Content Types. " + restClientException.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfUnsupportedContentTypeIsUsedEncode() throws URISyntaxException {
        final String baseUrl = applicationUrl + "/encode";
        URI uri = new URI(baseUrl);

        EncoderInputData encoderInputData = new EncoderInputData();
        encoderInputData.setSourceDeviceId("SOURCE_DEVICE_ID");
        encoderInputData.setCommandName("set config");
        encoderInputData.setCommandData("{\"set config\":{\"breakpoint\":\"false\",\"selfadapt\":\"true\",\"oneoff\":\"false\",\"alreport\":\"false\",\"pos\":\"0\",\"hb\":\"10\"}}");

        HashMap<String, String> args = new HashMap<>();
        args.put(LpwanEncoderInputData.SOURCE_DEVICE_EUI_KEY, "SOURCE_DEVICE_EUI_ID");
        args.put(DeviceInfo.DEVICE_MANUFACTURER, "MANUFACTURER_1");
        args.put(DeviceInfo.DEVICE_MODEL, "MODEL_1");
        encoderInputData.setArgs(args);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<EncoderInputData> entity = new HttpEntity<>(encoderInputData, headers);

        RestClientException restClientException = assertThrows(RestClientException.class, () -> {
            ResponseEntity<EncoderResult> result = restTemplate.postForEntity(uri, entity, EncoderResult.class);
        });
        assertNotNull(restClientException, "Contract does not support non-JSON Content Types. " + restClientException.getMessage());
    }

    @Test
    public void validateDecoderInputContract() {
        Field[] fields = DecoderInputData.class.getDeclaredFields();
        long fieldCount = Arrays.stream(fields).count();
        List<String> fieldNames = Arrays.stream(fields).map(Field::getName).collect(Collectors.toList());
        assertEquals(5, fieldCount);
        List<String> expectedFieldNames = Arrays.asList("serviceKey", "value", "args", "sourceDeviceId", "status");
        assertEquals(expectedFieldNames, fieldNames);
    }

    @Test
    public void validateDecoderResultContract() {
        Field[] fields = DecoderResult.class.getDeclaredFields();
        long fieldCount = Arrays.stream(fields).count();
        List<String> fieldNames = Arrays.stream(fields).map(Field::getName).collect(Collectors.toList());
        assertEquals(9, fieldCount);
        List<String> expectedFieldNames = Arrays.asList("internalServiceAlarms", "internalServiceEvents", "alarms", "alarmTypesToUpdate", "events", "measurements", "dataFragments", "message", "success");
        assertEquals(expectedFieldNames, fieldNames);
    }

    @Test
    public void validateEncoderInputContract() {
        Field[] fields = EncoderInputData.class.getDeclaredFields();
        long fieldCount = Arrays.stream(fields).count();
        List<String> fieldNames = Arrays.stream(fields).map(Field::getName).collect(Collectors.toList());
        assertEquals(5, fieldCount);
        List<String> expectedFieldNames = Arrays.asList("commandName", "commandData", "sourceDeviceId", "args", "status");
        assertEquals(expectedFieldNames, fieldNames);
    }

    @Test
    public void validateEncoderResultContract() {
        Field[] fields = EncoderResult.class.getDeclaredFields();
        long fieldCount = Arrays.stream(fields).count();
        List<String> fieldNames = Arrays.stream(fields).map(Field::getName).collect(Collectors.toList());
        assertEquals(4, fieldCount);
        List<String> expectedFieldNames = Arrays.asList("encodedCommand", "properties", "message", "success");
        assertEquals(expectedFieldNames, fieldNames);
    }

    @Test
    public void validContractTest() throws URISyntaxException, DecoderServiceException {
        final String baseUrl = applicationUrl + "/decode";
        URI uri = new URI(baseUrl);

        DecoderInputData decoderInputData = new DecoderInputData();
        decoderInputData.setValue("HEX VALUE");
        decoderInputData.setSourceDeviceId("SOURCE_DEVICE_ID");

        HashMap<String, String> args = new HashMap<>();
        args.put(LpwanDecoderInputData.SOURCE_DEVICE_EUI_KEY, "SOURCE_DEVICE_EUI_ID");
        args.put(DeviceInfo.DEVICE_MANUFACTURER, "MANUFACTURER_1");
        args.put(DeviceInfo.DEVICE_MODEL, "MODEL_1");
        decoderInputData.setArgs(args);

        DecoderResult decoderResult = new DecoderResult();
        decoderResult.setSuccess(true);

        when(decoderService.decode(eq("HEX VALUE"), eq(GId.asGId("SOURCE_DEVICE_ID")), eq(args))).thenReturn(decoderResult);

        ResponseEntity<DecoderResult> result = restTemplate.postForEntity(uri, decoderInputData, DecoderResult.class);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void validContractTestEncode() throws URISyntaxException, EncoderServiceException {
        final String baseUrl = applicationUrl + "/encode";
        URI uri = new URI(baseUrl);

        EncoderInputData encoderInputData = new EncoderInputData();
        encoderInputData.setSourceDeviceId("SOURCE_DEVICE_ID");
        encoderInputData.setCommandName("set config");
        encoderInputData.setCommandData("{\"set config\":{\"breakpoint\":\"false\",\"selfadapt\":\"true\",\"oneoff\":\"false\",\"alreport\":\"false\",\"pos\":\"0\",\"hb\":\"10\"}}");

        HashMap<String, String> args = new HashMap<>();
        args.put(LpwanEncoderInputData.SOURCE_DEVICE_EUI_KEY, "SOURCE_DEVICE_EUI_ID");
        args.put(DeviceInfo.DEVICE_MANUFACTURER, "MANUFACTURER_1");
        args.put(DeviceInfo.DEVICE_MODEL, "MODEL_1");
        encoderInputData.setArgs(args);

        EncoderResult encoderResult = new EncoderResult();
        encoderResult.setSuccess(true);

        when(encoderService.encode(encoderInputData)).thenReturn(encoderResult);

        ResponseEntity<EncoderResult> result = restTemplate.postForEntity(uri, encoderInputData, EncoderResult.class);
        Assert.assertEquals(200, result.getStatusCodeValue());
    }
}
