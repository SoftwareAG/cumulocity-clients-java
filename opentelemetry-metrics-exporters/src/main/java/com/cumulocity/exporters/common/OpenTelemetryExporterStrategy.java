package com.cumulocity.exporters.common;

import io.opentelemetry.api.OpenTelemetry;

public interface OpenTelemetryExporterStrategy {
    OpenTelemetry getOpenTelemetry();

    OpenTelemetryExporterStrategyEnum getExporterStrategyName();
}
