package com.cumulocity.microservice.subscription.model.core;

import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.springframework.beans.factory.annotation.Value;

import static org.springframework.util.StringUtils.isEmpty;

public class PlatformProperties {
    @java.beans.ConstructorProperties({"applicationName", "url", "mqttUrl", "microserviceBoostrapUser", "subscriptionDelay", "subscriptionInitialDelay", "isolation"})
    public PlatformProperties(String applicationName, Supplier<String> url, Supplier<String> mqttUrl, Credentials microserviceBoostrapUser, Integer subscriptionDelay, Integer subscriptionInitialDelay, IsolationLevel isolation) {
        this.applicationName = applicationName;
        this.url = url;
        this.mqttUrl = mqttUrl;
        this.microserviceBoostrapUser = microserviceBoostrapUser;
        this.subscriptionDelay = subscriptionDelay;
        this.subscriptionInitialDelay = subscriptionInitialDelay;
        this.isolation = isolation;
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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof PlatformProperties)) return false;
        final PlatformProperties other = (PlatformProperties) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$applicationName = this.getApplicationName();
        final Object other$applicationName = other.getApplicationName();
        if (this$applicationName == null ? other$applicationName != null : !this$applicationName.equals(other$applicationName))
            return false;
        final Object this$url = this.getUrl();
        final Object other$url = other.getUrl();
        if (this$url == null ? other$url != null : !this$url.equals(other$url)) return false;
        final Object this$mqttUrl = this.getMqttUrl();
        final Object other$mqttUrl = other.getMqttUrl();
        if (this$mqttUrl == null ? other$mqttUrl != null : !this$mqttUrl.equals(other$mqttUrl)) return false;
        final Object this$microserviceBoostrapUser = this.getMicroserviceBoostrapUser();
        final Object other$microserviceBoostrapUser = other.getMicroserviceBoostrapUser();
        if (this$microserviceBoostrapUser == null ? other$microserviceBoostrapUser != null : !this$microserviceBoostrapUser.equals(other$microserviceBoostrapUser))
            return false;
        final Object this$subscriptionDelay = this.getSubscriptionDelay();
        final Object other$subscriptionDelay = other.getSubscriptionDelay();
        if (this$subscriptionDelay == null ? other$subscriptionDelay != null : !this$subscriptionDelay.equals(other$subscriptionDelay))
            return false;
        final Object this$subscriptionInitialDelay = this.getSubscriptionInitialDelay();
        final Object other$subscriptionInitialDelay = other.getSubscriptionInitialDelay();
        if (this$subscriptionInitialDelay == null ? other$subscriptionInitialDelay != null : !this$subscriptionInitialDelay.equals(other$subscriptionInitialDelay))
            return false;
        final Object this$isolation = this.getIsolation();
        final Object other$isolation = other.getIsolation();
        if (this$isolation == null ? other$isolation != null : !this$isolation.equals(other$isolation)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $applicationName = this.getApplicationName();
        result = result * PRIME + ($applicationName == null ? 43 : $applicationName.hashCode());
        final Object $url = this.getUrl();
        result = result * PRIME + ($url == null ? 43 : $url.hashCode());
        final Object $mqttUrl = this.getMqttUrl();
        result = result * PRIME + ($mqttUrl == null ? 43 : $mqttUrl.hashCode());
        final Object $microserviceBoostrapUser = this.getMicroserviceBoostrapUser();
        result = result * PRIME + ($microserviceBoostrapUser == null ? 43 : $microserviceBoostrapUser.hashCode());
        final Object $subscriptionDelay = this.getSubscriptionDelay();
        result = result * PRIME + ($subscriptionDelay == null ? 43 : $subscriptionDelay.hashCode());
        final Object $subscriptionInitialDelay = this.getSubscriptionInitialDelay();
        result = result * PRIME + ($subscriptionInitialDelay == null ? 43 : $subscriptionInitialDelay.hashCode());
        final Object $isolation = this.getIsolation();
        result = result * PRIME + ($isolation == null ? 43 : $isolation.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PlatformProperties;
    }

    public String toString() {
        return "PlatformProperties(applicationName=" + this.getApplicationName() + ", url=" + this.getUrl() + ", mqttUrl=" + this.getMqttUrl() + ", microserviceBoostrapUser=" + this.getMicroserviceBoostrapUser() + ", subscriptionDelay=" + this.getSubscriptionDelay() + ", subscriptionInitialDelay=" + this.getSubscriptionInitialDelay() + ", isolation=" + this.getIsolation() + ")";
    }

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

    public static class PlatformPropertiesBuilder {
        private String applicationName;
        private Supplier<String> url;
        private Supplier<String> mqttUrl;
        private Credentials microserviceBoostrapUser;
        private Integer subscriptionDelay;
        private Integer subscriptionInitialDelay;
        private IsolationLevel isolation;

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

        public PlatformProperties build() {
            return new PlatformProperties(applicationName, url, mqttUrl, microserviceBoostrapUser, subscriptionDelay, subscriptionInitialDelay, isolation);
        }

        public String toString() {
            return "PlatformProperties.PlatformPropertiesBuilder(applicationName=" + this.applicationName + ", url=" + this.url + ", mqttUrl=" + this.mqttUrl + ", microserviceBoostrapUser=" + this.microserviceBoostrapUser + ", subscriptionDelay=" + this.subscriptionDelay + ", subscriptionInitialDelay=" + this.subscriptionInitialDelay + ", isolation=" + this.isolation + ")";
        }
    }
}
