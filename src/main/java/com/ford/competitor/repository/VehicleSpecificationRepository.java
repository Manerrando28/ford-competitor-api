package com.ford.competitor.repository;

import com.ford.competitor.model.VehicleSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VehicleSpecificationRepository extends JpaRepository<VehicleSpecification, Long> {

    Optional<VehicleSpecification> findByBrandIgnoreCaseAndModelIgnoreCaseAndVersionIgnoreCaseAndAttributeIgnoreCase(
            String brand, String model, String version, String attribute);
}