package com.claytonbc.barbershop.service;


import com.claytonbc.barbershop.dto.CreateAppointmentRequest;
import com.claytonbc.barbershop.entity.Appointment;
import com.claytonbc.barbershop.entity.Customer;
import com.claytonbc.barbershop.enums.Perfil;
import com.claytonbc.barbershop.enums.Status;
import com.claytonbc.barbershop.exception.BusinessException;
import com.claytonbc.barbershop.exception.NotFoundException;
import com.claytonbc.barbershop.repository.AppointmentRepository;
import com.claytonbc.barbershop.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AppointmentService service;

    private Customer client;
    private Customer barber;
    private CreateAppointmentRequest request;

    @BeforeEach
    void setup() {
        client = new Customer();
        client.setId(1L);
        client.setEmail("client@email.com");
        client.setRole(Perfil.CLIENT);

        barber = new Customer();
        barber.setId(2L);
        barber.setEmail("barber@email.com");
        barber.setRole(Perfil.BARBER);

        request = new CreateAppointmentRequest(
                2L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2)
        );
    }

    @Test
    void shouldCreateAppointment() {
        when(customerRepository.findByEmail(any())).thenReturn(Optional.of(client));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(barber));
        when(appointmentRepository.existsByBarberIdAndStatusNotAndStartTimeLessThanAndEndTimeGreaterThan(any(), any(), any(), any()))
                .thenReturn(false);
        when(appointmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        var response = service.create(request, client.getEmail());

        assertNotNull(response);
        verify(appointmentRepository).save(any());
    }

    @Test
    void shouldThrowWhenBarberNotFound() {
        when(customerRepository.findByEmail(any())).thenReturn(Optional.of(client));
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.create(request, client.getEmail()));
    }

    @Test
    void shouldThrowWhenNotBarber() {
        barber.setRole(Perfil.CLIENT);

        when(customerRepository.findByEmail(any())).thenReturn(Optional.of(client));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(barber));

        assertThrows(BusinessException.class,
                () -> service.create(request, client.getEmail()));
    }

    @Test
    void shouldThrowWhenInvalidTime() {
        request = new CreateAppointmentRequest(
                2L,
                LocalDateTime.now(),
                LocalDateTime.now().minusHours(1)
        );

        when(customerRepository.findByEmail(any())).thenReturn(Optional.of(client));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(barber));

        assertThrows(BusinessException.class,
                () -> service.create(request, client.getEmail()));
    }

    @Test
    void shouldThrowWhenConflict() {
        when(customerRepository.findByEmail(any())).thenReturn(Optional.of(client));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(barber));
        when(appointmentRepository.existsByBarberIdAndStatusNotAndStartTimeLessThanAndEndTimeGreaterThan(any(), any(), any(), any()))
                .thenReturn(true);

        assertThrows(BusinessException.class,
                () -> service.create(request, client.getEmail()));
    }

    @Test
    void shouldReturnAllForOwner() {
        client.setRole(Perfil.OWNER);

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setBarber(barber);

        when(customerRepository.findByEmail(any())).thenReturn(Optional.of(client));
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));

        var result = service.findAll(client.getEmail());

        assertEquals(1, result.size());
    }

    @Test
    void shouldDeleteOwnAppointment() {
        Appointment appointment = new Appointment();
        appointment.setClient(client);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        service.delete(1L, client.getEmail());

        verify(appointmentRepository).delete(appointment);
    }

    @Test
    void shouldThrowWhenDeletingOthersAppointment() {
        Appointment appointment = new Appointment();
        Customer other = new Customer();
        other.setEmail("other@email.com");
        appointment.setClient(other);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        assertThrows(BusinessException.class,
                () -> service.delete(1L, client.getEmail()));
    }

    @Test
    void shouldUpdateStatus() {
        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setBarber(barber);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);

        var response = service.updateStatus(1L, Status.CANCELLED);

        assertNotNull(response);
        verify(appointmentRepository).save(appointment);
    }
}
