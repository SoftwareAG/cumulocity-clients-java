/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cumulocity.sdk.client.devicecontrol;

import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anMoRefRepresentationLike;
import static com.cumulocity.rest.representation.builder.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.rest.representation.builder.SampleManagedObjectReferenceRepresentation.MO_REF_REPRESENTATION;
import static com.cumulocity.rest.representation.builder.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cumulocity.model.Agent;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.builder.ManagedObjectRepresentationBuilder;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.operation.OperationCollectionRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import com.cumulocity.sdk.client.devicecontrol.autopoll.OperationsByAgentAndStatusPollerImpl;
import com.cumulocity.sdk.client.devicecontrol.notification.OperationNotificationSubscriber;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.notification.Subscription;
import com.cumulocity.sdk.client.notification.SubscriptionListener;
import com.cumulocity.sdk.client.polling.FixedRatePoller;

//TODO inline step definitions (see AlarmIT or InventoryIT)
@Slf4j
public class DeviceControlIT extends JavaSdkITBase {

    String SOME_AGENT = "SomeAgent";
    String OTHER_AGENT = "OtherAgent";
    String SOME_DEVICE = "SomeDevice";
    String OTHER_DEVICE = "OtherDevice";

    private List<ManagedObjectRepresentation> managedObjects = new LinkedList<>();
    private DeviceControlApi deviceControlResource;
    private InventoryApi inventoryApi;
    private OperationNotificationSubscriber subscriber;
    private OperationRepresentation operationRep;
    private SimpleOperationProcessor operationProcessor = new SimpleOperationProcessor();
    private FixedRatePoller poller = null;
    private OperationCollectionRepresentation allOperations;

    @BeforeEach
    public void setUp() throws Exception {
        createManagedObjects();
        deviceControlResource = platform.getDeviceControlApi();
        operationRep = null;
        allOperations = null;
    }

    @AfterEach
    public void cleanup() {
        if (poller != null) {
            poller.stop();
        }
        if (subscriber != null) {
            subscriber.disconnect();
            subscriber = null;
        }
    }

    @Test
    public void createOperationAndPollIt() {
        // given
        iHaveAPollerForAgent();
        // when
        iCreateAnOperationForDevice(SOME_DEVICE);
        // then
        iShouldReceiveOperation(1);
    }

    @Test
    public void addingOperationToQueue() {
        // given
        iGetAllOperationsForAgent(SOME_DEVICE);
        // then
        iShouldReceiveXOperations(0);
        // when
        iCreateAnOperationForDevice(SOME_DEVICE);
        iGetAllOperationsForDeviceX(SOME_DEVICE);
        // then
        iShouldReceiveXOperations(1);
    }

    @Test
    public void getNotificationAboutNewOperation() {
        // given
        iHaveAOperationSubscriberForAgent();
        // when
        iCreateAnOperationForDevice(SOME_DEVICE);
        // then
        iShouldReceiveOperation(1);
    }

    @Test
    public void operationCRUD() {
        // given
        iCreateAnOperationForDevice(SOME_DEVICE);
        // when
        iCallGetOnCreatedOperation();
        // then
        iShouldReceiveOperationWithStatusX("PENDING");
        // when
        iUpdateCreatedOperationWithStatusX("EXECUTING");
        iCallGetOnCreatedOperation();
        // then
        iShouldReceiveOperationWithStatusX("EXECUTING");
    }

    @Test
    public void queryOperationByStatus() {
        // given
        iQueryOperationsWithStatusX("EXECUTING");
        int numOfExecuting = allOperations.getOperations().size();
        iQueryOperationsWithStatusX("PENDING");
        int numOfPending = allOperations.getOperations().size();
        // when
        iCreateAnOperationForDevice(SOME_DEVICE);
        iUpdateCreatedOperationWithStatusX("EXECUTING");
        iCreateAnOperationForDevice(SOME_DEVICE);
        iQueryOperationsWithStatusX("PENDING");
        // then
        iShouldReceiveXOperations(numOfPending + 1);
        allReceivedOperationsShouldHaveStatusX("PENDING");
        // when
        iQueryOperationsWithStatusX("EXECUTING");
        // then
        iShouldReceiveXOperations(numOfExecuting + 1);
        allReceivedOperationsShouldHaveStatusX("EXECUTING");
    }

    @Test
    public void queryOperationsByDevice() {
        // given
        iCreateAnOperationForDevice(SOME_DEVICE);
        iCreateAnOperationForDevice(SOME_DEVICE);
        iCreateAnOperationForDevice(OTHER_DEVICE);
        // when
        iGetAllOperationsForDeviceX(SOME_DEVICE);
        // then
        iShouldReceiveXOperations(2);
        // when
        iGetAllOperationsForDeviceX(OTHER_DEVICE);
        // then
        iShouldReceiveXOperations(1);
    }


