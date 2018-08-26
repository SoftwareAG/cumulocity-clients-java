package com.cumulocity.microservice.subscription.model.core;

import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.springframework.util.StringUtils.isEmpty;

public class PlatformProperties {
    @java.beans.ConstructorProperties({"applicationName", "url", "mqttUrl", "microserviceBoostrapUser", "subscriptionDelay", "subscriptionInitialDelay", "isolation"})
    public PlatformProperties(String applicationName, Supplier<String> url, Supplier<String> mqttUrl, Credentials microserviceBoostrapUser, Integer subscriptionDelay, Integer subscriptionInitialDelay, IsolationLevel isolation, boolean forceInitialHost) {
        this.applicationName = applicationName;
        this.url = url;
        this.mqttUrl = mqttUrl;
        this.microserviceBoostrapUser = microserviceBoostrapUser;
        this.subscriptionDelay = subscriptionDelay;
        this.subscriptionInitialDelay = subscriptionInitialDelay;
        this.isolation = isolation;
        this.forceInitialHost = forceInitialHost;
    }

    public static PlatformPropertiesBuilder builder() {
        return new PlatformPropertiesBuilder();
    }

    public String getApplicationName() {
        return this.applicationName;
    }

    public Supplier<String> getUrl() {
        return this.url;
    }

    public Supplier<String> getMqttUrl() {
        return this.mqttUrl;
    }

    public Credentials getMicroserviceBoostrapUser() {
        return this.microserviceBoostrapUser;
    }

    public Integer getSubscriptionDelay() {
        return this.subscriptionDelay;
    }

    public Integer getSubscriptionInitialDelay() {
        return this.subscriptionInitialDelay;
    }

    public IsolationLevel getIsolation() {
        return this.isolation;
    }

    public boolean getForceInitialHost() {
        return forceInitialHost;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setUrl(Supplier<String> url) {
        this.url = url;
    }

    public void setMqttUrl(Supplier<String> mqttUrl) {
        this.mqttUrl = mqttUrl;
    }

    public void setMicroserviceBoostrapUser(Credentials microserviceBoostrapUser) {
        this.microserviceBoostrapUser = microserviceBoostrapUser;
    }

    public void setSubscriptionDelay(Integer subscriptionDelay) {
        this.subscriptionDelay = subscriptionDelay;
    }

    public void setSubscriptionInitialDelay(Integer subscriptionInitialDelay) {
        this.subscriptionInitialDelay = subscriptionInitialDelay;
    }

    public void setIsolation(IsolationLevel isolation) {
        this.isolation = isolation;
    }


    public String toString() {
        return "PlatformProperties(applicationName=" + this.getApplicationName() + ", url=" + this.getUrl() + ", mqttUrl=" + this.getMqttUrl() + ", microserviceBoostrapUser=" + this.getMicroserviceBoostrapUser() + ", subscriptionDelay=" + this.getSubscriptionDelay() + ", subscriptionInitialDelay=" + this.getSubscriptionInitialDelay() + ", isolation=" + this.getIsolation() + ", forceInitialHost=" + this.getForceInitialHost() + " )";
    }

    public enum IsolationLevel {
        PER_TENANT, MULTI_TENANT
    }


    public static class PlatformPropertiesProvider {

        private final Logger log = LoggerFactory.getLogger(PlatformPropertiesProvider.class);

        @Value("${C8Y.bootstrap.register:true}")
        private boolean autoRegistration;

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
                            .oAuthAccessToken(null)
                            .xsrfToken(null)
                            .build())
                    .subscriptionDelay(microserviceSubscriptionDelay)
                    .subscriptionInitialDelay(microserviceSubscriptionInitialDelay)
                    .applicationName(name)
                    .isolation(isNullOrEmpty(isolation) ? null : IsolationLevel.valueOf(isolation))
                    .forceInitialHost(forceInitialHost)
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
    private boolean forceInitialHost;

    public static class PlatformPropertiesBuilder {
        private String applicationName;
        private Supplier<String> url;
        private Supplier<String> mqttUrl;
        private Credentials microserviceBoostrapUser;
        private Integer subscriptionDelay;
        private Integer subscriptionInitialDelay;
        private IsolationLevel isolation;
        private boolean forceInitialHost;

        PlatformPropertiesBuilder() {
        }

        public PlatformProperties.PlatformPropertiesBuilder applicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public PlatformProperties.PlatformPropertiesBuilder url(Supplier<String> url) {
            this.url = url;
            return this;
        }

        public PlatformProperties.PlatformPropertiesBuilder mqttUrl(Supplier<String> mqttUrl) {
            this.mqttUrl = mqttUrl;
            return this;
        }

        public PlatformProperties.PlatformPropertiesBuilder microserviceBoostrapUser(Credentials microserviceBoostrapUser) {
            this.microserviceBoostrapUser = microserviceBoostrapUser;
            return this;
        }

        public PlatformProperties.PlatformPropertiesBuilder subscriptionDelay(Integer subscriptionDelay) {
            this.subscriptionDelay = subscriptionDelay;
            return this;
        }

        public PlatformProperties.PlatformPropertiesBuilder subscriptionInitialDelay(Integer subscriptionInitialDelay) {
            this.subscriptionInitialDelay = subscriptionInitialDelay;
            return this;
        }

        public PlatformProperties.PlatformPropertiesBuilder isolation(IsolationLevel isolation) {
            this.isolation = isolation;
            return this;
        }

        public PlatformProperties.PlatformPropertiesBuilder forceInitialHost(boolean forceInitialHost) {
            this.forceInitialHost = forceInitialHost;
            return this;
        }

        public PlatformProperties build() {
            return new PlatformProperties(applicationName, url, mqttUrl, microserviceBoostrapUser, subscriptionDelay, subscriptionInitialDelay, isolation, forceInitialHost);
        }

        public String toString() {
            return "PlatformProperties.PlatformPropertiesBuilder(applicationName=" + this.applicationName + ", url=" + this.url + ", mqttUrl=" + this.mqttUrl + ", microserviceBoostrapUser=" + this.microserviceBoostrapUser + ", subscriptionDelay=" + this.subscriptionDelay + ", subscriptionInitialDelay=" + this.subscriptionInitialDelay + ", isolation=" + this.isolation + ")";
        }
    }
}
