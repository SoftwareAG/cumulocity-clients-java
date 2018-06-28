package com.cumulocity.microservice.health.indicator.memory;

import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class NonHeapMemoryHealthIndicator extends AbstractMemoryHealthIndicator {

    /**
     * Create a new {@code NonHeapMemoryHealthIndicator}.
     *
     * @param properties the memory properties
     */
    public NonHeapMemoryHealthIndicator(NonHeapMemoryHealthIndicatorProperties properties) {
        super(properties);
    }

    @Override
    protected MemoryUsage getMemoryUsage(MemoryMXBean memoryMXBean) {
        return memoryMXBean.getNonHeapMemoryUsage();
    }

}
