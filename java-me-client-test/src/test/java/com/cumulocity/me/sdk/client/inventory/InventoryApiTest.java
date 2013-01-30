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
package com.cumulocity.me.sdk.client.inventory;

import static com.cumulocity.me.rest.representation.inventory.InventoryMediaType.MANAGED_OBJECT;
import static com.cumulocity.me.rest.representation.inventory.InventoryRepresentationBuilder.anInventoryRepresentation;
import static com.cumulocity.model.pagination.PageRequest.DEFAULT_PAGE_SIZE;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.cumulocity.me.rest.representation.inventory.InventoryRepresentation;
import com.cumulocity.me.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.me.sdk.client.BaseApiTestCase;
import com.cumulocity.me.sdk.client.inventory.InventoryApi;
import com.cumulocity.me.sdk.client.inventory.InventoryApiImpl;

public class InventoryApiTest extends BaseApiTestCase {

	InventoryApi inventoryApi = new InventoryApiImpl(restConnector, templateUrlParser, PLATFORM_URL, DEFAULT_PAGE_SIZE);
	
	InventoryRepresentation inventoryRepresentation = anInventoryRepresentation(PLATFORM_URL)
	        .withManagedObjects()
	        .build();
	
	ManagedObjectRepresentation inputMo = new ManagedObjectRepresentation();
	ManagedObjectRepresentation outputMo = new ManagedObjectRepresentation();
	
	@Before
	public void setUp() throws Exception {
	    super.setUp();
        platformApiRepresentation.setInventory(inventoryRepresentation);
	}

    @Test
	public void shouldCreateManagedObject() throws Exception {
		when(restConnector.post(inventoryRepresentation.getManagedObjects().getSelf(), MANAGED_OBJECT, inputMo))
		    .thenReturn(outputMo);
	    
	    ManagedObjectRepresentation result = inventoryApi.create(inputMo);
		
		assertThat(result).isSameAs(outputMo);
	}
}