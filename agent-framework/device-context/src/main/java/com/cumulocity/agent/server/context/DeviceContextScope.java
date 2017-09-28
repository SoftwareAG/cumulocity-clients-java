package com.cumulocity.agent.server.context;

import com.cumulocity.agent.server.context.scope.BaseScope;
import com.cumulocity.agent.server.context.scope.ScopeContainer;

public class DeviceContextScope extends BaseScope {

    private final DeviceContextService contextService;

    private final ScopeContainerRegistry registry;

    public DeviceContextScope(DeviceContextService contextService, ScopeContainerRegistry registry) {
        this(contextService, registry, true);
    }

    public DeviceContextScope(DeviceContextService contextService, ScopeContainerRegistry registry, boolean sync) {
        super(sync);
        this.contextService = contextService;
        this.registry = registry;
    }

    @Override
    protected String getContextId() {
        final DeviceCredentials credentials = contextService.getCredentials();
        final String username = credentials.getUsername();
        final String tenant = credentials.getTenant();
        return tenant + "/" + username;
    }

    @Override
    protected ScopeContainer getScopeContainer() {
        return registry.get(contextService.getContext());
    }
}
