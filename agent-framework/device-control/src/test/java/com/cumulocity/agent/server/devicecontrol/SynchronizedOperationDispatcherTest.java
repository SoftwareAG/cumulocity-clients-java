package com.cumulocity.agent.server.devicecontrol;

import static com.cumulocity.model.idtype.GId.asGId;
import static com.cumulocity.model.operation.OperationStatus.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.core.task.TaskExecutor;

import com.cumulocity.agent.server.context.DeviceContext;
import com.cumulocity.agent.server.context.DeviceContextService;
import com.cumulocity.agent.server.context.MockDeviceContextService;
import com.cumulocity.agent.server.repository.DeviceControlRepository;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.operation.OperationRepresentation;

@Ignore
//TODO java8 error
public class SynchronizedOperationDispatcherTest {

    private static final GId ID = asGId("1");

    private static final GId ID_2 = asGId("2");

    private final OperationExecutor handler = mock(OperationExecutor.class);

    private final TaskExecutor executor = mock(TaskExecutor.class);

    private final DeviceContext context = mock(DeviceContext.class);

    private final DeviceContextService contextService = new MockDeviceContextService();

    private final DeviceControlRepository deviceControl = mock(DeviceControlRepository.class);

    private final SynchronizedOperationDispatcher dispatcher = new SynchronizedOperationDispatcher(handler, executor, context,
            contextService, deviceControl);

    private final ArgumentCaptor<Runnable> pollTask = forClass(Runnable.class);

    @Test
    public void shouldSchedulePollTaskAfterPush() {
        //Given
        OperationRepresentation operation = createOperation(ID);

        //When
        dispatcher.dispatch(operation);

        //Then
        verifyThatPollTaskWasScheduled(1);

    }

    @Test
    public void shouldScheduleOnlyOnePollTaskAfterManyPush() {
        //Given
        //When
        for (int i = 0; i < 10; i++) {
            dispatcher.dispatch(createOperation(GId.asGId(i)));
        }
        //Then
        verifyThatPollTaskWasScheduled(10);

    }

    @Test
    public void shouldExecuteOperationIfThereIsNotAnyInProgress() {
        //Given
        OperationRepresentation operation = createOperation(ID);

        //When
        dispatcher.dispatch(operation);
        executeNextOperation();

        //Then
        verify(handler).handle(operation);

    }

    @Test
    public void shouldSchedulePollTaskWhenCurrentOperationIsFinished() {
        //Given
        OperationRepresentation operation = createOperation(ID);
        //When
        dispatcher.dispatch(operation);
        executeNextOperation();
        markAsFinshed(ID);
        //Then
        verifyThatPollTaskWasScheduled(2);

    }

    public void markAsFinshed(GId id) {
        deviceControl.findById(id).setStatus(SUCCESSFUL.name());
    }

    @Test
    public void shouldHandleNextOperationAfterExecuteNext() {
        //Given

        //When
        dispatcher.dispatch(createOperation(ID));
        executeNextOperation();
        markAsFinshed(ID);
        dispatcher.dispatch(createOperation(ID_2));
        executeNextOperation();
        //Then
        verify(handler, times(2)).handle(Mockito.any(OperationRepresentation.class));
    }

    @Test
    public void shouldRemoveNotStartedOperationFromQueue() {
        //Given

        //When
        dispatcher.dispatch(createOperation(ID));
        dispatcher.dispatch(createOperation(ID_2));
        executeNextOperation();
        markAsFinshed(ID_2);
        executeNextOperation();

        //Then
        verify(handler, times(1)).handle(Mockito.any(OperationRepresentation.class));
    }

    @Test
    public void shouldNotExecuteOperationIfStatusOnPlatformIsDifferentThenPending() {
        //Given

        //When
        dispatcher.dispatch(createOperation(ID));
        OperationRepresentation pending = createOperation(ID_2);
        dispatcher.dispatch(pending);
        executeNextOperation();
        pending.setStatus(FAILED.name());
        when(deviceControl.findById(ID_2)).thenReturn(pending);
        executeNextOperation();

        //Then
        verify(handler, times(1)).handle(Mockito.any(OperationRepresentation.class));
    }

    private void executeNextOperation() {
        verify(executor, atLeastOnce()).execute(pollTask.capture());
        pollTask.getValue().run();
    }

    private void verifyThatPollTaskWasScheduled(int number) {
        verify(executor, times(number)).execute(pollTask.capture());
    }

    private OperationRepresentation createOperation(GId id) {
        OperationRepresentation operation = new OperationRepresentation();
        operation.setId(id);
        operation.setStatus(PENDING.name());
        when(deviceControl.findById(id)).thenReturn(operation);
        return operation;
    }

}
