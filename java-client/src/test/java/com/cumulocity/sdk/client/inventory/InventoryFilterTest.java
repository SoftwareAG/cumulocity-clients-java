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