    @Test
    public void queryOperationsByAgent() {
        // given
        iCreateAnOperationForDevice(SOME_DEVICE);
        iCreateAnOperationForDevice(SOME_DEVICE);
        iCreateAnOperationForDevice(OTHER_DEVICE);
        // when
        iGetAllOperationsForAgent(SOME_AGENT);
        // then
        iShouldReceiveXOperations(2);
        // when
        iGetAllOperationsForAgent(OTHER_AGENT);
        // then
        iShouldReceiveXOperations(1);
    }

    private void iHaveAPollerForAgent() {
        GId agentId = getMoId(SOME_AGENT);
        poller = new OperationsByAgentAndStatusPollerImpl(deviceControlResource, agentId.getValue(), OperationStatus.PENDING,
                operationProcessor);
        poller.start();
    }

    private void iHaveAOperationSubscriberForAgent() {
        GId agentId = getMoId(SOME_AGENT);
        subscriber = new OperationNotificationSubscriber(platform);
        subscriber.subscribe(agentId, new SubscriptionListener<GId, OperationRepresentation>() {
            @Override
            public void onNotification(Subscription<GId> subscription, OperationRepresentation notification) {
                operationProcessor.process(notification);
            }

            @Override
            public void onError(Subscription<GId> subscription, Throwable ex) {
                log.error("an error occurred", ex);
            }
        });
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void iCreateAnOperationForDevice(String deviceNum) {
        GId deviceId = getMoId(deviceNum);
        OperationRepresentation operationRepresentation = new OperationRepresentation();
        operationRepresentation.setDeviceId(deviceId);
        operationRepresentation.set("smaple_value", "sample_operation_type");
        operationRep = deviceControlResource.create(operationRepresentation);
    }

    private void iShouldReceiveOperation(int amount) {
        await().atMost(15, TimeUnit.SECONDS).until(() -> operationProcessor.getOperations().size(), is(equalTo(amount)));
    }

    private void iGetAllOperationsForDeviceX(String deviceNum) {
        OperationFilter filter = new OperationFilter().byDevice(getMoId(deviceNum).getValue());
        allOperations = deviceControlResource.getOperationsByFilter(filter).get();
    }

    private void iShouldReceiveXOperations(int amount) {
        assertThat(allOperations.getOperations().size(), is(equalTo(amount)));
    }

    private void iCallGetOnCreatedOperation() {
        GId operationId = operationRep.getId();
        operationRep = deviceControlResource.getOperation(operationId);
    }

    private void iShouldReceiveOperationWithStatusX(String status) {
        assertThat(operationRep.getStatus(), is(equalTo(status)));
    }

    private void iUpdateCreatedOperationWithStatusX(String status) {
        operationRep.setStatus(status);
        operationRep = deviceControlResource.update(operationRep);
    }

    private void iQueryOperationsWithStatusX(String status) {
        OperationFilter filter = new OperationFilter().byStatus(OperationStatus.valueOf(status));
        allOperations = deviceControlResource.getOperationsByFilter(filter).get();
    }

    private void allReceivedOperationsShouldHaveStatusX(String status) {
        for (OperationRepresentation operation : allOperations.getOperations()) {
            assertThat(operation.getStatus(), is(equalTo(status)));
        }
    }

    private void iGetAllOperationsForAgent(String agentName) throws SDKException {
        OperationFilter filter = new OperationFilter().byAgent(getMoId(agentName).getValue());
        allOperations = deviceControlResource.getOperationsByFilter(filter).get();
    }

    private GId getMoId(String moName) {
        return managedObjects.stream().filter(mo -> mo.getName().equalsIgnoreCase(moName)).findFirst().get().getId();
    }

    private void createManagedObjects() {
        ManagedObjectRepresentation agent = aSampleMo().withName(SOME_AGENT).withType("com.type").with(new Agent()).build();
        ManagedObjectRepresentation device = aSampleMo().withName(SOME_DEVICE).withType("com.type").build();
        ManagedObjectRepresentation agent2 = aSampleMo().withName(OTHER_AGENT).withType("com.type").with(new Agent()).build();
        ManagedObjectRepresentation device2 = aSampleMo().withName(OTHER_DEVICE).withType("com.type").build();

        inventoryApi = platform.getInventoryApi();

        agent = inventoryApi.create(agent);
        device = inventoryApi.create(device);
        agent2 = inventoryApi.create(agent2);
        device2 = inventoryApi.create(device2);

        managedObjects.add(agent);
        managedObjects.add(device);
        managedObjects.add(agent2);
        managedObjects.add(device2);

        addChildDevice(agent, device);
        addChildDevice(agent2, device2);
    }

    private void addChildDevice(ManagedObjectRepresentation parent, ManagedObjectRepresentation child) throws SDKException {
        ManagedObjectReferenceRepresentation deviceRef = anMoRefRepresentationLike(MO_REF_REPRESENTATION).withMo(child).build();
        inventoryApi.getManagedObjectApi(parent.getId()).addChildDevice(deviceRef);
    }

    private static ManagedObjectRepresentationBuilder aSampleMo() {
        return anMoRepresentationLike(MO_REPRESENTATION);
    }
}
