package com.claytonbc.barbershop.controller;

import com.claytonbc.barbershop.dto.AppointmentResponse;
import com.claytonbc.barbershop.dto.CreateAppointmentRequest;
import com.claytonbc.barbershop.dto.UpdateStatusRequest;
import com.claytonbc.barbershop.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Appointments",
        description = "Gerenciamento de agendamentos"
)
@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @Operation(
            summary = "Criar agendamento",
            description = "Cria um novo agendamento para o usuário autenticado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @PostMapping
    public ResponseEntity<AppointmentResponse> create(
            @Valid @RequestBody CreateAppointmentRequest request,
            Authentication auth
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request, auth.getName()));
    }

    @Operation(
            summary = "Listar agendamentos",
            description = "Retorna todos os agendamentos do usuário autenticado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamentos encontrados"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> findAll(Authentication auth) {
        return ResponseEntity.ok(service.findAll(auth.getName()));
    }

    @Operation(
            summary = "Buscar agendamento por ID",
            description = "Retorna um agendamento específico pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agendamento encontrado"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> findById(
            @PathVariable Long id,
            Authentication auth
    ) {
        return ResponseEntity.ok(service.findById(id, auth.getName()));
    }

    @Operation(
            summary = "Atualizar status do agendamento",
            description = "Atualiza o status de um agendamento"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Status inválido"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request
    ) {
        return ResponseEntity.ok(service.updateStatus(id, request.status()));
    }

    @Operation(
            summary = "Excluir agendamento",
            description = "Remove um agendamento pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Agendamento removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        service.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
