/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.lns.instance.model;

import com.cumulocity.lpwan.exception.InputDataValidationException;
import com.cumulocity.lpwan.smaple.instance.model.SampleInstance;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.JsonExpectationsHelper;

import static com.cumulocity.lpwan.smaple.instance.model.SampleInstance.*;
import static org.junit.jupiter.api.Assertions.*;


class LnsInstanceTest {

    private ObjectMapper jsonObjectMapper = new ObjectMapper();

    @Test
    void doValidateLnsInstance_valid_description_and_password_null() {
        try {
            assertTrue(VALID_SAMPLE_INSTANCE.isValid());
        } catch (InputDataValidationException e) {
            fail("Unexpected InputDataValidationException thrown with message " + e.getMessage());
        }
    }

    @Test
    void doValidateLnsInstance_invalid_name_null() {
        LnsInstance invalid_instance = SampleInstance.builder()
                                            .name(null)
                                            .description(null)
                                            .user("USER NAME")
                                            .password("**********")
                                            .build();

        InputDataValidationException inputDataValidationException = Assert.assertThrows(InputDataValidationException.class, invalid_instance::isValid);
        assertEquals("SampleInstance is missing mandatory fields: 'name'", inputDataValidationException.getMessage());
    }

    @Test
    void doValidateLnsInstance_invalid_name_blank() {
        LnsInstance invalid_instance = SampleInstance.builder()
                                            .name("   ")
                                            .description(null)
                                            .user("USER NAME")
                                            .password("**********")
                                            .build();

        InputDataValidationException inputDataValidationException = Assert.assertThrows(InputDataValidationException.class, invalid_instance::isValid);
        assertEquals("SampleInstance is missing mandatory fields: 'name'", inputDataValidationException.getMessage());
    }

    @Test
    void doValidateLnsInstance_invalid_user_null() {
        LnsInstance invalid_instance = SampleInstance.builder()
                                                .name("Sample Instance Name")
                                                .description("Sample Instance Description")
                                                .user(null)
                                                .password("**********")
                                                .build();

        InputDataValidationException inputDataValidationException = Assert.assertThrows(InputDataValidationException.class, invalid_instance::isValid);
        assertEquals("SampleInstance is missing mandatory fields: 'user'", inputDataValidationException.getMessage());
    }

    @Test
    void doValidateLnsInstance_invalid_user_balnk() {
        LnsInstance invalid_instance = SampleInstance.builder()
                                            .name("Sample Instance Name")
                                            .description("Sample Instance Description")
                                            .user("    ")
                                            .password("**********")
                                            .build();

        InputDataValidationException inputDataValidationException = Assert.assertThrows(InputDataValidationException.class, invalid_instance::isValid);
        assertEquals("SampleInstance is missing mandatory fields: 'user'", inputDataValidationException.getMessage());
    }

    @Test
    void doUpdateLnsInstance_with_non_null_password() {
        SampleInstance existingLnsInstance = SampleInstance.builder()
                                                    .name("Sample Instance Name")
                                                    .description("Sample Instance Description")
                                                    .user("USER NAME")
                                                    .password("**********")
                                                    .build();

        SampleInstance lnsInstanceWithUpdatedContents = SampleInstance.builder()
                                                                .name("Sample Instance Name (NEW)")
                                                                .description("Sample Instance Description (NEW)")
                                                                .user("USER NAME (NEW)")
                                                                .password("********** (NEW)")
                                                                .build();

        existingLnsInstance.initializeWith(lnsInstanceWithUpdatedContents);

        compare(lnsInstanceWithUpdatedContents, existingLnsInstance);
    }

