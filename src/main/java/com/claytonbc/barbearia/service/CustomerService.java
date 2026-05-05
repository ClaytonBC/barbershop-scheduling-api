package com.claytonbc.barbearia.service;
import com.claytonbc.barbearia.dto.CreateCustomerRequest;
import com.claytonbc.barbearia.dto.CustomerResponse;
import com.claytonbc.barbearia.entity.Customer;
import com.claytonbc.barbearia.enums.Perfil;
import com.claytonbc.barbearia.exception.EmailAlreadyExistsException;
import com.claytonbc.barbearia.exception.NotFoundException;
import com.claytonbc.barbearia.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerResponse create(CreateCustomerRequest request) {

        if (customerRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        Customer customer = new Customer();

        customer.setName(request.name());
        customer.setEmail(request.email());

        customer.setPassword(passwordEncoder.encode(request.password()));

        customer.setRole(Perfil.CLIENT);
        customer.setActive(true);

        Customer saved = customerRepository.save(customer);

        return toResponse(saved);
    }

    public List<CustomerResponse> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CustomerResponse findById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        return toResponse(customer);
    }

    public void delete(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        customerRepository.delete(customer);
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getRole()
        );
    }
}