package com.claytonbc.barbershop.controller;

import com.claytonbc.barbershop.dto.CreateServiceRequest;
import com.claytonbc.barbershop.dto.ServiceResponse;
import com.claytonbc.barbershop.service.BarbershopServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Services",
        description = "Gerenciamento de serviços da barbearia"
)
@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class BarbershopServiceController {

    private final BarbershopServiceService service;

    @Operation(
            summary = "Criar serviço",
            description = "Cria um novo serviço na barbearia"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Serviço criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<ServiceResponse> create(
            @Valid @RequestBody CreateServiceRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    @Operation(
            summary = "Listar serviços",
            description = "Retorna todos os serviços cadastrados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Serviços encontrados")
    })
    @GetMapping
    public ResponseEntity<List<ServiceResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Atualizar serviço",
            description = "Atualiza os dados de um serviço pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Serviço atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateServiceRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(
            summary = "Excluir serviço",
            description = "Remove um serviço pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Serviço removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
