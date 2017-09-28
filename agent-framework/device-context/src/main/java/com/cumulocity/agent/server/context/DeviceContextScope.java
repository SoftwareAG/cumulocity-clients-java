package com.cumulocity.agent.server.context;

import com.cumulocity.agent.server.context.scope.BaseScope;
import com.cumulocity.agent.server.context.scope.ScopeContainer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeviceContextScope extends BaseScope {

    private final DeviceContextService contextService;

    private final ScopeContainerRegistry registry;

    public DeviceContextScope(DeviceContextService contextService, ScopeContainerRegistry registry) {
        this(contextService, registry, true);
    }

    public DeviceContextScope(DeviceContextService contextService, ScopeContainerRegistry registry, boolean sync) {
        super(true);
        this.contextService = contextService;
        this.registry = registry;
    }

    @Override
    protected String getContextId() {
        try {
            final DeviceCredentials credentials = contextService.getCredentials();
            final String username = credentials.getUsername();
            final String tenant = credentials.getTenant();
            return tenant + "/" + username;
        } catch (final Exception ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    protected ScopeContainer getScopeContainer() {
        return registry.get(contextService.getContext());
    }
}
