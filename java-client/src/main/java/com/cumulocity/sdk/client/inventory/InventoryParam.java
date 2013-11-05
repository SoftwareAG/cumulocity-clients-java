package com.cumulocity.sdk.client.inventory;

import com.cumulocity.sdk.client.Param;

public enum InventoryParam implements Param {
    WITH_PARENTS("withParents"), SKIP_CHILDREN_NAMES("skipChildrenNames");
    
    private String paramName;

    InventoryParam(String paramName) {
        this.paramName = paramName;
    }

    public String getName() {
        return paramName;
    }
}
