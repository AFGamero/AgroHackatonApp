package com.agrotrace.agrotrace.modules.auth.domain.repository;

import com.agrotrace.agrotrace.modules.auth.domain.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {
    List<Session> findByUserIdAndStatus(UUID userId, String status);
}
