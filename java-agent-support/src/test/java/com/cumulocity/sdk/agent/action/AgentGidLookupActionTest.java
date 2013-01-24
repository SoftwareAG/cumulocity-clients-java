package com.cumulocity.sdk.agent.action;

import static com.cumulocity.model.builder.DomainObjectMother.aGIdLike;
import static org.hamcrest.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cumulocity.model.builder.SampleGId;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.idtype.XtId;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.agent.model.AbstractAgent;
import com.cumulocity.sdk.client.Platform;
import com.cumulocity.sdk.client.SDKException;

public class AgentGidLookupActionTest {

    private static final GId GID = (GId) aGIdLike(SampleGId.GID_1).build();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Platform platform;

    @Mock
    private AbstractAgent agent;

    private AgentGidLookupAction action;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        action = new AgentGidLookupAction(platform, agent);
    }

    @Test
    public void shouldSetGlobalId() throws Exception {
        // Given
        when(agent.getExternalId()).thenReturn("anyId");
        when(platform.getIdentityApi().getExternalId(argThat(any(XtId.class)))).thenReturn(idRepresentation(GID));
        // When
        action.run();
        // Then
        verify(agent).setGlobalId(GID);
    }

    @Test
    public void shouldCatch404AndWaitForProperResponse() throws Exception {
        when(platform.getIdentityApi().getExternalId(argThat(any(XtId.class)))).thenThrow(new SDKException(404, "sample message")).thenReturn(
                idRepresentation(GID));
        when(agent.getExternalId()).thenReturn("anyId");
        action.setRetryInterval(1);
        // When
        action.run();
        // Then
        verify(agent).setGlobalId(GID);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenXtIdNotKnown() throws Exception {
        when(agent.getExternalId()).thenReturn(null);
        // When
        action.run();
        // Then
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionOnSDKException() throws Exception {
        when(platform.getIdentityApi().getExternalId(argThat(any(XtId.class)))).thenThrow(new SDKException(""));
        when(agent.getExternalId()).thenReturn("anyId");
        // When
        action.run();
        // Then
    }

    private ExternalIDRepresentation idRepresentation(GId id) {
        ExternalIDRepresentation idRepresentation = new ExternalIDRepresentation();
        ManagedObjectRepresentation managedObjectRepresentation = new ManagedObjectRepresentation();
        managedObjectRepresentation.setId(id);
        idRepresentation.setManagedObject(managedObjectRepresentation);
        return idRepresentation;
    }
}
