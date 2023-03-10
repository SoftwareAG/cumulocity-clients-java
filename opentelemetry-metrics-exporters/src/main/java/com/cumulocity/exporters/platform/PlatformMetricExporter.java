package com.cumulocity.exporters.platform;

import com.cumulocity.rest.representation.application.microservice.MicroserviceBillingRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.metrics.InstrumentType;
import io.opentelemetry.sdk.metrics.data.AggregationTemporality;
import io.opentelemetry.sdk.metrics.data.DoublePointData;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;
import java.util.stream.Collectors;

import static io.opentelemetry.sdk.metrics.data.MetricDataType.*;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;


@Slf4j
public class PlatformMetricExporter implements MetricExporter {


    private RestConnector restConnector;

    private ApplicationEventPublisher applicationEventPublisher;

    public PlatformMetricExporter(RestConnector restConnector, ApplicationEventPublisher applicationEventPublisher) {
        this.restConnector = restConnector;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    private void exportNonGaugeMetrics(MetricData metric) {
        String name = metric.getName();
        double value = 0;
        if (metric.getType().equals(LONG_SUM)) {
            value = metric.getLongSumData().getPoints().stream().findFirst().get().getValue();
        } else if (metric.getType().equals(DOUBLE_SUM)) {
            value = metric.getDoubleSumData().getPoints().stream().findFirst().get().getValue();
        } else {
            value = 0;
        }
        log.info("Sending metric for tenant {} named {} whose value is {} and whose type is {}", restConnector.getPlatformParameters().getTenantId(), name, value, metric.getType().name());
        MicroserviceBillingRepresentation representation = new MicroserviceBillingRepresentation();
        representation.setDateTime(DateTime.now());
        representation.set(value, name);
        restConnector.post("/application/currentApplication/billing",
                APPLICATION_JSON_TYPE, representation);
    }

    private void exportGaugeMetrics(MetricData metric) {
        Collection<DoublePointData> points = metric.getDoubleGaugeData().getPoints();
        Map<DoublePointData, DateTime> pointDataDateTimeMap = new HashMap<>();
        for(DoublePointData pointData: points) {
            Attributes attributes = pointData.getAttributes();
            List<Object> values = attributes.asMap().values().stream().collect(Collectors.toList());
            DateTime dateTime = DateTime.parse((String) values.get(0));
            pointDataDateTimeMap.put(pointData, dateTime);
        }
        DoublePointData pointToExport = Collections.max(pointDataDateTimeMap.entrySet(), Map.Entry.comparingByValue()).getKey();
        String name = metric.getName();
        double value = pointToExport.getValue();
        MicroserviceBillingRepresentation representation = new MicroserviceBillingRepresentation();
        representation.setDateTime(DateTime.now());
        representation.set(value, name);
        restConnector.post("/application/currentApplication/billing",
                APPLICATION_JSON_TYPE, representation);
    }

    @Override
    public CompletableResultCode export(Collection<MetricData> collection) {
        List<MetricData> gaugeMetrics = collection.stream().filter(metric -> metric.getType().equals(DOUBLE_GAUGE)).collect(Collectors.toList());
        List<MetricData> otherMetrics = collection.stream().filter(metric -> !metric.getType().equals(DOUBLE_GAUGE)).collect(Collectors.toList());
        int gaugePointSize = 0;
        for(int gaugeIndex = 0; gaugeIndex < gaugeMetrics.size(); gaugeIndex++) {
            gaugePointSize += gaugeMetrics.get(gaugeIndex).getDoubleGaugeData().getPoints().size();
        }
        for(MetricData metric : otherMetrics) {
            exportNonGaugeMetrics(metric);
        }
        for(MetricData metric: gaugeMetrics) {
            exportGaugeMetrics(metric);
        }
        applicationEventPublisher.publishEvent(new ExportCompletedEvent("exportCompletedEvent", gaugePointSize));
        return CompletableResultCode.ofSuccess();
    }

    // This custom HTTP exporter does not batch metrics.
    @Override
    public CompletableResultCode flush() {
        return CompletableResultCode.ofSuccess();
    }

    // WebClient retrieve() disposes connection post response body consumption.
    @Override
    public CompletableResultCode shutdown() {
        return CompletableResultCode.ofSuccess();
    }

    @Override
    public AggregationTemporality getAggregationTemporality(InstrumentType instrumentType) {
        return AggregationTemporality.CUMULATIVE;
    }
}
