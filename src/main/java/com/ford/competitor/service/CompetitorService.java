package com.ford.competitor.service;

import com.ford.competitor.dto.CompetitorRequestDto;
import com.ford.competitor.dto.CompetitorResponseDto;
import com.ford.competitor.dto.SpecificationItemDto;
import com.ford.competitor.dto.VehicleSpecificationRequestDto;
import com.ford.competitor.dto.VehicleSpecificationResponseDto;
import com.ford.competitor.exception.ResourceAlreadyExistsException;
import com.ford.competitor.model.VehicleSpecification;
import com.ford.competitor.repository.VehicleSpecificationRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompetitorService {

    private final VehicleSpecificationRepository specificationRepository;

    public CompetitorService(VehicleSpecificationRepository specificationRepository) {
        this.specificationRepository = specificationRepository;
    }

    public CompetitorResponseDto processQuery(CompetitorRequestDto request) {
        List<SpecificationItemDto> items = new ArrayList<>();

        for (String requestedAttribute : request.getAttributes()) {
            Optional<VehicleSpecification> specOpt = specificationRepository
                    .findByBrandIgnoreCaseAndModelIgnoreCaseAndVersionIgnoreCaseAndAttributeIgnoreCase(
                            request.getBrand(),
                            request.getModel(),
                            request.getVersion(),
                            requestedAttribute
                    );

            if (specOpt.isPresent()) {
                items.add(new SpecificationItemDto(
                        requestedAttribute,
                        specOpt.get().getValue(),
                        true
                ));
            } else {
                // Atende ao requisito do edital: Se a informação não existir, fica explícito
                items.add(new SpecificationItemDto(
                        requestedAttribute,
                        "Não disponível",
                        false
                ));
            }
        }

        return new CompetitorResponseDto(
                request.getBrand(),
                request.getModel(),
                request.getVersion(),
                items
        );
    }

    @Transactional
    public VehicleSpecificationResponseDto createSpecification(VehicleSpecificationRequestDto request) {
        boolean alreadyExists = specificationRepository
                .findByBrandIgnoreCaseAndModelIgnoreCaseAndVersionIgnoreCaseAndAttributeIgnoreCase(
                        request.getBrand(), request.getModel(), request.getVersion(), request.getAttribute())
                .isPresent();
        if (alreadyExists) {
            throw new ResourceAlreadyExistsException("Já existe uma especificação para este veículo e atributo");
        }

        VehicleSpecification saved = specificationRepository.save(new VehicleSpecification(
                null,
                request.getBrand().trim(),
                request.getModel().trim(),
                request.getVersion().trim(),
                request.getAttribute().trim(),
                request.getValue().trim()
        ));
        return new VehicleSpecificationResponseDto(saved.getId(), saved.getBrand(), saved.getModel(),
                saved.getVersion(), saved.getAttribute(), saved.getValue());
    }
}
