package com.claytonbc.barbershop.service;

import com.claytonbc.barbershop.dto.CreateServiceRequest;
import com.claytonbc.barbershop.dto.ServiceResponse;
import com.claytonbc.barbershop.entity.BarbershopService;
import com.claytonbc.barbershop.exception.NotFoundException;
import com.claytonbc.barbershop.repository.BarbershopServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BarbershopServiceServiceTest {

    @Mock
    private BarbershopServiceRepository repository;

    @InjectMocks
    private BarbershopServiceService service;

    private CreateServiceRequest request;
    private BarbershopService entity;

    @BeforeEach
    void setup() {
        request = new CreateServiceRequest(
                "Corte",
                new BigDecimal("30.00"),
                30
        );

        entity = new BarbershopService();
        entity.setId(1L);
        entity.setName("Corte");
        entity.setPrice(new BigDecimal("30.00"));
        entity.setDuration(30);
    }

    @Test
    void shouldCreateService() {
        when(repository.save(any())).thenReturn(entity);

        ServiceResponse response = service.create(request);

        assertNotNull(response);
        assertEquals("Corte", response.name());
        assertTrue(response.price().compareTo(new BigDecimal("30.00")) == 0);

        verify(repository).save(any());
    }


    @Test
    void shouldReturnAllServices() {
        when(repository.findAll()).thenReturn(List.of(entity));

        List<ServiceResponse> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("Corte", result.get(0).name());

        verify(repository).findAll();
    }


    @Test
    void shouldUpdateService() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);

        ServiceResponse response = service.update(1L, request);

        assertEquals("Corte", response.name());
        verify(repository).save(entity);
    }


    @Test
    void shouldThrowWhenUpdateNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.update(1L, request));

        verify(repository, never()).save(any());
    }

    @Test
    void shouldDeleteService() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);

        verify(repository).delete(entity);
    }

    @Test
    void shouldThrowWhenDeleteNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.delete(1L));

        verify(repository, never()).delete(any());
    }
}