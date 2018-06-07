package com.cumulocity.microservice.health.indicator.platform;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("management.health.platform")
public class PlatformHealthIndicatorProperties {
    private String path = "/user/currentUser";
    private Details details = new Details();

    @Data
    public static class Details {
        private Boolean enabled = true;
        private String path = "/tenant/health";
    }
}
