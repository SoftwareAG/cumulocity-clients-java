package com.cumulocity.microservice.monitoring.health.indicator.subscribtion;

import com.cumulocity.microservice.monitoring.health.indicator.TestHealthIndicatorConfiguration;
import com.cumulocity.microservice.monitoring.MockMvcTestBase;
import com.cumulocity.microservice.subscription.service.MicroserviceSubscriptionsService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.ApiVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;

@DirtiesContext
@SpringBootTest(classes = {TestHealthIndicatorConfiguration.class, TestMicroserviceSubscriptionConfiguration.class})
@TestPropertySource(properties = {
        "management.endpoint.health.enabled=true",
        "management.endpoints.web.exposure.include=health"
})
public class SubscriptionHealthIndicatorTest extends MockMvcTestBase {

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
