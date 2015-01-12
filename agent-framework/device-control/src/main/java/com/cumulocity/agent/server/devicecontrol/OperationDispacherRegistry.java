package com.cumulocity.agent.server.devicecontrol;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;
import static com.google.common.cache.CacheBuilder.newBuilder;
import static org.springframework.beans.factory.BeanFactoryUtils.beansOfTypeIncludingAncestors;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.agent.server.repository.DeviceControlRepository;
import com.cumulocity.model.idtype.GId;
import com.google.common.base.Function;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.FluentIterable;

@Component
public class OperationDispacherRegistry {

    private final Executor executor;

    private final DeviceContextService contextService;

    private final LoadingCache<GId, Iterable<OperationDispatcher>> handlers;

    private final DeviceControlRepository deviceControl;

    @Autowired
    public OperationDispacherRegistry(Executor executor, DeviceContextService contextService, ListableBeanFactory beanFactory,
            DeviceControlRepository deviceControl) {
        this.executor = executor;
        this.contextService = contextService;
        this.deviceControl = deviceControl;
        this.handlers = newBuilder().build(new OperationsQueueCacheLoader(beanFactory));
    }

    public Iterable<OperationDispatcher> getDispatchers(GId deviceId) {
        try {
            return handlers.get(checkNotNull(deviceId));
        } catch (ExecutionException e) {
            throw propagate(e);
        }
    }

    private final class OperationsQueueCacheLoader extends CacheLoader<GId, Iterable<OperationDispatcher>> {

        private ListableBeanFactory beanFactory;

        public OperationsQueueCacheLoader(ListableBeanFactory beanFactory) {
            this.beanFactory = beanFactory;
        }

        @Override
        public Iterable<OperationDispatcher> load(GId deviceId) throws Exception {
            return FluentIterable.from(resolveHandlers()).transform(toOperationQueue()).toList();
        }

        private Iterable<OperationExecutor> resolveHandlers() {
            return beansOfTypeIncludingAncestors(beanFactory, OperationExecutor.class).values();
        }
    }

    private Function<OperationExecutor, OperationDispatcher> toOperationQueue() {
        return new Function<OperationExecutor, OperationDispatcher>() {
            @Override
            public OperationDispatcher apply(OperationExecutor input) {
                if (input.getClass().isAnnotationPresent(SynchronizedDispatch.class)) {
                    return new SynchronizedOperationDispatcher(input, executor, contextService.getContext(), contextService, deviceControl);
                } else {
                    return new ConcurrentOperationDispatcher(input, executor, contextService.getContext(), contextService);
                }

            }
        };
    }

}
