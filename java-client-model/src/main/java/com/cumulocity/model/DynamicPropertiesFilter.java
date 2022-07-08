package com.cumulocity.model;

import lombok.RequiredArgsConstructor;

import java.util.Set;

@FunctionalInterface
public interface DynamicPropertiesFilter {

    boolean apply(String name);

    @RequiredArgsConstructor
    final class CompositeDynamicPropertiesFilter implements DynamicPropertiesFilter {
        private final Set<DynamicPropertiesFilter> filters;

        @Override
        public boolean apply(final String name) {
            return filters.stream().allMatch(filters -> filters.apply(name));
        }
    }
}