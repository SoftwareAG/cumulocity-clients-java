package com.cumulocity.lpwan.lns.connection.model;

import com.cumulocity.sdk.client.ParamSource;
import com.cumulocity.sdk.client.inventory.InventoryFilter;

public class LpwanDeviceFilter extends InventoryFilter {

    @ParamSource
    private String query;

    private LpwanDeviceFilter(String lnsConnectionName) {
        query = "$filter=c8y_LpwanDevice.lnsConnectionName eq " +
                String.format("'%s'", lnsConnectionName);
    }

    public static LpwanDeviceFilter of(String lnsConnectionName) {
        return new LpwanDeviceFilter(lnsConnectionName);
    }
}
