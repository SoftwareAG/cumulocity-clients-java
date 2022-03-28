/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.lns.instance.model;

import com.cumulocity.lpwan.exception.InputDataValidationException;
import com.cumulocity.sdk.client.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        Validator validator = factory.getValidator();
//
//
//        Set<ConstraintViolation<LnsInstance>> constraintViolations = validator.validate(this);
//        if (!constraintViolations.isEmpty()) {
//            String message = constraintViolations.stream().map((cv) -> cv.getMessage()).collect(Collectors.joining(", "));
//            throw new InputDataValidationException(message);
//        }

        List<String> missingFields = new ArrayList<>(1);

        if (StringUtils.isBlank(name)) {
            missingFields.add("'name'");
        }

        if(!missingFields.isEmpty()) {
            throw new InputDataValidationException(this.getClass().getSimpleName() + " is missing mandatory fields: " + String.join(", ", missingFields));
        }

        this.validate();

        return true;
    }

    /**
     * Invoked by isValid().
     * Implementors are expected validate the fields and throw InputDataValidationException
     * is the instance data is semantically invalid.
     *
     * @throws InputDataValidationException Thrown if the instance is semantically invalid.
     */
    protected abstract void validate() throws InputDataValidationException;

    /**
     * Invoked by initializeWith().
     * Implementors are expected to update this instance with the contents from the passed-in instance and also
     * ensure that the passwords and other sensitive data is updated only if the corresponding contents of the
     * passed-in instance is non null and not blank.
     *
     * @param lnsInstanceWithNewData LNS Instance with updated data
     */
    protected abstract void update(@NotNull LnsInstance lnsInstanceWithNewData);
}

