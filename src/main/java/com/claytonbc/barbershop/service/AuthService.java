package com.claytonbc.barbershop.service;

import com.claytonbc.barbershop.dto.AuthResponse;
import com.claytonbc.barbershop.dto.CreateCustomerRequest;
import com.claytonbc.barbershop.dto.LoginRequest;
import com.claytonbc.barbershop.entity.Customer;
import com.claytonbc.barbershop.enums.Perfil;
import com.claytonbc.barbershop.exception.EmailAlreadyExistsException;
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

    public AuthResponse register(CreateCustomerRequest request) {

        if (customerRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        Customer customer = new Customer();

        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPassword(passwordEncoder.encode(request.password()));

        customer.setRole(Perfil.CLIENT);
        customer.setActive(true);

        customerRepository.save(customer);

        String token = jwtService.generateToken(customer);

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {

        Customer customer = customerRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), customer.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(customer);

        return new AuthResponse(token);
    }
}