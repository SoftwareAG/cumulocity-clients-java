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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
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

    static {
        LnsInstanceDeserializer.registerLnsInstanceConcreteClass("Test", SampleLnsInstance.class);
    }

    @Test
    public void doCall_getAll_and_ensure_only_publicView_is_returned_to_client() throws Exception {
        SampleLnsInstance testLnsInstance_1 = SampleLnsInstance.builder()
                .name("TEST-LNS-1")
                .description("Test LNS 1")
                .user("user-1")
                .password("password!!!")
                .build();

        SampleLnsInstance testLnsInstance_2 = SampleLnsInstance.builder()
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
        String lnsInstanceName = "TEST-LNS-1";
        SampleLnsInstance testLnsInstance = SampleLnsInstance.builder()
                .name(lnsInstanceName)
                .description("Test LNS 1")
                .user("user-1")
                .password("password!!!")
                .build();

        when(lnsInstanceService.getByName(eq(lnsInstanceName))).thenReturn(testLnsInstance);

        this.mockMvc.perform(get("/lns-instance/" + lnsInstanceName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonObjectMapper.writerWithView(LnsInstance.PublicView.class).writeValueAsString(testLnsInstance), true));

        verify(lnsInstanceService).getByName(eq(lnsInstanceName));
    }

    @Test
    public void doCall_create_and_ensure_only_publicView_is_returned_to_client() throws Exception {
        SampleLnsInstance testLnsInstance = SampleLnsInstance.builder()
                                                            .name("TEST-LNS-1")
                                                            .description("Test LNS 1")
                                                            .user("user-1")
                                                            .password("password!!!")
                                                            .build();

        when(lnsInstanceService.create(any())).thenReturn(testLnsInstance);

        this.mockMvc.perform(post("/lns-instance")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonObjectMapper.writerWithView(LnsInstance.InternalView.class).writeValueAsString(testLnsInstance)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonObjectMapper.writerWithView(LnsInstance.PublicView.class).writeValueAsString(testLnsInstance), true));

        verify(lnsInstanceService).create(lnsInstanceArgumentCaptor.capture());
        LnsInstance lnsInstanceCapturedArgument = lnsInstanceArgumentCaptor.getValue();
        compare(testLnsInstance, lnsInstanceCapturedArgument);
    }

    @Test
    public void doCall_update_and_ensure_only_publicView_is_returned_to_client() throws Exception {
        SampleLnsInstance testLnsInstance = SampleLnsInstance.builder()
                .name("TEST-LNS-1")
                .description("Test LNS 1")
                .user("user-1")
                .password("password!!!")
                .build();

        String existingLnsInstanceName = "EXISTING-TEST-LNS-1";
        when(lnsInstanceService.update(eq(existingLnsInstanceName), any())).thenReturn(testLnsInstance);

        this.mockMvc.perform(put("/lns-instance/" + existingLnsInstanceName)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonObjectMapper.writerWithView(LnsInstance.InternalView.class).writeValueAsString(testLnsInstance)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(jsonObjectMapper.writerWithView(LnsInstance.PublicView.class).writeValueAsString(testLnsInstance), true));

        verify(lnsInstanceService).update(eq(existingLnsInstanceName), lnsInstanceArgumentCaptor.capture());
        LnsInstance lnsInstanceCapturedArgument = lnsInstanceArgumentCaptor.getValue();
        compare(testLnsInstance, lnsInstanceCapturedArgument);
    }

    @Test
    public void doCall_delete() throws Exception {
        String lnsInstanceNameToDelete = "DELETE-TEST-LNS-1";

        this.mockMvc.perform(delete("/lns-instance/" + lnsInstanceNameToDelete))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(lnsInstanceService).delete(eq(lnsInstanceNameToDelete));
    }

    private void compare(SampleLnsInstance expected, LnsInstance actual) {
        Assert.assertTrue(expected.getClass().isAssignableFrom(actual.getClass()));

        SampleLnsInstance actualTestLnsInstance = (SampleLnsInstance) actual;

        Assert.assertEquals(expected.getName(), actualTestLnsInstance.getName());
        Assert.assertEquals(expected.getDescription(), actualTestLnsInstance.getDescription());
        Assert.assertEquals(expected.getUser(), actualTestLnsInstance.getUser());
        Assert.assertEquals(expected.getPassword(), actualTestLnsInstance.getPassword());
    }
}