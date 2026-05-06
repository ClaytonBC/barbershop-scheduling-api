package com.claytonbc.barbershop.controller;
import com.claytonbc.barbershop.dto.CustomerResponse;
import com.claytonbc.barbershop.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @GetMapping("/me")
    public ResponseEntity<CustomerResponse> me(Authentication auth) {
        return ResponseEntity.ok(service.findByEmail(auth.getName()));
    }
}