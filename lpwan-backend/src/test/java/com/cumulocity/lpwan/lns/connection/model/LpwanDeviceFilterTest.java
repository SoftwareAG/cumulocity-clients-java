package com.cumulocity.lpwan.lns.connection.model;

import com.cumulocity.sdk.client.inventory.InventoryFilter;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LpwanDeviceFilterTest {
    @Test
    public void returnedFilterShouldHaveEmptyQueryStringIfMapPassedIsEmpty() {
        InventoryFilter inventoryFilter = LpwanDeviceFilter.of(Collections.emptyMap());
        assertEquals("", inventoryFilter.getQueryParams().get("query"));
    }

    @Test
    public void returnedFilterDoesNotContainAnd() throws UnsupportedEncodingException {
        Map<String, String> filterCriteriaMap = new HashMap<>();
        filterCriteriaMap.put("lnsConnectionName", "lns-connection-test");
        InventoryFilter inventoryFilter = LpwanDeviceFilter.of(filterCriteriaMap);
        String resultQuery = "$filter=c8y_LpwanDevice.lnsConnectionName eq 'lns-connection-test'";
        String expectedUrl = URLEncoder.encode(resultQuery,"UTF-8");
        String actualUrl = inventoryFilter.getQueryParams().get("query");

        assertEquals(expectedUrl, actualUrl);
        assertFalse(inventoryFilter.getQueryParams().get("query").contains("and"));
    }

    @Test
    public void returnedFilterContainsAnd() throws UnsupportedEncodingException {
        Map<String, String> filterCriteriaMap = new HashMap<>();
        filterCriteriaMap.put("lnsConnectionName", "lns-connection-test");
        filterCriteriaMap.put("serviceProvider", "Sigfox");
        InventoryFilter inventoryFilter = LpwanDeviceFilter.of(filterCriteriaMap);
        String resultQuery = "$filter=c8y_LpwanDevice.serviceProvider eq 'Sigfox' and c8y_LpwanDevice.lnsConnectionName eq 'lns-connection-test'";
        String expectedUrl = URLEncoder.encode(resultQuery,"UTF-8");
        String actualUrl = inventoryFilter.getQueryParams().get("query");

        assertEquals(expectedUrl, actualUrl);
        assertTrue(inventoryFilter.getQueryParams().get("query").contains("and"));
    }
}