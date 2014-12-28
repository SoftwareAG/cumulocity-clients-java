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

import static com.cumulocity.me.rest.representation.RestRepresentationObjectMother.anMoRefRepresentationLike;
import static com.cumulocity.me.rest.representation.RestRepresentationObjectMother.anMoRepresentationLike;
import static com.cumulocity.me.rest.representation.SampleManagedObjectReferenceRepresentation.MO_REF_REPRESENTATION;
import static com.cumulocity.me.rest.representation.SampleManagedObjectRepresentation.MO_REPRESENTATION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import c8y.Relay;
import c8y.Relay.RelayState;

import com.cumulocity.me.lang.ArrayList;
import com.cumulocity.me.lang.Iterator;
import com.cumulocity.me.lang.List;
import com.cumulocity.me.model.idtype.GId;
import com.cumulocity.me.model.operation.OperationStatus;
import com.cumulocity.me.rest.convert.devicecontrol.AgentConverter;
import com.cumulocity.me.rest.convert.devicecontrol.RelayConverter;
import com.cumulocity.me.rest.representation.devicecontrol.SimpleOperationProcessor;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectCollectionRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectReferenceRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentationBuilder;
import com.cumulocity.me.rest.representation.operation.OperationCollectionRepresentation;
import com.cumulocity.me.rest.representation.operation.OperationRepresentation;
import com.cumulocity.me.sdk.SDKException;
import com.cumulocity.me.sdk.client.devicecontrol.DeviceControlApi;
import com.cumulocity.me.sdk.client.devicecontrol.OperationFilter;
import com.cumulocity.me.sdk.client.inventory.InventoryApi;
import com.cumulocity.model.Agent;
import com.cumulocity.sdk.client.common.JavaSdkITBase;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DeviceControlIT extends JavaSdkITBase {

    private List managedObjects = new ArrayList();

    private DeviceControlApi deviceControlResource;

    private InventoryApi inventoryApi;

    @BeforeClass
    public static void registerConverters() {
        AgentConverter agentConverter = new AgentConverter();
        platform.getConversionService().register(agentConverter);
        platform.getValidationService().register(agentConverter);
        
        RelayConverter relayConverter = new RelayConverter();
        platform.getConversionService().register(relayConverter);
        platform.getValidationService().register(relayConverter);
    }

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

        ManagedObjectRepresentation agent = aSampleMo().withName("Agent").withType("com.cumulocity.me.rest.representation.devicecontrol.Agent").with(new Agent()).build();
        ManagedObjectRepresentation device = aSampleMo().withName("Device").withType("com.type").build();
        ManagedObjectRepresentation agent2 = aSampleMo().withName("Agent2").withType("com.cumulocity.me.rest.representation.devicecontrol.Agent").with(new Agent()).build();
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
        List mosOn1stPage = getMOsFrom1stPage();
        while (!mosOn1stPage.isEmpty()) {
            deleteMOs(mosOn1stPage);
            mosOn1stPage = getMOsFrom1stPage();
        }
    }

    private void deleteMOs(List mosOn1stPage) throws SDKException {
        Iterator iterator = mosOn1stPage.iterator();
        while (iterator.hasNext()) {
            inventoryApi.getManagedObject(((ManagedObjectRepresentation) iterator.next()).getId()).delete();
        }
    }

    private List getMOsFrom1stPage() throws SDKException {
        return ((ManagedObjectCollectionRepresentation) inventoryApi.getManagedObjects().get()).getManagedObjects();
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
        int numOfPending = allOperations.getOperations().size();
        iQueryOperationsWithStatusX("PENDING");

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

    OperationCollectionRepresentation allOperations;

    @When("^I create an operation for device '([^']*)'$")
    public void iCreateAnOperationForDevice(int deviceNum) throws Exception {
        GId deviceId = getMoId(deviceNum);
        OperationRepresentation operationRepresentation = new OperationRepresentation();
        operationRepresentation.setDeviceId(deviceId);
        operationRepresentation.set("smaple_value", "sample_operation_type");
        operation1 = deviceControlResource.create(operationRepresentation);
    }
    
    @Test
    public void shouldCreateRelayOperation() {
        GId deviceId = getMoId(1);
        OperationRepresentation operation = new OperationRepresentation();
        Relay relayControl = new Relay();
        relayControl.setRelayState(RelayState.OPEN);
        operation.set(relayControl);
        operation.setDeviceId(deviceId);
        
        operation1 = deviceControlResource.create(operation);
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

    @When("^I get all operations for device '([^']*)'$")
    public void iGetAllOperationsForDeviceX(int deviceNum) throws Exception {
        OperationFilter filter = new OperationFilter().byDevice(getMoId(deviceNum).getValue());
        allOperations = (OperationCollectionRepresentation) deviceControlResource.getOperationsByFilter(filter).get();
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
        allOperations = (OperationCollectionRepresentation) deviceControlResource.getOperations().get();
    }

    @When("^I query operations with status '([^']*)'$")
    public void iQueryOperationsWithStatusX(String status) throws Exception {
        OperationFilter filter = new OperationFilter().byStatus(OperationStatus.valueOf(status));
        allOperations = (OperationCollectionRepresentation) deviceControlResource.getOperationsByFilter(filter).get();
    }

    @Then("^all received operations should have status '([^']*)'$")
    public void allRecievedOperationsShouldHaveStatusX(String status) {
        Iterator iterator = allOperations.getOperations().iterator();
        while (iterator.hasNext()) {
            assertThat(((OperationRepresentation) iterator.next()).getStatus(), is(equalTo(status)));
        }
    }

    @When("^I get all operations for agent '([^']*)'$")
    public void iGetAllOperationsForAgent(int agentNum) throws SDKException {
        OperationFilter filter = new OperationFilter().byAgent(getMoId(agentNum).getValue());
        allOperations = (OperationCollectionRepresentation) deviceControlResource.getOperationsByFilter(filter).get();
    }

    private GId getMoId(int arg1) {
        GId deviceId = ((ManagedObjectRepresentation) managedObjects.get(arg1)).getId();
        return deviceId;
    }

}
