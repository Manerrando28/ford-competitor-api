package com.ford.competitor.config;

import com.ford.competitor.model.Role;
import com.ford.competitor.model.User;
import com.ford.competitor.model.VehicleSpecification;
import com.ford.competitor.repository.UserRepository;
import com.ford.competitor.repository.VehicleSpecificationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VehicleSpecificationRepository specificationRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           VehicleSpecificationRepository specificationRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.specificationRepository = specificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            userRepository.save(new User(null, "admin@ford.com", passwordEncoder.encode("admin123"), Role.ROLE_ADMIN));
            userRepository.save(new User(null, "user@ford.com", passwordEncoder.encode("user123"), Role.ROLE_USER));
        }

        if (specificationRepository.count() == 0) {
            specificationRepository.saveAll(List.of(
                    new VehicleSpecification(null, "Ford", "Ranger", "Raptor", "Motor", "3.0 V6 Bi-Turbo Gasoline"),
                    new VehicleSpecification(null, "Ford", "Ranger", "Raptor", "Potencia", "397 cv"),
                    new VehicleSpecification(null, "Ford", "Ranger", "Raptor", "Torque", "583 Nm"),
                    new VehicleSpecification(null, "Ford", "Ranger", "Raptor", "Transmissao", "Automatica de 10 marchas"),
                    new VehicleSpecification(null, "Ford", "Ranger", "Raptor", "Tracao", "4WD com reduzida"),
                    new VehicleSpecification(null, "Ford", "Ranger", "Raptor", "Suspensao", "Fox Live Valve 2.5")
            ));
        }
    }
}