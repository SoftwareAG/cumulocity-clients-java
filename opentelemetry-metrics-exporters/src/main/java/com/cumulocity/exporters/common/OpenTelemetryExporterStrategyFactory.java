package com.cumulocity.exporters.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class OpenTelemetryExporterStrategyFactory {
    private Map<OpenTelemetryExporterStrategyEnum, OpenTelemetryExporterStrategy> exporterStrategyMap;

    @Autowired
    public OpenTelemetryExporterStrategyFactory(Set<OpenTelemetryExporterStrategy> exporterStrategySet) {
        createStrategy(exporterStrategySet);
    }

    public OpenTelemetryExporterStrategy findStrategy(OpenTelemetryExporterStrategyEnum exporterStrategyEnum) {
        return exporterStrategyMap.get(exporterStrategyEnum);
    }

    private void createStrategy(Set<OpenTelemetryExporterStrategy> exporterStrategySet) {
        exporterStrategyMap = new HashMap<>();
        exporterStrategySet.forEach(strategy -> exporterStrategyMap.put(strategy.getExporterStrategyName(), strategy));
    }
}
