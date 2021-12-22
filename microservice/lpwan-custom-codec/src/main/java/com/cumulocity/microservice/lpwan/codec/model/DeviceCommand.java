package com.cumulocity.microservice.lpwan.codec.model;

import com.google.common.base.Strings;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.*;


@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DeviceCommand {
    private static final String NAME = "name";
    private static final String CATEGORY = "category";
    private static final String COMMAND = "command";
    private static final String DELIVERY_TYPES = "deliveryTypes";

    @NotBlank
    @EqualsAndHashCode.Include
    private final String name;

    @NotBlank
    private final String category;

    @Null
    private final String command;

    @Null
    private final Set<DeliveryType> deliveryTypes;

    public DeviceCommand(@NotBlank String name, @NotBlank String category, @Null String command) {
        this(name, category, command, DeliveryType.DEFAULT);
    }

    public DeviceCommand(@NotBlank String name, @NotBlank String category, @Null String command, @NotNull DeliveryType... deliveryTypes) {
        this.name = name;
        this.category = category;
        this.command = command;
        this.deliveryTypes = new HashSet<DeliveryType>(Arrays.asList(deliveryTypes));
    }

    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(NAME, name);
        attributes.put(CATEGORY, category);
        attributes.put(COMMAND, command);
        attributes.put(DELIVERY_TYPES, deliveryTypes);
        return attributes;
    }


    public void validate() {
        List<String> missingParameters = new ArrayList<>(3);

        if (Strings.isNullOrEmpty(name)) {
            missingParameters.add("'name'");
        }

        if (Strings.isNullOrEmpty(category)) {
            missingParameters.add("'category'");
        }

        if(!missingParameters.isEmpty()) {
            throw new IllegalArgumentException("DeviceCommand is missing mandatory parameters: " + String.join(", ", missingParameters));
        }
    }
}
