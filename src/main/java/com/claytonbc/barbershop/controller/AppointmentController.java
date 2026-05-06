package com.claytonbc.barbershop.controller;

import com.claytonbc.barbershop.dto.AppointmentResponse;
import com.claytonbc.barbershop.dto.CreateAppointmentRequest;
import com.claytonbc.barbershop.dto.UpdateStatusRequest;
import com.claytonbc.barbershop.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(
            @RequestBody CreateAppointmentRequest request,
            Authentication auth
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request, auth.getName()));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> findAll(Authentication auth) {
        return ResponseEntity.ok(service.findAll(auth.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        service.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> findById(
            @PathVariable Long id,
            Authentication auth
    ) {
        return ResponseEntity.ok(service.findById(id, auth.getName()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest request
    ) {
        return ResponseEntity.ok(service.updateStatus(id, request.status()));
    }
}
