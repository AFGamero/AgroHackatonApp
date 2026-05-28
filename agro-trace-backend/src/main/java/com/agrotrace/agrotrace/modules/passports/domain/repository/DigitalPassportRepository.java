package com.agrotrace.agrotrace.modules.passports.domain.repository;

import com.agrotrace.agrotrace.modules.passports.domain.model.DigitalPassport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface DigitalPassportRepository extends JpaRepository<DigitalPassport, UUID> {
    Optional<DigitalPassport> findByLotId(UUID lotId);
    Optional<DigitalPassport> findByPublicIdentifier(String publicIdentifier);
}
