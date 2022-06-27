package com.cumulocity.lpwan.lns.connection.model;

import com.cumulocity.sdk.client.ParamSource;
import com.cumulocity.sdk.client.inventory.InventoryFilter;

public class LpwanDeviceFilter extends InventoryFilter {
    @ParamSource
    private String query;

    private LpwanDeviceFilter(String serviceProvider, String lnsConnectionName) {
        this.query = String.format("$filter=c8y_LpwanDevice.serviceProvider eq '%s' and c8y_LpwanDevice.lnsConnectionName eq '%s'", serviceProvider, lnsConnectionName);
    }

    public static LpwanDeviceFilter byServiceProviderAndLnsConnectionName(String serviceProvider, String lnsConnectionName) {
        return new LpwanDeviceFilter(encode(serviceProvider), encode(lnsConnectionName));
    }
}
