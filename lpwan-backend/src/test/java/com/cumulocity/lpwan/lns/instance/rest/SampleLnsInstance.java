/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.lns.instance.rest;

import com.cumulocity.lpwan.lns.instance.exception.InvalidInputDataException;
import com.cumulocity.lpwan.lns.instance.model.LnsInstance;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;


@JsonDeserialize(using = JsonDeserializer.None.class) // Important to override the base class' usage of LnsInstanceDeserializer which produces an infinite loop
@Getter
@Setter
@NoArgsConstructor
public class SampleLnsInstance extends LnsInstance {

    @NotBlank
    @JsonView(LnsInstance.PublicView.class)
    private String user;

    @JsonView(LnsInstance.InternalView.class)
    private String password;

    @Builder
    public SampleLnsInstance(String name, String description, String user, String password) {
        super(name, description);

        this.user = user;
        this.password = password;
    }

    @Override
    protected void validate() throws InvalidInputDataException {
        List<String> missingFields = new ArrayList<>(3);

        if (StringUtils.isBlank(user)) {
            missingFields.add("'user'");
        }

        if(!missingFields.isEmpty()) {
            throw new InvalidInputDataException("SampleLnsInstance is missing mandatory fields: " + String.join(", ", missingFields));
        }
    }

    @Override
    protected void update(LnsInstance lnsInstance) {
        SampleLnsInstance testLnsInstance = (SampleLnsInstance)lnsInstance;
        this.setUser(testLnsInstance.getUser());
        if(!StringUtils.isBlank(testLnsInstance.getPassword())) {
            this.setPassword(testLnsInstance.getPassword());
        }
    }
}
