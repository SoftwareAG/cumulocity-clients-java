package com.cumulocity.microservice.monitoring.health.indicator.memory;

import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class HeapMemoryHealthIndicator extends AbstractMemoryHealthIndicator {

    /**
     * Create a new {@code HeapMemoryHealthIndicator}.
     *
     * @param properties the memory properties
     */
    public HeapMemoryHealthIndicator(HeapMemoryHealthIndicatorProperties properties) {
        super(properties);
    }

    @Override
    protected MemoryUsage getMemoryUsage(MemoryMXBean memoryMXBean) {
        return memoryMXBean.getHeapMemoryUsage();
    }

}
