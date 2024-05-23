package com.cumulocity.microservice.monitoring.health.indicator.platform;

import com.cumulocity.microservice.monitoring.health.indicator.TestHealthIndicatorConfiguration;
import com.cumulocity.microservice.monitoring.MockMvcTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.endpoint.ApiVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;

@DirtiesContext
@SpringBootTest(classes = TestHealthIndicatorConfiguration.class)
public class PlatformNotPresentInContextTest extends MockMvcTestBase {

    @Test
    @WithMockUser(authorities = "ROLE_ACTUATOR")
    public void healthShouldBeUp() {
        given()
                .accept(ApiVersion.V3.getProducedMimeType().toString()).
        when()
                .get("/health").

        then()
                .statusCode(200)
                .contentType(JSON)
                .body("components.platform.status", equalTo(null));
    }
}
