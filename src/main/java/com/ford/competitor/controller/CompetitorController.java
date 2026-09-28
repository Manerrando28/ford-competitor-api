package com.ford.competitor.controller;

import com.ford.competitor.dto.CompetitorRequestDto;
import com.ford.competitor.dto.CompetitorResponseDto;
import com.ford.competitor.dto.ApiError;
import com.ford.competitor.service.CompetitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/competitors")
@Validated
public class CompetitorController {

    private final CompetitorService competitorService;

    public CompetitorController(CompetitorService competitorService) {
        this.competitorService = competitorService;
    }

    @Operation(summary = "Consulta especificações de um veículo", deprecated = true,
            description = "Rota legada. Prefira GET /api/v1/competitors/{brand}/{model}/{version}/specifications.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "JWT ausente, inválido ou expirado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping("/query")
    public ResponseEntity<CompetitorResponseDto> querySpecifications(@Valid @RequestBody CompetitorRequestDto request) {
        CompetitorResponseDto response = competitorService.processQuery(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consulta especificações de um veículo por recurso",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta realizada"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "401", description = "JWT ausente, inválido ou expirado",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{brand}/{model}/{version}/specifications")
    public ResponseEntity<CompetitorResponseDto> getSpecifications(
            @PathVariable @NotBlank(message = "A marca é obrigatória") String brand,
            @PathVariable @NotBlank(message = "O modelo é obrigatório") String model,
            @PathVariable @NotBlank(message = "A versão é obrigatória") String version,
            @RequestParam @NotEmpty(message = "Informe ao menos um atributo")
            List<@NotBlank(message = "Cada atributo deve ser informado") String> attributes) {
        return ResponseEntity.ok(competitorService.processQuery(
                new CompetitorRequestDto(brand, model, version, attributes)));
    }
}
