package com.claytonbc.barbershop.controller;

import com.claytonbc.barbershop.entity.Customer;
import com.claytonbc.barbershop.enums.Perfil;
import com.claytonbc.barbershop.repository.AppointmentRepository;
import com.claytonbc.barbershop.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppointmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void clean() {
        appointmentRepository.deleteAll();
        customerRepository.deleteAll();
    }

    private String getToken() throws Exception {
        String registerJson = """
        {
            "name": "Client",
            "email": "client@email.com",
            "password": "123456"
        }
        """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isCreated());

        String loginJson = """
        {
            "email": "client@email.com",
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

    private Long createBarber() {
        Customer barber = new Customer();
        barber.setName("Barber");
        barber.setEmail("barber@email.com");
        barber.setPassword("123"); // não precisa encode no teste direto
        barber.setRole(Perfil.BARBER);
        barber.setActive(true);

        return customerRepository.save(barber).getId();
    }

    @Test
    void shouldCreateAppointment() throws Exception {

        String token = getToken();
        Long barberId = createBarber();

        String json = """
        {
            "barberId": %d,
            "startTime": "2025-01-01T10:00:00",
            "endTime": "2025-01-01T11:00:00"
        }
        """.formatted(barberId);

        mockMvc.perform(post("/appointments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldListAppointments() throws Exception {

        String token = getToken();
        Long barberId = createBarber();

        String json = """
    {
        "barberId": %d,
        "startTime": "2025-01-01T10:00:00",
        "endTime": "2025-01-01T11:00:00"
    }
    """.formatted(barberId);

        mockMvc.perform(post("/appointments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated()); // 🔥 ESSENCIAL

        mockMvc.perform(get("/appointments")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldDeleteAppointment() throws Exception {

        String token = getToken();
        Long barberId = createBarber();

        String json = """
    {
        "barberId": %d,
        "startTime": "2025-01-01T10:00:00",
        "endTime": "2025-01-01T11:00:00"
    }
    """.formatted(barberId);

        String response = mockMvc.perform(post("/appointments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated()) // 🔥 ESSENCIAL
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        Long id = mapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/appointments/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldFailWithoutToken() throws Exception {
        mockMvc.perform(get("/appointments"))
                .andExpect(status().isForbidden());
    }
}