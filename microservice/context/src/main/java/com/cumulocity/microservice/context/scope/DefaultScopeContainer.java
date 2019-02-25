package com.cumulocity.microservice.context.scope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.TypeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.lang.String.format;

/**
 * Implements a scope container.
 * @author Darek Kaczynski
 */
public class DefaultScopeContainer implements ScopeContainer {

    private final Logger log = LoggerFactory.getLogger(DefaultScopeContainer.class);

    private final ConcurrentMap<String, Object> objectsMap;

    private final ConcurrentMap<String, List<Runnable>> callbacksMap;

    private final ConcurrentMap<String, Object> objectsInDestruction;

    private volatile boolean containerInDestruction;

    public DefaultScopeContainer() {
        this.objectsMap = new ConcurrentHashMap<>();
        this.callbacksMap = new ConcurrentHashMap<>();
        this.objectsInDestruction = new ConcurrentHashMap<>();
        this.containerInDestruction = false;
    }

    @Override
    public Set<String> getObjectNames() {
        ensureContainerNotInDestruction();
        return objectsMap.keySet();
    }

    @Override
    public boolean contains(String name) {
        ensureObjectNotInDestruction(name);
        return objectsMap.containsKey(name);
    }

    @Override
    public Object getObject(String name) {
        ensureObjectNotInDestruction(name);
        return objectsMap.get(name);
    }

    @Override
    public void putObject(String name, Object obj) {
        ensureObjectNotInDestruction(name);
        Object previous = objectsMap.putIfAbsent(name, obj);
        if (previous != null) {
            final IllegalArgumentException exception = new IllegalArgumentException(format("Object with name %s is already present in the container!", name));
            log.error(exception.getMessage(), exception);
            throw exception;
        }
    }

    @Override
    public Object removeObject(String name) {
        ensureContainerNotInDestruction();
        return doRemoveObject(name);
    }

    @Override
    public void addDestructionCallback(String name, Runnable callback) {
        ensureObjectNotInDestruction(name);
        List<Runnable> list = callbacksMap.get(name);
        if (list == null) {
            list = new ArrayList<>();
            callbacksMap.put(name, list);
        }
        list.add(callback);
    }

    @Override
    public void clear() {
        ensureContainerNotInDestruction();
        containerInDestruction = true;
        try {
            for (String name : objectsMap.keySet()) {
                doRemoveObject(name);
            }
            objectsMap.clear();
            callbacksMap.clear();
        } finally {
            containerInDestruction = false;
        }
    }

    private Object doRemoveObject(String name) {
        if (objectsInDestruction.putIfAbsent(name, name) != null) {
            throw new IllegalStateException("The object is currently in destruction!");
        }
        try {
            Object removed = objectsMap.remove(name);
            doReleaseResource(removed);
            runDestructionCallbacks(name);
            return removed;
        } finally {
            objectsInDestruction.remove(name);
        }
    }

    private void doReleaseResource(Object object) {
        if (ClassUtils.isAssignableValue(AutoCloseable.class, object)) {
            try {
                ((AutoCloseable) object).close();
            } catch (Exception e) {
                log.debug("Could not release resources of: {}", object.getClass().getSimpleName());
            }
        }
    }

    private void runDestructionCallbacks(String name) {
        List<Runnable> list = callbacksMap.remove(name);
        if (list == null) {
            return;
        }
        for (Runnable callback : list) {
            callback.run();
        }
    }

    private void ensureContainerNotInDestruction() {
        if (containerInDestruction) {
            throw new IllegalStateException("The scope container is currently in destruction!");
        }
    }

    private void ensureObjectNotInDestruction(String name) {
        ensureContainerNotInDestruction();
        if (objectsInDestruction.containsKey(name)) {
            throw new IllegalStateException("The object is currently in destruction!");
        }
    }
}
