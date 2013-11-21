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
package com.cumulocity.sdk.client.identity;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.identity.ExternalIDCollectionRepresentation;
import com.cumulocity.rest.representation.identity.ExternalIDRepresentation;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.common.JavaSdkITBase;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import com.cumulocity.sdk.client.inventory.ManagedObject;

/**
 * Test created to verify behaviour reported in the bug
 * CDSUP-1249 https://startups.jira.com/browse/CDSUP-1249
 * problem with Identity API while using thru C8Y SDK
 *
 * We have found a problem while performing the following scenario on Telematics system:
 *
 *    Create MO of type Device
 *    Assign an externalId for that MO - 85783456893
 *    Get MO by its external ID (85783456893)
 *    Delete MO with externalId = 85783456893
 *    Re-create MO of type Device
 *    Assign the same externalId for that MO - 85783456893
 *    Get MO by its external ID (85783456893) – this operation FAILS !
 *    sometimes it returns the correct MO and sometimes just 404 (doesn't find)
 */
public class ReassignExternaIDIT extends JavaSdkITBase {

    private static final String XT_ID = "extId";
    private static final String XT_TYPE = "type";
    private IdentityApi identity;
    private InventoryApi inventory;

    private List<ExternalIDRepresentation> xtCollection = new ArrayList<ExternalIDRepresentation>();


    @Before
    public void setup() throws Exception {
        identity = platform.getIdentityApi();
        inventory = platform.getInventoryApi();
        platform.setRequireResponseBody(true);
    }

    @After
    public void tearDown() throws SDKException {
        for (ExternalIDRepresentation e : xtCollection) {
            try {
                identity.deleteExternalId(e);
            } catch (SDKException e1) {
                if (e1.getHttpStatus() != 404) {
                    throw e1;
                }
            }
        }
    }

    @Test
    public void createAndDeleteManagedObject() throws Exception {
        // Given
        ExternalIDRepresentation xtIDRep = createExternalIDRep(XT_ID, XT_TYPE);

        ManagedObjectRepresentation moRep1 = createManagedObjectAndAssignExternalID("object1", xtIDRep);
        ManagedObject retrievedMO1 = getManageObjectByExternalID(getExternalIDBoundToGId(moRep1.getId()));
        retrievedMO1.delete();

        // When
        ManagedObjectRepresentation moRep2 = createManagedObjectAndAssignExternalID("object2", xtIDRep);
        ManagedObject retrievedMO2 = getManageObjectByExternalID(getExternalIDBoundToGId(moRep2.getId()));

        //Then
        assertThat(retrievedMO2, is(notNullValue()));
        assertThat(retrievedMO2.get().getName(), is("object2"));

        //cleanup
        retrievedMO2.delete();
    }

    private ManagedObjectRepresentation createManagedObjectAndAssignExternalID(String name, ExternalIDRepresentation xtIDRep) throws SDKException {
        ManagedObjectRepresentation originalMO = createManagedObject(name);
        assignExternalID(originalMO, xtIDRep);
        return originalMO;
    }

    private ManagedObject getManageObjectByExternalID(ExternalIDRepresentation externalIDRepresentation) throws SDKException {
        return inventory.getManagedObject(externalIDRepresentation.getManagedObject().getId());
    }

    private ExternalIDRepresentation getExternalIDBoundToGId(GId gId) throws SDKException {
        ExternalIDCollectionRepresentation xtIDcollectionRep = identity.getExternalIdsOfGlobalId(gId).get();
        return xtIDcollectionRep.getExternalIds().get(0);
    }

    private void assignExternalID(ManagedObjectRepresentation moRep, ExternalIDRepresentation xtIDRep) throws SDKException {
        xtIDRep.setManagedObject(moRep);
        ExternalIDRepresentation result = identity.create(xtIDRep);
        xtCollection.add(result);
    }

    private ExternalIDRepresentation createExternalIDRep(String extId, String type) {
        ExternalIDRepresentation xtIDRep = new ExternalIDRepresentation();
        xtIDRep.setExternalId(extId);
        xtIDRep.setType(type);
        return xtIDRep;
    }

    private ManagedObjectRepresentation createManagedObject(String name) throws SDKException {
        ManagedObjectRepresentation moRep = new ManagedObjectRepresentation();
        moRep.setName(name);
        return inventory.create(moRep);
    }


}
