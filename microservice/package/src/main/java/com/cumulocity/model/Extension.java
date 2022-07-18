package com.cumulocity.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.svenson.AbstractDynamicProperties;

import java.util.Map;


@Data
@NoArgsConstructor
public class Extension extends AbstractDynamicProperties {
    private String type;

    @Builder
    public Extension(String type, @Singular Map<String, Object> properties) {
        setType(type);
        properties.entrySet().forEach(property -> {
            super.setProperty(property.getKey(), property.getValue());
        });
    }
}
