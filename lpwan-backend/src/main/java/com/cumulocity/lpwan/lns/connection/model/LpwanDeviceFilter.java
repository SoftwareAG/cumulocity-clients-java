package com.cumulocity.lpwan.lns.connection.model;

import com.cumulocity.sdk.client.ParamSource;
import com.cumulocity.sdk.client.inventory.InventoryFilter;

public class LpwanDeviceFilter extends InventoryFilter {

    @ParamSource
    private String query;

    private LpwanDeviceFilter(String filterCriteria, String filterCriteriaValue) {
        query = "$filter=c8y_LpwanDevice." + filterCriteria + " eq " +
                String.format("'%s'", filterCriteriaValue);
    }

    public static LpwanDeviceFilter of(String filterCriteria, String filterCriteriaValue) {
        return new LpwanDeviceFilter(filterCriteria, filterCriteriaValue);
    }
}
