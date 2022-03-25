/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.lns.instance.model;

import com.cumulocity.lpwan.exception.InputDataValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.JsonExpectationsHelper;

import static com.cumulocity.lpwan.lns.instance.model.SampleLnsInstance.*;
import static org.junit.jupiter.api.Assertions.*;


class LnsInstanceTest {

    private ObjectMapper jsonObjectMapper = new ObjectMapper();

    @Test
    void doValidateLnsInstance_valid_description_and_password_null() {
        try {
            assertTrue(VALID_SAMPLE_LNS_INSTANCE.isValid());
        } catch (InputDataValidationException e) {
            fail("Unexpected InputDataValidationException thrown with message " + e.getMessage());
        }
    }

    @Test
    void doValidateLnsInstance_invalid_name_null() {
        LnsInstance invalid_instance = SampleLnsInstance.builder()
                                            .name(null)
                                            .description(null)
                                            .user("USER NAME")
                                            .password("**********")
                                            .build();

        InputDataValidationException inputDataValidationException = Assert.assertThrows(InputDataValidationException.class, invalid_instance::isValid);
        assertEquals("LnsInstance is missing mandatory fields: 'name'", inputDataValidationException.getMessage());
    }

    @Test
    void doValidateLnsInstance_invalid_name_blank() {
        LnsInstance invalid_instance = SampleLnsInstance.builder()
                                            .name("   ")
                                            .description(null)
                                            .user("USER NAME")
                                            .password("**********")
                                            .build();

        InputDataValidationException inputDataValidationException = Assert.assertThrows(InputDataValidationException.class, invalid_instance::isValid);
        assertEquals("LnsInstance is missing mandatory fields: 'name'", inputDataValidationException.getMessage());
    }

    @Test
    void doValidateLnsInstance_invalid_user_null() {
        LnsInstance invalid_instance = SampleLnsInstance.builder()
                                                .name("LNS Instance Name")
                                                .description("LNS Instance Description")
                                                .user(null)
                                                .password("**********")
                                                .build();

        InputDataValidationException inputDataValidationException = Assert.assertThrows(InputDataValidationException.class, invalid_instance::isValid);
        assertEquals("SampleLnsInstance is missing mandatory fields: 'user'", inputDataValidationException.getMessage());
    }

    @Test
    void doValidateLnsInstance_invalid_user_balnk() {
        LnsInstance invalid_instance = SampleLnsInstance.builder()
                                            .name("LNS Instance Name")
                                            .description("LNS Instance Description")
                                            .user("    ")
                                            .password("**********")
                                            .build();

        InputDataValidationException inputDataValidationException = Assert.assertThrows(InputDataValidationException.class, invalid_instance::isValid);
        assertEquals("SampleLnsInstance is missing mandatory fields: 'user'", inputDataValidationException.getMessage());
    }

    @Test
    void doUpdateLnsInstance_with_non_null_password() {
        SampleLnsInstance existingLnsInstance = SampleLnsInstance.builder()
                                                    .name("LNS Instance Name")
                                                    .description("LNS Instance Description")
                                                    .user("USER NAME")
                                                    .password("**********")
                                                    .build();

        SampleLnsInstance lnsInstanceWithUpdatedContents = SampleLnsInstance.builder()
                                                                .name("LNS Instance Name (NEW)")
                                                                .description("LNS Instance Description (NEW)")
                                                                .user("USER NAME (NEW)")
                                                                .password("********** (NEW)")
                                                                .build();

        existingLnsInstance.initializeWith(lnsInstanceWithUpdatedContents);

        compare(lnsInstanceWithUpdatedContents, existingLnsInstance);
    }

