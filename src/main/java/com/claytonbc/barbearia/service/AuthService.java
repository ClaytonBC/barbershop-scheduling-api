package com.claytonbc.barbearia.service;

import com.claytonbc.barbearia.dto.AuthResponse;
import com.claytonbc.barbearia.dto.LoginRequest;
import com.claytonbc.barbearia.entity.Customer;
import com.claytonbc.barbearia.repository.CustomerRepository;
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