package org.qazcodenarxoz.mainmicroservice1.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.qazcodenarxoz.mainmicroservice1.entity.MC1Entity;
import org.qazcodenarxoz.mainmicroservice1.service.MC1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MC1Controller.class)
@AutoConfigureMockMvc(addFilters = false)
class MC1ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MC1Service mc1Service;

    @Test
    void start_shouldReturnStarted() throws Exception {
        when(mc1Service.start()).thenReturn("Started");

        mockMvc.perform(get("/message/start"))
                .andExpect(status().isOk())
                .andExpect(content().string("Started"));
    }

    @Test
    void stop_shouldReturnStopped() throws Exception {
        when(mc1Service.stop()).thenReturn("Stopped");

        mockMvc.perform(get("/message/stop"))
                .andExpect(status().isOk())
                .andExpect(content().string("Stopped"));
    }

    @Test
    void receive_shouldAcceptMessage() throws Exception {
        mockMvc.perform(post("/message/receive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "sessionId": 1 }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string("Message received"));

        verify(mc1Service).cycle(any(MC1Entity.class));
    }
}

