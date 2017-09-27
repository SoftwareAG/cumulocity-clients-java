package com.cumulocity.agent.server.context;

import com.cumulocity.agent.server.context.scope.DefaultScopeContainer;
import com.cumulocity.agent.server.context.scope.ScopeContainer;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;

/**
 * May cause memory leaks for users that are removed or users with changed passwords - cache is never invalidated.
 */
public class DeviceScopeContainerRegistry implements ScopeContainerRegistry {

    private LoadingCache<DeviceCredentials, ScopeContainer> scopes = CacheBuilder.newBuilder().concurrencyLevel(16)
            .build(new CacheLoader<DeviceCredentials, ScopeContainer>() {
                public ScopeContainer load(DeviceCredentials key) throws Exception {
                    return new DefaultScopeContainer();
                }
            });

    public ScopeContainer get(DeviceContext context) {
        try {
            return scopes.get(context.getLogin());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
