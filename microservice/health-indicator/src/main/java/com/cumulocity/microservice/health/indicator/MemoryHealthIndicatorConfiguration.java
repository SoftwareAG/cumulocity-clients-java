package com.cumulocity.microservice.health.indicator;

import com.cumulocity.microservice.health.indicator.memory.HeapMemoryHealthIndicator;
import com.cumulocity.microservice.health.indicator.memory.HeapMemoryHealthIndicatorProperties;
import com.cumulocity.microservice.health.indicator.memory.NonHeapMemoryHealthIndicator;
import com.cumulocity.microservice.health.indicator.memory.NonHeapMemoryHealthIndicatorProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemoryHealthIndicatorConfiguration {
    @Configuration
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

    @Configuration
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
