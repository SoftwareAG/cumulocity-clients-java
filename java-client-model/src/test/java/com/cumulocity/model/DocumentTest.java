package com.cumulocity.model;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import static com.cumulocity.model.Document.copyDynamicProperties;
import static com.cumulocity.model.Document.deepCopyDynamicProperties;
import static com.google.common.collect.ImmutableMap.of;
import static org.assertj.core.api.Assertions.assertThat;

public class DocumentTest {

    @Test
    public void shouldCopyAllDynamicProperties() {
        
        //Given
        Document source = new Document() {};
        source.set("dynamic_property_value", "dynamic_property_name");
        Document target = new Document() {};
        
        //When
        Document.copyDynamicProperties(source, target);

        //Then
        assertThat(target.propertyNames() ).isNotEmpty().contains("dynamic_property_name");
        assertThat(target.get("dynamic_property_name") ).isNotNull().isEqualTo(source.get("dynamic_property_name"));
        
    }
    
    @Test
    public void shouldNotCopyAnyDynamicProperties() {
        
        //Given
        Document source = new Document() {};
        source.set("dynamic_property_value", "dynamic_property_name");
        Document target = new Document() {};
        
        //When
        Document.copyDynamicProperties(source, target , new DynamicPropertiesFilter() {
            
            @Override
            public boolean apply(String name) {
                return false;
            }
        });

        //Then
        assertThat(target.propertyNames() ).isEmpty();
        assertThat(target.get("dynamic_property_name") ).isNull();
        
    }
    
    @Test
    public void shouldNotCopyFiltredDynamicProperties() {
        
        //Given
        Document source = new Document() {};
        source.set("dynamic_property_value", "dynamic_property_name");
        source.set("otner_dynamic_property_value", "otner_dynamic_property_name");
        Document target = new Document() {};
        
        //When
        copyDynamicProperties(source, target , new DynamicPropertiesFilter() {
            @Override
            public boolean apply(String name) {
                return name.equals("dynamic_property_name");
            }
        });

        //Then
        assertThat(target.propertyNames() ).isNotEmpty().contains("dynamic_property_name");
        assertThat(target.get("dynamic_property_name") ).isNotNull().isEqualTo(source.get("dynamic_property_name"));
        
    }

    @Test
    public void shouldCopyDynamicPropertiesDeeply() {
        //given
        Document nestedObject = new Document() {};
        nestedObject.setProperty("c8y.NestedObject", of("value", 7));
        final ImmutableMap<String, ImmutableMap<String, Integer>> nestedMap = of("c8y.Measurement", of("value", 42));

        Document source = new Document() {};
        source.setProperty("c8y.SimpleProperty", "value");
        source.setProperty("c8y.CustomProperty", nestedMap);
        source.setProperty("c8y.CustomNestedObject", nestedObject);
        source.setProperty("c8y.NullProperty", null);

        //when
        Document copy = deepCopyDynamicProperties(source, new Document() {});

        //then
        assertThat(copy).isEqualTo(source);
        assertThat(copy.get("c8y.CustomNestedObject")).isNotSameAs(nestedObject);
        assertThat(copy.get("c8y.CustomProperty")).isNotSameAs(nestedMap);
    }

}
