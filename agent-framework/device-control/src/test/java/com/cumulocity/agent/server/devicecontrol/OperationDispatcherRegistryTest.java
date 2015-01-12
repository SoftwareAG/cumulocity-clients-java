package com.cumulocity.agent.server.devicecontrol;


import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.concurrent.Executor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.ListableBeanFactory;

import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.agent.server.repository.DeviceControlRepository;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.google.common.collect.ImmutableMap;

@RunWith(MockitoJUnitRunner.class)
public class OperationDispatcherRegistryTest {

    public static final GId DEVICE_ID = new GId("1");

    @Mock
    private Executor executor;

    @Mock
    private DeviceContextService contextService;

    @Mock
    private ListableBeanFactory beanFactory;

    @Mock
    private OperationExecutor handler;

    @Before
    public void setup() {
        when(beanFactory.getBeansOfType(OperationExecutor.class)).thenReturn(
                ImmutableMap.<String, OperationExecutor> builder().put("test", handler).build());
    }

    @SynchronizedDispatch
    private class AnnotatedExecutor implements OperationExecutor {

        @Override
        public boolean supports(OperationRepresentation operation) {
            return false;
        }

        @Override
        public void handle(OperationRepresentation operation) {
        }

        @Override
        public String getFragment() {
            return null;
        }
        
    }

    @Test
    public void shouldReturnOperationQueuesForDevice() throws Exception {

        //Given
        OperationDispacherRegistry operationQueueRegistry = givenRegistry();

        when(handler.supports(Mockito.any(OperationRepresentation.class))).thenReturn(true);
        //When
        Iterable<OperationDispatcher> queues = operationQueueRegistry.getDispatchers(DEVICE_ID);

        OperationDispatcher dispatcher = queues.iterator().next();
        OperationRepresentation operation = mock(OperationRepresentation.class);
        dispatcher.supports(operation);
        //Then
        verify(handler).supports(operation);

    }

    public OperationDispacherRegistry givenRegistry() {
        return new OperationDispacherRegistry(executor, contextService, beanFactory,
                mock(DeviceControlRepository.class));
    }

    @Test
    public void shouldReturnOperationConcurrentDispacherForNotAnntatedExecutor() throws Exception {

        //Given
        OperationDispacherRegistry operationQueueRegistry = givenRegistry();

        when(handler.supports(Mockito.any(OperationRepresentation.class))).thenReturn(true);
        //When
        Iterable<OperationDispatcher> queues = operationQueueRegistry.getDispatchers(DEVICE_ID);
        //Then
        assertThat(queues.iterator().next()).isInstanceOf(ConcurrentOperationDispatcher.class);
    }

    @Test
    public void shouldReturnOperationSynchronizedDispacherForAnntatedExecutor() throws Exception {

        //Given
        OperationDispacherRegistry operationQueueRegistry = givenRegistry();
        when(beanFactory.getBeansOfType(OperationExecutor.class)).thenReturn(
                ImmutableMap.<String, OperationExecutor> builder().put("test", new AnnotatedExecutor()).build());
        when(handler.supports(Mockito.any(OperationRepresentation.class))).thenReturn(true);
        //When
        Iterable<OperationDispatcher> queues = operationQueueRegistry.getDispatchers(DEVICE_ID);
        //Then
        assertThat(queues.iterator().next()).isInstanceOf(SynchronizedOperationDispatcher.class);
    }
}
