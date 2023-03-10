package com.cumulocity.exporters.platform;

import com.cumulocity.exporters.common.OpenTelemetryExporterStrategy;
import com.cumulocity.exporters.common.OpenTelemetryExporterStrategyEnum;
import io.opentelemetry.api.OpenTelemetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class PlatformOpenTelemetryExporterStrategy implements OpenTelemetryExporterStrategy {

    @Autowired
    private ConfigurableBeanFactory beanFactory;

    @Override
    public OpenTelemetry getOpenTelemetry() {
        return (OpenTelemetry) beanFactory.getBean("platformOtel");
    }

    @Override
    public OpenTelemetryExporterStrategyEnum getExporterStrategyName() {
        return OpenTelemetryExporterStrategyEnum.PLATFORM;
    }
}
