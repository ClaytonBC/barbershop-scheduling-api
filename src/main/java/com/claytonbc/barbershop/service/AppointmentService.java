package com.claytonbc.barbershop.service;

import com.claytonbc.barbershop.dto.AppointmentResponse;
import com.claytonbc.barbershop.dto.CreateAppointmentRequest;
import com.claytonbc.barbershop.entity.Appointment;
import com.claytonbc.barbershop.entity.Customer;
import com.claytonbc.barbershop.enums.Perfil;
import com.claytonbc.barbershop.enums.Status;
import com.claytonbc.barbershop.exception.BusinessException;
import com.claytonbc.barbershop.exception.NotFoundException;
import com.claytonbc.barbershop.repository.AppointmentRepository;
import com.claytonbc.barbershop.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;

    public AppointmentResponse create(CreateAppointmentRequest request) {

        Customer client = customerRepository.findById(request.clientId())
                .orElseThrow(() -> new NotFoundException("Client not found"));

        Customer barber = customerRepository.findById(request.barberId())
                .orElseThrow(() -> new NotFoundException("Barber not found"));

        if (barber.getRole() != Perfil.BARBER) {
            throw new BusinessException("Selected user is not a barber");
        }

        if (!request.startTime().isBefore(request.endTime())) {
            throw new BusinessException("Invalid time range");
        }

        List<Appointment> conflicts = appointmentRepository.findConflicts(
                barber.getId(),
                request.startTime(),
                request.endTime()
        );

        if (!conflicts.isEmpty()) {
            throw new BusinessException("Time slot already booked");
        }

        Appointment appointment = new Appointment();
        appointment.setStartTime(request.startTime());
        appointment.setEndTime(request.endTime());
        appointment.setClient(client);
        appointment.setBarber(barber);
        appointment.setStatus(Status.PENDING);

        Appointment saved = appointmentRepository.save(appointment);

        return toResponse(saved);
    }

    public List<AppointmentResponse> findAll(String email) {

        Customer user = customerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Appointment> appointments;

        if (user.getRole() == Perfil.CLIENT) {
            appointments = appointmentRepository.findByClientId(user.getId());
        } else if (user.getRole() == Perfil.BARBER) {
            appointments = appointmentRepository.findByBarberId(user.getId());
        } else {
            appointments = appointmentRepository.findAll();
        }

        return appointments.stream().map(this::toResponse).toList();
    }

    public void delete(Long id, String email) {

        Customer user = customerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Customer target = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        boolean isOwner = user.getRole() == Perfil.OWNER;
        boolean isSelf = user.getId().equals(id);

        if (!isOwner && !isSelf) {
            throw new BusinessException("Access denied");
        }

        customerRepository.delete(target);
    }

    private AppointmentResponse toResponse(Appointment a) {
        return new AppointmentResponse(
                a.getId(),
                a.getStartTime(),
                a.getEndTime(),
                a.getClient().getId(),
                a.getBarber().getId(),
                a.getStatus()
        );
    }
}
