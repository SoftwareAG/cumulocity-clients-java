/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2022 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.lns.instance.rest;

import com.cumulocity.lpwan.lns.instance.model.LnsInstance;
import com.cumulocity.lpwan.lns.instance.model.LnsInstanceDeserializer;
import com.cumulocity.lpwan.lns.instance.service.LnsInstanceService;
import com.cumulocity.lpwan.smaple.instance.model.SampleInstance;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.cumulocity.lpwan.smaple.instance.model.SampleInstance.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LnsInstanceController.class)
@AutoConfigureMockMvc(addFilters = false)
@EnableWebMvc
public class LnsInstanceControllerTest {
    private ObjectMapper jsonObjectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LnsInstanceService lnsInstanceService;

    @Captor
    private ArgumentCaptor<LnsInstance> lnsInstanceArgumentCaptor;


    @Before
    public void setup() {
        LnsInstanceDeserializer.registerLnsInstanceConcreteClass(SampleInstance.class);
    }

    @Test
    public void doCall_getAll_and_ensure_only_publicView_is_returned_to_client() throws Exception {
        SampleInstance testLnsInstance_1 = SampleInstance.builder()
                .name("TEST-LNS-1")
                .description("Test LNS 1")
                .user("user-1")
                .password("password!!!")
                .build();

        SampleInstance testLnsInstance_2 = SampleInstance.builder()
                .name("TEST-LNS-1")
                .description("Test LNS 1")
                .user("user-1")
                .password("password!!!")
                .build();

        List<LnsInstance> lnsInstances = Arrays.asList(new LnsInstance[] {testLnsInstance_1, testLnsInstance_2});
        when(lnsInstanceService.getAll()).thenReturn(lnsInstances);

        this.mockMvc.perform(get("/lns-instance"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonObjectMapper.writerWithView(LnsInstance.PublicView.class).writeValueAsString(lnsInstances), true));

        verify(lnsInstanceService).getAll();
    }

    @Test
    public void doCall_getAll_return_empty_list() throws Exception {
        List<LnsInstance> emptyLnsInstanceList = Collections.emptyList();
        when(lnsInstanceService.getAll()).thenReturn(emptyLnsInstanceList);

        this.mockMvc.perform(get("/lns-instance"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]", true));

        verify(lnsInstanceService).getAll();
    }

    @Test
    public void doCall_getByName_and_ensure_only_publicView_is_returned_to_client() throws Exception {
        when(lnsInstanceService.getByName(eq(VALID_SAMPLE_INSTANCE.getName()))).thenReturn(VALID_SAMPLE_INSTANCE);

        this.mockMvc.perform(get("/lns-instance/" + VALID_SAMPLE_INSTANCE.getName()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(VALID_SAMPLE_INSTANCE_JSON_PUBLIC_VIEW, true));

        verify(lnsInstanceService).getByName(eq(VALID_SAMPLE_INSTANCE.getName()));
    }

    @Test
    public void doCall_create_and_ensure_only_publicView_is_returned_to_client() throws Exception {
        when(lnsInstanceService.create(any())).thenReturn(VALID_SAMPLE_INSTANCE);

        this.mockMvc.perform(post("/lns-instance")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(VALID_SAMPLE_INSTANCE_JSON_INTERNAL_VIEW))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(VALID_SAMPLE_INSTANCE_JSON_PUBLIC_VIEW, true));

        verify(lnsInstanceService).create(lnsInstanceArgumentCaptor.capture());
        LnsInstance lnsInstanceCapturedArgument = lnsInstanceArgumentCaptor.getValue();
        compare(VALID_SAMPLE_INSTANCE, lnsInstanceCapturedArgument);
    }

    @Test
    public void doCall_update_and_ensure_only_publicView_is_returned_to_client() throws Exception {
        String existingLnsInstanceName = "EXISTING-TEST-LNS-1";
        when(lnsInstanceService.update(eq(existingLnsInstanceName), any())).thenReturn(VALID_SAMPLE_INSTANCE);

        this.mockMvc.perform(put("/lns-instance/" + existingLnsInstanceName)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(VALID_SAMPLE_INSTANCE_JSON_INTERNAL_VIEW))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(VALID_SAMPLE_INSTANCE_JSON_PUBLIC_VIEW, true));

        verify(lnsInstanceService).update(eq(existingLnsInstanceName), lnsInstanceArgumentCaptor.capture());
        LnsInstance lnsInstanceCapturedArgument = lnsInstanceArgumentCaptor.getValue();
        compare(VALID_SAMPLE_INSTANCE, lnsInstanceCapturedArgument);
    }

    @Test
    public void doCall_delete() throws Exception {
        String lnsInstanceNameToDelete = "DELETE-TEST-LNS-1";

        this.mockMvc.perform(delete("/lns-instance/" + lnsInstanceNameToDelete))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(lnsInstanceService).delete(eq(lnsInstanceNameToDelete));
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