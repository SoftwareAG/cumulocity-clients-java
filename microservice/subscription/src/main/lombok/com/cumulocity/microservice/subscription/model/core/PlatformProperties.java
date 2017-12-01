package com.cumulocity.microservice.subscription.model.core;

import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import static org.springframework.util.StringUtils.isEmpty;

@Data
@Builder
@AllArgsConstructor
public class PlatformProperties {
    enum IsolationLevel {
        PER_TENANT, MULTI_TENANT
    }

    public static class PlatformPropertiesProvider {
        @Value("${application.name:}")
        private String applicationName;

        @Value("${C8Y.baseURL:${platform.url:http://localhost:8181}}")
        private String url;

        @Value("${C8Y.baseURL.mqtt:${platform.url.mqtt:tcp://localhost:1883}}")
        private String mqttUrl;

        @Value("${C8Y.forceInitialHost:${platform.forceInitialHost:true}}")
        private boolean forceInitialHost;

        @Value("${C8Y.bootstrap.tenant:${platform.bootstrap.agent.tenant:management}}")
        private String microserviceBootstrapTenant;

        @Value("${C8Y.bootstrap.user:${platform.bootstrap.agent.name:servicebootstrap}}")
        private String microserviceBootstrapName;

        @Value("${C8Y.bootstrap.password:${platform.bootstrap.agent.password:!j5iBT0GE7,a73s2;4q51h_52m&6%#}}")
        private String microserviceBootstrapPassword;

        @Value("${C8Y.bootstrap.delay:${platform.bootstrap.agent.delay:10000}}")
        private int microserviceSubscriptionDelay;

        @Value("${C8Y.bootstrap.initialDelay:30000}")
        private int microserviceSubscriptionInitialDelay;

        @Value(value = "${C8Y.microservice.isolation:}")
        private String isolation;

        public PlatformProperties platformProperties(String defaultName) {
            String name;
            if (!isEmpty(this.applicationName)) {
                name = this.applicationName;
            } else {
                name = defaultName;
            }
            if (isEmpty(name)) {
                throw new IllegalStateException("Please set up application name");
            }
            return PlatformProperties.builder()
                    .url(Suppliers.ofInstance(url))
                    .mqttUrl(Suppliers.ofInstance(mqttUrl))
                    .microserviceBoostrapUser(MicroserviceCredentials.builder()
                            .tenant(microserviceBootstrapTenant)
                            .username(microserviceBootstrapName)
                            .password(microserviceBootstrapPassword)
                            .build())
                    .subscriptionDelay(microserviceSubscriptionDelay)
                    .subscriptionInitialDelay(microserviceSubscriptionInitialDelay)
                    .applicationName(name)
                    .isolation(Strings.isNullOrEmpty(isolation) ? null : IsolationLevel.valueOf(isolation))
                    .build();
        }
    }

    private String applicationName;
    private Supplier<String> url;
    private Supplier<String> mqttUrl;
    private Credentials microserviceBoostrapUser;
    private Integer subscriptionDelay;
    private Integer subscriptionInitialDelay;
    private IsolationLevel isolation;
}
