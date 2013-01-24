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
package com.cumulocity.sdk.client.inventory;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.cumulocity.model.util.ExtensibilityConverter;

public class InventoryFilterTest {

    @Test
    public void shouldHaveFragmentTypeAndType() {
        // Given
        String fragmentType = "myFragment";
        String type = "myType";

        // When
        InventoryFilter filter = new InventoryFilter().byFragmentType(fragmentType).byType(type);

        // Then
        assertThat(filter.getFragmentType(), is(fragmentType));
        assertThat(filter.getType(), is(type));
    }

    @Test
    public void shouldSupportFragmentTypeWhichIsClass() throws Exception {
        // Given
        Class<?> fragmentClass = Object.class;

        // When
        InventoryFilter filter = new InventoryFilter().byFragmentType(fragmentClass);

        // Then
        String expectedFragmentType = ExtensibilityConverter.classToStringRepresentation(fragmentClass);
        assertThat(filter.getFragmentType(), is(expectedFragmentType));
    }
}
