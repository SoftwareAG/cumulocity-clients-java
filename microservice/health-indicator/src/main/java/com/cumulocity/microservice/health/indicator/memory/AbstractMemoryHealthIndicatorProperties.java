package com.cumulocity.microservice.health.indicator.memory;

import org.springframework.util.Assert;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

public abstract class AbstractMemoryHealthIndicatorProperties {

    private static final double DEFAULT_THRESHOLD = .05;
    /**
     * The {@link MemoryMXBean} used to compute the available memory.
     */
    private MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    /**
     * Minimum memory space that should be available, as a percentage.
     */
    private double threshold = DEFAULT_THRESHOLD;

    public final MemoryMXBean getMemoryMXBean() {
        return this.memoryMXBean;
    }

    public final void setMemoryMXBean(MemoryMXBean memoryMXBean) {
        Assert.notNull(memoryMXBean, "MemoryMXBean must not be null");
        this.memoryMXBean = memoryMXBean;
    }

    public final double getThreshold() {
        return this.threshold;
    }

    public final void setThreshold(double threshold) {
        Assert.isTrue(threshold >= 0, "threshold must be greater than or equal to 0");
        this.threshold = threshold;
    }

}