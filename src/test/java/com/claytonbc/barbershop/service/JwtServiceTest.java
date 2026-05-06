package com.claytonbc.barbershop.service;

import com.claytonbc.barbershop.entity.Customer;
import com.claytonbc.barbershop.enums.Perfil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;
    private Customer customer;

    @BeforeEach
    void setup() {
        jwtService = new JwtService();

        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("test@email.com");
        customer.setRole(Perfil.CLIENT);
    }

    @Test
    void shouldGenerateToken() {
        String token = jwtService.generateToken(customer);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldExtractUsername() {
        String token = jwtService.generateToken(customer);

        String email = jwtService.extractUsername(token);

        assertEquals("test@email.com", email);
    }

    @Test
    void shouldValidateToken() {
        String token = jwtService.generateToken(customer);

        boolean isValid = jwtService.isTokenValid(token, customer);

        assertTrue(isValid);
    }

    @Test
    void shouldReturnFalseWhenTokenInvalid() {
        String token = jwtService.generateToken(customer);

        Customer other = new Customer();
        other.setEmail("other@email.com");

        boolean isValid = jwtService.isTokenValid(token, other);

        assertFalse(isValid);
    }
}
