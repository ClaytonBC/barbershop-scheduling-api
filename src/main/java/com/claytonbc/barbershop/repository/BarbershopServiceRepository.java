package com.claytonbc.barbershop.repository;

import com.claytonbc.barbershop.entity.BarbershopService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarbershopServiceRepository extends JpaRepository<BarbershopService, Long> {
}
