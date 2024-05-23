package com.cumulocity.microservice.monitoring.health.indicator;

import com.cumulocity.microservice.monitoring.health.annotation.EnableHealthIndicator;
import com.cumulocity.microservice.monitoring.health.indicator.memory.HeapMemoryHealthIndicator;
import com.cumulocity.microservice.monitoring.health.indicator.memory.HeapMemoryHealthIndicatorProperties;
import com.cumulocity.microservice.monitoring.health.indicator.memory.NonHeapMemoryHealthIndicator;
import com.cumulocity.microservice.monitoring.health.indicator.memory.NonHeapMemoryHealthIndicatorProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnBean(value = Object.class, annotation = EnableHealthIndicator.class)
public class MemoryHealthIndicatorConfiguration {

    @AutoConfiguration
    public static class HeapMemoryHealthIndicatorConfiguration {

        @Bean
        @ConditionalOnMissingBean(name = "heapMemoryHealthIndicator")
        public HeapMemoryHealthIndicator heapMemoryHealthIndicator(
                HeapMemoryHealthIndicatorProperties properties) {
            return new HeapMemoryHealthIndicator(properties);
        }

        @Bean
        public HeapMemoryHealthIndicatorProperties heapMemoryHealthIndicatorProperties() {
            return new HeapMemoryHealthIndicatorProperties();
        }
    }

    @AutoConfiguration
    public static class NonHeapMemoryHealthIndicatorConfiguration {

        @Bean
        @ConditionalOnMissingBean(name = "nonHeapMemoryHealthIndicator")
        public NonHeapMemoryHealthIndicator nonHeapMemoryHealthIndicator(NonHeapMemoryHealthIndicatorProperties properties) {
            return new NonHeapMemoryHealthIndicator(properties);
        }

        @Bean
        public NonHeapMemoryHealthIndicatorProperties nonHeapMemoryHealthIndicatorProperties() {
            return new NonHeapMemoryHealthIndicatorProperties();
        }
    }
}
