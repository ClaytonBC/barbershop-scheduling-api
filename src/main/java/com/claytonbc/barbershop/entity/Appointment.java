package com.claytonbc.barbershop.entity;

import com.claytonbc.barbershop.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    private Customer client;

    @ManyToOne
    private Customer barber;

    @Enumerated(EnumType.STRING)
    private Status status;
}
