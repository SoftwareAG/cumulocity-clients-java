package com.cumulocity.exporters.platform;

import com.cumulocity.exporters.common.OpenTelemetryExporterStrategy;
import com.cumulocity.exporters.common.OpenTelemetryExporterStrategyEnum;
import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.context.inject.TenantScope;
import com.cumulocity.sdk.client.*;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.MetricReader;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Configuration
@Slf4j
public class PlatformOpenTelemetryConfiguration {
//    @Autowired
//    ScheduledExecutorService scheduledExecutorService;
    @Value("${C8Y.baseURL}")
    private String baseURL;

    @Autowired
    private ContextService<MicroserviceCredentials> contextService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Bean("platformOtel")
    @TenantScope
    public OpenTelemetry initOpenTelemetry() {
//        PlatformExporter platformExporter = new PlatformExporter(contextService.getContext().getTenant(), applicationContext);
        // Create an instance of PeriodicMetricReader and configure it
        // to export via the logging exporter
        Platform platform = PlatformBuilder.platform().withBaseUrl(baseURL).withForceInitialHost(true).withCredentials(contextService.getContext().toCumulocityCredentials()).build();
        RestConnector restConnector = new RestConnector((PlatformParameters) platform, new ResponseParser());
        PlatformMetricExporter platformExporter = new PlatformMetricExporter(restConnector, applicationEventPublisher);
        MetricReader periodicReader =
                PeriodicMetricReader.builder(platformExporter)
                        .setInterval(Duration.ofMinutes(2))
                        .build();
        // This will be used to create instruments
        SdkMeterProvider meterProvider =
                SdkMeterProvider.builder().registerMetricReader(periodicReader).build();

//        return OpenTelemetrySdk.builder()
//                .setMeterProvider(meterProvider)
//                .buildAndRegisterGlobal();
        OpenTelemetry openTelemetry = null;
        try {
            openTelemetry  = OpenTelemetrySdk.builder()
                    .setMeterProvider(meterProvider)
                    .build();

            GlobalOpenTelemetry.set(openTelemetry);
        } catch(IllegalStateException ex) {
            log.error(ex.getMessage());
        }
        return openTelemetry;
    }

//    @Component
//    public class PlatformOpenTelemetryExporterStrategy implements OpenTelemetryExporterStrategy {
//
//        @Override
//        @Bean
//        @TenantScope
//        public OpenTelemetry getOpenTelemetry() {
//            return initOpenTelemetry();
//        }
//
//        @Override
//        public OpenTelemetryExporterStrategyEnum getExporterStrategyName() {
//            return OpenTelemetryExporterStrategyEnum.PLATFORM;
//        }
//    }
}
