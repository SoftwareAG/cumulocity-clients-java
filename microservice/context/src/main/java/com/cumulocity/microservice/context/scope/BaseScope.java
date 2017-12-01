package com.cumulocity.microservice.context.scope;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

@Slf4j
public abstract class BaseScope implements Scope {

    private final KeyBasedLocksMap locks = new KeyBasedLocksMap();

    private LoadingCache<String, ScopeContainer> scopes = CacheBuilder.newBuilder().concurrencyLevel(16)
            .build(new CacheLoader<String, ScopeContainer>() {
                public ScopeContainer load(String key) {
                    return new DefaultScopeContainer();
                }
            });

    private final boolean sync;

    public BaseScope(boolean sync) {
        this.sync = sync;
    }

    protected abstract String getContextId();

    protected ScopeContainer getScopeContainer() {
        return scopes.getUnchecked(getContextId());
    }

    @Override
    public Object get(final String name, final ObjectFactory<?> objectFactory) {
        if (sync) {
            return doGetSynchronized(name, objectFactory);
        } else {
            return doGet(name, objectFactory);
        }
    }

    private Object doGetSynchronized(String name, ObjectFactory<?> objectFactory) {
        // synchronization is done only on request for the same contextId and bean name
        KeyBasedLocksMap.KeyBasedLock lock = locks.lockForKeyElements(getContextId(), name);
        try {
            return doGet(name, objectFactory);
        } finally {
            lock.unlock();
        }
    }

    protected Object doGet(String name, ObjectFactory<?> objectFactory) {
        ScopeContainer scopeContainer = getScopeContainer();
        if (scopeContainer.contains(name)) {
            return getExisting(scopeContainer, name);
        } else {
            return createNew(scopeContainer, name, objectFactory);
        }
    }

    private Object getExisting(ScopeContainer container, String name) {
        Object scoped = container.getObject(name);
        log.trace("Returned existing scoped instance of bean '{}' for '{}'.", name, getContextId());
        return scoped;
    }

    private Object createNew(ScopeContainer container, String name, ObjectFactory<?> objectFactory) {
        Object scoped = getObjectFromFactory(objectFactory);
        container.putObject(name, scoped);
        log.trace("Created new scoped instance of bean '{}' for '{}'.", name, getContextId());
        return scoped;
    }

    @Override
    public Object remove(final String name) {
        log.trace("Removing tenant scoped instance of bean '{}' for tenant '{}'.", name, getContextId());
        return getScopeContainer().removeObject(name);
    }

    @Override
    public void registerDestructionCallback(final String name, final Runnable callback) {
        log.trace("Registering destruction callback for tenant scoped bean '{}' for tenant '{}'.", name, getContextId());
        getScopeContainer().addDestructionCallback(name, callback);
    }

    @Override
    public Object resolveContextualObject(final String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return getContextId();
    }

    protected Object getObjectFromFactory(final ObjectFactory<?> objectFactory) {
        return objectFactory.getObject();
    }
}
