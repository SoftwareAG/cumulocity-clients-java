package com.cumulocity.agent.server.feature;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import c8y.inject.ContextScope;
import c8y.inject.DeviceScope;

import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.agent.server.context.DeviceCredentials;
import com.cumulocity.agent.server.context.scope.ContextScopedSubscriber;
import com.cumulocity.model.authentication.CumulocityCredentials;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
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
import com.cumulocity.sdk.client.notification.Subscriber;
import com.google.common.base.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class CumulocityClientFeature {
    
    private final Logger log = LoggerFactory.getLogger(CumulocityClientFeature.class);

    public static final String C8Y_HOST_PROP = "C8Y.baseURL";

    public static final String C8Y_PROXY_PROP = "C8Y.proxy";

    public static final String C8Y_PROXY_PORT_PROP = "C8Y.proxyPort";
    
    public static final String C8Y_FORCE_INITIAL_HOST = "C8Y.forceInitialHost";

    @Value("${" + C8Y_HOST_PROP + "}")
    private String host;

    @Value("${" + C8Y_PROXY_PROP + ":}")
    private String proxy;

    @Value("${" + C8Y_PROXY_PORT_PROP + ":0}")
    private Integer proxyPort;

    @Value("${" + C8Y_FORCE_INITIAL_HOST + ":false}")
    private boolean forceInitialHost;
    
    @Bean
    @Autowired
    @DeviceScope
    public CumulocityClientFactoryBean cumulocityClient(DeviceContextService contextService) {
        if(forceInitialHost) {
            log.debug("Client will prefer {} value instead of self links when making requests.", C8Y_HOST_PROP);
        }
        return new CumulocityClientFactoryBean(contextService, host, proxy, proxyPort, forceInitialHost);
    }

    @Bean
    @Autowired
    @DeviceScope
    public RestConnector connector(PlatformParameters platformParameters) {
        return new RestConnector(platformParameters, new ResponseParser());
    }

    @Bean
    @Autowired
    @ContextScope
    public InventoryApi inventoryApi(Platform platform) throws SDKException {
        return platform.getInventoryApi();
    }

    @Bean
    @Autowired
    @ContextScope
    public DeviceCredentialsApi deviceCredentialsApi(Platform platform) throws SDKException {
        return platform.getDeviceCredentialsApi();
    }

    @Bean
    @Autowired
    @ContextScope
    public EventApi eventApi(Platform platform) throws SDKException {
        return platform.getEventApi();
    }

    @Bean
    @Autowired
    @ContextScope
    public MeasurementApi measurementApi(Platform platform) throws SDKException {
        return platform.getMeasurementApi();
    }

    @Bean
    @Autowired
    @ContextScope
    public IdentityApi identityApi(Platform platform) throws SDKException {
        return platform.getIdentityApi();
    }

    @Bean
    @Autowired
    @ContextScope
    public AlarmApi alarmApi(Platform platform) throws SDKException {
        return platform.getAlarmApi();
    }

    @Bean
    @Autowired
    @ContextScope
    public AuditRecordApi auditRecordApi(Platform platform) throws SDKException {
        return platform.getAuditRecordApi();
    }

    @Bean
    @Autowired
    @ContextScope
    public DeviceControlApi deviceControlApi(Platform platform) throws SDKException {
        return platform.getDeviceControlApi();
    }

    @Bean
    @Autowired
    @ContextScope
    public CepApi cepApi(Platform platform) throws SDKException {
        return platform.getCepApi();
    }
    
    @Bean
    @Autowired
    @ContextScope
    public BinariesApi binariesApi(Platform platform) throws SDKException {
        return platform.getBinariesApi();
    }

    @Bean(destroyMethod = "disconnect")
    @Autowired
    @DeviceScope
    public Subscriber<GId, OperationRepresentation> deviceControlNotificationsSubscriber(final DeviceContextService contextService,
            final DeviceControlApi deviceControlApi) throws SDKException {
        return new ContextScopedSubscriber<GId, OperationRepresentation>(deviceControlApi.getNotificationsSubscriber(), contextService);
    }

    @Bean(destroyMethod = "disconnect")
    @Autowired
    @DeviceScope
    public Subscriber<String, Object> customNotificationsSubscriber(CepApi cepApi, DeviceContextService contextService) {
        return new ContextScopedSubscriber<String, Object>(cepApi.getCustomNotificationsSubscriber(), contextService);
    }

    public static class CumulocityClientFactoryBean extends AbstractFactoryBean<PlatformImpl> implements FactoryBean<PlatformImpl> {

        private final DeviceContextService contextService;

        private final String host;
        
        private final boolean forceInitialHost;

        private final Optional<String> proxy;

        private final Optional<Integer> proxyPort;

        public CumulocityClientFactoryBean(DeviceContextService contextService, String host, String proxy, Integer proxyPort, 
                boolean forceInitialHost) {
            this.contextService = contextService;
            this.host = host;
            this.proxy = Optional.fromNullable(proxy);
            this.proxyPort = Optional.fromNullable(proxyPort);
            this.forceInitialHost = forceInitialHost;
        }

        @Override
        protected PlatformImpl createInstance() throws Exception {
            return create(contextService.getCredentials());
        }

        private PlatformImpl create(DeviceCredentials login) throws Exception {
            final PlatformImpl platformImpl = new PlatformImpl(host, new CumulocityCredentials(login.getTenant(), login.getUsername(), login.getPassword(),
                    login.getAppKey()), new ClientConfiguration(null, false), login.getPageSize());
            platformImpl.setForceInitialHost(forceInitialHost);

            return proxy(platformImpl);
        }

        private PlatformImpl proxy(PlatformImpl platform) {
            String proxyHost = proxy.or("");
            if (proxyHost.length() > 0) {
                platform.setProxyHost(proxyHost);
            }
            Integer port = proxyPort.or(0);
            if (port > 0) {
                platform.setProxyPort(port);
            }
            return platform;
        }

        @Override
        public Class<?> getObjectType() {
            return PlatformImpl.class;
        }

        @Override
        public boolean isSingleton() {
            return true;
        }

    }

}
