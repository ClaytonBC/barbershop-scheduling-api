package com.claytonbc.barbershop.controller;

import com.claytonbc.barbershop.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void clean() {
        customerRepository.deleteAll();
    }

    @Test
    void shouldFailWithoutToken() throws Exception {
        mockMvc.perform(get("/customers/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnAuthenticatedUser() throws Exception {

        String registerJson = """
        {
            "name": "Clayton",
            "email": "test@email.com",
            "password": "123456"
        }
        """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isCreated());

        String loginJson = """
        {
            "email": "test@email.com",
            "password": "123456"
        }
        """;

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = response.split(":")[1].replace("\"", "").replace("}", "");

        mockMvc.perform(get("/customers/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@email.com"));
    }
}