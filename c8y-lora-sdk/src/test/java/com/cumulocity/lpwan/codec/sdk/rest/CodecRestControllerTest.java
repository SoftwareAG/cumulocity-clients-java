package com.cumulocity.lpwan.codec.sdk.rest;

import com.cumulocity.lpwan.codec.sdk.model.DecodeRequest;
import com.cumulocity.lpwan.codec.sdk.service.CodecService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodecRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@EnableWebMvc
public class CodecRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CodecService codecService;

    @Test
    public void shouldTestDecodeApiWithOk() throws Exception {
        this.mockMvc.perform(post("/decode")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(getJsonData())).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void shouldTestDecodeApiWithBadRequest() throws Exception {
        this.mockMvc.perform(post("/decode")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content("")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldTestDecodeApiWithUnsupportedMediaType() throws Exception {
        this.mockMvc.perform(post("/decode")
                            .content(getJsonData())).andDo(print()).andExpect(status().isUnsupportedMediaType());
    }

    private String getJsonData() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(new DecodeRequest("23124567","12345"));
    }
}