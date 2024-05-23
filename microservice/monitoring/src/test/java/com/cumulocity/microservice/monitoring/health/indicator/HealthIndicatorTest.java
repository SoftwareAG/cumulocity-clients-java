package com.cumulocity.microservice.monitoring.health.indicator;

import com.cumulocity.microservice.monitoring.MockMvcTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.hamcrest.Matchers.equalTo;

@DirtiesContext
@SpringBootTest(classes = TestHealthIndicatorConfiguration.class)
public class HealthIndicatorTest extends MockMvcTestBase {

    @Test
    @WithMockUser(authorities = "ROLE_ACTUATOR")
    public void healthShouldBeUp() {
        when()
                .get("/health").

        then()
                .statusCode(200)
                .contentType(JSON)
                .body("status", equalTo("UP"));
    }
}
