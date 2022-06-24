package com.cumulocity.lpwan.lns.connection.model;

import com.cumulocity.sdk.client.ParamSource;
import com.cumulocity.sdk.client.inventory.InventoryFilter;

import java.util.Map;

public class LpwanDeviceFilter extends InventoryFilter {

    @ParamSource
    private String query;

    private LpwanDeviceFilter(Map<String, String> filterCriteriaMap) {
        Map.Entry<String, String> firstFilterCriterion = filterCriteriaMap.entrySet().stream().findFirst().get();
        query = "$filter=c8y_LpwanDevice." + firstFilterCriterion.getKey() + " eq " +
                String.format("'%s'", firstFilterCriterion.getValue());
        for(Map.Entry<String, String> filterCriterion : filterCriteriaMap.entrySet()) {
            if(!(filterCriterion.getKey().equals(firstFilterCriterion.getKey()))) {
                query += " and c8y_LpwanDevice." + filterCriterion.getKey() + " eq " +
                        String.format("'%s'", filterCriterion.getValue());
            }
        }
    }

    public static LpwanDeviceFilter of(Map<String, String> filterCriteriaMap) {
        return new LpwanDeviceFilter(filterCriteriaMap);
    }
}
