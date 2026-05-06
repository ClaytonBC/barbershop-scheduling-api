package com.claytonbc.barbershop.controller;
import com.claytonbc.barbershop.dto.CustomerResponse;
import com.claytonbc.barbershop.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@Tag(
        name = "Customers",
        description = "Gerenciamento de clientes"
)
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @Operation(
            summary = "Obter dados do usuário autenticado",
            description = "Retorna os dados do cliente autenticado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/me")
    public ResponseEntity<CustomerResponse> me(Authentication auth) {
        return ResponseEntity.ok(service.findByEmail(auth.getName()));
    }
}