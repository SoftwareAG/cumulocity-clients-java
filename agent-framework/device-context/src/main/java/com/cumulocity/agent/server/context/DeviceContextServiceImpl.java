package com.cumulocity.agent.server.context;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;

public class DeviceContextServiceImpl implements DeviceContextService {

    private final Logger log = LoggerFactory.getLogger(DeviceContextServiceImpl.class);

    private final ThreadLocal<Deque<DeviceContext>> localContext = new NamedThreadLocal<Deque<DeviceContext>>("deviceLocalContext");

    @Override
    public DeviceContext getContext() {
        DeviceContext context = doGetContext();
        if (context == null) {
            throw new IllegalStateException("Not within any context!");
        }
        return context;
    }

    private DeviceContext doGetContext() {
        return localContext.get().peek();
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
        enterContext(context);

        try {
            return task.call();
        } catch (Exception e) {
            log.warn("execution of task failed within tenant : " + context.getLogin().getTenant(), e);
            throw e;
        } finally {

            leaveContext();
        }
    }

    @Override
    public void enterContext(DeviceContext newContext) {
        log.debug("entering to  {} ", newContext);
        DeviceContext contextCopy = new DeviceContext(newContext.getLogin());
        if (localContext.get() == null) {
            localContext.set(new LinkedList<DeviceContext>());
        }
        ;
        localContext.get().push(contextCopy);

    }

    @Override
    public void leaveContext() {

        final Deque<DeviceContext> deque = localContext.get();
        if (deque != null) {
            log.debug("leaving from {} ", deque.poll());
            if (deque.isEmpty()) {
                localContext.remove();
            }
        }
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
}
