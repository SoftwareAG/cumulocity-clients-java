package com.cumulocity.lpwan.lns.connection.model;

import com.cumulocity.sdk.client.ParamSource;
import com.cumulocity.sdk.client.inventory.InventoryFilter;

public class LpwanDeviceFilter extends InventoryFilter {
    @ParamSource
    private String query;

    private LpwanDeviceFilter(String query) {
        this.query = query;
    }

    public static LpwanDeviceFilter byServiceProviderAndLnsConnectionName(String serviceProvider, String lnsConnectionName) {
        return new LpwanDeviceFilter(String.format("$filter=c8y_LpwanDevice.serviceProvider eq '%s' and c8y_LpwanDevice.lnsConnectionName eq '%s'", serviceProvider, lnsConnectionName));
    }

    public static LpwanDeviceFilter byServiceProvider(String serviceProvider) {
        return new LpwanDeviceFilter(String.format("$filter=c8y_LpwanDevice.serviceProvider eq '%s'", serviceProvider));
    }

    public static LpwanDeviceFilter of(String filterQuery){
        return new LpwanDeviceFilter(filterQuery);
    }

    public static LpwanDeviceFilter of(String filterQuery){
        return new LpwanDeviceFilter(filterQuery);
    }
}
