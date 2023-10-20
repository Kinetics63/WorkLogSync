package com.entimo.worklogsync;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MvcRequestTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, World")));
    }

    @Test
    void shouldRetrieveUserProjects() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/userProjects").param("persKurz", "rw")).andDo(print()).andExpect(status().isOk()).andReturn();
        Assertions.assertFalse(mvcResult.getResponse().getContentAsString().isEmpty());
    }

    @Test
    void shouldStartTheTimer() throws Exception {
        this.mockMvc.perform(put("/startTimer")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void shouldStopTheTimer() throws Exception {
        this.mockMvc.perform(put("/stopTimer")).andDo(print()).andExpect(status().isOk());
    }

}
