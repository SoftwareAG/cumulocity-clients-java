package com.cumulocity.agent.server.context;

import java.util.concurrent.Callable;

import com.cumulocity.model.idtype.GId;

public class MockDeviceContextService implements DeviceContextService {

    public static final DeviceContext DEFAULT_CONTEXT = new DeviceContext(new DeviceCredentials("testTenant", "testUser", "testPasswd",
            "testAppKey", GId.asGId("test")));

    private DeviceContext context;

    public MockDeviceContextService() {
        this(DEFAULT_CONTEXT);
    }

    public MockDeviceContextService(DeviceContext context) {
        this.context = context;
    }

    public void setContext(DeviceContext context) {
        this.context = context;
    }

    @Override
    public DeviceContext getContext() {
        return context;
    }
    
    @Override
	public boolean isInContext() {
		return context != null;
	}

	@Override
    public DeviceCredentials getCredentials() {
        return context.getLogin();
    }

    @Override
    public void runWithinContext(DeviceContext context, Runnable task) {
        DeviceContext previous = this.context;
        this.context = context;
        try {
            task.run();
        } finally {
            this.context = previous;
        }
    }

    @Override
    public <V> V callWithinContext(DeviceContext context, Callable<V> task) throws Exception {
        DeviceContext previous = this.context;
        this.context = context;
        try {
            return task.call();
        } finally {
            this.context = previous;
        }
    }

    @Override
    public Runnable withinContext(final DeviceContext context, final Runnable task) {
        return new Runnable() {
            @Override
            public void run() {
                runWithinContext(context, task);
            }
        };
    }

    @Override
    public <V> Callable<V> withinContext(final DeviceContext context, final Callable<V> task) {
        return new Callable<V>() {
            @Override
            public V call() throws Exception {
                return callWithinContext(context, task);
            }
        };
    }

    @Override
    public Runnable withinContext(Runnable task) {
        return withinContext(getContext(), task);
    }

    @Override
    public void enterContext(DeviceContext context) {

    }

    @Override
    public void leaveContext() {

    }

    @Override
    public <V> Callable<V> withinContext(Callable<V> task) {
        return withinContext(context, task);
    }

}
