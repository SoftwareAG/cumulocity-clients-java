/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.lns.instance.model;

import com.cumulocity.lpwan.exception.InputDataValidationException;
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



    // BELOW CONSTANTS ARE USED IN LnsInstanceTest.java and LnsInstanceControllerTest.java
    public static final SampleLnsInstance VALID_SAMPLE_LNS_INSTANCE = SampleLnsInstance.builder()
                                                                            .name("LNS Instance Name")
                                                                            .description("LNS Instance Description")
                                                                            .user("USER NAME")
                                                                            .password("**********")
                                                                            .build();

    // Use the below commented code to generate the Json for INTERNAL VIEW (which includes "password" field)
    // System.out.println(new ObjectMapper().writerWithView(LnsInstance.InternalView.class).writeValueAsString(VALID_SAMPLE_LNS_INSTANCE));
    //    {
    //        "name": "LNS Instance Name",
    //        "description": "LNS Instance Description",
    //        "user": "USER NAME",
    //        "password": "**********"
    //    }
    public static final String VALID_SAMPLE_LNS_INSTANCE_JSON_INTERNAL_VIEW = "{\"name\":\"LNS Instance Name\",\"description\":\"LNS Instance Description\",\"user\":\"USER NAME\",\"password\":\"**********\"}";

    // Use the below commented code to generate the Json for PUBLIC VIEW (which excludes "password" field)
    // System.out.println(new ObjectMapper().writerWithView(LnsInstance.PublicView.class).writeValueAsString(VALID_SAMPLE_LNS_INSTANCE));
    //    {
    //        "name": "LNS Instance Name",
    //        "description": "LNS Instance Description",
    //        "user": "USER NAME"
    //    }
    public static final String VALID_SAMPLE_LNS_INSTANCE_JSON_PUBLIC_VIEW = "{\"name\":\"LNS Instance Name\",\"description\":\"LNS Instance Description\",\"user\":\"USER NAME\"}";



    @Builder
    public SampleLnsInstance(String name, String description, String user, String password) {
        super(name, description);

        this.user = user;
        this.password = password;
    }

    @Override
    protected void validate() throws InputDataValidationException {
        List<String> missingFields = new ArrayList<>(3);

        if (StringUtils.isBlank(user)) {
            missingFields.add("'user'");
        }

        if(!missingFields.isEmpty()) {
            throw new InputDataValidationException("SampleLnsInstance is missing mandatory fields: " + String.join(", ", missingFields));
        }
    }

    @Override
    protected void update(LnsInstance lnsInstanceWithNewData) {
        SampleLnsInstance testLnsInstance = (SampleLnsInstance) lnsInstanceWithNewData;
        this.setUser(testLnsInstance.getUser());
        if(!StringUtils.isBlank(testLnsInstance.getPassword())) {
            this.setPassword(testLnsInstance.getPassword());
        }
    }
}
