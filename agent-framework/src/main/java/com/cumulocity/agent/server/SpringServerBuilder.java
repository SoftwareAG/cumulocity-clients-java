package com.cumulocity.agent.server;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Iterables.concat;
import static java.util.Arrays.asList;

import java.util.HashSet;
import java.util.Set;

abstract class SpringServerBuilder<T> {

    protected final Set<Class<?>> components = new HashSet<Class<?>>();
    
    protected final Set<String> packages = new HashSet<String>();
    
    
    @SuppressWarnings("unchecked")
    public T component(Class<?> component) {
        components.add(component);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T scan(String packageToScan) {
        packages.add(packageToScan);
        return (T) this;
    }
    
    @SuppressWarnings("rawtypes")
    protected Class[] annotatedClasses(Class... classes) {
        return from(concat(asList(classes), components)).toArray(Class.class);
    }
}
