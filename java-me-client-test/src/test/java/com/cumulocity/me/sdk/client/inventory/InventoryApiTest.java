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