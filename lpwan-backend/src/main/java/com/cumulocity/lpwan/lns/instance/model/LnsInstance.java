/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.lns.instance.model;

import com.cumulocity.lpwan.lns.instance.exception.InputDataValidationException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementers of LnsInstance class have to annotate the fields of the concrete class
 * to be part of either @JsonView(LnsInstance.PublicView.class) or @JsonView(LnsInstance.InternalView.class).
 *
 * All the fields with sensitive data which should not be serialized and sent to the client
 * have to be marked with InternalView and rest with PublicView.
 */
@JsonDeserialize(using = LnsInstanceDeserializer.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class LnsInstance {

    @NotBlank
    @JsonView(LnsInstance.PublicView.class)
    private String name;

    @JsonView(LnsInstance.PublicView.class)
    private String description;

    // JSON View interface for tagging Publicly visible fields
    public interface PublicView {}

    // JSON View interface for tagging Internally visible fields
    public interface InternalView extends PublicView {}

    public void initializeWith(LnsInstance lnsInstance) {
        this.setName(lnsInstance.getName());
        this.setDescription(lnsInstance.getDescription());

        this.update(lnsInstance);
    }

    @JsonIgnore
    public boolean isValid() throws InputDataValidationException {
        List<String> missingFields = new ArrayList<>(3);

        if (StringUtils.isBlank(name)) {
            missingFields.add("'name'");
        }

        if(!missingFields.isEmpty()) {
            throw new InputDataValidationException("LnsInstance is missing mandatory fields: " + String.join(", ", missingFields));
        }

        this.validate();

        return true;
    }

    protected abstract void validate() throws InputDataValidationException;

    protected abstract void update(LnsInstance lnsInstance);
}

