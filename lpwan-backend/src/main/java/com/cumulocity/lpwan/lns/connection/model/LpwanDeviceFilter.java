package com.cumulocity.lpwan.lns.connection.model;

import com.cumulocity.sdk.client.ParamSource;
import com.cumulocity.sdk.client.inventory.InventoryFilter;

import java.util.Map;
import java.util.Optional;

public class LpwanDeviceFilter extends InventoryFilter {

    @ParamSource
    private String query;

    private LpwanDeviceFilter(Map<String, String> filterCriteriaMap) {
        Optional<Map.Entry<String, String>> firstFilterCriterionOptional = filterCriteriaMap.entrySet().stream().findFirst();
        query = "";
        if(firstFilterCriterionOptional.isPresent()) {
            query = "$filter=c8y_LpwanDevice." + firstFilterCriterionOptional.get().getKey() + " eq " +
                    String.format("'%s'", firstFilterCriterionOptional.get().getValue());
            for(Map.Entry<String, String> filterCriterion : filterCriteriaMap.entrySet()) {
                if(!(filterCriterion.getKey().equals(firstFilterCriterionOptional.get().getKey()))) {
                    query += " and c8y_LpwanDevice." + filterCriterion.getKey() + " eq " +
                            String.format("'%s'", filterCriterion.getValue());
                }
            }
        }
    }

    public static LpwanDeviceFilter of(Map<String, String> filterCriteriaMap) {
        return new LpwanDeviceFilter(filterCriteriaMap);
    }
}
