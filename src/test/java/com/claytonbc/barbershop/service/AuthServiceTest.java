package com.claytonbc.barbershop.service;

import com.claytonbc.barbershop.dto.CreateCustomerRequest;
import com.claytonbc.barbershop.dto.LoginRequest;
import com.claytonbc.barbershop.entity.Customer;
import com.claytonbc.barbershop.enums.Perfil;
import com.claytonbc.barbershop.exception.EmailAlreadyExistsException;
import com.claytonbc.barbershop.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService service;

    private CreateCustomerRequest registerRequest;
    private LoginRequest loginRequest;
    private Customer customer;

    @BeforeEach
    void setup() {
        registerRequest = new CreateCustomerRequest(
                "Clayton",
                "test@email.com",
                "123456"
        );

        loginRequest = new LoginRequest(
                "test@email.com",
                "123456"
        );

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Clayton");
        customer.setEmail("test@email.com");
        customer.setPassword("encoded");
        customer.setRole(Perfil.CLIENT);
        customer.setActive(true);
    }

    @Test
    void shouldRegister() {
        when(customerRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode(any()))
                .thenReturn("encoded");

        when(jwtService.generateToken(any()))
                .thenReturn("token");

        var response = service.register(registerRequest);

        assertNotNull(response);
        assertEquals("token", response.token());

        verify(customerRepository).save(any());
        verify(jwtService).generateToken(any());
    }

    @Test
    void shouldThrowWhenEmailExists() {
        when(customerRepository.findByEmail(any()))
                .thenReturn(Optional.of(customer));

        assertThrows(EmailAlreadyExistsException.class,
                () -> service.register(registerRequest));

        verify(customerRepository, never()).save(any());
    }

    @Test
    void shouldLogin() {
        when(customerRepository.findByEmail(any()))
                .thenReturn(Optional.of(customer));

        when(passwordEncoder.matches(any(), any()))
                .thenReturn(true);

        when(jwtService.generateToken(any()))
                .thenReturn("token");

        var response = service.login(loginRequest);

        assertNotNull(response);
        assertEquals("token", response.token());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(customerRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.login(loginRequest));
    }

    @Test
    void shouldThrowWhenPasswordInvalid() {
        when(customerRepository.findByEmail(any()))
                .thenReturn(Optional.of(customer));

        when(passwordEncoder.matches(any(), any()))
                .thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> service.login(loginRequest));
    }
}
