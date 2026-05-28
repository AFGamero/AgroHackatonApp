package com.agrotrace.agrotrace.modules.certifications.domain.repository;

import com.agrotrace.agrotrace.modules.certifications.domain.model.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CertificationRepository extends JpaRepository<Certification, UUID> {
}
