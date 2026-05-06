package com.claytonbc.barbershop.service;

import com.claytonbc.barbershop.dto.AuthResponse;
import com.claytonbc.barbershop.dto.LoginRequest;
import com.claytonbc.barbershop.entity.Customer;
import com.claytonbc.barbershop.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {

        Customer customer = customerRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), customer.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(customer);

        return new AuthResponse(token);
    }
}