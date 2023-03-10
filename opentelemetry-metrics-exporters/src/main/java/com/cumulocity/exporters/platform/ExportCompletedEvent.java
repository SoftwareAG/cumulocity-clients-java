package com.cumulocity.exporters.platform;

import org.springframework.context.ApplicationEvent;

public class ExportCompletedEvent extends ApplicationEvent {
    private int metricCollectionSize;
    public ExportCompletedEvent(Object source, int size) {
        super(source);
        this.metricCollectionSize = size;
    }

    public int getMetricCollectionSize() {
        return metricCollectionSize;
    }
}
