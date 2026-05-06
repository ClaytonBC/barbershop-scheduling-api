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

    public AppointmentResponse create(CreateAppointmentRequest request, String email) {

        Customer client = customerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Customer barber = customerRepository.findById(request.barberId())
                .orElseThrow(() -> new NotFoundException("Barber not found"));

        if (barber.getRole() != Perfil.BARBER) {
            throw new BusinessException("Selected user is not a barber");
        }

        if (request.startTime() == null || request.endTime() == null) {
            throw new BusinessException("Start and end time are required");
        }

        if (!request.startTime().isBefore(request.endTime())) {
            throw new BusinessException("Invalid time range");
        }

        boolean hasConflict = appointmentRepository
                .existsByBarberIdAndStatusNotAndStartTimeLessThanAndEndTimeGreaterThan(
                        barber.getId(),
                        Status.CANCELLED,
                        request.endTime(),
                        request.startTime()
                );

        if (hasConflict) {
            throw new BusinessException("Time slot not available");
        }

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setBarber(barber);
        appointment.setStartTime(request.startTime());
        appointment.setEndTime(request.endTime());
        appointment.setStatus(Status.SCHEDULED);

        return toResponse(appointmentRepository.save(appointment));
    }

    public List<AppointmentResponse> findAll(String email) {

        Customer user = customerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getRole() == Perfil.OWNER) {
            return appointmentRepository.findAll()
                    .stream()
                    .map(this::toResponse)
                    .toList();
        }

        return appointmentRepository.findByClientId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(Long id, String email) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        if (!appointment.getClient().getEmail().equals(email)) {
            throw new BusinessException("You can only cancel your own appointment");
        }

        appointmentRepository.delete(appointment);
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

    public AppointmentResponse findById(Long id, String email) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        Customer user = customerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getRole() == Perfil.OWNER) {
            return toResponse(appointment);
        }

        if (appointment.getClient().getEmail().equals(email)) {
            return toResponse(appointment);
        }

        throw new BusinessException("Access denied");
    }

    public AppointmentResponse updateStatus(Long id, Status status) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        appointment.setStatus(status);

        return toResponse(appointmentRepository.save(appointment));
    }

}
