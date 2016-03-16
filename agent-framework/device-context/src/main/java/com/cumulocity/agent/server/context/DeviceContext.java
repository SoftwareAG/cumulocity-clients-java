package com.cumulocity.agent.server.context;

import com.cumulocity.agent.server.context.scope.DefaultScopeContainer;
import com.cumulocity.agent.server.context.scope.ScopeContainer;

public class DeviceContext {

    private final DeviceCredentials login;

    private final ScopeContainer scope;
    
    private final String loggingUser;

    public DeviceContext(DeviceCredentials login) {
    	this(login, null);
    }
    
    public DeviceContext(DeviceCredentials login, String loggingUser) {
    	this.login = login;
    	this.scope = new DefaultScopeContainer();
		this.loggingUser = loggingUser;
	}

	public DeviceCredentials getLogin() {
        return login;
    }

    public ScopeContainer getScope() {
        return scope;
    }
    
    public String getLoggingUser() {
		return loggingUser;
	}

	@Override
    public String toString() {
        return "DeviceContext [login=" + login + "]";
    }
    
}
