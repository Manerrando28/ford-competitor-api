package com.ford.competitor.controller;

import com.ford.competitor.dto.ApiError;
import com.ford.competitor.dto.VehicleSpecificationRequestDto;
import com.ford.competitor.dto.VehicleSpecificationResponseDto;
import com.ford.competitor.service.CompetitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/vehicle-specifications")
public class VehicleSpecificationController {

    private final CompetitorService competitorService;

    public VehicleSpecificationController(CompetitorService competitorService) {
        this.competitorService = competitorService;
    }

    @Operation(summary = "Cadastra uma especificação de veículo (somente ADMIN)",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Especificação criada",
                    content = @Content(schema = @Schema(implementation = VehicleSpecificationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Corpo inválido",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "JWT ausente, inválido ou expirado",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "403", description = "Usuário sem perfil ADMIN",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Especificação já cadastrada",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VehicleSpecificationResponseDto> create(@Valid @RequestBody VehicleSpecificationRequestDto request) {
        VehicleSpecificationResponseDto response = competitorService.createSpecification(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
}
