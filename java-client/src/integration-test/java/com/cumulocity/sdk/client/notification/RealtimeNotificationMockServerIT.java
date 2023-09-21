package com.cumulocity.sdk.client.notification;

import com.cumulocity.rest.representation.alarm.AlarmRepresentation;
import com.cumulocity.sdk.client.PlatformImpl;
import com.cumulocity.sdk.client.common.SystemPropertiesOverrider;
import com.cumulocity.sdk.client.inventory.InventoryIT;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.mock.action.ExpectationCallback;
import org.mockserver.mock.action.ExpectationResponseCallback;
import org.mockserver.model.*;
import org.mockserver.verify.VerificationTimes;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static com.cumulocity.model.authentication.CumulocityBasicCredentials.from;
import static java.lang.Integer.parseInt;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpClassCallback.callback;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class RealtimeNotificationMockServerIT {

    private static ClientAndServer mockServer;
    private static Properties cumulocityProps;
    private static SystemPropertiesOverrider props;
    private static Gson gson;

    private Subscriber<String, AlarmRepresentation> subscriber;

    @BeforeAll
    public static void createTenantWithApplication() throws IOException {
        cumulocityProps = new Properties();
        cumulocityProps.load(InventoryIT.class.getClassLoader().getResourceAsStream("cumulocity-test.properties"));
        props = new SystemPropertiesOverrider(cumulocityProps);
        mockServer = startClientAndServer(parseInt(props.get("mock.server.port")));
        gson = new Gson();
    }

    @AfterAll
    public static void removeTenantAndApplication() {
        mockServer.stop();
    }

    @BeforeEach
    public void setUp() throws IOException {
        subscriber = givenAlarmSubscriber();
    }

    @AfterEach
    public void cleanUp() {
        subscriber.disconnect();
    }

    @Test
    public void shouldResubscribeWhen402UnknownClientError() throws IOException {

        //given
        givenMockWithCallbackResponse(1, ".*/meta/handshake.*", MetaHandshakeSuccessfulResponseCallback.class);
        givenMockWithCallbackResponse(1, ".*/meta/connect.*", MetaConnectSuccessfulResponseCallback.class);
        givenMockWithCallbackResponse(2, ".*/meta/subscribe.*", MetaSubscribeErrorResponseCallback.class);

        //when
        subscribe(subscriber);

        //then
        wait(3000);
        mockServer.verify(request()
                .withPath("/notification/realtime")
                .withBody(new RegexBody(".*/meta/handshake.*")));

        mockServer.verify(request()
                .withPath("/notification/realtime")
                .withBody(new RegexBody(".*/meta/subscribe.*")), VerificationTimes.atLeast(2));
    }

    @Test
    public void shouldReHandshakeWhen402UnknownClientError() throws IOException {

        //given
        givenMockWithCallbackResponse(1, ".*/meta/handshake.*", MetaHandshakeErrorResponseCallback.class);
        givenMockWithCallbackResponse(2, ".*/meta/handshake.*", MetaHandshakeSuccessfulResponseCallback.class);
        givenMockWithCallbackResponse(1, ".*/meta/connect.*", MetaConnectErrorResponseCallback.class);
        givenMockWithCallbackResponse(2, ".*/meta/connect.*", MetaConnectSuccessfulResponseCallback.class);
        givenMockWithCallbackResponse(2, ".*/meta/subscribe.*", MetaSubscribeErrorResponseCallback.class);

        //when
        subscribe(subscriber);

        //then
        wait(3000);
        mockServer.verify(request()
                .withPath("/notification/realtime")
                .withBody(new RegexBody(".*/meta/handshake.*")), VerificationTimes.atLeast(2));

        mockServer.verify(request()
                .withPath("/notification/realtime")
                .withBody(new RegexBody(".*/meta/connect.*")), VerificationTimes.atLeast(2));

        mockServer.verify(request()
                .withPath("/notification/realtime")
                .withBody(new RegexBody(".*/meta/subscribe.*")), VerificationTimes.atLeast(2));
    }

    private void subscribe(Subscriber<String, AlarmRepresentation> subscriber) {
        subscriber.subscribe("/alarms/*", new SubscriptionListener<String, AlarmRepresentation>() {
            @Override
            public void onNotification(Subscription<String> subscription, AlarmRepresentation notification) {
            }

            @Override
            public void onError(Subscription<String> subscription, Throwable ex) {
            }
        });
    }

    private void wait(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Subscriber<String, AlarmRepresentation> givenAlarmSubscriber() throws IOException {
        return SubscriberBuilder.<String, AlarmRepresentation>anSubscriber()
                .withParameters(new PlatformImpl(props.get("mock.server.host") + ":" + props.get("mock.server.port"), from(props.get("mock.server.credentials"))))
                .withEndpoint(SubscriberBuilder.REALTIME)
                .withSubscriptionNameResolver(id -> id)
                .withDataType(AlarmRepresentation.class)
                .build();
    }

    private static void givenMockWithCallbackResponse(int repeatTimes, String requestBodyRegex, Class<? extends ExpectationCallback<? extends HttpMessage>> responseCallback) {
        mockServer.when(request()
                        .withHeader(Header.header("Content-Type", "application/json"))
                        .withBody(new RegexBody(requestBodyRegex))
                        .withPath("/notification/realtime"), Times.exactly(repeatTimes))
                .respond(callback().withCallbackClass(responseCallback));
    }

    public static class MetaSubscribeErrorResponseCallback implements ExpectationResponseCallback {
        @Override
        public HttpResponse handle(HttpRequest httpRequest) {
            String jsonBody = httpRequest.getBodyAsString().substring(1, httpRequest.getBodyAsString().length() - 1);
            Map map = gson.fromJson(jsonBody, Map.class);
            String requestId = (String) map.get("id");
            return response("[{\"channel\":\"/meta/subscribe\",\"id\":\"" + requestId + "\",\"subscription\":\"/alarms/*\",\"error\":\"402::Unknown client\",\"data\":null,\"successful\":false}]");
        }
    }

    public static class MetaConnectErrorResponseCallback implements ExpectationResponseCallback {
        @Override
        public HttpResponse handle(HttpRequest httpRequest) {
            String jsonBody = httpRequest.getBodyAsString().substring(1, httpRequest.getBodyAsString().length() - 1);
            Map map = gson.fromJson(jsonBody, Map.class);
            String requestId = (String) map.get("id");
            return response("[{\"channel\":\"/meta/connect\",\"id\":\"" + requestId + "\",\"error\":\"402::Unknown client\",\"data\":null,\"advice\":{\"interval\":0,\"reconnect\":\"handshake\"},\"successful\":false}]");
        }
    }

    public static class MetaConnectSuccessfulResponseCallback implements ExpectationResponseCallback {
        @Override
        public HttpResponse handle(HttpRequest httpRequest) {
            String jsonBody = httpRequest.getBodyAsString().substring(1, httpRequest.getBodyAsString().length() - 1);
            Map map = gson.fromJson(jsonBody, Map.class);
            String requestId = (String) map.get("id");
            return response("[{\"channel\":\"/meta/connect\",\"id\":\"" + requestId + "\",\"data\":null,\"advice\":{\"interval\":0,\"timeout\":5400000,\"reconnect\":\"retry\"},\"successful\":true}]");
        }
    }

    public static class MetaHandshakeSuccessfulResponseCallback implements ExpectationResponseCallback {
        @Override
        public HttpResponse handle(HttpRequest httpRequest) {
            String jsonBody = httpRequest.getBodyAsString().substring(1, httpRequest.getBodyAsString().length() - 1);
            Map map = gson.fromJson(jsonBody, Map.class);
            String requestId = (String) map.get("id");
            return response("[{\"ext\":{\"ack\":true},\"minimumVersion\":\"1.0\",\"clientId\":\"2s1128n70miv86f189yoto03tp9a\",\"supportedConnectionTypes\":[\"long-polling\",\"smartrest-long-polling\",\"websocket\"],\"data\":null,\"channel\":\"/meta/handshake\",\"id\":\"" + requestId + "\",\"version\":\"1.0\",\"successful\":true}]");
        }
    }

    public static class MetaHandshakeErrorResponseCallback implements ExpectationResponseCallback {
        @Override
        public HttpResponse handle(HttpRequest httpRequest) {
            String jsonBody = httpRequest.getBodyAsString().substring(1, httpRequest.getBodyAsString().length() - 1);
            Map map = gson.fromJson(jsonBody, Map.class);
            String requestId = (String) map.get("id");
            return response("[{\"ext\":{\"ack\":true},\"minimumVersion\":\"1.0\",\"clientId\":\"99yvbr3zrfl3901cxv3wjeekjo9\",\"supportedConnectionTypes\":[\"long-polling\",\"smartrest-long-polling\",\"websocket\"],\"data\":null,\"channel\":\"/meta/handshake\",\"id\":\"" + requestId + "\",\"version\":\"1.0\",\"successful\":true}]");
        }
    }
}
