package com.claytonbc.barbershop.controller;

import com.claytonbc.barbershop.entity.BarbershopService;
import com.claytonbc.barbershop.entity.Customer;
import com.claytonbc.barbershop.enums.Perfil;
import com.claytonbc.barbershop.repository.BarbershopServiceRepository;
import com.claytonbc.barbershop.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BarbershopServiceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BarbershopServiceRepository serviceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void clean() {
        serviceRepository.deleteAll();
        customerRepository.deleteAll();
    }

    private String getOwnerToken() throws Exception {

        Customer owner = new Customer();
        owner.setName("Owner");
        owner.setEmail("owner@email.com");
        owner.setPassword(passwordEncoder.encode("123456"));
        owner.setRole(Perfil.OWNER);
        owner.setActive(true);

        customerRepository.save(owner);

        String loginJson = """
        {
            "email": "owner@email.com",
            "password": "123456"
        }
        """;

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response).get("token").asText();
    }

    @Test
    void shouldCreateService() throws Exception {

        String token = getOwnerToken();

        String json = """
        {
            "name": "Corte",
            "price": 30.0,
            "duration": 30
        }
        """;

        mockMvc.perform(post("/services")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Corte"));
    }

    @Test
    void shouldListServices() throws Exception {

        mockMvc.perform(get("/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldUpdateService() throws Exception {

        String token = getOwnerToken();

        BarbershopService service = new BarbershopService();
        service.setName("Corte");
        service.setPrice(BigDecimal.valueOf(30));
        service.setDuration(30);

        Long id = serviceRepository.save(service).getId();

        String json = """
        {
            "name": "Corte Premium",
            "price": 50.0,
            "duration": 40
        }
        """;

        mockMvc.perform(put("/services/" + id)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Corte Premium"));
    }

    @Test
    void shouldDeleteService() throws Exception {

        String token = getOwnerToken();

        BarbershopService service = new BarbershopService();
        service.setName("Corte");
        service.setPrice(BigDecimal.valueOf(30));
        service.setDuration(30);

        Long id = serviceRepository.save(service).getId();

        mockMvc.perform(delete("/services/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldFailWithoutOwnerRole() throws Exception {

        String json = """
        {
            "name": "Corte",
            "price": 30.0,
            "duration": 30
        }
        """;

        mockMvc.perform(post("/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }
}