    @Test
    void doUpdateLnsInstance_with_null_password() {
        SampleLnsInstance existingLnsInstance = SampleLnsInstance.builder()
                                                    .name("LNS Instance Name")
                                                    .description("LNS Instance Description")
                                                    .user("USER NAME")
                                                    .password("**********")
                                                    .build();

        SampleLnsInstance lnsInstanceWithUpdatedContents = SampleLnsInstance.builder()
                                                                .name("LNS Instance Name (NEW)")
                                                                .description("LNS Instance Description (NEW)")
                                                                .user("USER NAME (NEW)")
                                                                .password(null)
                                                                .build();

        existingLnsInstance.initializeWith(lnsInstanceWithUpdatedContents);

        compare(SampleLnsInstance.builder()
                        .name(lnsInstanceWithUpdatedContents.getName())
                        .description(lnsInstanceWithUpdatedContents.getDescription())
                        .user(lnsInstanceWithUpdatedContents.getUser())
                        .password(existingLnsInstance.getPassword())
                        .build()
                , existingLnsInstance);
    }

    @Test
    void doUpdateLnsInstance_with_blank_password() {
        SampleLnsInstance existingLnsInstance = SampleLnsInstance.builder()
                                                        .name("LNS Instance Name")
                                                        .description("LNS Instance Description")
                                                        .user("USER NAME")
                                                        .password("**********")
                                                        .build();

        SampleLnsInstance lnsInstanceWithUpdatedContents = SampleLnsInstance.builder()
                                                                .name("LNS Instance Name (NEW)")
                                                                .description("LNS Instance Description (NEW)")
                                                                .user("USER NAME (NEW)")
                                                                .password("   ")
                                                                .build();

        existingLnsInstance.initializeWith(lnsInstanceWithUpdatedContents);


        compare(SampleLnsInstance.builder()
                        .name(lnsInstanceWithUpdatedContents.getName())
                        .description(lnsInstanceWithUpdatedContents.getDescription())
                        .user(lnsInstanceWithUpdatedContents.getUser())
                        .password(existingLnsInstance.getPassword())
                        .build()
                , existingLnsInstance);
    }

    @Test
    void doDeSerializeLnsInstance_with_valueType_as_LnsInstance_insteadof_SampleLnsInstance() throws JsonProcessingException {
        LnsInstanceDeserializer.registerLnsInstanceConcreteClass("Sample", SampleLnsInstance.class);

        LnsInstance lnsInstance = jsonObjectMapper.readValue(VALID_SAMPLE_LNS_INSTANCE_JSON_INTERNAL_VIEW, LnsInstance.class);

        compare(VALID_SAMPLE_LNS_INSTANCE, lnsInstance);
    }

    @Test
    void doSerializeLnsInstance_with_PublicView_must_EXCLUDE_Password() throws Exception {
        String sampleInstanceSerializedWithPublicView = jsonObjectMapper.writerWithView(LnsInstance.PublicView.class).writeValueAsString(VALID_SAMPLE_LNS_INSTANCE);

        new JsonExpectationsHelper().assertJsonEqual(VALID_SAMPLE_LNS_INSTANCE_JSON_PUBLIC_VIEW, sampleInstanceSerializedWithPublicView, true);
    }

    @Test
    void doSerializeLnsInstance_with_InternalView_must_INCLUDE_Password() throws Exception {
        String sampleInstanceSerializedWithPublicView = jsonObjectMapper.writerWithView(LnsInstance.InternalView.class).writeValueAsString(VALID_SAMPLE_LNS_INSTANCE);

        new JsonExpectationsHelper().assertJsonEqual(VALID_SAMPLE_LNS_INSTANCE_JSON_INTERNAL_VIEW, sampleInstanceSerializedWithPublicView, true);
    }

    private void compare(SampleLnsInstance expected, LnsInstance actual) {
        assertTrue(expected.getClass().isAssignableFrom(actual.getClass()));

        SampleLnsInstance actualTestLnsInstance = (SampleLnsInstance) actual;

        assertEquals(expected.getName(), actualTestLnsInstance.getName());
        assertEquals(expected.getDescription(), actualTestLnsInstance.getDescription());
        assertEquals(expected.getUser(), actualTestLnsInstance.getUser());
        assertEquals(expected.getPassword(), actualTestLnsInstance.getPassword());
    }
}