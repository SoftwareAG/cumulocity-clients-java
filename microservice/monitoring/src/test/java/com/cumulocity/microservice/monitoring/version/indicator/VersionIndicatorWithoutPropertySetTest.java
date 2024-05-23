package com.cumulocity.microservice.monitoring.version.indicator;

import com.cumulocity.microservice.monitoring.MockMvcTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;

@SpringBootTest
public class VersionIndicatorWithoutPropertySetTest extends MockMvcTestBase {

    /*
    This test enables the /version endpoint, but leaves the
    spring.info.build.location unset and fallback location also does not exist.
    In this case, the endpoint is expected to return "NOT FOUND (404)",
    because the application doesn't know its version.
    */
    @Test
    @WithMockUser(authorities = "ROLE_ACTUATOR")
    public void versionShouldNotBeAvailable() {

        when()
                .get("/version").
                then()
                .statusCode(SC_NOT_FOUND);
    }
}
