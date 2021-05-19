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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.LinkedList;
import java.util.List;

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

    private List<ManagedObjectRepresentation> managedObjects = new LinkedList<ManagedObjectRepresentation>();
    private DeviceControlApi deviceControlResource;
    private InventoryApi inventoryApi;
    private OperationNotificationSubscriber subscriber;
    private OperationRepresentation operation1;
    private SimpleOperationProcessor operationProcessor = new SimpleOperationProcessor();
    private FixedRatePoller poller = null;
    private OperationCollectionRepresentation allOperations;

    @BeforeEach
    public void setUp() throws Exception {
        createManagedObjects();

        deviceControlResource = platform.getDeviceControlApi();
        operation1 = null;
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
        iHaveAPollerForAgent(0);
        // when
        iCreateAnOperationForDevice(1);
        // then
        pollerShouldReceiveOperation();
    }

    @Test
    public void addingOperationToQueue() {
        // given
        iGetAllOperationsForAgent(1);
        // then
        iShouldReceiveXOperations(0);
        // when
        iCreateAnOperationForDevice(1);
        iGetAllOperationsForDeviceX(1);
        // then
        iShouldReceiveXOperations(1);
    }

    @Test
    public void getNotificationAboutNewOperation() {
        // given
        iHaveAOperationSubscriberForAgent(0);
        // when
        iCreateAnOperationForDevice(1);
        // then
        subscriberShouldReceiveOperation();
    }

    @Test
    public void operationCRUD() {
        // given
        iCreateAnOperationForDevice(1);
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
        iCreateAnOperationForDevice(1);
        iUpdateCreatedOperationWithStatusX("EXECUTING");
        iCreateAnOperationForDevice(1);
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
        iCreateAnOperationForDevice(1);
        iCreateAnOperationForDevice(1);
        iCreateAnOperationForDevice(3);
        // when
        iGetAllOperationsForDeviceX(1);
        // then
        iShouldReceiveXOperations(2);
        // when
        iGetAllOperationsForDeviceX(3);
        // then
        iShouldReceiveXOperations(1);
    }


    @Test
    public void queryOperationsByAgent() {
        // given
        iCreateAnOperationForDevice(1);
        iCreateAnOperationForDevice(1);
        iCreateAnOperationForDevice(3);
        // when
        iGetAllOperationsForAgent(0);
        // then
        iShouldReceiveXOperations(2);
        // when
        iGetAllOperationsForAgent(2);
        // then
        iShouldReceiveXOperations(1);
    }

    private void iHaveAPollerForAgent(int arg1) {
        GId agentId = getMoId(arg1);
        poller = new OperationsByAgentAndStatusPollerImpl(deviceControlResource, agentId.getValue(), OperationStatus.PENDING,
                operationProcessor);
        poller.start();
    }

    private void iHaveAOperationSubscriberForAgent(int arg1) {
        GId agentId = getMoId(arg1);
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

    private void iCreateAnOperationForDevice(int deviceNum) {
        GId deviceId = getMoId(deviceNum);
        OperationRepresentation operationRepresentation = new OperationRepresentation();
        operationRepresentation.setDeviceId(deviceId);
        operationRepresentation.set("smaple_value", "sample_operation_type");
        operation1 = deviceControlResource.create(operationRepresentation);
    }

    private void pollerShouldReceiveOperation() {
        try {
            Thread.sleep(11000);
            assertThat(operationProcessor.getOperations().size(), is(equalTo(1)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void subscriberShouldReceiveOperation() {
        try {
            Thread.sleep(11000);
            assertThat(operationProcessor.getOperations().size(), is(equalTo(1)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void iGetAllOperationsForDeviceX(int deviceNum) {
        OperationFilter filter = new OperationFilter().byDevice(getMoId(deviceNum).getValue());
        allOperations = deviceControlResource.getOperationsByFilter(filter).get();
    }

    private void iShouldReceiveXOperations(int amount) {
        assertThat(allOperations.getOperations().size(), is(equalTo(amount)));
    }

    private void iCallGetOnCreatedOperation() {
        GId operationId = operation1.getId();
        operation1 = deviceControlResource.getOperation(operationId);
    }

    private void iShouldReceiveOperationWithStatusX(String status) {
        assertThat(operation1.getStatus(), is(equalTo(status)));
    }

    private void iUpdateCreatedOperationWithStatusX(String status) {
        operation1.setStatus(status);
        operation1 = deviceControlResource.update(operation1);
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

    private void iGetAllOperationsForAgent(int agentNum) throws SDKException {
        OperationFilter filter = new OperationFilter().byAgent(getMoId(agentNum).getValue());
        allOperations = deviceControlResource.getOperationsByFilter(filter).get();
    }

    private GId getMoId(int arg1) {
        GId deviceId = managedObjects.get(arg1).getId();
        return deviceId;
    }

    private void createManagedObjects() {
        ManagedObjectRepresentation agent = aSampleMo().withName("Agent").withType("com.type").with(new Agent()).build();
        ManagedObjectRepresentation device = aSampleMo().withName("Device").withType("com.type").build();
        ManagedObjectRepresentation agent2 = aSampleMo().withName("Agent2").withType("com.type").with(new Agent()).build();
        ManagedObjectRepresentation device2 = aSampleMo().withName("Device2").withType("com.type").build();

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
        inventoryApi.getManagedObject(parent.getId()).addChildDevice(deviceRef);
    }

    private static ManagedObjectRepresentationBuilder aSampleMo() {
        return anMoRepresentationLike(MO_REPRESENTATION);
    }
}
