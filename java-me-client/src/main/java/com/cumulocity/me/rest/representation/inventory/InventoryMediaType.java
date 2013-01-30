/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cumulocity.me.rest.representation.inventory;

import com.cumulocity.me.rest.representation.BaseCumulocityMediaType;

public class InventoryMediaType extends BaseCumulocityMediaType {

    public static final InventoryMediaType MANAGED_OBJECT = new InventoryMediaType("managedObject");

    public static final InventoryMediaType MANAGED_OBJECT_COLLECTION = new InventoryMediaType("managedObjectCollection");

    public static final InventoryMediaType MANAGED_OBJECT_REFERENCE = new InventoryMediaType("managedObjectReference");

    public static final InventoryMediaType MANAGED_OBJECT_REFERENCE_COLLECTION = new InventoryMediaType("managedObjectReferenceCollection");

    public static final InventoryMediaType INVENTORY_API = new InventoryMediaType("inventoryApi");

    public static final String MANAGED_OBJECT_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "managedObject+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final String MANAGED_OBJECT_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "managedObjectCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final String MANAGED_OBJECT_REFERENCE_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "managedObjectReference+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final String MANAGED_OBJECT_REFERENCE_COLLECTION_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY
            + "managedObjectReferenceCollection+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public static final String INVENTORY_API_TYPE = APPLICATION_VND_COM_NSN_CUMULOCITY + "inventoryApi+json;" + VND_COM_NSN_CUMULOCITY_PARAMS;

    public InventoryMediaType(String string) {
        super(string);
    }
}