    @Test
    void doUpdateLnsInstance_with_null_password() {
        SampleInstance existingLnsInstance = SampleInstance.builder()
                                                    .name("Sample Instance Name")
                                                    .description("Sample Instance Description")
                                                    .user("USER NAME")
                                                    .password("**********")
                                                    .build();

        SampleInstance lnsInstanceWithUpdatedContents = SampleInstance.builder()
                                                                .name("Sample Instance Name (NEW)")
                                                                .description("Sample Instance Description (NEW)")
                                                                .user("USER NAME (NEW)")
                                                                .password(null)
                                                                .build();

        existingLnsInstance.initializeWith(lnsInstanceWithUpdatedContents);

        compare(SampleInstance.builder()
                        .name(lnsInstanceWithUpdatedContents.getName())
                        .description(lnsInstanceWithUpdatedContents.getDescription())
                        .user(lnsInstanceWithUpdatedContents.getUser())
                        .password(existingLnsInstance.getPassword())
                        .build()
                , existingLnsInstance);
    }

    @Test
    void doUpdateLnsInstance_with_blank_password() {
        SampleInstance existingLnsInstance = SampleInstance.builder()
                                                        .name("Sample Instance Name")
                                                        .description("Sample Instance Description")
                                                        .user("USER NAME")
                                                        .password("**********")
                                                        .build();

        SampleInstance lnsInstanceWithUpdatedContents = SampleInstance.builder()
                                                                .name("Sample Instance Name (NEW)")
                                                                .description("Sample Instance Description (NEW)")
                                                                .user("USER NAME (NEW)")
                                                                .password("   ")
                                                                .build();

        existingLnsInstance.initializeWith(lnsInstanceWithUpdatedContents);


        compare(SampleInstance.builder()
                        .name(lnsInstanceWithUpdatedContents.getName())
                        .description(lnsInstanceWithUpdatedContents.getDescription())
                        .user(lnsInstanceWithUpdatedContents.getUser())
                        .password(existingLnsInstance.getPassword())
                        .build()
                , existingLnsInstance);
    }

    @Test
    void doDeSerializeLnsInstance_with_valueType_as_LnsInstance_insteadof_SampleInstance() throws JsonProcessingException {
        LnsInstanceDeserializer.registerLnsInstanceConcreteClass(SampleInstance.class);

        LnsInstance lnsInstance = jsonObjectMapper.readValue(VALID_SAMPLE_INSTANCE_JSON_INTERNAL_VIEW, LnsInstance.class);

        compare(VALID_SAMPLE_INSTANCE, lnsInstance);
    }

    @Test
    void doSerializeLnsInstance_with_PublicView_must_EXCLUDE_Password() throws Exception {
        String sampleInstanceSerializedWithPublicView = jsonObjectMapper.writerWithView(LnsInstance.PublicView.class).writeValueAsString(VALID_SAMPLE_INSTANCE);

        new JsonExpectationsHelper().assertJsonEqual(VALID_SAMPLE_INSTANCE_JSON_PUBLIC_VIEW, sampleInstanceSerializedWithPublicView, true);
    }

    @Test
    void doSerializeLnsInstance_with_InternalView_must_INCLUDE_Password() throws Exception {
        String sampleInstanceSerializedWithPublicView = jsonObjectMapper.writerWithView(LnsInstance.InternalView.class).writeValueAsString(VALID_SAMPLE_INSTANCE);

        new JsonExpectationsHelper().assertJsonEqual(VALID_SAMPLE_INSTANCE_JSON_INTERNAL_VIEW, sampleInstanceSerializedWithPublicView, true);
    }

    private void compare(SampleInstance expected, LnsInstance actual) {
        assertTrue(expected.getClass().isAssignableFrom(actual.getClass()));

        SampleInstance actualTestLnsInstance = (SampleInstance) actual;

        assertEquals(expected.getName(), actualTestLnsInstance.getName());
        assertEquals(expected.getDescription(), actualTestLnsInstance.getDescription());
        assertEquals(expected.getUser(), actualTestLnsInstance.getUser());
        assertEquals(expected.getPassword(), actualTestLnsInstance.getPassword());
    }
}