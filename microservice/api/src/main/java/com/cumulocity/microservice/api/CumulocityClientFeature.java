package com.cumulocity.microservice.api;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.context.credentials.UserCredentials;
import com.cumulocity.microservice.context.inject.TenantScope;
import com.cumulocity.microservice.context.inject.UserScope;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.model.authentication.CumulocityCredentialsFactory;
import com.cumulocity.sdk.client.*;
import com.cumulocity.sdk.client.alarm.AlarmApi;
import com.cumulocity.sdk.client.audit.AuditRecordApi;
import com.cumulocity.sdk.client.cep.CepApi;
import com.cumulocity.sdk.client.devicecontrol.DeviceControlApi;
import com.cumulocity.sdk.client.devicecontrol.DeviceCredentialsApi;
import com.cumulocity.sdk.client.event.EventApi;
import com.cumulocity.sdk.client.identity.IdentityApi;
import com.cumulocity.sdk.client.inventory.BinariesApi;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.measurement.MeasurementApi;
import com.cumulocity.sdk.client.messaging.notifications.NotificationSubscriptionApi;
import com.cumulocity.sdk.client.messaging.notifications.TokenApi;
import com.cumulocity.sdk.client.option.SystemOptionApi;
import com.cumulocity.sdk.client.option.TenantOptionApi;
import com.cumulocity.sdk.client.user.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CumulocityClientFeature {

    @Value("${C8Y.baseURL:${platform.url:http://localhost:8181}}")
    private String host;

    @Value("${C8Y.proxy:}")
    private String proxyHost;

    @Value("${C8Y.proxyPort:0}")
    private Integer proxyPort;

    //in milliseconds, 0 = infinite
    @Value("${C8Y.httpReadTimeout:}")
    private Integer httpReadTimeout;

    @Bean
    @ConfigurationProperties(prefix = "c8y.httpclient")
    public HttpClientConfig httpClientConfig() {
        return HttpClientConfig.httpConfig().build();
    }

    @Autowired(required = false)
    private ResponseMapper responseMapper;

    @Bean
    @UserScope
    public PlatformImpl userPlatform(ContextService<UserCredentials> contextService) {
        final Credentials login = contextService.getContext();
        CumulocityCredentials credentials = new CumulocityCredentialsFactory()
                .withTenant(login.getTenant())
                .withUsername(login.getUsername())
                .withPassword(login.getPassword())
                .withOAuthAccessToken(login.getOAuthAccessToken())
                .withXsrfToken(login.getXsrfToken())
                .withApplicationKey(login.getAppKey())
                .getCredentials();
        PlatformImpl platform = (PlatformImpl) PlatformBuilder.platform()
                .withBaseUrl(host)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withCredentials(credentials)
                .withTfaToken(login.getTfaToken())
                .withResponseMapper(responseMapper)
                .withForceInitialHost(true)
                .build();
        setHttpClientConfig(platform);
        return platform;
    }

    @Bean
    @Primary
    @TenantScope
    public PlatformImpl tenantPlatform(ContextService<MicroserviceCredentials> contextService) {
        final Credentials login = contextService.getContext();
        CumulocityCredentials credentials = new CumulocityCredentialsFactory()
                .withTenant(login.getTenant())
                .withUsername(login.getUsername())
                .withPassword(login.getPassword())
                .withOAuthAccessToken(login.getOAuthAccessToken())
                .withXsrfToken(login.getXsrfToken())
                .withApplicationKey(login.getAppKey())
                .getCredentials();
        PlatformImpl platform = (PlatformImpl) PlatformBuilder.platform()
                .withBaseUrl(host)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withCredentials(credentials)
                .withTfaToken(login.getTfaToken())
                .withResponseMapper(responseMapper)
                .withForceInitialHost(true)
                .build();
        setHttpClientConfig(platform);
        return platform;
    }

    @Bean
    @UserScope
    public RestConnector userRestConnector(@Qualifier("userPlatform") PlatformParameters platformParameters) {
        return new RestConnector(platformParameters, new ResponseParser(responseMapper));
    }

    @Bean
    @Primary
    @TenantScope
    public RestConnector tenantRestConnector(@Qualifier("tenantPlatform") PlatformParameters platformParameters) {
        return new RestConnector(platformParameters, new ResponseParser(responseMapper));
    }

    @Bean
    @UserScope
    public InventoryApi userInventoryApi(@Qualifier("userPlatform") Platform userPlatform) throws SDKException {
        return userPlatform.getInventoryApi();
    }

    @Bean
    @Primary
    @TenantScope
    public InventoryApi inventoryApi(Platform platform) throws SDKException {
        return platform.getInventoryApi();
    }

    @Bean
    @TenantScope
    public DeviceCredentialsApi deviceCredentialsApi(Platform platform) throws SDKException {
        return platform.getDeviceCredentialsApi();
    }

    @Bean
    @TenantScope
    public EventApi eventApi(Platform platform) throws SDKException {
        return platform.getEventApi();
    }

    @Bean
    @TenantScope
    public MeasurementApi measurementApi(Platform platform) throws SDKException {
        return platform.getMeasurementApi();
    }

    @Bean
    @TenantScope
    public IdentityApi identityApi(Platform platform) throws SDKException {
        return platform.getIdentityApi();
    }

    @Bean
    @TenantScope
    public AlarmApi alarmApi(Platform platform) throws SDKException {
        return platform.getAlarmApi();
    }

    @Bean
    @TenantScope
    public AuditRecordApi auditRecordApi(Platform platform) throws SDKException {
        return platform.getAuditRecordApi();
    }

    @Bean
    @TenantScope
    public DeviceControlApi deviceControlApi(Platform platform) throws SDKException {
        return platform.getDeviceControlApi();
    }

    @Bean
    @TenantScope
    public CepApi cepApi(Platform platform) throws SDKException {
        return platform.getCepApi();
    }

    @Bean
    @TenantScope
    public BinariesApi binariesApi(Platform platform) throws SDKException {
        return platform.getBinariesApi();
    }

    @Bean
    @TenantScope
    public UserApi userApi(Platform platform) throws SDKException {
        return platform.getUserApi();
    }

    @Bean
    @UserScope
    public TenantOptionApi userTenantOptionApi(@Qualifier("userPlatform") Platform userPlatform) throws SDKException {
        return userPlatform.getTenantOptionApi();
    }

    @Bean
    @TenantScope
    @Primary
    public TenantOptionApi tenantOptionApi(Platform platform) throws SDKException {
        return platform.getTenantOptionApi();
    }

    @Bean
    @TenantScope
    public SystemOptionApi systemOptionApi(Platform platform) throws SDKException {
        return platform.getSystemOptionApi();
    }

    @Bean
    @TenantScope
    public TokenApi tokenApi(Platform platform) throws SDKException {
        return platform.getTokenApi();
    }

    @Bean
    @TenantScope
    public NotificationSubscriptionApi notificationSubscriptionApi(Platform platform) throws SDKException {
        return platform.getNotificationSubscriptionApi();
    }

    private void setHttpClientConfig(PlatformImpl platform) {
        HttpClientConfig httpClientConfig = httpClientConfig();
        if (httpReadTimeout != null) {
            httpClientConfig.setHttpReadTimeout(httpReadTimeout);
        }
        platform.setHttpClientConfig(httpClientConfig);
    }
}
