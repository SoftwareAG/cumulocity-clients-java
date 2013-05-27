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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cumulocity.model.Agent;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.model.operation.OperationStatus;
import com.cumulocity.rest.representation.builder.ManagedObjectRepresentationBuilder;
import com.cumulocity.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.rest.representation.operation.OperationCollectionRepresentation;
import com.cumulocity.rest.representation.operation.OperationRepresentation;
import com.cumulocity.sdk.client.FixedRatePoller;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import com.cumulocity.sdk.client.devicecontrol.autopoll.OperationsByAgentAndStatusPollerImpl;
import com.cumulocity.sdk.client.devicecontrol.notification.OperationNotificationSubscriber;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.notification.Subscription;
import com.cumulocity.sdk.client.notification.SubscriptionListener;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

//TODO inline step definitions (see AlarmIT or InventoryIT)
public class DeviceControlIT extends JavaSdkITBase {

    private List<ManagedObjectRepresentation> managedObjects = new LinkedList<ManagedObjectRepresentation>();

    private DeviceControlApi deviceControlResource;

    private InventoryApi inventoryApi;

    private OperationNotificationSubscriber subscriber;

    public void createManagedObjects() throws Exception {
        //        And I have managed object with name 'Agent', type 'com.type' and 'com.cumulocity.model.Agent' fragment
        //        And I have managed object with name 'Device' and type 'com.type'
        //        And I have managed object with name 'Agent2', type 'com.type' and 'com.cumulocity.model.Agent' fragment
        //        And I have managed object with name 'Device2' and type 'com.type'
        //        And I create all
        //        And I add child devices as per the following:
        //        | parent | child |
        //        | 0      | 1     |
        //        | 2      | 3     |

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

    @Before
    public void setUp() throws Exception {
        createManagedObjects();

        deviceControlResource = platform.getDeviceControlApi();
        operation1 = null;
        allOperations = null;
    }

    @After
    public void deleteManagedObjects() throws Exception {
        List<ManagedObjectRepresentation> mosOn1stPage = getMOsFrom1stPage();
        while (!mosOn1stPage.isEmpty()) {
            deleteMOs(mosOn1stPage);
            mosOn1stPage = getMOsFrom1stPage();
        }
        if (poller != null) {
            poller.stop();
        }
        if (subscriber != null) {
            subscriber.stop();
            subscriber = null;
        }
    }

    private void deleteMOs(List<ManagedObjectRepresentation> mosOn1stPage) throws SDKException {
        for (ManagedObjectRepresentation mo : mosOn1stPage) {
            inventoryApi.getManagedObject(mo.getId()).delete();
        }
    }

    private List<ManagedObjectRepresentation> getMOsFrom1stPage() throws SDKException {
        return inventoryApi.getManagedObjects().get().getManagedObjects();
    }

    //    Scenario: Create Operation and poll it
    @Test
    public void createOperationAndPollIt() throws Exception {
        //    Given I have a poller for agent '0'
        iHaveAPollerForAgent(0);
        //    When I create an operation for device '1'
        iCreateAnOperationForDevice(1);
        //    Then poller should receive operation
        pollerShouldRecieveOperation();
    }

    //
    //    Scenario: adding operations to queue
    @Test
    public void addingOperationToQueue() throws Exception {
        //    When I get all operations for device '1'
        iGetAllOperationsForAgent(1);
        //    Then I should receive '0' operations
        iShouldReceiveXOperations(0);
        //    When I create an operation for device '1'
        iCreateAnOperationForDevice(1);
        //    And I get all operations for device '1'
        iGetAllOperationsForDeviceX(1);
        //    Then I should receive '1' operations
        iShouldReceiveXOperations(1);
    }

    //  Scenario: get notification about new operation
    @Test
    public void getNotificationAboutNewOperation() throws Exception {
        //      Given I have a operation subscriber for agent '0'
        iHaveAOperationSubscriberForAgent(0);
        //    When I create an operation for device '1'
        iCreateAnOperationForDevice(1);
        //    Then poller should receive operation
        subscriberShouldReceiveOperation();
    }

    //
    //    Scenario: Operation CRUD

    @Test
    public void operationCRUD() throws Exception {
        //    When I create an operation for device '1'
        iCreateAnOperationForDevice(1);
        //    And I call get on created operation
        iCallGetOnCreatedOperation();
        //    Then I should receive operation with status 'PENDING'
        iShouldReceiveOperationWithStatusX("PENDING");
        //    When I update created operation with status 'EXECUTING'
        iUpdateCreatedOperationWithStatusX("EXECUTING");
        //    And I call get on created operation
        iCallGetOnCreatedOperation();
        //    Then I should receive operation with status 'EXECUTING'
        iShouldReceiveOperationWithStatusX("EXECUTING");
    }

    //
    //    Scenario: query operations by status
    @Test
    public void queryOperationByStatus() throws Exception {
        iGetAllOperations();
        int numOfAll = allOperations.getOperations().size();
        iQueryOperationsWithStatusX("EXECUTING");
        int numOfExecuting = allOperations.getOperations().size();
        iQueryOperationsWithStatusX("PENDING");
        int numOfPending = allOperations.getOperations().size();

        //    When I create an operation for device '1'
        iCreateAnOperationForDevice(1);
        //    And I update created operation with status 'EXECUTING'
        iUpdateCreatedOperationWithStatusX("EXECUTING");
        //    And I create an operation for device '1'
        iCreateAnOperationForDevice(1);
        //    And I get all operations
        iGetAllOperations();
        //    Then I should receive '2' operations
        iShouldReceiveXOperations(numOfAll + 2);
        //    When I query operations with status 'PENDING'
        iQueryOperationsWithStatusX("PENDING");
        //    Then I should receive '1' operations
        iShouldReceiveXOperations(numOfPending + 1);
        //    And all received operations should have status 'PENDING'
        allRecievedOperationsShouldHaveStatusX("PENDING");
        //    When I query operations with status 'EXECUTING'
        iQueryOperationsWithStatusX("EXECUTING");
        //    Then I should receive '1' operations
        iShouldReceiveXOperations(numOfExecuting + 1);
        //    And all received operations should have status 'EXECUTING'
        allRecievedOperationsShouldHaveStatusX("EXECUTING");
    }

    //
    //    Scenario: query operations by device

    @Test
    public void queryOperationsByDevice() throws Exception {
        //    And I create an operation for device '1'
        iCreateAnOperationForDevice(1);
        //    And I create an operation for device '1'
        iCreateAnOperationForDevice(1);
        //    And I create an operation for device '3'
        iCreateAnOperationForDevice(3);
        //    When I get all operations for device '1'
        iGetAllOperationsForDeviceX(1);
        //    Then I should receive '2' operations
        iShouldReceiveXOperations(2);
        //    When I get all operations for device '3'
        iGetAllOperationsForDeviceX(3);
        //    Then I should receive '1' operations
        iShouldReceiveXOperations(1);
    }

    //
    //    Scenario: query operations by agent

    @Test
    public void queryOperationsByAgent() throws Exception {
        //    And I create an operation for device '1'
        iCreateAnOperationForDevice(1);
        //    And I create an operation for device '1'
        iCreateAnOperationForDevice(1);
        //    And I create an operation for device '3'
        iCreateAnOperationForDevice(3);
        //    When I get all operations for agent '0'
        iGetAllOperationsForAgent(0);
        //    Then I should receive '2' operations
        iShouldReceiveXOperations(2);
        //    When I get all operations for agent '2'
        iGetAllOperationsForAgent(2);
        //    Then I should receive '1' operations
        iShouldReceiveXOperations(1);
    }

    private OperationRepresentation operation1;

    SimpleOperationProcessor operationProcessor = new SimpleOperationProcessor();

    FixedRatePoller poller = null;

    OperationCollectionRepresentation allOperations;

    @Given("^I have a poller for agent '([^']*)'$")
    public void iHaveAPollerForAgent(int arg1) throws Exception {
        GId agentId = getMoId(arg1);
        poller = new OperationsByAgentAndStatusPollerImpl(deviceControlResource, agentId.getValue(), OperationStatus.PENDING,
                operationProcessor);
        poller.start();
    }

    @Given("^I have a operation subscriber for agent '([^']*)'$")
    public void iHaveAOperationSubscriberForAgent(int arg1) throws Exception {
        GId agentId = getMoId(arg1);
        subscriber = new OperationNotificationSubscriber(platform);
        subscriber.start();
        subscriber.subscribe(agentId, new SubscriptionListener<GId, OperationRepresentation>() {

            @Override
            public void onNotification(Subscription<GId> subscription, OperationRepresentation notification) {
                operationProcessor.process(notification);
            }

            @Override
            public void onError(Subscription<GId> subscription, Throwable ex) {
            }
        });
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("^I create an operation for device '([^']*)'$")
    public void iCreateAnOperationForDevice(int deviceNum) throws Exception {
        GId deviceId = getMoId(deviceNum);
        OperationRepresentation operationRepresentation = new OperationRepresentation();
        operationRepresentation.setDeviceId(deviceId);
        operationRepresentation.set("smaple_value", "sample_operation_type");
        operation1 = deviceControlResource.create(operationRepresentation);
    }

    @Then("^poller should receive operation$")
    public void pollerShouldRecieveOperation() {
        try {
            Thread.sleep(11000);
            assertThat(operationProcessor.getOperations().size(), is(equalTo(1)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Then("^subscriber should receive operation$")
    public void subscriberShouldReceiveOperation() {
        try {
            Thread.sleep(11000);
            assertThat(operationProcessor.getOperations().size(), is(equalTo(1)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @When("^I get all operations for device '([^']*)'$")
    public void iGetAllOperationsForDeviceX(int deviceNum) throws Exception {
        OperationFilter filter = new OperationFilter().byDevice(getMoId(deviceNum).getValue());
        allOperations = deviceControlResource.getOperationsByFilter(filter).get();
    }

    @Then("^I should receive '([^']*)' operations$")
    public void iShouldReceiveXOperations(int amount) {
        assertThat(allOperations.getOperations().size(), is(equalTo(amount)));
    }

    @When("^I call get on created operation$")
    public void iCallGetOnCreatedOperation() throws Exception {
        GId operationId = operation1.getId();
        operation1 = deviceControlResource.getOperation(operationId);
    }

    @Then("^I should receive operation with status '([^']*)'$")
    public void iShouldReceiveOperationWithStatusX(String status) {
        assertThat(operation1.getStatus(), is(equalTo(status)));
    }

    @When("^I update created operation with status '([^']*)'$")
    public void iUpdateCreatedOperationWithStatusX(String status) throws Exception {
        operation1.setStatus(status);
        operation1 = deviceControlResource.update(operation1);
    }

    @When("^I get all operations$")
    public void iGetAllOperations() throws Exception {
        allOperations = deviceControlResource.getOperations().get();
    }

    @When("^I query operations with status '([^']*)'$")
    public void iQueryOperationsWithStatusX(String status) throws Exception {
        OperationFilter filter = new OperationFilter().byStatus(OperationStatus.valueOf(status));
        allOperations = deviceControlResource.getOperationsByFilter(filter).get();
    }

    @Then("^all received operations should have status '([^']*)'$")
    public void allRecievedOperationsShouldHaveStatusX(String status) {
        for (OperationRepresentation operation : allOperations.getOperations()) {
            assertThat(operation.getStatus(), is(equalTo(status)));
        }
    }

    @When("^I get all operations for agent '([^']*)'$")
    public void iGetAllOperationsForAgent(int agentNum) throws SDKException {
        OperationFilter filter = new OperationFilter().byAgent(getMoId(agentNum).getValue());
        allOperations = deviceControlResource.getOperationsByFilter(filter).get();
    }

    private GId getMoId(int arg1) {
        GId deviceId = managedObjects.get(arg1).getId();
        return deviceId;
    }

}
