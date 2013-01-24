package com.cumulocity.me.sdk.client;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import com.cumulocity.me.sdk.client.PlatformImpl;
import com.cumulocity.me.sdk.client.inventory.InventoryApi;

public class PlatformImplTest {

	PlatformImpl platform = new PlatformImpl(null, null, null, null, null);
	
	@Test
	public void shouldProvideInventoryApi() throws Exception {
		InventoryApi inventory = platform.getInventoryApi();
		
		assertThat(inventory).isNotNull();
	}
	
}
