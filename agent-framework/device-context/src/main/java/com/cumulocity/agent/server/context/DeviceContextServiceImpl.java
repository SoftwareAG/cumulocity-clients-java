package com.cumulocity.agent.server.context;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.NamedThreadLocal;

import com.cumulocity.model.idtype.GId;

public class DeviceContextServiceImpl implements DeviceContextService {

    private static final String TENANT_LOG_FLAG = "tenant";
    private static final String DEVICE_LOG_FLAG = "device";
    
    private final Logger log = LoggerFactory.getLogger(DeviceContextServiceImpl.class);

    private final ThreadLocal<DeviceContext> localContext = new NamedThreadLocal<DeviceContext>("deviceLocalContext");

    @Override
    public DeviceContext getContext() {
        DeviceContext context = doGetContext();
        if (context == null) {
            throw new IllegalStateException("Not within any context!");
        }
        return context;
    }

    private DeviceContext doGetContext() {
        return localContext.get();
    }

    @Override
    public DeviceCredentials getCredentials() {
        return getContext().getLogin();
    }

    @Override
    public void runWithinContext(DeviceContext context, Runnable task) {
        try {
            callWithinContext(context, new CallableRunnableWrapper(task));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <V> V callWithinContext(DeviceContext context, Callable<V> task) throws Exception {
        DeviceContext previousContext = doGetContext();
        enterContext(context);
        try {
            return task.call();
        } catch (Exception e) {
            log.warn("execution of task failed within tenant : " + getContextTenant(context), e);
            throw e;
        } finally {
            leaveContext(previousContext);
        }
    }

    @Override
    public void enterContext(DeviceContext newContext) {
        // Add flags for logging framework
        MDC.put(TENANT_LOG_FLAG, getContextTenant(newContext));
        MDC.put(DEVICE_LOG_FLAG, getContextDevice(newContext));
        log.debug("entering to  {} ", newContext);
        DeviceContext contextCopy = new DeviceContext(newContext.getLogin());
        localContext.set(contextCopy);
    }

    private void leaveContext(DeviceContext previousContext) {
        // Remove logging flags
        MDC.remove(TENANT_LOG_FLAG);
        MDC.remove(DEVICE_LOG_FLAG);
        if (previousContext == null) {
            localContext.remove();
        } else {
            localContext.set(previousContext);
        }
    }
    
    @Override
    public void leaveContext() {
        // Remove logging flags
        MDC.remove(TENANT_LOG_FLAG);
        MDC.remove(DEVICE_LOG_FLAG);
        localContext.remove();
    }

    private static class CallableRunnableWrapper implements Callable<Void> {

        private final Runnable runnable;

        public CallableRunnableWrapper(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public Void call() throws Exception {
            runnable.run();
            return null;
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
    public <V> Callable<V> withinContext(Callable<V> task) {
        return withinContext(getContext(), task);
    }
    
	private static String getContextDevice(DeviceContext newContext) {
		GId deviceId = newContext.getLogin().getDeviceId();
		return deviceId == null ? null : deviceId.getValue();
	}

    private static String getContextTenant(DeviceContext newContext) {
        return newContext.getLogin().getTenant();
    }
}
