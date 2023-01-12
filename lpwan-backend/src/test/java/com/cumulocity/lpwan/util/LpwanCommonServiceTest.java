package com.cumulocity.lpwan.util;

import c8y.Agent;
import c8y.LpwanDevice;
import com.cumulocity.lpwan.lns.connection.model.LnsConnectionDeserializer;
import com.cumulocity.lpwan.sample.connection.model.SampleConnection;
import com.cumulocity.microservice.context.ContextService;
import com.cumulocity.microservice.context.credentials.Credentials;
import com.cumulocity.microservice.context.credentials.MicroserviceCredentials;
import com.cumulocity.microservice.subscription.model.core.PlatformProperties;
import com.cumulocity.microservice.subscription.repository.application.ApplicationApi;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.inventory.InventoryFilter;
import com.cumulocity.sdk.client.inventory.ManagedObjectCollection;
import com.cumulocity.sdk.client.inventory.PagedManagedObjectCollectionRepresentation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class LpwanCommonServiceTest {

    @Mock
    ContextService<Credentials> contextService;

    @Mock
    PlatformProperties platformProperties;

    @Mock
    ApplicationApi applicationApi;

    @Mock
    InventoryApi inventoryApi;

    @InjectMocks
    LpwanCommonService lpwanCommonService;

    @Test
    public void shouldMigrateOldDeviceWithNewAgentFragment() throws Exception {
        MicroserviceCredentials microserviceCredentials = new MicroserviceCredentials("aTenant", "aServiceUser", "aPassword", null, null, null, null);
        when(platformProperties.getMicroserviceBoostrapUser()).thenReturn(microserviceCredentials);
        ArgumentCaptor<Callable> callableArgumentCaptor = ArgumentCaptor.forClass(Callable.class);

        mockInventoryReturnsWithDevice("lns-connection-1", GId.asGId("12345"));
        LnsConnectionDeserializer.registerLnsConnectionConcreteClass("SampleConnection", SampleConnection.class);

        lpwanCommonService.migrateOldDeviceWithNewAgentFragment();

        verify(contextService).callWithinContext(eq(microserviceCredentials), callableArgumentCaptor.capture());
        callableArgumentCaptor.getValue().call();
        verify(applicationApi).currentApplication();

        ArgumentCaptor<ManagedObjectRepresentation> deviceMoCaptor = ArgumentCaptor.forClass(ManagedObjectRepresentation.class);
        verify(inventoryApi).update(deviceMoCaptor.capture());
        ManagedObjectRepresentation deviceMoToBeUpdated = deviceMoCaptor.getValue();
        assertEquals(GId.asGId("12345"), deviceMoToBeUpdated.getId());
        Agent agent = deviceMoToBeUpdated.get(Agent.class);
        assertEquals(LpwanCommonService.MAINTAINER, agent.getMaintainer());
        assertEquals("SampleConnection", agent.getName());
    }

    @Test
    public void shouldMigrateOldDeviceWithNewAgentFragmentWithPrePopulatedVersion() {
        mockInventoryReturnsWithDevice("lns-connection-1", GId.asGId("12345"));
        LnsConnectionDeserializer.registerLnsConnectionConcreteClass("SampleConnection", SampleConnection.class);
        lpwanCommonService.migrateOldDeviceWithNewAgentFragment("Sample Version");
        ArgumentCaptor<ManagedObjectRepresentation> deviceMoCaptor = ArgumentCaptor.forClass(ManagedObjectRepresentation.class);
        verify(inventoryApi).update(deviceMoCaptor.capture());
        ManagedObjectRepresentation deviceMoToBeUpdated = deviceMoCaptor.getValue();
        assertEquals(GId.asGId("12345"), deviceMoToBeUpdated.getId());
        Agent agent = deviceMoToBeUpdated.get(Agent.class);
        assertEquals(LpwanCommonService.MAINTAINER, agent.getMaintainer());
        assertEquals("SampleConnection", agent.getName());
        assertEquals("Sample Version", agent.getVersion());
    }

    @Test
    public void shouldPrepareAgentFragment() throws Exception {
        MicroserviceCredentials microserviceCredentials = new MicroserviceCredentials("aTenant", "aServiceUser", "aPassword", null, null, null, null);
        when(platformProperties.getMicroserviceBoostrapUser()).thenReturn(microserviceCredentials);
        ArgumentCaptor<Callable> callableArgumentCaptor = ArgumentCaptor.forClass(Callable.class);
        LnsConnectionDeserializer.registerLnsConnectionConcreteClass("SampleConnection", SampleConnection.class);
        Agent agent = lpwanCommonService.prepareAgentFragment();
        verify(contextService).callWithinContext(eq(microserviceCredentials), callableArgumentCaptor.capture());
        callableArgumentCaptor.getValue().call();
        verify(applicationApi).currentApplication();
        assertEquals(LpwanCommonService.MAINTAINER, agent.getMaintainer());
        assertEquals("SampleConnection", agent.getName());
    }

    @Test
    public void shouldPrepareAgentFragmentWithPrePopulatedVersion() throws Exception {
        MicroserviceCredentials microserviceCredentials = new MicroserviceCredentials("aTenant", "aServiceUser", "aPassword", null, null, null, null);
        when(platformProperties.getMicroserviceBoostrapUser()).thenReturn(microserviceCredentials);
        ArgumentCaptor<Callable> callableArgumentCaptor = ArgumentCaptor.forClass(Callable.class);
        LnsConnectionDeserializer.registerLnsConnectionConcreteClass("SampleConnection", SampleConnection.class);
        Agent agent = lpwanCommonService.prepareAgentFragment("Sample Version");
        verify(contextService, never()).callWithinContext(eq(microserviceCredentials), callableArgumentCaptor.capture());
        verify(applicationApi, never()).currentApplication();
        assertEquals(LpwanCommonService.MAINTAINER, agent.getMaintainer());
        assertEquals("SampleConnection", agent.getName());
        assertEquals("Sample Version", agent.getVersion());
    }

    private void mockInventoryReturnsWithDevice(String lnsConnectionName, GId gId) {
        List<ManagedObjectRepresentation> moList = new ArrayList<>();
        ManagedObjectRepresentation managedObject = new ManagedObjectRepresentation();
        managedObject.setName("Dummy_LPWAN_Device");
        managedObject.setType("type");
        LpwanDevice lpwanDevice = new LpwanDevice();
        lpwanDevice.setLnsConnectionName(lnsConnectionName);
        managedObject.set(lpwanDevice);
        managedObject.setId(gId);
        moList.add(managedObject);

        ManagedObjectCollection managedObjectCollection = mock(ManagedObjectCollection.class);
        PagedManagedObjectCollectionRepresentation paged = mock(PagedManagedObjectCollectionRepresentation.class);
        when(managedObjectCollection.get()).thenReturn(paged);
        when(paged.allPages()).thenReturn(moList);

        when(inventoryApi.getManagedObjectsByFilter(any(InventoryFilter.class))).
                thenReturn(managedObjectCollection);
    }
}