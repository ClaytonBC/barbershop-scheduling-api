package com.claytonbc.barbershop.service;

import com.claytonbc.barbershop.dto.CreateCustomerRequest;
import com.claytonbc.barbershop.entity.Customer;
import com.claytonbc.barbershop.enums.Perfil;
import com.claytonbc.barbershop.exception.EmailAlreadyExistsException;
import com.claytonbc.barbershop.exception.NotFoundException;
import com.claytonbc.barbershop.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerService service;

    private CreateCustomerRequest request;
    private Customer customer;

    @BeforeEach
    void setup() {
        request = new CreateCustomerRequest(
                "Clayton",
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
    void shouldCreateCustomer() {
        when(customerRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(customerRepository.save(any())).thenReturn(customer);

        var response = service.create(request);

        assertNotNull(response);
        assertEquals("Clayton", response.name());
        verify(passwordEncoder).encode("123456");
        verify(customerRepository).save(any());
    }

    @Test
    void shouldThrowWhenEmailExists() {
        when(customerRepository.findByEmail(any()))
                .thenReturn(Optional.of(customer));

        assertThrows(EmailAlreadyExistsException.class,
                () -> service.create(request));

        verify(customerRepository, never()).save(any());
    }

    @Test
    void shouldReturnAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        var result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("Clayton", result.get(0).name());
    }

    @Test
    void shouldFindById() {
        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        var response = service.findById(1L);

        assertNotNull(response);
        assertEquals("Clayton", response.name());
    }

    @Test
    void shouldThrowWhenCustomerNotFound() {
        when(customerRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.findById(1L));
    }

    @Test
    void shouldDeleteCustomer() {
        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        service.delete(1L);

        verify(customerRepository).delete(customer);
    }

    @Test
    void shouldThrowWhenDeleteNotFound() {
        when(customerRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.delete(1L));
    }

    @Test
    void shouldFindByEmail() {
        when(customerRepository.findByEmail(any()))
                .thenReturn(Optional.of(customer));

        var response = service.findByEmail("test@email.com");

        assertNotNull(response);
        assertEquals("Clayton", response.name());
    }

    @Test
    void shouldThrowWhenEmailNotFound() {
        when(customerRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.findByEmail("test@email.com"));
    }
}
