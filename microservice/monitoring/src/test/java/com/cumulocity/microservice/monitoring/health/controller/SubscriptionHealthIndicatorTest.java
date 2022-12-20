package com.cumulocity.microservice.monitoring.health.controller;

import com.cumulocity.microservice.monitoring.health.controller.configuration.TestConfiguration;
import com.cumulocity.microservice.monitoring.health.controller.configuration.TestMicroserviceSubscriptionConfiguration;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.ApiVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@DirtiesContext(classMode = AFTER_CLASS)
@SpringBootTest(classes = { TestMicroserviceSubscriptionConfiguration.class, TestConfiguration.class },
        properties = {
        "management.endpoint.health.enabled=true",
        "management.endpoints.web.exposure.include=health"
})
public class SubscriptionHealthIndicatorTest {

    @Autowired
    private MicroserviceSubscriptionsService subscriptionsService;

    @Test
    @WithMockUser(authorities = "ROLE_ACTUATOR")
    public void healthShouldBeDown() {
        given(subscriptionsService.isRegisteredSuccessfully()).willReturn(false);

        RestAssuredMockMvc.given()
                .accept(ApiVersion.V3.getProducedMimeType().toString()).
        when()
                .get("/health").

        then()
                .statusCode(503)
                .contentType(JSON)
                .body("components.subscription.status", equalTo("DOWN"));
    }
}
