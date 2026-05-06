package com.claytonbc.barbershop.service;

import com.claytonbc.barbershop.dto.CreateServiceRequest;
import com.claytonbc.barbershop.dto.ServiceResponse;
import com.claytonbc.barbershop.entity.BarbershopService;
import com.claytonbc.barbershop.exception.NotFoundException;
import com.claytonbc.barbershop.repository.BarbershopServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BarbershopServiceService {

    private final BarbershopServiceRepository repository;

    public ServiceResponse create(CreateServiceRequest request) {
        BarbershopService service = new BarbershopService();

        service.setName(request.name());
        service.setPrice(request.price());
        service.setDuration(request.duration());

        return toResponse(repository.save(service));
    }

    public List<ServiceResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ServiceResponse update(Long id, CreateServiceRequest request) {
        BarbershopService service = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service not found"));

        service.setName(request.name());
        service.setPrice(request.price());
        service.setDuration(request.duration());

        return toResponse(repository.save(service));
    }

    public void delete(Long id) {
        BarbershopService service = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service not found"));

        repository.delete(service);
    }

    private ServiceResponse toResponse(BarbershopService service) {
        return new ServiceResponse(
                service.getId(),
                service.getName(),
                service.getPrice(),
                service.getDuration()
        );
    }
}
