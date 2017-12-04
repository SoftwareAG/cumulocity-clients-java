package com.cumulocity.microservice.platform.api.annotation;

import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.context.inject.TenantScope;
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
import com.cumulocity.sdk.client.user.UserApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CumulocityClientFeature {

    @Value("${C8Y.baseURL:${platform.url:http://localhost:8181}}")
    private String host;

    @Value("${C8Y.proxy:}")
    private String proxyHost;

    @Value("${C8Y.proxyPort:0}")
    private Integer proxyPort;

    @Bean
    @TenantScope
    public PlatformImpl tenantPlatform(final ContextService<MicroserviceCredentials> contextService) {
        final MicroserviceCredentials login = contextService.getContext();

        return (PlatformImpl) PlatformBuilder.platform()
                .withBaseUrl(host)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withTenant(login.getTenant())
                .withPassword(login.getPassword())
                .withUsername(login.getUsername())
                .withTfaToken(login.getTfaToken())
                .build();
    }

    @Bean
    @TenantScope
    public RestConnector connector(PlatformParameters platformParameters) {
        return new RestConnector(platformParameters, new ResponseParser());
    }

    @Bean
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
}
